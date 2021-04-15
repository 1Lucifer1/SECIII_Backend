package team.software.irbl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import team.software.irbl.core.domain.StructuredBugReport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.BugReport;
import team.software.irbl.util.SavePath;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileTranslator {
    public static void writeBugReport(List<StructuredBugReport> obj) throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.json";
        //String content="[";
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
//        for(int i=0;i<obj.size();i++){
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(obj.get(i));
//            //System.out.println(json);
//            //content = content+json+"\r\n";
//            content = content+json+",";
//        }
//        content += ']';
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static void writeCodeFile(List<StructuredCodeFile> obj) throws IOException {
        String path = SavePath.getFilePath()+"codeFile.json";
//        String content="";
//        for(int i=0;i<obj.size();i++){
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(obj.get(i));
//            //System.out.println(json);
//            content = content+json+"\r\n";
//        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static List<StructuredBugReport> readBugReport() throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.txt";
        File file =new File(path);
        String content="";
        List<StructuredBugReport> res = new ArrayList<>();
        if(file.exists()&&file.length()!=0){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(path));
                String contentLine;
                while ((contentLine = br.readLine()) != null) {
                    //System.out.println(contentLine);
                    //System.out.println(contentLine);
                    ObjectMapper mapper = new ObjectMapper();
                    StructuredBugReport sbr = mapper.readValue(contentLine, StructuredBugReport.class);
                    res.add(sbr);
                    //System.out.println(contentLine);
                }
                //输出数组
                //System.out.println(content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error in closing the BufferedReader");
                }
            }
        }
        return res;
    }
    public static List<StructuredCodeFile> readCodeFile() throws IOException {
        String path = SavePath.getFilePath()+"codeFile.txt";
        File file =new File(path);
        List<StructuredCodeFile> res = new ArrayList<>();
        if(file.exists()&&file.length()!=0){
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(path));
                String contentLine;
                while ((contentLine = br.readLine()) != null) {
                    //System.out.println(contentLine);
                    //System.out.println(contentLine);
                    ObjectMapper mapper = new ObjectMapper();
                    StructuredCodeFile scf = mapper.readValue(contentLine, StructuredCodeFile.class);
                    res.add(scf);
                    //System.out.println(contentLine);
                }
                //输出数组
                //System.out.println(content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error in closing the BufferedReader");
                }
            }
        }
        return res;
    }
}
