package team.software.irbl.core.versionHistoryComponent;

import team.software.irbl.domain.FixedFile;

import java.util.List;

public class CommitInfo {

    private int ProjectIndex;

    private String id;

    private String author;

    private String date;

    private String title;

    private List<FixedFile> fixedFiles;

    private int daysBetween;

    public int getProjectIndex() {
        return ProjectIndex;
    }

    public void setProjectIndex(int projectIndex) {
        ProjectIndex = projectIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FixedFile> getFixedFiles() {
        return fixedFiles;
    }

    public void setFixedFiles(List<FixedFile> fixedFiles) {
        this.fixedFiles = fixedFiles;
    }

    public int getDaysBetween() {
        return daysBetween;
    }

    public void setDaysBetween(int daysBetween) {
        this.daysBetween = daysBetween;
    }
}
