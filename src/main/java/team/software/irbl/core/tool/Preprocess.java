package team.software.irbl.core.tool;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.util.SavePath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Preprocess {

     public static File preprocessCodeFile(CodeFile codeFile){
          return new File(SavePath.getAbsolutePath("class_preprocessed/" + codeFile.getFileName().replace(".java",".txt")));
     }

     public static String preprocessBugReport(BugReport bugReport){
          File file = new File(SavePath.getAbsolutePath("report_preprocessed/report"+Integer.toString(bugReport.getBugId())+".txt"));
          StringBuilder builder = new StringBuilder();
          try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               String line = reader.readLine();
               while (line != null) {
                    builder.append(line).append(' ');
                    line = reader.readLine();
               }
          }catch (IOException e){
               e.printStackTrace();
          }
          return builder.toString();
     }
}
