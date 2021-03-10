package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.HashMap;

@Data
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
    private HashMap<String, FileWord> wordMap;



    public CodeFile(String fileName, String filePath, int projectIndex){
        this.fileName = fileName;
        this.filePath = filePath;
        this.projectIndex = projectIndex;
    }

    public CodeFile(int fileIndex, int projectIndex, String fileName, String filePath, int wordCount) {
        this.fileIndex = fileIndex;
        this.projectIndex = projectIndex;
        this.fileName = fileName;
        this.filePath = filePath;
        this.wordCount = wordCount;
    }
}

