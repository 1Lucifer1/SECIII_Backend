package team.software.irbl.core.utils.jdt;

import org.junit.Before;
import org.junit.Test;
import team.software.irbl.core.domain.StructuredCodeFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JavaParserTests {

    private String content;

    private List<StructuredCodeFile> codeFiles;

    @Before
    public void prepare(){
        content = "package team.software.irbl.core.test;" + System.getProperty("line.separator")+
                "/**" + System.getProperty("line.separator")+
                " *" + System.getProperty("line.separator")+
                " */" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
                "/**" + System.getProperty("line.separator")+
                " * type doc" + System.getProperty("line.separator")+
                " */" + System.getProperty("line.separator")+
                "public class Test1 {" + System.getProperty("line.separator")+
                "    /**" + System.getProperty("line.separator")+
                "     * field doc" + System.getProperty("line.separator")+
                "     */" + System.getProperty("line.separator")+
                "    private int field1;" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
                "    /**" + System.getProperty("line.separator")+
                "     * method doc" + System.getProperty("line.separator")+
                "     */" + System.getProperty("line.separator")+
                "    public void methodJava(int arg1){" + System.getProperty("line.separator")+
                "        /*" + System.getProperty("line.separator")+
                "        * block comment" + System.getProperty("line.separator")+
                "         */" + System.getProperty("line.separator")+
                "        int arg2 = arg1 * 2; // line comment" + System.getProperty("line.separator")+
                "        System.out.println(arg2);" + System.getProperty("line.separator")+
                "    }" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
                "    class innerTest{" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
                "    }" + System.getProperty("line.separator")+
                "}" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
                "class outerTest{" + System.getProperty("line.separator")+
                "" + System.getProperty("line.separator")+
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
    }

    private void equal(StructuredCodeFile codeFile1, StructuredCodeFile codeFile2){
        if(codeFile1 == codeFile2) return;
        assertEquals(codeFile1.getFileName(), codeFile2.getFileName());
        assertEquals(codeFile1.getFilePath(), codeFile2.getFilePath());
        assertEquals(codeFile1.getProjectIndex(), codeFile2.getProjectIndex());
        assertEquals(codeFile1.getPackageName(), codeFile2.getPackageName());
        assertEquals(codeFile1.getComments().get(0), codeFile2.getComments().get(0));
        assertEquals(codeFile1.getFields().get(0), codeFile2.getFields().get(0));
        assertEquals(codeFile1.getContexts().get(0), codeFile2.getContexts().get(0));
        assertEquals(codeFile1.getMethods().get(0), codeFile2.getMethods().get(0));
        assertEquals(codeFile1.getTypes().get(0), codeFile2.getTypes().get(0));
    }

    @Test
    public void testParse(){
        StructuredCodeFile codeFile = new StructuredCodeFile("Test1.java", "Test1.java", 1);
        JavaParser.parse(content, codeFile);
        equal(codeFiles.get(0), codeFile);
    }

    @Test
    public void testParseCodeFilesInDir(){
        String dirPath = "./resource/data/test/";
        List<StructuredCodeFile> codeFileList = JavaParser.parseCodeFilesInDir(dirPath, 1);
        assertEquals(1, codeFileList.size());
        equal(codeFiles.get(0), codeFileList.get(0));
    }

    @Test
    public void testReadFileToString(){
        String filePath = "./resource/data/test/Test1.java";
        String res = JavaParser.readFileToString(filePath);
        assertEquals(content, res);
    }

}
