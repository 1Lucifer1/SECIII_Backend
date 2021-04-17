package team.software.irbl.core.dbstore;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.domain.*;
import team.software.irbl.mapper.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        return codeFileMapper.insertOrUpdateBatch(codeFiles);
    }

    @Override
    public int saveBugReports(List<BugReport> bugReports) {
        int res = bugReportMapper.insertOrUpdateBatch(bugReports);
        List<CodeFile> codeFiles = codeFileMapper.selectList(new QueryWrapper<CodeFile>().eq("project_index", bugReports.get(0).getProjectIndex()));
        ConcurrentHashMap<String, Integer> packageNameMap = new ConcurrentHashMap<>();
        for(CodeFile codeFile : codeFiles){
            packageNameMap.put(codeFile.getPackageName(), codeFile.getFileIndex());
        }
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(BugReport bugReport : bugReports){
            for(FixedFile fixedFile: bugReport.getFixedFiles()){
                fixedFile.setReportIndex(bugReport.getReportIndex());
                if(packageNameMap.containsKey(fixedFile.getFilePackageName())) {
                    fixedFile.setFileIndex(packageNameMap.get(fixedFile.getFilePackageName()));
                }else {
                    System.out.println(fixedFile.getId());
                    System.out.println(fixedFile.getFilePackageName());
                }
                fixedFiles.add(fixedFile);
            }
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

    @Override
    public int saveRankRecord(List<RankRecord> records) {
        return rankRecordMapper.insertOrUpdateBatch(records);
    }

    @Override
    public Project getProjectByIndex(int projectIndex) {
        return projectMapper.selectById(projectIndex);
    }
}
