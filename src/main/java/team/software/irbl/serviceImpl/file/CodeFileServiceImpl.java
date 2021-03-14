package team.software.irbl.serviceImpl.file;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.service.file.CodeFileService;
import team.software.irbl.util.Err;
import team.software.irbl.util.FileUtils;
import team.software.irbl.util.SavePath;

import java.io.IOException;
import java.util.*;

@Service
public class CodeFileServiceImpl implements CodeFileService {
    @Autowired
    private CodeFileMapper codeFileMapper;
    @Autowired
    private RankRecordMapper rankRecordMapper;

    private final static String FILE_NOTFOUND = "文件不存在";
    private final static String OTHER_ERROR = "其他问题";

    @Override
    public FileContent readFile(Integer fileIndex) throws Err {
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

    @Override
    public List<File> getSortedFiles(Integer reportIndex) throws Err {
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("report_index", reportIndex);
            List<RankRecord> rankRecordList = rankRecordMapper.selectByMap(conditions);
            rankRecordList.sort(Comparator.comparing(RankRecord::getFileRank));
            List<File> files = new ArrayList<>();
            for (RankRecord record : rankRecordList) {
                File file = new File();
                BeanUtils.copyProperties(record, file);
                CodeFile codeFile = codeFileMapper.selectById(file.getFileIndex());
                file.setFileName(codeFile.getFileName());
                files.add(file);
            }
            return files;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            throw new Err(OTHER_ERROR);
        }
    }
}
