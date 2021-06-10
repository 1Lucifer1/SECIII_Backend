package team.software.irbl.core.similarReportComponent;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.maptool.CodeFileMap;
import team.software.irbl.core.vsm.Lexicon;
import team.software.irbl.core.vsm.SubLexicon;
import team.software.irbl.core.vsm.VSM;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;

import java.util.ArrayList;
import java.util.List;

import static team.software.irbl.core.filestore.XMLParser.getBugReportsFromXML;

public class SimilarReportRank {
    private List<StructuredCodeFile> codeFile;
    private List<StructuredBugReport> reports = new ArrayList<>();
    public SimilarReportRank(List<StructuredCodeFile> codeFiles){
        this.codeFile = codeFiles;
    }
    //获得已经有的所有bugReport的内容
    public List<List<String>> getFiles(){
        List<List<String>> files = new ArrayList<>();
        for(int i=0;i<reports.size();i++){
            List<String> file = new ArrayList<>();
            file.addAll(reports.get(i).getDescriptionWords());
            file.addAll(reports.get(i).getSummaryWords());
            files.add(file);
        }
        return files;
    }
    //得到每个源文件的得分
    public void getRank(StructuredBugReport bugReport){
        for(int i=0;i<reports.size();i++){
            BugReport oldBugReport = reports.get(i);
            List<FixedFile> fixedFiles = oldBugReport.getFixedFiles();

            List<List<String>> files = getFiles();
            List<String> file = new ArrayList<>();
            file.addAll(bugReport.getDescriptionWords());
            file.addAll(bugReport.getSummaryWords());

            VSM lexicon = new VSM(files);
            SubLexicon subLexicon = new SubLexicon(file);
            double similarity = lexicon.getSimilarity(subLexicon, i);
            double newRank = similarity/Math.sqrt(fixedFiles.size());
            for(int j=0;j<fixedFiles.size();j++){
                int fixedIndex = getFileIndex(fixedFiles.get(j).getFilePackageName());
                codeFile.get(fixedIndex).setScore(codeFile.get(fixedIndex).getScore()+newRank);
            }
        }
    }
    //定位到具体的源文件
    public int getFileIndex(String packageName){
        CodeFileMap cfm = new CodeFileMap(this.codeFile);
        List<CodeFile> res = CodeFileMap.getCodeFileFromMap(String packageName);
    }
    //得到排序
    public List<RankRecord> rank(StructuredBugReport bugReport){
        this.reports.add(bugReport);
        getRank(bugReport);
        List<RankRecord> records = new ArrayList<>();
        List<StructuredCodeFile> cf = this.codeFile;
        for(int i=0;i<cf.size()-1;i++){
            for(int j=1;j<cf.size();j++){
                if(cf.get(j).getScore()<cf.get(j-1).getScore()){
                    StructuredCodeFile tem = cf.get(j);
                    cf.set(j,cf.get(j-1));
                    cf.set(j-1,tem);
                }
            }
        }
        for(int i=0;i<cf.size();i++){
            records.add(new RankRecord(bugReport.getReportIndex(), cf.get(i).getFileIndex(), i+1, cf.get(i).getScore()));
        }
        return records;
    }
    public static void main(String[] args){

    }


}
