package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class RankRecord implements Comparable<RankRecord> {
    @TableField(value = "report_index")
    private int reportIndex;
    @TableField(value = "file_index")
    private int fileIndex;
    @TableField(value = "file_rank")
    private int fileRank;
    @TableField(value = "score")
    private double score;

    public RankRecord(){}
    public RankRecord(int reportIndex, int fileIndex, double score) {
        this.reportIndex = reportIndex;
        this.fileIndex = fileIndex;
        this.score = score;
    }

    public RankRecord(int reportIndex, int fileIndex, int fileRank, double score) {
        this.reportIndex = reportIndex;
        this.fileIndex = fileIndex;
        this.fileRank = fileRank;
        this.score = score;
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

    public int getFileRank() {
        return fileRank;
    }

    public void setFileRank(int fileRank) {
        this.fileRank = fileRank;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(RankRecord record) {
        return Double.compare(score, record.getScore());
    }
}

