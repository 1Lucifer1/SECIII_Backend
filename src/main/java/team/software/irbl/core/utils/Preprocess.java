package team.software.irbl.core.utils;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.util.SavePath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Preprocess {

     public static File preprocessCodeFile(CodeFile codeFile){
          return new File(SavePath.getPreProcessClassPath(codeFile.getFileName().replace(".java",".txt")));
     }

     public static String preprocessBugReport(BugReport bugReport){
          File file = new File(SavePath.getPreProcessReportPath("report"+ bugReport.getBugId() +".txt"));
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