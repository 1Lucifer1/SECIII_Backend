package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BugReport {
    @TableId(value = "report_index", type = IdType.AUTO)
    private int reportIndex;
    @TableField(value = "project_index")
    private int projectIndex;
    @TableField(value = "bug_id")
    private int bugId;
    @TableField(value = "open_date")
    private String openDate;
    @TableField(value = "fix_date")
    private String fixDate;
    private String summary;
    @TableField(value = "word_count")
    private int wordCount;
    @TableField(exist = false)
    private String description;
    @TableField(exist = false)
    private List<FixedFile> fixedFiles;
    @TableField(exist = false)
    private ConcurrentHashMap<String, FileWord> wordMap;
    @TableField(exist = false)
    private List<RankRecord> ranks;


    public BugReport(int projectIndex, int bugId, String openDate, String fixDate, String summary, String description, List<FixedFile> fixedFiles) {
        this.projectIndex = projectIndex;
        this.bugId = bugId;
        this.openDate = openDate;
        this.fixDate = fixDate;
        this.summary = summary;
        this.description = description;
        this.fixedFiles = fixedFiles;
        this.wordCount = 0;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param //所有表中字段对应属性
     */
    public BugReport(int reportIndex, int projectIndex, int bugId, String openDate, String fixDate, String summary, int wordCount) {
        this.reportIndex = reportIndex;
        this.projectIndex = projectIndex;
        this.bugId = bugId;
        this.openDate = openDate;
        this.fixDate = fixDate;
        this.summary = summary;
        this.wordCount = wordCount;
    }

    public int getReportIndex() {
        return reportIndex;
    }

    public void setReportIndex(int reportIndex) {
        this.reportIndex = reportIndex;
    }

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getFixDate() {
        return fixDate;
    }

    public void setFixDate(String fixDate) {
        this.fixDate = fixDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FixedFile> getFixedFiles() {
        return fixedFiles;
    }

    public void setFixedFiles(List<FixedFile> fixedFiles) {
        this.fixedFiles = fixedFiles;
    }

    public ConcurrentHashMap<String, FileWord> getWordMap() {
        return wordMap;
    }

    public void setWordMap(ConcurrentHashMap<String, FileWord> wordMap) {
        this.wordMap = wordMap;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public List<RankRecord> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankRecord> ranks) {
        this.ranks = ranks;
    }
}
