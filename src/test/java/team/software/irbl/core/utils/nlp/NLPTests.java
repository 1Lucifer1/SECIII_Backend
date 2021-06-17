package team.software.irbl.core.utils.nlp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NLPTests {

    //sample text for tests
    private static final String example = "/**The China_history \"\" of <p>coreOfNLPMethods</p> generally starts in the [1950s], although ChineseHistories() can be found from my.earlier(new Date()) {periods}.*/";

    @Test
    public void standfordNLPTest(){
        List<String> wordList = NLP.standfordNLP(example, false);
        for (String word:wordList) {
            System.out.println(word);
        }
        List<String> expected = new ArrayList<>();
        expected.add("china");
        expected.add("history");
        expected.add("coreOfNLPMethods");
        expected.add("core");
        expected.add("nlp");
        expected.add("method");
        expected.add("generally");
        expected.add("start");
        expected.add("1950s");
        expected.add("although");
        expected.add("ChineseHistories");
        expected.add("chinese");
        expected.add("history");
        expected.add("can");
        expected.add("find");
        expected.add("from");
        expected.add("my");
        expected.add("earlier");
        expected.add("new");
        expected.add("date");
        expected.add("period");

        assertEquals(expected, wordList);
    }
}
