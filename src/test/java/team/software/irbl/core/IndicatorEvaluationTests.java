package team.software.irbl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.mapper.FixedFileMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.util.Err;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
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

        assertThat(indicator.getTop1(), greaterThanOrEqualTo(0.398));
        assertThat(indicator.getTop5(), greaterThanOrEqualTo(0.673));
        assertThat(indicator.getTop10(), greaterThanOrEqualTo(0.826));
        assertThat(indicator.getMRR(), greaterThanOrEqualTo(0.53));
        assertThat(indicator.getMAP(), greaterThanOrEqualTo(0.45));
    }
}
