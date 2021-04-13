package team.software.irbl.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import team.software.irbl.core.domain.StructuredBugReport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.util.SavePath;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTranslator {
    public static void writeBugReport(StructuredBugReport obj) throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.txt";
        File file =new File(path);
        String content="";
        if(file.exists()&&file.length()!=0){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            //System.out.println(len);
            content = new String(data,0,len);
            //System.out.println(content);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        //mapper.writeValue(new File("/Users/fengxinze/Desktop/IRBL/json/src/main/java/a.txt"),json);
        System.out.println(json);
        FileWriter writer = new FileWriter(path);
        writer.write(content+json+" ");
        writer.close();
    }
    public  void writeCodeFile(StructuredCodeFile obj) throws IOException {
        String path = SavePath.getFilePath()+"codeFile.txt";
        File file =new File(path);
        String content="";
        if(file.exists()&&file.length()!=0){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            //System.out.println(data);
            content = new String(data,0,len);
            //System.out.println(content);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        //mapper.writeValue(new File("/Users/fengxinze/Desktop/IRBL/json/src/main/java/a.txt"),json);
        //System.out.println(json);
        FileWriter writer = new FileWriter(path);
        writer.write(content+json+" ");
        writer.close();
    }
    public static List<StructuredBugReport> readBugReport() throws IOException {
        String path = SavePath.getFilePath()+"bugReportFile.txt";
        File file =new File(path);
        String content="";
        if(file.exists()&&file.length()!=0){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[1024];
            int len=input.read(data);
            //System.out.println(data);
            content = new String(data,0,len);
            System.out.println(content);
        }
        String[] jsons = content.split(" ");
        int l = jsons.length;
        List<StructuredBugReport> res = new ArrayList<>();
        //StructuredBugReport[] res = new StructuredBugReport[l];
        for(int i=0;i<l;i++){
            ObjectMapper mapper = new ObjectMapper();
            StructuredBugReport br = mapper.readValue(jsons[i], StructuredBugReport.class);
            res.add(br);
        }
        return res;
    }
    public List<StructuredCodeFile> readCodeFile() throws IOException {
        String path = SavePath.getFilePath()+"codeFile.txt";
        File file =new File(path);
        String content="";
        if(file.exists()&&file.length()!=0){
            InputStream input=new FileInputStream(file);
            byte[] data=new byte[2048];
            int len=input.read(data);
            //System.out.println(data);
            content = new String(data,0,len);
            //System.out.println(content);
        }
        String[] jsons = content.split(" ");
        int l = jsons.length;
        List<StructuredCodeFile> res = new ArrayList<>();
        for(int i=0;i<l;i++){
            ObjectMapper mapper = new ObjectMapper();
            StructuredCodeFile cf = mapper.readValue(jsons[i], StructuredCodeFile.class);
            res.add(cf);
        }
        return res;
    }
}
