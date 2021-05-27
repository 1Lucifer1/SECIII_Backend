package team.software.irbl.core.filestore;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import team.software.irbl.core.domain.StructuredBugReport;

import team.software.irbl.core.domain.StructuredCodeFile;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTranslator {
    public static void writeBugReport(List<StructuredBugReport> obj,String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path + ".json");
        writer.write(json);
        writer.close();
    }
    public static void writeCodeFile(List<StructuredCodeFile> obj,String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path+ ".json");
        writer.write(json);
        writer.close();
    }
    public static List<StructuredBugReport> readBugReport(String path) throws IOException {
        File file =new File(path + ".json");
        if(!file.exists()||file.length()==0) return null;
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredBugReport.class);
        return mapper.readValue(file,javaType);
    }
    public static List<StructuredCodeFile> readCodeFile(String path) throws IOException {
        File file =new File(path + ".json");
        if(!file.exists()||file.length()==0) return null;
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredCodeFile.class);
        return mapper.readValue(file, javaType);
    }
}
