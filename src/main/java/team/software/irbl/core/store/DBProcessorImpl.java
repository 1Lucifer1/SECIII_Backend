package team.software.irbl.core.store;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.domain.*;
import team.software.irbl.mapper.*;


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
        return codeFileMapper.insertOrUpdateBatch(codeFiles);
    }

    @Override
    public int saveBugReports(List<BugReport> bugReports) {
        int res = saveBugReports(bugReports);
        List<CodeFile> codeFiles = codeFileMapper.selectList(new QueryWrapper<>());
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(BugReport bugReport : bugReports){

        }
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
    public int saveRankRecord(List<RankRecord> records) {
        return rankRecordMapper.insertOrUpdateBatch(records);
    }
}
