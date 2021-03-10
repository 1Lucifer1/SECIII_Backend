package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class FileWord {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField(value = "file_index")
    private int fileIndex;
    private String word;
    @TableField(value = "appear_times")
    private int appearTimes;
    private int tf;

    public FileWord(String word, int appearTimes){
        this.word = word;
        this.appearTimes = appearTimes;
    }

    public FileWord(int id, int fileIndex, String word, int appearTimes, int tf) {
        this.id = id;
        this.fileIndex = fileIndex;
        this.word = word;
        this.appearTimes = appearTimes;
        this.tf = tf;
    }
}
