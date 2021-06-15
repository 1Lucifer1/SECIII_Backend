package team.software.irbl.core.stacktraceComponent;

import team.software.irbl.core.maptool.CodeFileMap;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.dbstore.DBProcessor;
import team.software.irbl.core.dbstore.DBProcessorFake;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.filestore.XMLParser;
import team.software.irbl.core.jdt.JavaParser;
import team.software.irbl.core.maptool.FilePathMap;
import team.software.irbl.core.maptool.PackageMap;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.SavePath;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackRank {

    /**
     * 该正则表达式可以输出多组匹配,其中部分组可能为null
     * Example： at org.eclipse.core.launcher.Main.main(Main.java:668)
     * group1 : at org.eclipse.core.launcher.Main.main(Main.java:668)
     * group2 : at org.eclipse.core.launcher.Main.main
     * group3 : Unknown Source (与group4不共存，用以特殊处理堆栈报错中出现Unknown Source的情况)
     * group4 : Main
     */
    private String tracePattern = "(\\sat\\s(org\\.[^\\s].*?)\\((?:Native\\sMethod|(Unknown\\sSource)|([^)]+?)\\.java.*?|[^)]+?build.+?|[^)]+?\\.aj.*?)\\))";

    /**
     * 决定取前几个堆栈报错信息（暂未采用）
     */
    private int stackDepthLimit = 10;


    /**
     * 以包名对源代码文件的映射
     */
    private CodeFileMap codeFileMap;


    public StackRank(CodeFileMap fileMap){
        codeFileMap = fileMap;
    }


    public List<RankRecord> rank(BugReport report) {

        List<String> packageNames = getTraceList(report.getDescription());
        // 只有存在符合规定特征的report.description且匹配的堆栈报错信息一条以上才会对其应用堆栈特征处理
        if(packageNames.size() <= 1){
            return null;
        }

        List<RankRecord> records = new ArrayList<>();
        HashSet<String> tracePackageNames = new HashSet<>();
        int depth = 1;
        for(String packageName: packageNames){
            if(!tracePackageNames.contains(packageName)){
                List<CodeFile> codeFiles = codeFileMap.getCodeFileFromMap(packageName);
                if(codeFiles != null){
                    double score = 1.0/depth;
                    for(CodeFile codeFile: codeFiles){
                        records.add(new RankRecord(report.getReportIndex(), codeFile.getFileIndex(), -1, score));
                    }
                    depth++;
                    tracePackageNames.add(packageName);
                }
            }
        }

        for(CodeFile codeFile: codeFileMap.values()){
            if(!tracePackageNames.contains(codeFile.getPackageName())){
                records.add(new RankRecord(report.getReportIndex(), codeFile.getFileIndex(), -1, 0));
            }
        }

        return records;
    }


    /**
     * 从堆栈信息中依次提取涉及的文件包名列表
     * @param description
     * @return
     */
    private List<String> getTraceList(String description){
        //System.out.println(description);
        List<String> res = new ArrayList<>();

        Pattern reg = Pattern.compile(tracePattern);
        Matcher matcher = reg.matcher(description);
        int matchStart = 0;
        while (matcher.find(matchStart)){
//            if(matcher.group(1).length()> 200){
//                System.out.println(matcher.group(1));
//            }
//            Logger.errorLog(matcher.group(1));
//            Logger.errorLog(matcher.group(2));
            // 从匹配的每条堆栈报错信息中提取对应的文件包名
            if(matcher.group(2) != null && (matcher.group(3) != null || matcher.group(4) != null)){
//                String[] parts = matcher.group(2).split("\\sat\\s");
//                if(parts.length> 1){
//                    System.out.println(matcher.group(2));
//                }
//                String trace = parts[parts.length-1].replaceAll("\\s", "");
                String trace = matcher.group(2).replaceAll("\\s", "");
                String packageName;
                if(matcher.group(3) != null){
                    // Unknown Source
                    // 拆分以.区分的各部分以便去掉方法调用部分
                    String[] packages = trace.split("\\.");
                    // 部分文件名后以$连接额外信息，需要去掉
                    packages[packages.length - 2] = packages[packages.length - 2].split("\\$")[0];
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<packages.length-1;++i){
                        sb.append(packages[i]);
                        sb.append('.');
                    }
                    sb.append("java");
                    packageName = sb.toString();
                }else{
                    // 正常情况
                    String fileName = matcher.group(4).replaceAll("\\s", "");
                    packageName = trace.replaceFirst(fileName + ".+?$", fileName + ".java");
                }
//                Logger.errorLog(packageName);
                res.add(packageName);
            }

            matchStart = matcher.end();
        }

        return res;
    }

    public static void main(String[] args) {

        String projectName = "aspectj";
        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName+"/"), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, new FilePathMap(new ArrayList<>(codeFiles)));

        if(reports != null) {
            List<BugReport> traceReports = Collections.synchronizedList(new ArrayList<>());
            StackRank stackRank = new StackRank(new PackageMap(new ArrayList<>(codeFiles)));
            reports.parallelStream().forEach(report -> {
                List<RankRecord> records = stackRank.rank(report);
                if(records != null){
                    report.setRanks(records);
                    traceReports.add(report);
                }
            });
            System.out.println(traceReports.size());

            IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
            Indicator indicator = indicatorEvaluation.getEvaluationIndicator(traceReports);
            indicator.print();
        }


