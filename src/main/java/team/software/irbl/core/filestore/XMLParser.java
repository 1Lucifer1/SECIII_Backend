package team.software.irbl.core.filestore;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import team.software.irbl.domain.BugReport;
import team.software.irbl.core.versionHistoryComponent.CommitInfo;
import team.software.irbl.domain.FixedFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static List<CommitInfo> getCommitInfosFromXML(String filePath, int projectIndex){
        String regex = "(.*fix.*)|(.*bug.*)|(.*Fix.*)|(.*Bug.*)|(.*FIX.*)|(.*BUG.*)";
        Pattern pattern = Pattern.compile(regex);

        String javaFileRegex = ".*\\.java";
        Pattern javaFilePattern = Pattern.compile(javaFileRegex);

        List<CommitInfo> commitInfos = new ArrayList<>();
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nodeList = doc.getElementsByTagName("commit");
            for (int i=0; i<nodeList.getLength();++i){
                CommitInfo commitInfo = new CommitInfo();
                commitInfo.setProjectIndex(projectIndex);
                List<FixedFile> fixedFiles = new ArrayList<>();

                NamedNodeMap attributes = nodeList.item(i).getAttributes();
                commitInfo.setId(attributes.getNamedItem("id").getNodeValue());
                commitInfo.setAuthor(attributes.getNamedItem("author").getNodeValue());
                commitInfo.setDate(attributes.getNamedItem("date").getNodeValue());

                for(Node insideNode = nodeList.item(i).getFirstChild();insideNode!=null;insideNode = insideNode.getNextSibling()){
                    if(insideNode.getNodeType()!=Node.ELEMENT_NODE){
                        continue;
                    }
                    if(insideNode.getNodeName().equals("title")){
                        if(insideNode.getFirstChild()!=null) commitInfo.setTitle(insideNode.getFirstChild().getNodeValue());
                    }
                    else if(insideNode.getNodeName().equals("fixedFiles")){
                        for(Node node=insideNode.getFirstChild(); node!=null; node=node.getNextSibling()){
                            if(node.getNodeType() == Node.ELEMENT_NODE){
                                String fileName = "";
                                if(node.getFirstChild()!=null) {
                                    fileName = node.getFirstChild().getNodeValue();
                                }

                                if(! javaFilePattern.matcher(fileName).matches()){
                                    continue;
                                }
                                // emmm，这个包名的替换可能有问题
                                String packageName = fileName.replace('/','.');
                                fixedFiles.add(new FixedFile(-1, packageName));
                            }
                        }
                        commitInfo.setFixedFiles(fixedFiles);
                    }
                }

                // commit日志必须与以下正则表达式匹配regex:(.*fix.*)|(.*bug.*)
                Matcher matcher = pattern.matcher(commitInfo.getTitle());
                if(matcher.matches()){
                    commitInfos.add(commitInfo);
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }

        return commitInfos;
    }

    public static void main(String[] args) {
        //String filePath = "./IRBL/data/test/Test.xml";
        String filePath = "./IRBL/data/SWTBugRepository.xml";
        List<BugReport> reports = getBugReportsFromXML(filePath, 1);
    }
}
