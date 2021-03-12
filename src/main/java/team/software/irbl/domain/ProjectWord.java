package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


public class ProjectWord {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField(value = "project_index")
    private int projectIndex;
    private String word;
    @TableField(value = "appear_files")
    private int appearFiles;
    private double idf;

    public ProjectWord(String word, int projectIndex){
        this.word = word;
        this.projectIndex = projectIndex;
        this.appearFiles = 1;
    }

    public void addAppearFiles(){
        appearFiles++;
    }

    public void addAppearFiles(int num){
        appearFiles += num;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param 所有表中字段对应属性
     */
    public ProjectWord(int id, int projectIndex, String word, int appearFiles, double idf) {
        this.id = id;
        this.projectIndex = projectIndex;
        this.word = word;
        this.appearFiles = appearFiles;
        this.idf = idf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getAppearFiles() {
        return appearFiles;
    }

    public void setAppearFiles(int appearFiles) {
        this.appearFiles = appearFiles;
    }

    public double getIdf() {
        return idf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
    }
}
