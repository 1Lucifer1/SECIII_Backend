package team.software.irbl.serviceImpl.file;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.service.file.CodeFileService;
import team.software.irbl.util.Err;
import team.software.irbl.util.FileUtils;
import team.software.irbl.util.SavePath;

import java.io.IOException;

@Service
public class CodeFileServiceImpl implements CodeFileService {
    @Autowired
    private CodeFileMapper codeFileMapper;

    private final static String FILE_NOTFOUND = "文件不存在";

    @Override
    public FileContent readFile(Integer fileIndex) throws Err{
        CodeFile codeFile = codeFileMapper.selectById(fileIndex);
        String path = SavePath.getAbsolutePath(codeFile.getFilePath());
        FileContent content = new FileContent();
        BeanUtils.copyProperties(codeFile,content);
        try {
            content.setContent(FileUtils.readFile(path));
        }
        catch (IOException e){
            throw new Err(FILE_NOTFOUND);
        }
//        content.setSimilarity();
        return content;
    }
}
