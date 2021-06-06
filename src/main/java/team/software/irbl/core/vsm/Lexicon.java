package team.software.irbl.core.vsm;

import team.software.irbl.util.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Lexicon {

    private SubLexicon[] subs;

    private int fileNum;

    private ConcurrentHashMap<String, IDF> wordMap;

    public Lexicon(List<List<String>> files){
        subs = new SubLexicon[files.size()];
        wordMap = new ConcurrentHashMap<>();
        fileNum = files.size();

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

    private void putWord(String word){
        if(wordMap.containsKey(word)){
            wordMap.get(word).add();
        }else {
            wordMap.put(word, new IDF());
        }
    }

    private void calculateIDF(){
        wordMap.values().forEach(idf -> {
            idf.setIdf(Calculate.calculateIdf(idf.getAppearFiles(), fileNum));
        });
    }


    public double getIDF(String word){
        if(wordMap.containsKey(word)){
            return wordMap.get(word).getIdf();
        }else {
            return 0;
        }
    }

    public double getTF(int index, String word){
        if(index < subs.length) {
            return subs[index].getTF(word);
        }else {
            Logger.errorLog("Lexicon: 数组越界.");
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
