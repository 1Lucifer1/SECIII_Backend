package team.software.irbl.core.common.dbstore;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.domain.*;
import team.software.irbl.mapper.*;
import team.software.irbl.util.Logger;


import java.util.ArrayList;
import java.util.List;

@Component
public class DBProcessorImpl implements DBProcessor {

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private BugReportMapper bugReportMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private FixedFileMapper fixedFileMapper;

    @Autowired
    private RankRecordMapper rankRecordMapper;

    @Override
    public int saveCodeFiles(List<CodeFile> codeFiles) {
        int current = codeFileMapper.selectCount(new QueryWrapper<CodeFile>().gt("file_index",0));
        for(CodeFile codeFile:codeFiles){
            current++;
            codeFile.setFileIndex(current);
        }
        return codeFileMapper.insertOrUpdateBatch(codeFiles);
    }

    @Override
    public int saveBugReports(List<BugReport> bugReports, CodeFileMap codeFileMap) {
        int current = bugReportMapper.selectCount(new QueryWrapper<BugReport>().gt("report_index",0));
        for(BugReport report: bugReports){
            current++;
            report.setReportIndex(current);
        }
        int res = bugReportMapper.insertOrUpdateBatch(bugReports);
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(BugReport bugReport : bugReports){
            List<FixedFile> extendFixedFiles = new ArrayList<>();
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                List<CodeFile> codeFiles = codeFileMap.getCodeFileFromMap(fixedFile.getFileIdentifyString());
                if(codeFiles != null){
                    for(CodeFile codeFile: codeFiles){
                        extendFixedFiles.add(new FixedFile( bugReport.getReportIndex(), codeFile.getFileIndex(), codeFile.getPackageName(), codeFile.getFilePath()));
                    }
                }else {
                    Logger.errorLog("Not found " + fixedFile.getFilePackageName());
                }
            }
            bugReport.setFixedFiles(extendFixedFiles);
            fixedFiles.addAll(extendFixedFiles);
        }
        saveFixedFiles(fixedFiles);
        return res;
    }

    @Override
    public int saveFixedFiles(List<FixedFile> fixedFiles) {
        return fixedFileMapper.insertOrUpdateBatch(fixedFiles);
    }

    @Override
    public int saveProject(Project project) {
        return projectMapper.insert(project);
    }

    @Override
    public int updateProject(Project project) {
        return projectMapper.updateById(project);
    }

    /**
     * 清除与项目相关的所有源代码记录，错误报告记录，以及联系二者的fixedFile记录与rankRecord记录
     * @param projectIndex
     * @return
     */
    @Override
    public int cleanProject(int projectIndex) {
        codeFileMapper.delete(new QueryWrapper<CodeFile>().eq("project_index", projectIndex));
        List<BugReport> bugReports = bugReportMapper.selectList(new QueryWrapper<BugReport>().eq("project_index", projectIndex));
        List<Integer> reportIndexList = new ArrayList<>();
        for(BugReport report: bugReports){
            reportIndexList.add(report.getReportIndex());
            fixedFileMapper.delete(new QueryWrapper<FixedFile>().eq("report_index", report.getReportIndex()));
            rankRecordMapper.delete(new QueryWrapper<RankRecord>().eq("report_index", report.getReportIndex()));
        }
        bugReportMapper.deleteBatchIds(reportIndexList);
        return 0;
    }

    @Override
    public int saveRankRecord(List<RankRecord> records) {
        int maxSize = 100000;
        int length = records.size();
        int count = 0, start = 0;
        while (start + maxSize < length) {
            count += rankRecordMapper.insertOrUpdateBatch(records.subList(start, start+maxSize));
            start += maxSize;
        }
        count += rankRecordMapper.insertOrUpdateBatch(records.subList(start, length));
        return count;
    }

    @Override
    public Project getProjectByIndex(int projectIndex) {
        return projectMapper.selectById(projectIndex);
    }

    @Override
    public Project getProjectByName(String projectName) {
        return projectMapper.selectOne(new QueryWrapper<Project>().eq("project_name", projectName));
    }

    @Override
    public List<CodeFile> getCodeFilesByProjectIndex(int projectIndex) {
        return codeFileMapper.selectList(new QueryWrapper<CodeFile>().eq("project_index", projectIndex));
    }

    @Override
    public List<BugReport> getBugReportsByProjectIndex(int projectIndex) {
        return bugReportMapper.selectList(new QueryWrapper<BugReport>().eq("project_index", projectIndex));
    }
}
