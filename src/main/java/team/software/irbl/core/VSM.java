package team.software.irbl.core;


import org.springframework.stereotype.Component;
import team.software.irbl.core.domain.CodeLexicon;
import team.software.irbl.core.domain.Lexicon;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.MapTools;
import team.software.irbl.domain.*;
import team.software.irbl.enums.CodeWordsType;
import team.software.irbl.enums.ReportWordsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@Component
public class VSM {

    private List<StructuredBugReport> structuredBugReports;

    private List<StructuredCodeFile> structuredCodeFiles;

    private ArrayList<Lexicon> reportMaps;

    private ArrayList<CodeLexicon> codeMaps;

    public List<List<RankRecord>> startRank(List<StructuredBugReport> reportList, List<StructuredCodeFile> codeFileList){
        structuredBugReports = reportList;
        structuredCodeFiles = codeFileList;
        reportMaps = MapTools.getReportWordMap(structuredBugReports);
        codeMaps = MapTools.getCodeWordMap(structuredCodeFiles);
        System.out.println();
        List<List<RankRecord>> ranks = new ArrayList<>();
        for(int i = 0; i < structuredBugReports.size();++i){
            ranks.add(rank(structuredBugReports.get(i), i));
        }
        return ranks;
    }

    private List<RankRecord> rank(StructuredBugReport structuredBugReport, int reportIndex){
        List<RankRecord> ranks = new ArrayList<>(structuredCodeFiles.size());
        for(int i = 0; i < structuredCodeFiles.size();++i){
            double similarity = getSimilarity(structuredBugReport, reportIndex, i);
            ranks.add(new RankRecord(structuredBugReport.getReportIndex(),
                    structuredCodeFiles.get(i).getFileIndex(), similarity));
        }
        ranks.sort(Comparator.reverseOrder());
        for(int i=0; i<ranks.size(); ++i){
            ranks.get(i).setFileRank(i + 1);
        }
        return ranks;
    }

    private double getSimilarity(StructuredBugReport structuredBugReport, int reportIndex, int codeIndex){
        List<String> descriptionWords = structuredBugReport.getDescriptionWords();
        List<String> summaryWords = structuredBugReport.getSummaryWords();

        ArrayList<Double> summarySimilarity = new ArrayList<>();
        summarySimilarity.add(getSimilarityInList(summaryWords, ReportWordsType.SUMMARY_WORDS, CodeWordsType.TYPES, reportIndex, codeIndex));
        summarySimilarity.add(getSimilarityInList(summaryWords, ReportWordsType.SUMMARY_WORDS, CodeWordsType.METHODS, reportIndex, codeIndex));
        summarySimilarity.add(getSimilarityInList(summaryWords, ReportWordsType.SUMMARY_WORDS, CodeWordsType.FIELDS, reportIndex, codeIndex));
        summarySimilarity.add(getSimilarityInList(summaryWords, ReportWordsType.SUMMARY_WORDS, CodeWordsType.COMMENTS, reportIndex, codeIndex));
        summarySimilarity.add(getSimilarityInList(summaryWords, ReportWordsType.SUMMARY_WORDS, CodeWordsType.CONTEXTS, reportIndex, codeIndex));

        ArrayList<Double> descriptionSimilarity = new ArrayList<>();
        descriptionSimilarity.add(getSimilarityInList(descriptionWords, ReportWordsType.DESCRIPTION_WORDS, CodeWordsType.TYPES, reportIndex, codeIndex));
        descriptionSimilarity.add(getSimilarityInList(descriptionWords, ReportWordsType.DESCRIPTION_WORDS, CodeWordsType.METHODS, reportIndex, codeIndex));
        descriptionSimilarity.add(getSimilarityInList(descriptionWords, ReportWordsType.DESCRIPTION_WORDS, CodeWordsType.FIELDS, reportIndex, codeIndex));
        descriptionSimilarity.add(getSimilarityInList(descriptionWords, ReportWordsType.DESCRIPTION_WORDS, CodeWordsType.COMMENTS, reportIndex, codeIndex));
        descriptionSimilarity.add(getSimilarityInList(descriptionWords, ReportWordsType.DESCRIPTION_WORDS, CodeWordsType.CONTEXTS, reportIndex, codeIndex));

        double res = 0;
        for (Double num : summarySimilarity) {
            res += num;
        }
        for (Double num: descriptionSimilarity) {
            res += num;
        }

        return res / (summarySimilarity.size() + descriptionSimilarity.size());
    }

    private double getSimilarityInList(List<String> reportWords, ReportWordsType reportWordsType, CodeWordsType codeWordsType, int reportIndex, int codeIndex){
        Lexicon reportLexicon = reportMaps.get(reportWordsType.value());
        CodeLexicon codeLexicon = codeMaps.get(codeWordsType.value());
        double innerProduct = 0;
        double lengthOfVectorX = 0;
        double lengthOfVectorY = 0;
        for (String reportWord : reportWords) {
            double idf = codeLexicon.getProjectWord(reportWord).getIdf();
            double tfIdfX = idf * reportLexicon.getFileWord(reportIndex, reportWord).getTf();
            double tfIdfY = idf * codeLexicon.getFileWord(codeIndex, reportWord).getTf();
            innerProduct += tfIdfX * tfIdfY;
            lengthOfVectorX += tfIdfX * tfIdfX;
            lengthOfVectorY += tfIdfY * tfIdfY;
        }

        if(lengthOfVectorX == 0 || lengthOfVectorY == 0) return 0;
        return innerProduct / (Math.sqrt(lengthOfVectorX) * Math.sqrt(lengthOfVectorY));
    }
}
