package team.software.irbl.core;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DBProcessor {

    private ConcurrentHashMap<String, Integer> packageMap = new ConcurrentHashMap<>();

    public void saveCodeFiles(List<CodeFile> codeFiles){
        for(int i=0; i<codeFiles.size(); ++i){
            CodeFile codeFile = codeFiles.get(i);
            codeFile.setFileIndex(i);
            packageMap.put(codeFile.getPackageName(), i);
        }
    }

    public void saveBugReports(List<BugReport> bugReports){
        for(int i=0; i<bugReports.size(); ++i){
            BugReport bugReport = bugReports.get(i);
            bugReport.setReportIndex(i);
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                fixedFile.setReportIndex(i);
                fixedFile.setFileIndex(packageMap.get(fixedFile.getFilePackageName()));
            }
        }
    }
}
