package team.software.irbl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import team.software.irbl.core.utils.Calculate;
import team.software.irbl.core.utils.Preprocess;
import team.software.irbl.domain.*;
import team.software.irbl.enums.WordType;
import team.software.irbl.mapper.*;
import team.software.irbl.util.SavePath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportProcess {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private BugReportMapper bugReportMapper;

    @Autowired
    private FixedFileMapper fixedFileMapper;

    @Autowired
    private FileWordMapper fileWordMapper;

    @Autowired
    private RankRecordMapper rankRecordMapper;

    public List<BugReport> getBugReportsFromXML(String reportFileName, int projectIndex){
        List<BugReport> bugReports = new ArrayList<>();
        File file = new File(SavePath.getAbsolutePath(reportFileName));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nodeList = doc.getElementsByTagName("bug");
            for(int i=0; i<nodeList.getLength(); ++i){
                NamedNodeMap attributes = nodeList.item(i).getAttributes();
                int bugId = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                String openDate = attributes.getNamedItem("opendate").getNodeValue();
                String fixDate = attributes.getNamedItem("fixdate").getNodeValue();

                String summary = "";
                String description = "";
                List<FixedFile> files = new ArrayList<>();
                for(Node insideNode = nodeList.item(i).getFirstChild(); insideNode!=null; insideNode = insideNode.getNextSibling()){
                    if(insideNode.getNodeType()!=Node.ELEMENT_NODE){
                        continue;
                    }
                    if(insideNode.getNodeName().equals("buginformation")){
                        for(Node node = insideNode.getFirstChild(); node!=null; node=node.getNextSibling()){
                            if(node.getNodeType() == Node.ELEMENT_NODE){
                                if(node.getNodeName().equals("summary")){
                                    if(node.getFirstChild()!=null) summary = node.getFirstChild().getNodeValue();
                                }else if(node.getNodeName().equals("description")){
                                    if(node.getFirstChild()!=null) description = node.getFirstChild().getNodeValue();
                                }
                            }
                        }
                    }else if(insideNode.getNodeName().equals("fixedFiles")){
                        for(Node node=insideNode.getFirstChild(); node!=null; node=node.getNextSibling()){
                            if(node.getNodeType() == Node.ELEMENT_NODE ){
                                //String filePath =SavePath.getPathFromPackage(node.getFirstChild().getNodeValue());
                                //CodeFile fixedFile = codeFileMapper.selectOne(new QueryWrapper<CodeFile>()
                                //        .eq("project_index", projectIndex)
                                //        .like("file_path", "%"+filePath));
                                //if(fixedFile != null){
                                    //files.add(new FixedFile(-1, fixedFile.getFileIndex()));
                                //}
                                files.add(new FixedFile(-1, node.getFirstChild().getNodeValue()));
                            }
                        }
                    }
                }
                bugReports.add(new BugReport(projectIndex, bugId, openDate, fixDate, summary, description,files));
            }
            bugReportMapper.insertBatchSomeColumn(bugReports);
            Project project = projectMapper.selectById(projectIndex);
            project.setReportCount(bugReports.size());
            projectMapper.updateById(project);
            scanReportWord(bugReports);
            return bugReports;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void scanReportWord(List<BugReport> bugReports){
        bugReports.forEach( bugReport -> {
            bugReport.getFixedFiles().forEach( fixedFile -> {
                fixedFile.setReportIndex(bugReport.getReportIndex());
            });
            fixedFileMapper.insertBatchSomeColumn(bugReport.getFixedFiles());
            String report = Preprocess.preprocessBugReport(bugReport);
            ConcurrentHashMap<String, FileWord> wordMap = new ConcurrentHashMap<>();
            int wordCount = 0;
            for(String word:report.split(" ")){
                wordCount++;
                if(wordMap.containsKey(word)){
                    wordMap.get(word).addAppearTimes();
                }else{
                    wordMap.put(word, new FileWord(word, bugReport.getReportIndex(), WordType.BugReportWord));
                }
            }
            // 避免lambda表达式报错
            final int finalCount = wordCount;
            wordMap.values().forEach(word -> {
                double tf = Calculate.calculateTf(word.getAppearTimes());
                word.setTf(tf);
            });
            List<FileWord> words = new ArrayList<>(wordMap.values());
            fileWordMapper.insertBatchSomeColumn(words);
            //bugReport.setWordCount(wordCount);
            //bugReport.setWordMap(wordMap);
        });
        bugReportMapper.insertOrUpdateBatch(bugReports);
    }

    public List<BugReport> getBugReportsFromDB(int projectIndex){
        List<BugReport> bugReports = bugReportMapper.selectList(new QueryWrapper<BugReport>().eq("project_index", projectIndex));
        bugReports.forEach(bugReport -> {
            List<FixedFile> fixedFiles = fixedFileMapper.selectList(new QueryWrapper<FixedFile>().eq("report_index", bugReport.getReportIndex()));
            bugReport.setFixedFiles(fixedFiles);
            List<FileWord> words = fileWordMapper.selectList(new QueryWrapper<FileWord>()
                                                                .eq("file_index", bugReport.getReportIndex())
                                                                .eq("type", WordType.BugReportWord.value()));
            ConcurrentHashMap<String, FileWord> wordMap = new ConcurrentHashMap<>();
            words.forEach(word -> {
                wordMap.put(word.getWord(), word);
            });
            //bugReport.setWordMap(wordMap);
        });
        return bugReports;
    }

    public void saveRanks(List<RankRecord> records){
        rankRecordMapper.insertOrUpdateBatch(records);
    }
}
