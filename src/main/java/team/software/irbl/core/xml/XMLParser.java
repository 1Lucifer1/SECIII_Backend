package team.software.irbl.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    public static List<BugReport> getBugReportsFromXML(String reportFilePath, int projectIndex){
        List<BugReport> bugReports = new ArrayList<>();
        File file = new File(reportFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nodeList = doc.getElementsByTagName("bug");
            for(int i=0; i<nodeList.getLength(); ++i){
                NamedNodeMap attributes = nodeList.item(i).getAttributes();
                int bugId = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                String openDate = attributes.getNamedItem("opendate").getNodeValue();
                String fixDate = attributes.getNamedItem("fixdate").getNodeValue();

                String summary = "";
                String description = "";
                List<FixedFile> files = new ArrayList<>();
                for(Node insideNode = nodeList.item(i).getFirstChild(); insideNode!=null; insideNode = insideNode.getNextSibling()){
                    if(insideNode.getNodeType()!=Node.ELEMENT_NODE){
                        continue;
                    }
                    if(insideNode.getNodeName().equals("buginformation")){
                        for(Node node = insideNode.getFirstChild(); node!=null; node=node.getNextSibling()){
                            if(node.getNodeType() == Node.ELEMENT_NODE){
                                if(node.getNodeName().equals("summary")){
                                    if(node.getFirstChild()!=null) summary = node.getFirstChild().getNodeValue();
                                }else if(node.getNodeName().equals("description")){
                                    if(node.getFirstChild()!=null) description = node.getFirstChild().getNodeValue();
                                }
                            }
                        }
                    }else if(insideNode.getNodeName().equals("fixedFiles")){
                        for(Node node=insideNode.getFirstChild(); node!=null; node=node.getNextSibling()){
                            if(node.getNodeType() == Node.ELEMENT_NODE ){
                                files.add(new FixedFile(-1, node.getFirstChild().getNodeValue()));
                            }
                        }
                    }
                }
                bugReports.add(new BugReport(projectIndex, bugId, openDate, fixDate, summary, description,files));
            }
            return bugReports;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        //String filePath = "./IRBL/data/test/Test.xml";
        String filePath = "./IRBL/data/SWTBugRepository.xml";
        List<BugReport> reports = getBugReportsFromXML(filePath, 1);
    }
}
