package team.software.irbl.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.util.Err;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndicatorEvaluationTests {

    @Autowired
    private IndicatorEvaluation indicatorEvaluation;

    @Test
    public void TopTest() throws Err {
        double top1 = indicatorEvaluation.Top(2,1);
        double top5 = indicatorEvaluation.Top(2,5);
        double top10 = indicatorEvaluation.Top(2,10);
        System.out.println(top1);
        System.out.println(top5);
        System.out.println(top10);
        assertThat(top1,greaterThanOrEqualTo(0.071));
        assertThat(top5,greaterThanOrEqualTo(0.316));
        assertThat(top10,greaterThanOrEqualTo(0.49));
    }

    @Test
    public void MRRTest() throws Err {
        double mrr = indicatorEvaluation.MRR(2);
        System.out.println(mrr);
        assertThat(mrr,greaterThanOrEqualTo(0.20438985978353597));
    }

    @Test
    public void MAPTest() throws Err {
        double map = indicatorEvaluation.MAP(2);
        System.out.println(map);
        assertThat(map,greaterThanOrEqualTo(0.18109653077639296));
    }
}
