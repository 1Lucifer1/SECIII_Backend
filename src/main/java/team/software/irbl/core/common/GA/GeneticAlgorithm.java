package team.software.irbl.core.common.GA;

import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.domain.BugReport;

import java.util.List;

public class GeneticAlgorithm {

    public static void main(String[] args) {
        String dir = "D:/大三下/软工三/resource/data/";
        List<BugReport> reports = FileTranslator.readOriginBugReport(dir+"eclipse-res");
        assert reports != null;
        System.out.println(reports.size());
    }
}
