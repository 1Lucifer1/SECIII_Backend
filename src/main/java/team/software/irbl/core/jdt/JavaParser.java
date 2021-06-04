package team.software.irbl.core.jdt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.*;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

public class JavaParser {

    /**
     * 将源代码的结构化信息以及纯文本填入传入的codeFile对象
     * @param str
     * @param codeFile
     * @return
     */
    public static StructuredCodeFile parse(String str, StructuredCodeFile codeFile) {

        ASTParser parser = ASTParser.newParser(AST.JLS15);
        parser.setSource(str.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);


        codeFile.addContext(str);

        PackageDeclaration packageName = cu.getPackage();
        Logger.debugLog(codeFile.getFileName());
        Logger.debugLog(codeFile.getFilePath());
        // 如果代码文件里没有声明包名，则直接以文件名代替
        if(packageName!=null && packageName.getName() != null) {
            codeFile.setPackageName(packageName.getName().getFullyQualifiedName() + "." + codeFile.getFileName());
        }else {
            codeFile.setPackageName(codeFile.getFileName());
        }
        MyASTVisitor myASTVisitor = new MyASTVisitor(cu);
        cu.accept(myASTVisitor);
        codeFile.addField(myASTVisitor.getFields());
        codeFile.addMethod(myASTVisitor.getMethods());
        codeFile.addType(myASTVisitor.getTypes());

        MyCommentVisitor commentVisitor = new MyCommentVisitor(cu, str.split("\n"));
        List comments = cu.getCommentList();
        // 第一个注释为版权声明，直接舍去
        for(int i=1; i<comments.size(); ++i){
            Comment comment = (Comment)comments.get(i);
            comment.accept(commentVisitor);
        }
        codeFile.addComment(commentVisitor.getComments());

        return codeFile;
    }

    /**
     * 读取源代码文件为一个字符串
     * @param filePath
     */
    public static String readFileToString(String filePath) {

        StringBuilder fileData = new StringBuilder(1000);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            char[] buf = new char[1024];
            int numRead;

            while ((numRead = reader.read(buf)) != -1) {
                //fileData.append(buf);
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }

            reader.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        return fileData.toString();
    }

    /**
     * 遍历目录下Java源代码，并使用jdt提取结构化信息
     * @param dirPath
     */
    public static List<StructuredCodeFile> parseCodeFilesInDir(String dirPath, int projectIndex){
        List<StructuredCodeFile> codeFiles = new ArrayList<>();

        File root = new File(dirPath);
        if(!root.exists()){
            Logger.log("Error: 目录不存在或路径错误。");
            return codeFiles;
        }

        LinkedList<File> dirs = new LinkedList<>();
        dirs.add(root);

        while (!dirs.isEmpty()) {
            File dir = dirs.removeFirst();
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        dirs.add(file);
                    } else if (file.getName().contains(".java")) {
                        StructuredCodeFile codeFile = new StructuredCodeFile(file.getName(), SavePath.pathTransformFromWinToLinux(file.getPath()).replaceFirst(dirPath, ""), projectIndex);
                        codeFiles.add(codeFile);
                    }
                }
            }
        }
        codeFiles.parallelStream().forEach(codeFile -> {
            parse(readFileToString(dirPath + codeFile.getFilePath()), codeFile);
        });
        return codeFiles;
    }

    public static void main(String[] args) {
        //String dirPath = "./IRBL/data/test";
        String dirPath = SavePath.getSourcePath("swt-3.1")+"/";
        parseCodeFilesInDir(dirPath, 1);

    }
}
