package team.software.irbl.core.domain;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;

import java.util.List;

public class StructuredBugReport{

    private List<String> summaryWords;
    private List<String> descriptionWords;
    /**
     * 采用装饰模式而非继承，目的在于解耦从xml中读取bugReport的操作
     */
    private BugReport bugReport;
    /*private int reportIndex;
    private int projectIndex;
    private int bugId;
    private String openDate;
    private String fixDate;
    private String summary;
    private String description;
    List<FixedFile> fixedFiles;
    List<RankRecord> ranks;*/

    public StructuredBugReport(){};

    public StructuredBugReport(BugReport bugReport){
        this.bugReport = bugReport;
    }


    public BugReport getBugReport() { return bugReport; }
    public void setBugReport(BugReport bugReport) {this.bugReport = bugReport;}

    public List<String> getSummaryWords() {
        return summaryWords;
    }

    public void setSummaryWords(List<String> summaryWords) {
        this.summaryWords = summaryWords;
    }

    public List<String> getDescriptionWords() {
        return descriptionWords;
    }

    public void setDescriptionWords(List<String> descriptionWords) {
        this.descriptionWords = descriptionWords;
    }

    public int getReportIndex() {
        return bugReport.getReportIndex();
    }

    public void setReportIndex(int reportIndex) {
        bugReport.setReportIndex(reportIndex);
    }

    public int getProjectIndex() {
        return bugReport.getProjectIndex();
    }

    public void setProjectIndex(int projectIndex) {
        bugReport.setReportIndex(projectIndex);
    }

    public int getBugId() {
        return bugReport.getBugId();
    }

    public void setBugId(int bugId) {
        bugReport.setBugId(bugId);
    }

    public String getOpenDate() {
        return bugReport.getOpenDate();
    }

    public void setOpenDate(String openDate) {
        bugReport.setOpenDate(openDate);
    }

    public String getFixDate() {
        return bugReport.getFixDate();
    }

    public void setFixDate(String fixDate) {
        bugReport.setFixDate(fixDate);
    }

    public String getSummary() {
        return bugReport.getSummary();
    }

    public void setSummary(String summary) {
        bugReport.setSummary(summary);
    }

    public String getDescription() {
        return bugReport.getDescription();
    }

    public void setDescription(String description) {
        bugReport.setDescription(description);
    }

    public List<FixedFile> getFixedFiles() {
        return bugReport.getFixedFiles();
    }

    public void setFixedFiles(List<FixedFile> fixedFiles) {
        bugReport.setFixedFiles(fixedFiles);
    }

    public List<RankRecord> getRanks() {
        return bugReport.getRanks();
    }

    public void setRanks(List<RankRecord> ranks) {
        bugReport.setRanks(ranks);
    }
}
