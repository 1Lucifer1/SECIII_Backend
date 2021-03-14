package team.software.irbl.dto.file;

import java.util.Objects;

public class File {
    private int fileIndex;
    private String fileName;
    private double cosineSimilarity;
    private int fileRank;

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

    public double getCosineSimilarity() {
        return cosineSimilarity;
    }

    public void setCosineSimilarity(double cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    public int getFileRank() {
        return fileRank;
    }

    public void setFileRank(int fileRank) {
        this.fileRank = fileRank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileIndex,fileName,cosineSimilarity,fileRank);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)   //判断对象地址是否相等，如果是就不用判断，提高效率
            return true;
        if (obj == null)   //对象为空，则不往下走了
            return false;
        if (getClass() != obj.getClass())  //判断两个对象是否一样
            return false;
        File file =(File) obj;
        if(fileIndex != file.fileIndex){
            return false;
        }
        if (fileRank != file.fileRank){
            return false;
        }
        if(cosineSimilarity != file.cosineSimilarity){
            return false;
        }
        if (fileName == null){
            if(file.fileName!=null){
                return false;
            }
        }
        else if(!fileName.equals(file.fileName)){
            return false;
        }

        return true;
    }
}
