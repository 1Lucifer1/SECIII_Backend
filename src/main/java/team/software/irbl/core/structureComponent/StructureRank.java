package team.software.irbl.core.structureComponent;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.enums.CodeWordsType;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.core.vsm.VSM;

import java.util.ArrayList;
import java.util.List;

public class StructureRank {

    private final static int CODE_PART_NUM = 5;
    private final static int REPORT_PART_NUM = 2;

    private List<VSM> vsmList;

    private List<StructuredCodeFile> codeFiles;

    public StructureRank(List<StructuredCodeFile> codeFiles){ 
        this.codeFiles = codeFiles;
        initial();
    }

    public List<RankRecord> rank(StructuredBugReport report){
        return null;
    }

    private void initial(){
        List<List<List<String>>> parts = new ArrayList<>();
        for(int i=0; i<CODE_PART_NUM ;++i){
            parts.add(new ArrayList<>());
        }

        for(StructuredCodeFile codeFile: codeFiles){
            parts.get(CodeWordsType.TYPES.value()).add(codeFile.getTypes());
            parts.get(CodeWordsType.METHODS.value()).add(codeFile.getMethods());
            parts.get(CodeWordsType.FIELDS.value()).add(codeFile.getFields());
            parts.get(CodeWordsType.COMMENTS.value()).add(codeFile.getComments());
            parts.get(CodeWordsType.CONTEXTS.value()).add(codeFile.getContexts());
        }

        vsmList = new ArrayList<>();
        for(int i=0; i<CODE_PART_NUM ;++i){
            vsmList.add(new VSM(parts.get(i)));
        }
    }
}
