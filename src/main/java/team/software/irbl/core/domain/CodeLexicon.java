package team.software.irbl.core.domain;

import team.software.irbl.core.vsm.Calculate;
import team.software.irbl.domain.ProjectWord;

import java.util.concurrent.ConcurrentHashMap;

public class CodeLexicon extends Lexicon{
    private final ConcurrentHashMap<String, ProjectWord> idfMap;
    public CodeLexicon(int fileNumber) {
        super(fileNumber);
        idfMap = new ConcurrentHashMap<>();
    }

    public void addProjectWord(int fileCount, String s, int projectIndex){
        if(idfMap.containsKey(s)){
            idfMap.get(s).setAppearFiles(fileCount);
        }else{
            idfMap.put(s, new ProjectWord(s, projectIndex));
        }
    }

    public ProjectWord getProjectWord(String s){
        return idfMap.getOrDefault(s, new ProjectWord(-1, -1, null, -1, 0));
    }

    public void calculateIDF(int totalFile){
        idfMap.values().forEach(projectWord -> {
            projectWord.setIdf(Calculate.calculateIdf(projectWord.getAppearFiles(), totalFile));
        });
    }
}
