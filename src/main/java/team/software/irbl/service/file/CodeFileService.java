package team.software.irbl.service.file;

import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.util.Err;

import java.util.List;

public interface CodeFileService {
    /**
     * 读取文件内容
     * @param fileIndex
     * @return
     * @throws Err
     */
    public FileContent readFile(Integer fileIndex) throws Err;

    /**
     * 读取指定缺陷报告下按相似度降序排列的文件列表
     * @param reportIndex
     * @return
     * @throws Err
     */
    public List<File> getSortedFiles(Integer reportIndex) throws Err;
}
