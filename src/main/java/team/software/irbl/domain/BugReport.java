package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.List;

public class BugReport {
    @TableId(value = "report_index", type = IdType.AUTO)
    protected int reportIndex;
    @TableField(value = "project_index")
    protected int projectIndex;
    @TableField(value = "bug_id")
    protected int bugId;
    @TableField(value = "open_date")
    protected String openDate;
    @TableField(value = "fix_date")
    protected String fixDate;
    protected String summary;
    @TableField(exist = false)
    protected String description;
    @TableField(exist = false)
    protected List<FixedFile> fixedFiles;
    @TableField(exist = false)
    protected List<RankRecord> ranks;

    public BugReport(){}
    public BugReport(int projectIndex, int bugId, String openDate, String fixDate, String summary, String description, List<FixedFile> fixedFiles) {
        this.projectIndex = projectIndex;
        this.bugId = bugId;
        this.openDate = openDate;
        this.fixDate = fixDate;
        this.summary = summary;
        this.description = description;
        this.fixedFiles = fixedFiles;
    }

    /**
     * 用于mybatis-plus的构造函数
     *
     * @param //所有表中字段对应属性
     */
    public BugReport(int reportIndex, int projectIndex, int bugId, String openDate, String fixDate, String summary) {
        this.reportIndex = reportIndex;
        this.projectIndex = projectIndex;
        this.bugId = bugId;
        this.openDate = openDate;
        this.fixDate = fixDate;
        this.summary = summary;
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

    public List<RankRecord> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankRecord> ranks) {
        this.ranks = ranks;
    }


}
