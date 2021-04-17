package team.software.irbl.serviceImpl.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.service.file.CodeFileService;
import team.software.irbl.util.Err;
import team.software.irbl.util.FileUtils;
import team.software.irbl.util.SavePath;

import java.io.IOException;
import java.util.*;

@Service
public class CodeFileServiceImpl implements CodeFileService {


    private CodeFileMapper codeFileMapper;

    private RankRecordMapper rankRecordMapper;

    private ProjectMapper projectMapper;

    private final static String FILE_NOTFOUND = "文件不存在";
    private final static String OTHER_ERROR = "其他问题";

    @Autowired
    public CodeFileServiceImpl(ProjectMapper projectMapper, CodeFileMapper codeFileMapper, RankRecordMapper rankRecordMapper){
        this.projectMapper = projectMapper;
        this.codeFileMapper = codeFileMapper;
        this.rankRecordMapper = rankRecordMapper;
    }

    @Override
    public FileContent readFile(Integer fileIndex) throws Err {
        CodeFile codeFile = codeFileMapper.selectById(fileIndex);
        String path = SavePath.getAbsolutePath(projectMapper.selectById(codeFile.getProjectIndex()).getProjectName()+"/"+codeFile.getFilePath());
        FileContent content = new FileContent();
        BeanUtils.copyProperties(codeFile,content);
        try {
            content.setContent(FileUtils.readFile(path));
        }
        catch (IOException e){
            throw new Err(FILE_NOTFOUND + " " + path);
        }
//        content.setSimilarity();
        return content;
    }

    @Override
    public List<File> getSortedFiles(Integer reportIndex) throws Err {
        try {
            List<RankRecord> rankRecordList = rankRecordMapper.selectList(new QueryWrapper<RankRecord>().eq("report_index", reportIndex));
            rankRecordList.sort(Comparator.comparing(RankRecord::getFileRank));
            List<File> files = new ArrayList<>();
            for (RankRecord record : rankRecordList) {
                File file = new File();
                BeanUtils.copyProperties(record, file);
                CodeFile codeFile = codeFileMapper.selectById(file.getFileIndex());
                file.setFileName(codeFile.getFileName());
                file.setPackageName(codeFile.getPackageName());
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
