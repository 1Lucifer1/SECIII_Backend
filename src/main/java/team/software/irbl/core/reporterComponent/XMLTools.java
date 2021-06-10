package team.software.irbl.core.reporterComponent;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


public class XMLTools {
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static void createFile(String path, Document document) throws Exception{
        // 创建TransformerFactory对象
        TransformerFactory tff = TransformerFactory.newInstance();
        // 创建 Transformer对象
        Transformer tf = tff.newTransformer();

        // 输出内容是否使用换行
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        // 创建xml文件并写入内容
        tf.transform(new DOMSource(document), new StreamResult(new File(path)));
        System.out.println("生成reporters.xml成功");
    }

    public static Document parseXML(String path) throws Exception{
         return factory.newDocumentBuilder().parse(new File(path));
    }

    public static Document createDocument() throws Exception{
        return factory.newDocumentBuilder().newDocument();
    }
}
