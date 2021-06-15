package team.software.irbl.core.utils.filestore;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.software.irbl.core.domain.StructuredBugReport;

import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.BugReport;
import team.software.irbl.util.Logger;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTranslator {
    public static void writeBugReport(List<StructuredBugReport> obj,String path){
        writeObject(obj, path);
    }
    public static void writeCodeFile(List<StructuredCodeFile> obj,String path){
        writeObject(obj, path);
    }

    public static void writeObject(Object obj, String path){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(obj);
            FileWriter writer = new FileWriter(path + ".json");
            writer.write(json);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            Logger.errorLog("Saving json file failed.");
        }
    }

    public static List<StructuredBugReport> readBugReport(String path) {
        try {
            File file =new File(path + ".json");
            if(!file.exists()||file.length()==0) return null;
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredBugReport.class);
            return mapper.readValue(file,javaType);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.errorLog("Reading json file failed.");
            return null;
        }
    }
    public static List<StructuredCodeFile> readCodeFile(String path){
        try {
            File file =new File(path + ".json");
            if(!file.exists()||file.length()==0) return null;
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredCodeFile.class);
            return mapper.readValue(file,javaType);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.errorLog("Reading json file failed.");
            return null;
        }
    }

    public static void writeOriginBugReport(List<BugReport> reports, String path) {
        int maxSize = 100;
        int length = reports.size();
        int start = 0;
        int count = 1;
        while (start + maxSize < length) {
            writeObject(reports.subList(start, start+maxSize), path + count);
            count++;
            start += maxSize;
        }
        writeObject(reports.subList(start, length), path + count);
    }

    public static List<BugReport> readOriginBugReport(String path){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, StructuredBugReport.class);

            List<StructuredBugReport> reports = new ArrayList<>();
            int count = 1;
            File file = new File(path+count+".json");
            while (file.exists()){
                //System.out.println(count);
                reports.addAll(mapper.readValue(file, javaType));
                count++;
                file = new File(path+count+".json");
            }
            return new ArrayList<>(reports);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.errorLog("Reading json file failed.");
            return null;
        }
    }
}
