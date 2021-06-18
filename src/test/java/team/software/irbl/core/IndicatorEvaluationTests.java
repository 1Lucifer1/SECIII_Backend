package team.software.irbl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.IRBLProdApplication;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.domain.Indicator;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.mapper.FixedFileMapper;
import team.software.irbl.mapper.RankRecordMapper;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IRBLProdApplication.class)
@ActiveProfiles("prod")
public class IndicatorEvaluationTests {

    @Autowired
    private IndicatorEvaluation indicatorEvaluation;
    @Autowired
    private RankRecordMapper rankRecordMapper;
    @Autowired
    private BugReportMapper bugReportMapper;
    @Autowired
    private FixedFileMapper fixedFileMapper;

    @Test
    public void getEvaluationIndicatorTest() {
        List<BugReport> bugReportList = bugReportMapper.selectList(new QueryWrapper<BugReport>().eq("project_index", 2));

        for(BugReport bugReport: bugReportList){
            int reportIndex = bugReport.getReportIndex();
            bugReport.setFixedFiles(fixedFileMapper.selectList(new QueryWrapper<FixedFile>().eq("report_index", reportIndex)));

            List<RankRecord> rankRecordList = rankRecordMapper.selectList(new QueryWrapper<RankRecord>().eq("report_index", reportIndex));
            rankRecordList.sort(Comparator.comparing(RankRecord::getFileRank));
            bugReport.setRanks(rankRecordList);
        }

        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(bugReportList);
        double top1 = indicator.getTop1();
        double top5 = indicator.getTop5();
        double top10 = indicator.getTop10();
        double mrr = indicator.getMRR();
        double map = indicator.getMAP();
        System.out.println(top1);
        System.out.println(top5);
        System.out.println(top10);
        System.out.println(mrr);
        System.out.println(map);

        assertTrue(indicator.getTop1() >= 0 && indicator.getTop1() <= 1);
        assertTrue(indicator.getTop5() >= 0 && indicator.getTop5() <= 1);
        assertTrue(indicator.getTop10() >= 0 && indicator.getTop10() <= 1);
        assertTrue(indicator.getMAP() >= 0 && indicator.getMAP() <= 1);
        assertTrue(indicator.getMRR() >= 0 && indicator.getMRR() <= 1);
        assertTrue(indicator.getTop1() < indicator.getTop5());
        assertTrue(indicator.getTop5() < indicator.getTop10());
    }
}
