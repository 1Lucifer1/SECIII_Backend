package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.concurrent.ConcurrentHashMap;


public class CodeFile {
    @TableId(value = "file_index", type = IdType.AUTO)
    protected int fileIndex;
    @TableField(value = "project_index")
    protected int projectIndex;
    @TableField(value = "file_name")
    protected String fileName;
    @TableField(value = "file_path")
    protected String filePath;
    @TableField(value = "package_name")
    protected String packageName;
    protected double score;

    public CodeFile(){}

    public CodeFile(String fileName, String filePath, int projectIndex){
        this.fileName = fileName;
        this.filePath = filePath;
        this.projectIndex = projectIndex;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param所有表中字段对应属性
     */
    public CodeFile(int fileIndex, int projectIndex, String fileName, String filePath, String packageName) {
        this.fileIndex = fileIndex;
        this.projectIndex = projectIndex;
        this.fileName = fileName;
        this.filePath = filePath;
        this.packageName = packageName;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double rank) {
        this.score = score;
    }
}

