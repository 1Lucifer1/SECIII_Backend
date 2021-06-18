package team.software.irbl.core.component.reporterComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.component.ComponentRank;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.filestore.XMLParser;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.core.common.maptool.FilePathMap;
import team.software.irbl.core.common.maptool.PackageMap;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.domain.Indicator;
import team.software.irbl.util.SavePath;

import java.util.*;

public class ReporterRank implements ComponentRank {

    private final Map<Integer, String> reportsReporter;

    private final Map<String, Set<String>> reportersPastPackages;

    private final CodeFileMap codeFileMap;

    public ReporterRank(String projectName, CodeFileMap fileMap){
        reportsReporter = new HashMap<>();
        reportersPastPackages = new HashMap<>();
        codeFileMap = fileMap;
        String xmlFilePath = SavePath.getSourcePath(projectName) + "/reporters.xml";
        Document doc;
        try {
            doc = XMLParser.parseXML(xmlFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        NodeList nodeList = doc.getElementsByTagName("reporter");
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            StringBuilder sb = new StringBuilder();
            int id = 0;
            for (Node insideNode = node.getFirstChild(); insideNode != null; insideNode = insideNode.getNextSibling()) {
                if (insideNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (insideNode.getNodeName().equals("bugId")) {
                    id = Integer.parseInt(insideNode.getFirstChild().getNodeValue());
                }
                if (insideNode.getNodeName().equals("name")) {
                    if (insideNode.getFirstChild() != null)
                        sb.append(insideNode.getFirstChild().getNodeValue()).append(" ");
                }
                if (insideNode.getNodeName().equals("id")) {
                    sb.append(insideNode.getFirstChild().getNodeValue());
                }
            }
            reportsReporter.put(id, sb.toString());
        }
    }

    public List<RankRecord> rank(BugReport report) {
        List<RankRecord> records = new ArrayList<>();
        String reporter = reportsReporter.get(report.getBugId());
        Set<String> packages;
        if (reportersPastPackages.containsKey(reporter)) {
            packages = reportersPastPackages.get(reporter);
        } else {
            packages = new HashSet<>();
        }

        for (CodeFile codeFile : codeFileMap.values()) {
            String packageName = codeFile.getPackageName();
            RankRecord rankRecord = new RankRecord(report.getReportIndex(), codeFile.getFileIndex(), -1, 0);
            if (packages.contains(packageName)) rankRecord.setScore(1);
            records.add(rankRecord);
//            if(rankRecord.getScore() == 1.0) System.out.print(1);
        }
        for (FixedFile fixedFile : report.getFixedFiles()) {
            packages.add(fixedFile.getFilePackageName());
        }
        reportersPastPackages.put(reporter, packages);
        return records;
    }

    public static void main(String[] args){
        String projectName = "aspectj";

        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName + "/"), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
//        dbProcessor.saveBugReports(reports, codeFileMap);
        dbProcessor.saveBugReports(reports, new FilePathMap(new ArrayList<>(codeFiles)));

        ReporterRank reporterRank = new ReporterRank(projectName, codeFileMap);

        assert reports != null;
        reports.forEach(report -> {
            List<RankRecord> records = reporterRank.rank(report);
            records.sort(Collections.reverseOrder());
            for(int i=0; i<records.size(); ++i){
                records.get(i).setFileRank(i+1);
            }
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        indicator.print();
    }
}
