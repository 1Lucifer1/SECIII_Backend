package team.software.irbl.core.filestore;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.software.irbl.core.domain.StructuredBugReport;

import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.util.SavePath;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTranslator {
    public static void writeBugReport(List<StructuredBugReport> obj,String lastPath) throws IOException {
        String path = SavePath.getFilePath()+lastPath;
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static void writeCodeFile(List<StructuredCodeFile> obj,String lastPath) throws IOException {
        String path = SavePath.getFilePath()+lastPath;
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static List<StructuredBugReport> readBugReport(String lastPath) throws IOException {
        String path = SavePath.getFilePath()+lastPath;
        File file =new File(path);
        if(!file.exists()||file.length()==0) return null;
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredBugReport.class);
        return mapper.readValue(file,javaType);
    }
    public static List<StructuredCodeFile> readCodeFile(String latsPath) throws IOException {
        String path = SavePath.getFilePath()+latsPath;
        File file =new File(path);
        //System.out.println(file.length());
        if(!file.exists()||file.length()==0) return null;
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredCodeFile.class);
        return mapper.readValue(file, javaType);
    }
}
