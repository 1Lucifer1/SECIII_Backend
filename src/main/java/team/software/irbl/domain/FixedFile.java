package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.TableField;

public class FixedFile {
    private int id;
    @TableField(value = "report_index")
    private int reportIndex;
    @TableField(value = "file_index")
    private int fileIndex;

    public FixedFile(int reportIndex, int fileIndex) {
        this.reportIndex = reportIndex;
        this.fileIndex = fileIndex;
    }

    public FixedFile(int id, int reportIndex, int fileIndex) {
        this.id = id;
        this.reportIndex = reportIndex;
        this.fileIndex = fileIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportIndex() {
        return reportIndex;
    }

    public void setReportIndex(int reportIndex) {
        this.reportIndex = reportIndex;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }
}
