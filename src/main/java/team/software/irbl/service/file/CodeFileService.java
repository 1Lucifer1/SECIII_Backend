package team.software.irbl.service.file;

import team.software.irbl.dto.file.FileContent;
import team.software.irbl.util.Err;

public interface CodeFileService {
    public FileContent readFile(Integer fileIndex) throws Err;
}
