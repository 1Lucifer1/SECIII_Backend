package team.software.irbl.service.file;

import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.util.Err;

import java.util.List;

public interface CodeFileService {
    public FileContent readFile(Integer fileIndex) throws Err;

    public List<File> getSortedFiles(Integer reportIndex) throws Err;
}
