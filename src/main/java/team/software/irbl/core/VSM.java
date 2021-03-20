package team.software.irbl.core;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.domain.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VSM {

    @Autowired
    private ReportProcess reportProcess;

    @Autowired
    private ProjectProcess projectProcess;


    public void startLocalRank(){
        Project project = projectProcess.getProject("swt-3.1");
        List<BugReport> reports = reportProcess.getBugReportsFromDB(project.getProjectIndex());
        if(reports.size() == 0){
            reports = reportProcess.getBugReportsFromXML("SWTBugRepository.xml", project.getProjectIndex());
        }
        reports.forEach(bugReport -> {
            reportProcess.saveRanks(rank(bugReport, project.getCodeFiles(), project.getWordMap()));
        });
    }

    public List<RankRecord> rank(BugReport bugReport, List<CodeFile> codeFiles, ConcurrentHashMap<String, ProjectWord> lexicon){
        List<RankRecord> ranks = new ArrayList<>(codeFiles.size());
        codeFiles.forEach(codeFile -> {
            double similarity = getCosineSimilarity(bugReport, codeFile, lexicon);
            ranks.add(new RankRecord(bugReport.getReportIndex(), codeFile.getFileIndex(), similarity));
        });
        ranks.sort(Comparator.reverseOrder());
        for(int i=0; i<ranks.size(); ++i){
            ranks.get(i).setFileRank(i+1);
        }
        return ranks;
    }


    public double getCosineSimilarity(BugReport bugReport, CodeFile codeFile, ConcurrentHashMap<String, ProjectWord> lexicon){
        List<FileWord> reportWords = new ArrayList<>(bugReport.getWordMap().values());
        ConcurrentHashMap<String, FileWord> fileWordMap = codeFile.getWordMap();
        double innerProduct = 0;
        double lengthOfVectorX = 0;
        double lengthOfVectorY = 0;
        for(FileWord word: reportWords){
            String wordS = word.getWord();
            double idf;
            if(lexicon.containsKey(wordS)) {
                idf = lexicon.get(wordS).getIdf();
            }else {
                idf = 0;
            }
            double tfIdfX = word.getTf() * idf;
            double tfIdfY;
            if(fileWordMap.containsKey(wordS)){
                tfIdfY = fileWordMap.get(wordS).getTf() * idf;
            }else {
                tfIdfY = 0;
            }

            innerProduct += tfIdfX * tfIdfY;
            lengthOfVectorX += tfIdfX * tfIdfX;
            lengthOfVectorY += tfIdfY * tfIdfY;
        }
        if(lengthOfVectorX == 0 || lengthOfVectorY == 0){
            return 0;
        }
        return innerProduct / (Math.sqrt(lengthOfVectorX) * Math.sqrt(lengthOfVectorY));
    }
}
