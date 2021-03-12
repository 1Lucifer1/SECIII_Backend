package team.software.irbl.core;

import team.software.irbl.domain.CodeFile;
import team.software.irbl.util.SavePath;

import java.io.File;

public class Preprocess {

     public static File preprocessCodeFile(CodeFile codeFile){
          return new File(SavePath.getAbsolutePath("class_preprocessed\\\\"+codeFile.getFileName().replace(".java",".txt")));
     }
}
