package team.software.irbl.core.similarReportComponent;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;

import java.util.List;

public class SimilarReportRank {
    private List<StructuredBugReport> bugReports;

    public SimilarReportRank(List<StructuredBugReport> bugReports){
        this.bugReports = bugReports;
    }
}
