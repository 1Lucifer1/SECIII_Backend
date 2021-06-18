package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class Project {
    @TableId(value = "project_index")
    private int projectIndex;
    @TableField(value = "project_name")
    private String projectName;
    @TableField(value = "code_file_count")
    private int codeFileCount;
    @TableField(value = "report_count")
    private int reportCount;
    @TableField(exist = false)
    private List<CodeFile> codeFiles;
    @TableField(exist = false)
    private ConcurrentHashMap<String, ProjectWord> wordMap;

    public Project(String projectName){
        this.projectName = projectName;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param 所有表中字段对应属性
     */
    public Project(int projectIndex, String projectName, int codeFileCount, int reportCount) {
        this.projectIndex = projectIndex;
        this.projectName = projectName;
        this.codeFileCount = codeFileCount;
        this.reportCount = reportCount;
    }

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCodeFileCount() {
        return codeFileCount;
    }

    public void setCodeFileCount(int codeFileCount) {
        this.codeFileCount = codeFileCount;
    }

    public List<CodeFile> getCodeFiles() {
        return codeFiles;
    }

    public void setCodeFiles(List<CodeFile> codeFiles) {
        this.codeFiles = codeFiles;
    }

    public ConcurrentHashMap<String, ProjectWord> getWordMap() {
        return wordMap;
    }

    public void setWordMap(ConcurrentHashMap<String, ProjectWord> wordMap) {
        this.wordMap = wordMap;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
}