//        String test = "i20050301 (Daniela) J2RE 1.4.2 IBM J9 2.2 Windows XP x86-32 j9n142-20040917 (JIT enabled) J9VM - 20040916_0908_lHdSMR JIT -" +
//                " r7_level20040915_1801 Got an error dialog while debugging. It seems a background job caused SWT API that must run in the UI thread " +
//                "to be called. Found in the log a DebugException right before the SWTException happened, thought it might be the trigger. !ENTRY org." +
//                "eclipse.debug.ui 4 120 2005-03-03 10:34:12.428 !MESSAGE Error logged from Debug UI: !STACK 1 org.eclipse.debug.core.DebugException: " +
//                "Invalid stack frame at org.eclipse.jdt.internal.debug.core.model.JDIStackFrame.getUnderlyingStackFrame(Unknown Source) at org.eclips" +
//                "e.jdt.internal.debug.core.model.JDILocalVariable.retrieveValue(Unknown Source) at org.eclipse.jdt.internal.debug.core.model.JDIVaria" +
//                "ble.getCurrentValue(Unknown Source) at org.eclipse.jdt.internal.debug.core.model.JDIVariable.getValue(Unknown Source) at org.eclipse" +
//                ".debug.internal.ui.views.variables.VariablesView.populateDetailPaneFromSelection(Unknown Source) at org.eclipse.debug.internal.ui.vi" +
//                "ews.variables.VariablesView$8.run(Unknown Source) at org.eclipse.core.internal.jobs.Worker.run(Unknown Source) !SUBENTRY 1 org.eclips" +
//                "e.jdt.debug 4 130 2005-03-03 10:34:12.459 !MESSAGE Invalid stack frame !ENTRY org.eclipse.core.runtime 4 2 2005-03-03 10:34:12.803 !M" +
//                "ESSAGE An internal error occurred during: &quot;Populate Detail Pane&quot;. !STACK 0 org.eclipse.swt.SWTException: Invalid thread acc" +
//                "ess at org.eclipse.swt.SWT.error(Unknown Source) at org.eclipse.swt.SWT.error(Unknown Source) at org.eclipse.swt.SWT.error(Unknown So" +
//                "urce) at org.eclipse.swt.widgets.Widget.error(Unknown Source) at org.eclipse.swt.widgets.Widget.checkWidget(Unknown Source) at org.ec" +
//                "lipse.swt.widgets.Scrollable.getVerticalBar(Unknown Source) at org.eclipse.swt.custom.StyledText.reset(Unknown Source) at org.eclips" +
//                "e.swt.custom.StyledText.handleTextSet(Unknown Source) at org.eclipse.swt.custom.StyledText$6.textSet(Unknown Source) at org.eclipse." +
//                "jface.text.DefaultDocumentAdapter.fireTextSet(Unknown Source) at org.eclipse.jface.text.DefaultDocumentAdapter.documentChanged(Unknow" +
//                "n Source) at org.eclipse.jface.text.AbstractDocument.doFireDocumentChanged2(Unknown Source) at org.eclipse.jface.text.AbstractDocumen" +
//                "t.doFireDocumentChanged(Unknown Source) at org.eclipse.jface.text.AbstractDocument.doFireDocumentChanged(Unknown Source) at org.ecli" +
//                "pse.jface.text.AbstractDocument.fireDocumentChanged(Unknown Source) at org.eclipse.jface.text.AbstractDocument.set(Unknown Source) a" +
//                "t org.eclipse.debug.internal.ui.views.variables.VariablesView.populateDetailPaneFromSelection(Unknown Source) at org.eclipse.debug." +
//                "internal.ui.views.variables.VariablesView$8.run(Unknown Source) at org.eclipse.core.internal.jobs.Worker.run(Unknown Source)";
//        StackRank stackRank = new StackRank();
//        stackRank.getTraceList(test);
    }
}
