package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.TableField;

public class FixedFile {
    private int id;
    @TableField(value = "report_index")
    private int reportIndex;
    @TableField(value = "file_package_name")
    private String filePackageName;
    @TableField(value = "file_index")
    private int fileIndex;

    public FixedFile(int reportIndex, String filePackageName) {
        this.reportIndex = reportIndex;
        this.filePackageName = filePackageName;
    }

    public FixedFile(int id, int reportIndex, int fileIndex, String filePackageName) {
        this.id = id;
        this.reportIndex = reportIndex;
        this.fileIndex = fileIndex;
        this.filePackageName = filePackageName;
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

    public String getFilePackageName() {
        return filePackageName;
    }

    public void setFilePackageName(String filePackageName) {
        this.filePackageName = filePackageName;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }
}
