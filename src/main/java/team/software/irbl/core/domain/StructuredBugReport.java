package team.software.irbl.core.domain;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;

import java.util.List;

public class StructuredBugReport extends BugReport{

    private List<String> summaryWords;
    private List<String> descriptionWords;

    public StructuredBugReport(){}
    public StructuredBugReport(BugReport bugReport){
        super(bugReport);
    }


    public List<String> getSummaryWords() {
        return summaryWords;
    }

    public void setSummaryWords(List<String> summaryWords) {
        this.summaryWords = summaryWords;
    }

    public List<String> getDescriptionWords() {
        return descriptionWords;
    }

    public void setDescriptionWords(List<String> descriptionWords) {
        this.descriptionWords = descriptionWords;
    }
}
