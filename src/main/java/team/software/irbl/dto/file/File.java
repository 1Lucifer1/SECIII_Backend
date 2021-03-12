package team.software.irbl.dto.file;

public class File {
    private int fileIndex;
    private String fileName;
    private double Similarity;

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

    public double getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(double similarity) {
        Similarity = similarity;
    }
}
