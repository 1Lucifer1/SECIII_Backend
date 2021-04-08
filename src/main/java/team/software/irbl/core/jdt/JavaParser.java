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
import team.software.irbl.util.SavePath;

public class JavaParser {
    public static StructuredCodeFile parse(String str, StructuredCodeFile codeFile) {

        ASTParser parser = ASTParser.newParser(AST.JLS15);
        parser.setSource(str.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);


        codeFile.addContext(str);

        PackageDeclaration packageName = cu.getPackage();
        codeFile.setPackageName(packageName.getName().getFullyQualifiedName()+"."+ codeFile.getFileName());
        // System.out.println(packageName.getName());
        MyASTVisitor myASTVisitor = new MyASTVisitor();
        cu.accept(myASTVisitor);
        //return codeFile;

        /*
        List comments = cu.getCommentList();
        List typeList = cu.types();
        for(Object ele: typeList){
            TypeDeclaration type = (TypeDeclaration)ele;
            System.out.println(type.getName());
            TypeDeclaration[] types = type.getTypes();
            for(FieldDeclaration field: type.getFields()){
                for(Object fra: field.fragments()){
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment)fra;
                    System.out.println(fragment.getName());
                }
                System.out.println(field.getType());
                System.out.println(field.getJavadoc());
            }
            //System.out.println();
            for(MethodDeclaration method:type.getMethods()){
                System.out.println(method.getName());
                System.out.println(method.getJavadoc());
                Block block = method.getBody();
                List params = method.parameters();

                //method.getBody().getAST().
                System.out.println(method.getBody());
            }
        }
        */
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

            while (reader.read(buf) != -1) {
                fileData.append(buf);
                //String readData = String.valueOf(buf, 0, numRead);
                //fileData.append(readData);
                buf = new char[1024];
            }

            reader.close();

        }catch (IOException e){
            e.printStackTrace();
        }

        //System.out.println(fileData.toString());
        return fileData.toString();
    }

    /**
     * 遍历目录下Java源代码，并使用jdt提取结构化信息
     * @param dirPath
     */
    public static List<StructuredCodeFile> parseCodeFilesInDir(String dirPath, int projectIndex){

        File root = new File(dirPath);
        if(!root.exists()){
            System.out.println("Error: 目录不存在或路径错误。");
            return null;
        }

        List<StructuredCodeFile> codeFiles = new ArrayList<>();
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
                        StructuredCodeFile codeFile = new StructuredCodeFile(file.getName(), )
                        codeFiles.add(parse(readFileToString(file.getAbsolutePath()), codeFile));
                    }
                }
            }
        }
        return codeFiles;
    }

    public static void main(String[] args) {
        //String dirPath = "./IRBL/data/test";
        String dirPath = SavePath.getAbsolutePath("swt-3.1")+File.separatorChar+"src"+File.separatorChar;
        parseCodeFilesInDir(dirPath, 1);

    }
}
