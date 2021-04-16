package team.software.irbl.core.domain;

import team.software.irbl.core.vsm.Calculate;
import team.software.irbl.domain.FileWord;
import team.software.irbl.enums.WordType;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Lexicon {

    private final ArrayList<ConcurrentHashMap<String, FileWord>> tfMap;

    public Lexicon(int fileNumber){
        tfMap = new ArrayList<>();
        for(int i = 0; i < fileNumber;++i){
            tfMap.add(new ConcurrentHashMap<>());
        }
    }

    public void addFileWord(int index, String s, int fileIndex, WordType wordType){
        ConcurrentHashMap<String, FileWord> map = tfMap.get(index);
        if(map.containsKey(s)){
            map.get(s).addAppearTimes();
        }else{
            map.put(s, new FileWord(s, fileIndex, wordType));
        }
    }

    public FileWord getFileWord(int index, String s){
        return tfMap.get(index).getOrDefault(s, new FileWord(-1, -1, null, -1, 0, -1));
    }

    public void calculateTF(){
        tfMap.forEach(fileWordMap -> {
            fileWordMap.values().forEach(fileWord -> {
                fileWord.setTf(Calculate.calculateTf(fileWord.getAppearTimes()));
            });
        });
    }
}
