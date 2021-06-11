package team.software.irbl.core.similarReportComponent;

import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.dbstore.DBProcessor;
import team.software.irbl.core.dbstore.DBProcessorFake;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.filestore.XMLParser;
import team.software.irbl.core.jdt.JavaParser;
import team.software.irbl.core.maptool.CodeFileMap;
import team.software.irbl.core.maptool.PackageMap;
import team.software.irbl.core.reporterComponent.ReporterRank;
import team.software.irbl.core.vsm.Lexicon;
import team.software.irbl.core.vsm.SubLexicon;
import team.software.irbl.core.vsm.VSM;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.SavePath;

import java.time.LocalTime;
import java.util.*;

import static team.software.irbl.core.filestore.XMLParser.getBugReportsFromXML;

public class SimilarReportRank {
    //private List<StructuredCodeFile> codeFile;
    private CodeFileMap codeFileMap;
    //private List<StructuredBugReport> reports = new ArrayList<>();
    private List<BugReport> reports = new ArrayList<>();
    Map<CodeFile, Double> fileScore;
    public SimilarReportRank(CodeFileMap codeFileMap){
        //this.codeFile = codeFiles;
        this.codeFileMap = codeFileMap;
    }
    public void init(){
        fileScore = new HashMap<>();
        List<CodeFile> codeFile = codeFileMap.values();
        for(int n=0;n<codeFile.size();n++){
            fileScore.put(codeFile.get(n),0.0);
        }
    }
    //获得已经有的所有bugReport的内容
    public List<List<String>> getFiles(){
        List<List<String>> files = new ArrayList<>();
        for(int i=0;i<reports.size();i++){
            List<String> file = new ArrayList<>();
            file.addAll(Collections.singletonList(reports.get(i).getDescription()));
            file.addAll(Collections.singletonList(reports.get(i).getSummary()));
            files.add(file);
        }
        return files;
    }
    //得到每个源文件的得分
    public void getRank(BugReport bugReport){
        List<List<String>> files = getFiles();
        List<String> file = new ArrayList<>();
        //file.addAll(bugReport.getDescriptionWords());
        //file.addAll(bugReport.getSummaryWords());
        file.addAll(Collections.singletonList(bugReport.getDescription()));
        file.addAll(Collections.singletonList(bugReport.getSummary()));

        VSM lexicon = new VSM(files);
        double[] score = lexicon.getScores(file);
        //System.out.println(score.length);


        for(int i=0;i<score.length;i++){
            BugReport oldBugReport = reports.get(i);
            List<FixedFile> fixedFiles = oldBugReport.getFixedFiles();
            String newDate = bugReport.getOpenDate();
            //System.out.println(newDate);
            String oldDate = oldBugReport.getFixDate();
            //System.out.println(oldDate);
            int res = newDate.compareTo(oldDate);
//            if(res>0){
//                //System.out.println("come in");
//                double newRank = score[i]/Math.sqrt(fixedFiles.size());
//                for(int j=0;j<fixedFiles.size();j++){
//                    List<CodeFile> codeFiles = codeFileMap.getCodeFileFromMap(fixedFiles.get(j).getFilePackageName());
//                    for(int k=0;k<codeFiles.size();k++){
//                        fileScore.put(codeFiles.get(k),fileScore.get(codeFiles.get(k))+newRank);
//                    }
//                }
//            }
            double newRank = score[i]/Math.sqrt(fixedFiles.size());
            for(int j=0;j<fixedFiles.size();j++){
                List<CodeFile> codeFiles = codeFileMap.getCodeFileFromMap(fixedFiles.get(j).getFilePackageName());
                for(int k=0;k<codeFiles.size();k++){
                    fileScore.put(codeFiles.get(k),fileScore.get(codeFiles.get(k))+newRank);
                }
            }
        }
//        System.out.println("查看得分:");
//        for(int i=0;i<fileScore.size();i++){
//            System.out.println(fileScore.get(codeFile.get(i)));
//        }
    }
    //得到排序
    public List<RankRecord> rank(BugReport bugReport){
        List<CodeFile> cf = codeFileMap.values();
        this.reports.add(bugReport);
        getRank(bugReport);
        List<RankRecord> records = new ArrayList<>();
        for(int i=0;i<cf.size()-1;i++){
            for(int j=1;j<cf.size();j++){
                if(fileScore.get(cf.get(j))<fileScore.get(cf.get(j-1))){
                    CodeFile tem = cf.get(j);
                    cf.set(j,cf.get(j-1));
                    cf.set(j-1,tem);
                }
            }
        }
        for(int i=0;i<cf.size();i++){
            records.add(new RankRecord(bugReport.getReportIndex(), cf.get(i).getFileIndex(), -1, fileScore.get(cf.get(i))));
        }
        return records;
    }
    public static void main(String[] args){
        String projectName = "swt-3.1";
        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, codeFileMap);

        SimilarReportRank similarReportRank = new SimilarReportRank(codeFileMap);
        similarReportRank.init();

        assert reports != null;
        reports.forEach(report -> {
            List<RankRecord> records = similarReportRank.rank(report);
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        System.out.println("Top@1:  "+indicator.getTop1());
        System.out.println("Top@5:  "+indicator.getTop5());
        System.out.println("Top@10: "+indicator.getTop10());
        System.out.println("MRR:    "+indicator.getMRR());
        System.out.println("MAP:    "+indicator.getMAP());

    }


}
