package team.software.irbl.serviceImpl.file;

import org.springframework.stereotype.Service;
import team.software.irbl.service.file.CodeFileService;

@Service
public class CodeFileServiceImpl implements CodeFileService {

    @Override
    public String readFile(Integer fileIndex) {
        return "文件";
    }
}
