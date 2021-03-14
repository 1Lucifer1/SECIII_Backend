package team.software.irbl.dto.report;

public class Report {
    private int reportIndex;
    // 要展示在界面的内容（最好再显示一个从1开始的标号，reportIndex本身不行）
    private String summary;
    // 下面这几个放不下可以不展示（或者用鼠标停留显示的方式？）
    private int bugId;
    private String openDate;
    private String fixDate;
//    private String description;


    public int getReportIndex() {
        return reportIndex;
    }

    public void setReportIndex(int reportIndex) {
        this.reportIndex = reportIndex;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
}
