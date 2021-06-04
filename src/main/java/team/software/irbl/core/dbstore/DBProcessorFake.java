package team.software.irbl.core.dbstore;

import team.software.irbl.domain.*;
import team.software.irbl.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不使用数据库而伪造数据库方法调用，方便本地调试vsm
 */
public class DBProcessorFake implements DBProcessor {

    private ConcurrentHashMap<String, Integer> packageMap = new ConcurrentHashMap<>();

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
            packageMap.put(codeFile.getPackageName(), i+1);
        }
        return codeFiles.size();
    }

    /**
     * 会为BugReport加上编号并为其包含的FixedFile设置fileIndex与reportIndex，
     * 注意！！！该方法只能在 saveCodeFiles后调用才能达到预期效果
     * @param bugReports
     * @return
     */
    @Override
    public int saveBugReports(List<BugReport> bugReports){
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(int i=0; i<bugReports.size(); ++i){
            BugReport bugReport = bugReports.get(i);
            bugReport.setReportIndex(i+1);
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                fixedFile.setReportIndex(bugReport.getReportIndex());
                if(packageMap.containsKey(fixedFile.getFilePackageName())) {
                    fixedFile.setFileIndex(packageMap.get(fixedFile.getFilePackageName()));
                }else {
                    Logger.errorLog("Not found " + fixedFile.getFilePackageName());
                }
                fixedFiles.add(fixedFile);
            }
        }
        saveFixedFiles(fixedFiles);
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
        Project project = new Project(projectName);
        project.setProjectIndex(2);
        return project;
    }
}
