package team.software.irbl.core.domain;

import team.software.irbl.core.enums.ComponentType;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;

import java.util.ArrayList;
import java.util.List;

public class RawResult {

    private BugReport report;

    private List<List<RankRecord>> rankResults;

    public RawResult(BugReport report){
        this.report = miniReport(report);
        rankResults = new ArrayList<>();
        for(int i = 0; i< ComponentType.total.value(); ++i) rankResults.add(null);
    }

    public BugReport getReport(){
        return report;
    }

    public void setRankResults(List<RankRecord> records, int index){
        rankResults.set(index, records);
    }

    public List<RankRecord> getRankResults(int index){
        return rankResults.get(index);
    }

    /**
     * 去除report中用不到域已降低数据大小
     * @param report
     * @return
     */
    private BugReport miniReport(BugReport report){
        BugReport reportMini = new BugReport();
        reportMini.setReportIndex(report.getReportIndex());
        reportMini.setProjectIndex(report.getProjectIndex());
        List<FixedFile> fixedFiles = new ArrayList<>();
        for(FixedFile fixedFile:report.getFixedFiles()){
            FixedFile fixedFileMini = new FixedFile();
            fixedFileMini.setFileIndex(fixedFile.getFileIndex());
            fixedFileMini.setReportIndex(fixedFile.getReportIndex());
            fixedFiles.add(fixedFileMini);
        }
        reportMini.setFixedFiles(fixedFiles);
        return reportMini;
    }

}
