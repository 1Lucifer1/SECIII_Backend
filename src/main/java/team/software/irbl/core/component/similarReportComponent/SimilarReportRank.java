package team.software.irbl.core.component.similarReportComponent;

import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.component.ComponentRank;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.FilePathMap;
import team.software.irbl.core.common.vsm.VSM;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.SavePath;

import java.util.*;

public class SimilarReportRank implements ComponentRank {
    //private List<StructuredCodeFile> codeFile;
    private List<CodeFile> codeFiles;
    //private List<StructuredBugReport> reports = new ArrayList<>();
    private List<StructuredBugReport> reports;

    private VSM vsm;
    private Map<Integer, Double> scoreMap;
    public SimilarReportRank(List<CodeFile> codeFiles){
        //this.codeFile = codeFiles;
        this.codeFiles = codeFiles;
        vsm = new VSM(new ArrayList<>());
        reports = new ArrayList<>();
    }
//    public void init(){
//        scoreMap = new HashMap<>();
//        List<CodeFile> codeFile = codeFileMap.values();
//        for (CodeFile file : codeFile) {
//            scoreMap.put(file, 0.0);
//        }
//    }
    //获得已经有的所有bugReport的内容
//    public List<List<String>> getFiles(){
//        List<List<String>> files = new ArrayList<>();
//        for(int i=0;i<reports.size();i++){
//            List<String> file = new ArrayList<>();
//            file.addAll(Collections.singletonList(reports.get(i).getDescription()));
//            file.addAll(Collections.singletonList(reports.get(i).getSummary()));
//            files.add(file);
//        }
//        return files;
//    }
    //得到每个源文件的得分
    public void getRank(StructuredBugReport bugReport){
        scoreMap = new HashMap<>();
        //List<List<String>> files = getFiles();
        List<String> file = new ArrayList<>();
        //file.addAll(bugReport.getDescriptionWords());
        //file.addAll(bugReport.getSummaryWords());
        file.addAll(bugReport.getDescriptionWords());
        file.addAll(bugReport.getSummaryWords());

        //VSM lexicon = new VSM(files);
        double[] score = vsm.getScores(file);
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
//                        scoreMap.put(codeFiles.get(k),scoreMap.get(codeFiles.get(k))+newRank);
//                    }
//                }
//            }
            if(res > 0) {
                double newRank = score[i] / Math.sqrt(fixedFiles.size());
                for (FixedFile fixedFile : fixedFiles) {
                    if(scoreMap.containsKey(fixedFile.getFileIndex())) {
                        scoreMap.put(fixedFile.getFileIndex(), scoreMap.get(fixedFile.getFileIndex()) + newRank);
                    }else {
                        scoreMap.put(fixedFile.getFileIndex(), newRank);
                    }
                }
            }
        }
        reports.add(bugReport);
        vsm.addFile(file);
//        System.out.println("查看得分:");
//        for(int i=0;i<scoreMap.size();i++){
//            System.out.println(scoreMap.get(codeFile.get(i)));
//        }
    }
    //得到排序
    public List<RankRecord> rank(BugReport bugReport){
        //this.reports.add(bugReport);
        getRank((StructuredBugReport) bugReport);
        List<RankRecord> records = new ArrayList<>();
        for(CodeFile codeFile: codeFiles){
            int index = codeFile.getFileIndex();
            if(scoreMap.containsKey(index)){
                records.add(new RankRecord(bugReport.getReportIndex(), index, -1, scoreMap.get(index)));
            }else {
                records.add(new RankRecord(bugReport.getReportIndex(), index, -1, 0));
            }
        }
//        for(int i=0;i<cf.size()-1;i++){
//            for(int j=1;j<cf.size();j++){
//                if(scoreMap.get(cf.get(j))<scoreMap.get(cf.get(j-1))){
//                    CodeFile tem = cf.get(j);
//                    cf.set(j,cf.get(j-1));
//                    cf.set(j-1,tem);
//                }
//            }
//        }
//        for(int i=0;i<cf.size();i++){
//            records.add(new RankRecord(bugReport.getReportIndex(), cf.get(i).getFileIndex(), -1, scoreMap.get(cf.get(i))));
//        }
        return records;
    }
    public static void main(String[] args){
        String projectName = "swt-3.1";
        List<StructuredBugReport> reports = FileTranslator.readBugReport(SavePath.getPreProcessReportPath(projectName));
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName)+'/', 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        assert reports != null;
        dbProcessor.saveBugReports(new ArrayList<>(reports), new FilePathMap(new ArrayList<>(codeFiles)));

        SimilarReportRank similarReportRank = new SimilarReportRank(new ArrayList<>(codeFiles));
        //similarReportRank.init();

        reports.forEach(report -> {
            List<RankRecord> records = similarReportRank.rank(report);
            records.sort(Collections.reverseOrder());
            for(int i=0; i<records.size(); ++i){
                records.get(i).setFileRank(i+1);
            }
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(new ArrayList<>(reports));
        indicator.print();

    }


}
