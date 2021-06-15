package team.software.irbl.core.common.vsm;

import team.software.irbl.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保证同时读或同时写的线程安全，但不保证同时读写的线程安全，也不建议这么用
 */
public class Lexicon {

    private SubLexicon[] subs;

    private int fileNum;

    private ConcurrentHashMap<String, IDF> wordMap;

    private volatile boolean hasCalculateIDF;

    public Lexicon(List<List<String>> files){
        fileNum = files.size();
        // 预留部分空间方便扩展
        int size = fileNum;
        if(size < 10) size = 10;
        else size = size * 3 / 2;
        subs = new SubLexicon[size];
        wordMap = new ConcurrentHashMap<>();

        // 在并行的同时保持subs与files对应顺序
        List<Integer> indexes = new ArrayList<>();
        for(int i=0; i<fileNum; ++i){
            indexes.add(i);
        }
        indexes.parallelStream().forEach(index -> {
            subs[index] = new SubLexicon(files.get(index));
            HashSet<String> fileWords = new HashSet<>(files.get(index));
            // ConcurrentHashMap也无法解决一些并发问题，本处为put冲突问题，故采用同步
            synchronized (this){
                // 因为HashSet保证word不重复，故不会出现重复put问题
                fileWords.parallelStream().forEach(this::putWord);
            }
        });
        calculateIDF();
    }

    public void addFile(List<String> file){
        int place;
        // 不想费脑子所以使用简单同步避免线程不安全
        synchronized (this) {
            place = fileNum;
            fileNum++;
            if (fileNum > subs.length) {
                subs = Arrays.copyOf(subs, subs.length * 3 / 2);
            }
        }
        subs[place] = new SubLexicon(file);
        HashSet<String> fileWords = new HashSet<>(file);
        synchronized (this){
            // 确保不会出现重复相同put问题
            fileWords.parallelStream().forEach(this::putWord);
            hasCalculateIDF = false;
        }
    }

    private void putWord(String word){
        if(wordMap.containsKey(word)){
            wordMap.get(word).add();
        }else {
            wordMap.put(word, new IDF());
        }
    }

    private synchronized void calculateIDF(){
        wordMap.values().forEach(idf -> {
            idf.setIdf(Calculate.calculateIdf(idf.getAppearFiles(), fileNum));
        });
        hasCalculateIDF = true;
    }

    // 未考虑并行读写问题，故不要一边addFile一边读tf或idf
    public double getIDF(String word){
        // 经典双检锁保证线程安全
        if(!hasCalculateIDF){
            synchronized (this) {
                if(!hasCalculateIDF) calculateIDF();
            }
        }
        if(wordMap.containsKey(word)){
            return wordMap.get(word).getIdf();
        }else {
            return 0;
        }
    }

    public double getTF(int index, String word){
        if(index < fileNum) {
            return subs[index].getTF(word);
        }else {
            Logger.errorLog("Lexicon: 文件索引越界.");
            return 0;
        }
    }

    public int getFileNum(){
        return fileNum;
    }

}

class IDF{

    private int appearFiles;
    private double idf;

    public IDF(){
        appearFiles = 1;
        idf = 0;
    }

    public void add(){
        appearFiles++;
    }

    public int getAppearFiles() {
        return appearFiles;
    }

    public void setAppearFiles(int appearFiles) {
        this.appearFiles = appearFiles;
    }

    public double getIdf() {
        return idf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
    }
}
