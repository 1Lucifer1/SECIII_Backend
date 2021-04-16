package team.software.irbl.core;
import org.junit.Test;
import team.software.irbl.core.domain.StructuredBugReport;
//import team.software.irbl.core.domain.Driver;
import team.software.irbl.core.store.FileTranslator;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TranslatorTest {
    @Test
    public void Translator() throws IOException {
        List<FixedFile> fixedFiles = new ArrayList<>();
        fixedFiles.add(new FixedFile(-1, "org.eclipse.swt.widgets.Button.java"));
        fixedFiles.add(new FixedFile(-1, "org.eclipse.swt.widgets.ToolItem.java"));
        BugReport report = new BugReport(1, 78548, "2004-11-12 16:15:00", "2004-11-24 14:30:00",
                "[consistency] Button Selection fires before MouseUp",
                "- run the ControlExample, Button tab - turn on listeners MouseUp and Selection - click on an example Button -&gt; on OSX you'll get Selection - MouseUp -&gt; everywhere else you'll get MouseUp - Selection",
                fixedFiles);
        //List<StructuredBugReport> bugReport;
        List<String> summaryWords = new ArrayList<>();

        summaryWords.add("apple");

        List<String> descriptionWords = new ArrayList<>();
        descriptionWords.add("banana");
        StructuredBugReport sbr = new StructuredBugReport(report);
        sbr.setSummaryWords(summaryWords);
        sbr.setDescriptionWords(descriptionWords);
        List<StructuredBugReport> res = new ArrayList<>();
        res.add(sbr);

        //Driver driver = new Driver();

        //List<StructuredBugReport> bugReports = driver.preProcessBugReports("SWTBugRepository.xml", 1);
        //System.out.println(bugReports.get(0).getDescription());
        FileTranslator.writeBugReport(res,"/test/bugReportFile.json");

    }
    @Test
    public void codeFileTranslator() throws IOException {
        String content;
        List<StructuredCodeFile> codeFiles;
        content = "package team.software.irbl.core.test;\r\n" +
                "/**\r\n" +
                " *\r\n" +
                " */\r\n" +
                "\r\n" +
                "/**\r\n" +
                " * type doc\r\n" +
                " */\r\n" +
                "public class Test1 {\r\n" +
                "    /**\r\n" +
                "     * field doc\r\n" +
                "     */\r\n" +
                "    private int field1;\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "     * method doc\r\n" +
                "     */\r\n" +
                "    public void methodJava(int arg1){\r\n" +
                "        /*\r\n" +
                "        * block comment\r\n" +
                "         */\r\n" +
                "        int arg2 = arg1 * 2; // line comment\r\n" +
                "        System.out.println(arg2);\r\n" +
                "    }\r\n" +
                "\r\n" +
                "    class innerTest{\r\n" +
                "\r\n" +
                "    }\r\n" +
                "}\r\n" +
                "\r\n" +
                "class outerTest{\r\n" +
                "\r\n" +
                "}";

        StructuredCodeFile codeFile = new StructuredCodeFile("Test1.java","Test1.java",1);
        codeFile.setPackageName("team.software.irbl.core.test.Test1.java");
        codeFile.addType("Test1 innerTest outerTest ");
        codeFile.addMethod("methodJava ");
        codeFile.addField("field1 arg1 arg2 ");
        codeFile.addContext(content);
        String comments = "/** \n" +
                " * type doc\n" +
                " */\n\n" +
                "/** \n" +
                " * field doc\n" +
                " */\n\n" +
                "/** \n" +
                " * method doc\n" +
                " */\n\n" +
                "/*\n" +
                "* block comment\n" +
                "*/\n" +
                "line comment\n";
        codeFile.addComment(comments);
        codeFiles = new ArrayList<>();
        codeFiles.add(codeFile);
        //Driver driver = new Driver();
        //List<StructuredCodeFile> codeFiles = driver.preProcessProject("swt-3.1", 1);
        FileTranslator.writeCodeFile(codeFiles,"/test/codeFile.json");
    }
    @Test
    public void reTranslator() throws IOException {
        List<StructuredBugReport> res = FileTranslator.readBugReport("/test/bugReportFile.json");
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i).getReportIndex());
        }
        //System.out.println(0);
    }
    @Test
    public void reCFTranslator() throws IOException {
        List<StructuredCodeFile> res = FileTranslator.readCodeFile("/test/codeFile.json");
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i).getFileIndex());
        }
    }
}
