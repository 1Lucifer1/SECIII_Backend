package team.software.irbl.core;

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
    public static void writeBugReport(List<StructuredBugReport> obj) throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.json";
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static void writeCodeFile(List<StructuredCodeFile> obj) throws IOException {
        String path = SavePath.getFilePath()+"codeFile.json";
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        FileWriter writer = new FileWriter(path);
        writer.write(json);
        writer.close();
    }
    public static List<StructuredBugReport> readBugReport() throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.json";
        File file =new File(path);
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredBugReport.class);
        return mapper.readValue(file,javaType);
    }
    public static List<StructuredCodeFile> readCodeFile() throws IOException {
        String path = SavePath.getFilePath()+"codeFile.json";
        File file =new File(path);
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,StructuredCodeFile.class);
        return mapper.readValue(file, javaType);
    }
}
