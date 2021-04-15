package team.software.irbl.core;
import org.junit.Test;
import team.software.irbl.core.domain.StructuredBugReport;
//import team.software.irbl.core.domain.Driver;
import team.software.irbl.core.FileTranslator;
import team.software.irbl.core.domain.StructuredCodeFile;
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
        Driver driver = new Driver();
        List<StructuredBugReport> bugReports = driver.preProcessBugReports("SWTBugRepository.xml", 1);
        System.out.println(bugReports.get(0).getDescription());
        FileTranslator.writeBugReport(bugReports);

    }
    @Test
    public void codeFileTranslator() throws IOException {
        Driver driver = new Driver();
        List<StructuredCodeFile> codeFiles = driver.preProcessProject("swt-3.1", 1);
        FileTranslator.writeCodeFile(codeFiles);
    }
    @Test
    public void reTranslator() throws IOException {
        List<StructuredBugReport> res = FileTranslator.readBugReport();
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i).getDescriptionWords());
        }
    }
    @Test
    public void reCFTranslator() throws IOException {
        List<StructuredCodeFile> res = FileTranslator.readCodeFile();
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i).getFileIndex());
        }
    }
}
