package team.software.irbl.core;
import org.junit.Test;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.FileTranslator;
import team.software.irbl.core.nlp.NLP;
import team.software.irbl.core.xml.XMLParser;
import team.software.irbl.domain.BugReport;
import team.software.irbl.util.SavePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranslatorTest {
    @Test
    public void Translator() throws IOException {
//        List<String> summaryWords = new ArrayList();
//        summaryWords.add("apple");
//        summaryWords.add("banana");
//        summaryWords.add("cat");
//        List<String> descriptionWords = new ArrayList();
//        descriptionWords.add("apple");
//        descriptionWords.add("banana");
//        descriptionWords.add("cat");
        String bugReportFile = "SWTBugRepository.xml";
        String filePath = SavePath.getAbsolutePath(bugReportFile);
        List<BugReport> rawReports = XMLParser.getBugReportsFromXML(filePath, 1);
        StructuredBugReport report = new StructuredBugReport(rawReports.get(0));
        // 虽然没有格式化，但是由于专有名词出现频率不高，和格式化过的一起使用没有专有名词的停用词列表
        report.setDescriptionWords(NLP.standfordNLP(rawReports.get(0).getDescription(),true));
        report.setSummaryWords(NLP.standfordNLP(rawReports.get(0).getSummary(),true));
        FileTranslator.writeBugReport(report);

    }
    @Test
    public void reTranslator() throws IOException {
        List<StructuredBugReport> res = FileTranslator.readBugReport();
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i).getDescriptionWords());
        }
    }
}
