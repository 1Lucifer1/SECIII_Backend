package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;


public class FileWord {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField(value = "file_index")
    private int fileIndex;
    private String word;
    @TableField(value = "appear_times")
    private int appearTimes;
    private double tf;

    public FileWord(String word, int fileIndex){
        this.word = word;
        this.fileIndex = fileIndex;
        this.appearTimes = 1;
    }

    public void addAppearTimes(){
        appearTimes++;
    }

    public void addAppearTimes(int num){
        appearTimes += num;
    }
    /**
     * 用于mybatis-plus的构造函数
     *
     * @param 所有表中字段对应属性
     */
    public FileWord(int id, int fileIndex, String word, int appearTimes, int tf) {
        this.id = id;
        this.fileIndex = fileIndex;
        this.word = word;
        this.appearTimes = appearTimes;
        this.tf = tf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getAppearTimes() {
        return appearTimes;
    }

    public void setAppearTimes(int appearTimes) {
        this.appearTimes = appearTimes;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }
}
