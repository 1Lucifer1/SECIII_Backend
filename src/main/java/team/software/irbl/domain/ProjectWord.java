package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProjectWord {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField(value = "project_index")
    private int projectIndex;
    private String word;
    @TableField(value = "appear_files")
    private int appearFiles;
    private double idf;

    public ProjectWord(String word, int appearFiles){
        this.word = word;
        this.appearFiles = appearFiles;
    }

    public ProjectWord(int id, int projectIndex, String word, int appearFiles, double idf) {
        this.id = id;
        this.projectIndex = projectIndex;
        this.word = word;
        this.appearFiles = appearFiles;
        this.idf = idf;
    }
}
