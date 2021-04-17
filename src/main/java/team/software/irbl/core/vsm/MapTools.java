package team.software.irbl.core.vsm;

import team.software.irbl.core.domain.CodeLexicon;
import team.software.irbl.core.domain.Lexicon;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.enums.CodeWordsType;
import team.software.irbl.core.enums.ReportWordsType;
import team.software.irbl.enums.WordType;

import java.util.ArrayList;
import java.util.List;

public class MapTools {
    public static ArrayList<Lexicon> getReportWordMap(List<StructuredBugReport> structuredBugReports){
        ArrayList<Lexicon> reportWordMaps = new ArrayList<>();
        for(int i = 0; i < ReportWordsType.values().length; ++i){
            reportWordMaps.add(new Lexicon(structuredBugReports.size()));
        }

        int fileCount = 0;
        for(StructuredBugReport structuredBugReport:structuredBugReports){
            fileCount++;
            int finalFileCount = fileCount;
            structuredBugReport.getSummaryWords().forEach(s -> {
                insertFileWord(reportWordMaps.get(ReportWordsType.SUMMARY_WORDS.value()), s, finalFileCount, structuredBugReport.getReportIndex(), WordType.BugReportWord);
            });
            structuredBugReport.getDescriptionWords().forEach(s -> {
                insertFileWord(reportWordMaps.get(ReportWordsType.DESCRIPTION_WORDS.value()), s, finalFileCount, structuredBugReport.getReportIndex(), WordType.BugReportWord);
            });
        }
        for (Lexicon reportWordMap : reportWordMaps) {
            reportWordMap.calculateTF();
        }
        return reportWordMaps;
    }

    public static ArrayList<CodeLexicon> getCodeWordMap(List<StructuredCodeFile> structuredCodeFiles){
        ArrayList<CodeLexicon> codeWordMaps = new ArrayList<>();
        for(int i = 0; i < CodeWordsType.values().length; ++i){
            codeWordMaps.add(new CodeLexicon(structuredCodeFiles.size()));
        }
        int fileCount = 0;
        for(StructuredCodeFile structuredCodeFile:structuredCodeFiles){
            fileCount++;
            int finalFileCount = fileCount;
            structuredCodeFile.getTypes().forEach(s -> {
                insertFileWord(codeWordMaps.get(CodeWordsType.TYPES.value()), s, finalFileCount, structuredCodeFile.getFileIndex(),  WordType.CodeFileWord);
                insertProjectWord(codeWordMaps.get(CodeWordsType.TYPES.value()), s, finalFileCount, structuredCodeFile.getProjectIndex());
            });
            structuredCodeFile.getMethods().forEach(s -> {
                insertFileWord(codeWordMaps.get(CodeWordsType.METHODS.value()), s, finalFileCount, structuredCodeFile.getFileIndex(),  WordType.CodeFileWord);
                insertProjectWord(codeWordMaps.get(CodeWordsType.METHODS.value()), s, finalFileCount, structuredCodeFile.getProjectIndex());
            });
            structuredCodeFile.getFields().forEach(s -> {
                insertFileWord(codeWordMaps.get(CodeWordsType.FIELDS.value()), s, finalFileCount, structuredCodeFile.getFileIndex(),  WordType.CodeFileWord);
                insertProjectWord(codeWordMaps.get(CodeWordsType.FIELDS.value()), s, finalFileCount, structuredCodeFile.getProjectIndex());
            });
            structuredCodeFile.getComments().forEach(s -> {
                insertFileWord(codeWordMaps.get(CodeWordsType.COMMENTS.value()), s, finalFileCount, structuredCodeFile.getFileIndex(),  WordType.CodeFileWord);
                insertProjectWord(codeWordMaps.get(CodeWordsType.COMMENTS.value()), s, finalFileCount, structuredCodeFile.getProjectIndex());
            });
            structuredCodeFile.getContexts().forEach(s -> {
                insertFileWord(codeWordMaps.get(CodeWordsType.CONTEXTS.value()), s, finalFileCount, structuredCodeFile.getFileIndex(),  WordType.CodeFileWord);
                insertProjectWord(codeWordMaps.get(CodeWordsType.CONTEXTS.value()), s, finalFileCount, structuredCodeFile.getProjectIndex());
            });
        }
        for (CodeLexicon codeWordMap : codeWordMaps) {
            codeWordMap.calculateTF();
            codeWordMap.calculateIDF(fileCount);
        }
        return codeWordMaps;
    }

    private static void insertProjectWord(CodeLexicon codeLexicon, String s, int fileCount, int projectIndex){
        codeLexicon.addProjectWord(fileCount, s, projectIndex);
    }

    private static void insertFileWord(Lexicon lexicon, String s, int fileCount, int fileIndex, WordType wordType){
        lexicon.addFileWord(fileCount - 1,s, fileIndex, wordType);
    }
}
