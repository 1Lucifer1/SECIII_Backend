package team.software.irbl.core.component.versionHistoryComponent;

import org.springframework.beans.factory.annotation.Autowired;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.component.ComponentRank;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.filestore.XMLParser;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.core.common.maptool.PackageMap;
import team.software.irbl.domain.*;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.util.SavePath;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VersionHistoryRank implements ComponentRank {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static int k = 15;

    private List<CommitInfo> commitInfoList;

    private List<CodeFile> codeFileList;

    public VersionHistoryRank(List<CodeFile> codeFileList, Project project){
        this.codeFileList = codeFileList;
        this.commitInfoList = XMLParser.getCommitInfosFromXML(SavePath.getSourcePath(project.getProjectName())+"/CommitRepository.xml", project.getProjectIndex());
    }

    public List<RankRecord> rank(BugReport bugReport){
        List<CommitInfo> commitInfoList = new ArrayList<>(this.commitInfoList);

        try {
            // 注意：可选项FixDate | OpenDate
            Date date = format.parse(bugReport.getFixDate());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -k);
            Date tolerableDate = calendar.getTime();

            for (int i=0;i<commitInfoList.size();i++) {
                CommitInfo commitInfo = commitInfoList.get(i);
                Date commitDate = format.parse(commitInfo.getDate());
                if(commitDate.before(tolerableDate) || commitDate.after(date) ){
                    commitInfoList.remove(commitInfo);
                    i--;
                }
                else{
                    int daysBetween = (int)(date.getTime()-commitDate.getTime())/86400000;
                    commitInfo.setDaysBetween(daysBetween);
                }
            }
        }
        catch (ParseException e){
            e.printStackTrace();
            return null;
        }

        List<RankRecord> records = new ArrayList<>();
        for(CodeFile codeFile: codeFileList){
            RankRecord rankRecord = new RankRecord();
            rankRecord.setReportIndex(bugReport.getReportIndex());
            rankRecord.setFileIndex(codeFile.getFileIndex());
            rankRecord.setScore(calculateSuspiciousScore(codeFile.getPackageName(), commitInfoList));
            rankRecord.setFileRank(-1);
            records.add(rankRecord);
        }

        records.sort(Comparator.comparing(RankRecord::getScore).reversed());

        for(int i=0; i<records.size(); i++){
            RankRecord rankRecord = records.get(i);
            rankRecord.setFileRank(i+1);
        }

        return records;
    }

    // 计算某个源文件的可疑度得分
    private double calculateSuspiciousScore(String packageName, List<CommitInfo> commitInfoList){
        double score = 0;
        for(CommitInfo commitInfo: commitInfoList){
            List<FixedFile> fixedFileList = commitInfo.getFixedFiles();
            // 注意除法计算时类型的变化
            double t = commitInfo.getDaysBetween();
            for(FixedFile fixedFile: fixedFileList){
                if (fixedFile.getFilePackageName().contains(packageName)){
                    score += (1.0 / (1.0 + Math.pow(Math.E, 12.0*(1.0-((k-t)/k)))));
                }
            }
        }
        return score;
    }

    public static void main(String[] args) {
        String projectName = "eclipse-3.1";
        int projectIndex = 1;
        Project project = new Project(projectName);
        project.setProjectIndex(projectIndex);

        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, codeFileMap);

        VersionHistoryRank versionHistoryRank = new VersionHistoryRank(new ArrayList<>(codeFiles), project);

        assert reports != null;
        reports.forEach(report -> {
            List<RankRecord> records = versionHistoryRank.rank(report);
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        indicator.print();

//        try {
//            Date date = format.parse("2021-06-04 11:00:00");
//            System.out.println(date);
//            int t=10;
//            System.out.println(((1+Math.pow(2, 12*(1.0-((double) (k-t)/k))))));
//            double x = (double) (k-t)/k;
//            System.out.println(x);
//
//        }
//        catch (ParseException e){
//            e.printStackTrace();
//        }
    }
}
