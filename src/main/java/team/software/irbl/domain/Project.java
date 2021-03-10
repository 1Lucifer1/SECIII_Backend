package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class Project {
    @TableId(value = "project_index", type = IdType.AUTO)
    private int projectIndex;
    @TableField(value = "project_name")
    private String projectName;
    @TableField(value = "code_file_count")
    private int codeFileCount;
    @TableField(exist = false)
    private List<CodeFile> codeFiles;
    @TableField(exist = false)
    private HashMap<String, ProjectWord> wordMap;

    public Project(String projectName){
        this.projectName = projectName;
    }

    public Project(int projectIndex, String projectName, int codeFileCount) {
        this.projectIndex = projectIndex;
        this.projectName = projectName;
        this.codeFileCount = codeFileCount;
    }
}
