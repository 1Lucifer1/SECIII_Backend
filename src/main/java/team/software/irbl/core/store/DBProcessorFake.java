package team.software.irbl.core.store;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DBProcessorFake implements DBProcessor {

    private ConcurrentHashMap<String, Integer> packageMap = new ConcurrentHashMap<>();

    @Override
    public int saveCodeFiles(List<CodeFile> codeFiles){
        for(int i=0; i<codeFiles.size(); ++i){
            CodeFile codeFile = codeFiles.get(i);
            codeFile.setFileIndex(i);
            packageMap.put(codeFile.getPackageName(), i);
        }
        return codeFiles.size();
    }

    @Override
    public int saveBugReports(List<BugReport> bugReports){
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(int i=0; i<bugReports.size(); ++i){
            BugReport bugReport = bugReports.get(i);
            bugReport.setReportIndex(i);
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                fixedFile.setReportIndex(i);
                fixedFile.setFileIndex(packageMap.get(fixedFile.getFilePackageName()));
                fixedFiles.add(fixedFile);
            }
        }
        saveFixedFiles(fixedFiles);
        return bugReports.size();
    }

    @Override
    public int saveFixedFiles(List<FixedFile> fixedFiles){
        for(int i=0; i<fixedFiles.size(); ++i){
            fixedFiles.get(i).setId(i);
        }
        return fixedFiles.size();
    }

    @Override
    public int saveProject(Project project){
        project.setProjectIndex(1);
        return 1;
    }

    @Override
    public int saveRankRecord(List<RankRecord> records) {
        return records.size();
    }
}
