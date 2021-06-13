package team.software.irbl.core.dbstore;

import team.software.irbl.core.maptool.CodeFileMap;
import team.software.irbl.domain.*;
import team.software.irbl.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 不使用数据库而伪造数据库方法调用，方便本地调试vsm
 */
public class DBProcessorFake implements DBProcessor {

    /**
     * 会仿造存数据库为要保存的列表数据加上编号
     * @param codeFiles
     * @return
     */
    @Override
    public int saveCodeFiles(List<CodeFile> codeFiles){
        for(int i=0; i<codeFiles.size(); ++i){
            CodeFile codeFile = codeFiles.get(i);
            codeFile.setFileIndex(i+1);
        }
        return codeFiles.size();
    }

    /**
     * 会为BugReport加上编号并为其包含的FixedFile设置fileIndex与reportIndex
     * @param bugReports
     * @return
     */
    @Override
    public int saveBugReports(List<BugReport> bugReports, CodeFileMap codeFileMap){
        List<FixedFile> fixedFiles = new ArrayList<>();
        int count = 0;
        for(int i=0; i<bugReports.size(); ++i){
            BugReport bugReport = bugReports.get(i);
            bugReport.setReportIndex(i+1);
            List<FixedFile> extendFixedFiles = new ArrayList<>();
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                List<CodeFile> codeFiles = codeFileMap.getCodeFileFromMap(fixedFile.getFileIdentifyString());
                if(codeFiles != null){
                    if(codeFiles.size()!=1) count += codeFiles.size();
                    for(CodeFile codeFile: codeFiles){
                        extendFixedFiles.add(new FixedFile(-1, bugReport.getReportIndex(), codeFile.getFileIndex(), codeFile.getPackageName(), codeFile.getFilePath()));
                    }
                }else {
                    Logger.errorLog("Not found " + fixedFile.getFileIdentifyString());
                }
            }
            bugReport.setFixedFiles(extendFixedFiles);
            fixedFiles.addAll(extendFixedFiles);
        }
        saveFixedFiles(fixedFiles);
        System.out.println(count);
        return bugReports.size();
    }

    /**
     * 会为每条内容加上编号
     * @param fixedFiles
     * @return
     */
    @Override
    public int saveFixedFiles(List<FixedFile> fixedFiles){
        for(int i=0; i<fixedFiles.size(); ++i){
            fixedFiles.get(i).setId(i+1);
        }
        return fixedFiles.size();
    }

    /**
     * 为project固定加上编号2
     * @param project
     * @return
     */
    @Override
    public int saveProject(Project project){
        project.setProjectIndex(2);
        return 1;
    }

    @Override
    public int updateProject(Project project) {
        return 1;
    }

    @Override
    public int cleanProject(int projectIndex) {
        return 0;
    }

    @Override
    public int saveRankRecord(List<RankRecord> records) {
        return records.size();
    }

    @Override
    public Project getProjectByIndex(int projectIndex) {
        return null;
    }

    /**
     * 会固定返回一个index为2的project
     * @param projectName
     * @return
     */
    @Override
    public Project getProjectByName(String projectName) {
        return null;
    }

    @Override
    public List<CodeFile> getCodeFilesByProjectIndex(int projectIndex) {
        return new ArrayList<>();
    }

    @Override
    public List<BugReport> getBugReportsByProjectIndex(int projectIndex) {
        return new ArrayList<>();
    }
}
