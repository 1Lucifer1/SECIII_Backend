package team.software.irbl.dto.project;

public class ProjectInfo {
    private int projectIndex;

    private String projectName;

    private int codeFileCount;

    private int reportCount;

    public int getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        this.projectIndex = projectIndex;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCodeFileCount() {
        return codeFileCount;
    }

    public void setCodeFileCount(int codeFileCount) {
        this.codeFileCount = codeFileCount;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
}
