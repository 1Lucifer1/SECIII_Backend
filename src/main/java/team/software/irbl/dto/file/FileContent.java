package team.software.irbl.dto.file;

public class FileContent {
    private int fileIndex;
    private String fileName;
    private String filePath;
    private double Similarity;
    private String content;

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(double similarity) {
        Similarity = similarity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
