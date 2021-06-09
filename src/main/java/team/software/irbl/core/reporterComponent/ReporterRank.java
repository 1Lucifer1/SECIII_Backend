package team.software.irbl.core.reporterComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import team.software.irbl.core.maptool.CodeFileMap;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.util.Err;
import team.software.irbl.util.SavePath;

import java.util.*;

public class ReporterRank {

    private Map<Integer, String> reportsReporter;

    private Map<String, Set<String>> reportersPastPackages;

    private CodeFileMap codeFileMap;

    public ReporterRank(String fileName, CodeFileMap fileMap) throws Err {
        reportsReporter = new HashMap<>();
        reportersPastPackages = new HashMap<>();
        codeFileMap = fileMap;
        String xmlFilePath = SavePath.getSourcePath(fileName) + "/reporters.xml";
        try {
            Document doc = XMLTools.parseXML(xmlFilePath);
            NodeList nodeList = doc.getElementsByTagName("reporter");
            for(int i = 0; i < nodeList.getLength(); ++i){
                Node node = nodeList.item(i);
                StringBuilder sb = new StringBuilder();
                int id = 0;
                for(Node insideNode = node.getFirstChild(); insideNode != null; insideNode = insideNode.getNextSibling()){
                    if(insideNode.getNodeType()!=Node.ELEMENT_NODE){
                        continue;
                    }
                    if(insideNode.getNodeName().equals("bugId")){
                        id = Integer.parseInt(insideNode.getFirstChild().getNodeValue());
                    }
                    if(insideNode.getNodeName().equals("name")){
                        sb.append(insideNode.getFirstChild().getNodeValue());
                    }
                    if(insideNode.getNodeName().equals("id")){
                        sb.append(insideNode.getFirstChild().getNodeValue());
                    }
                }
                reportsReporter.put(id, sb.toString());
            }
        } catch (Exception e) {
            throw new Err(e.getMessage());
        }
    }

    public List<RankRecord> rank(BugReport report){
        List<RankRecord> records = new ArrayList<>();
        String reporter = reportsReporter.get(report.getBugId());
        Set<String> packages;
        if(reportersPastPackages.containsKey(reporter)){
            packages = reportersPastPackages.get(reporter);
        }else{
            packages = new HashSet<>();
            reportersPastPackages.put(reporter, new HashSet<>());
        }
        for(CodeFile codeFile: codeFileMap.values()){
            String packageName = codeFile.getPackageName();
            RankRecord rankRecord = new RankRecord(report.getReportIndex(), codeFile.getFileIndex(), -1, 0);
            for(String s: packages){
                if(s.equals(packageName)) rankRecord.setScore(1);
            }
            records.add(rankRecord);
        }
        for(FixedFile fixedFile: report.getFixedFiles()){
            reportersPastPackages.get(reporter).add(fixedFile.getFilePackageName());
        }
        return records;
    }

}
