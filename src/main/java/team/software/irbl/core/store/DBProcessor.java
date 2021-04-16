package team.software.irbl.core.store;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DBProcessor {

    private ConcurrentHashMap<String, Integer> packageMap = new ConcurrentHashMap<>();

    public int saveCodeFiles(List<StructuredCodeFile> codeFiles){
        for(int i=0; i<codeFiles.size(); ++i){
            CodeFile codeFile = codeFiles.get(i);
            codeFile.setFileIndex(i);
            packageMap.put(codeFile.getPackageName(), i);
        }
        return codeFiles.size();
    }

    public int saveBugReports(List<StructuredBugReport> bugReports){
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

    public int saveFixedFiles(List<FixedFile> fixedFiles){
        for(int i=0; i<fixedFiles.size(); ++i){
            fixedFiles.get(i).setId(i);
        }
        return fixedFiles.size();
    }

    public int saveProject(Project project){
        project.setProjectIndex(1);
        return 1;
    }
}
