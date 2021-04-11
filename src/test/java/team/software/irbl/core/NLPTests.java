package team.software.irbl.core;

import org.junit.Test;
import team.software.irbl.core.nlp.NLP;

import java.util.List;

import static org.junit.Assert.*;

public class NLPTests {

    //sample text for tests
    private static final String example = "/**The China_history of <p>coreOfNLPMethods</p> generally starts in the [1950s], although ChineseHistories() can be found from earlier {periods}.*/";

    @Test
    public void standfordNLPTest(){
        List<String> wordList = NLP.standfordNLP(example, false);
        for (String word:wordList) {
            System.out.println(word);
        }
    }
}
