package team.software.irbl.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.software.irbl.domain.*;
import team.software.irbl.mapper.*;

import java.util.List;

@SpringBootTest
public class DataBaseConnectTests {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectWordMapper projectWordMapper;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private FileWordMapper fileWordMapper;

    @Autowired
    private BugReportMapper bugReportMapper;

    @Autowired
    private FixedFileMapper fixedFileMapper;

    @Autowired
    private RankRecordMapper rankRecordMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Project> projectList = projectMapper.selectList(null);
        System.out.println(projectList.size());
        List<CodeFile> codeFileList = codeFileMapper.selectList(null);
        System.out.println(codeFileList.size());
        List<ProjectWord> projectWordList = projectWordMapper.selectList(null);
        System.out.println(projectWordList.size());
        List<FileWord> fileWordList = fileWordMapper.selectList(null);
        System.out.println(fileWordList.size());
        List<BugReport> bugReportList = bugReportMapper.selectList(null);
        System.out.println(bugReportList.size());
        List<FixedFile> fixedFileList = fixedFileMapper.selectList(null);
        System.out.println(fixedFileList.size());
        List<RankRecord> rankRecordList = rankRecordMapper.selectList(null);
        System.out.println(rankRecordList.size());
    }
}
