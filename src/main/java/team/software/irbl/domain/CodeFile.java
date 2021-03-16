package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.concurrent.ConcurrentHashMap;


public class CodeFile {
    @TableId(value = "file_index", type = IdType.AUTO)
    private int fileIndex;
    @TableField(value = "project_index")
    private int projectIndex;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "file_path")
    private String filePath;
    @TableField(value = "word_count")
    private int wordCount;
    @TableField(exist = false)
    private ConcurrentHashMap<String, FileWord> wordMap;



    public CodeFile(String fileName, String filePath, int projectIndex){
        this.fileName = fileName;
        this.filePath = filePath;
        this.projectIndex = projectIndex;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param 所有表中字段对应属性
     */
    public CodeFile(int fileIndex, int projectIndex, String fileName, String filePath, int wordCount) {
        this.fileIndex = fileIndex;
        this.projectIndex = projectIndex;
        this.fileName = fileName;
        this.filePath = filePath;
        this.wordCount = wordCount;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public ConcurrentHashMap<String, FileWord> getWordMap() {
        return wordMap;
    }

    public void setWordMap(ConcurrentHashMap<String, FileWord> wordMap) {
        this.wordMap = wordMap;
    }
}

