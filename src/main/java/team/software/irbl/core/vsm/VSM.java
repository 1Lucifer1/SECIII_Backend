package team.software.irbl.core.vsm;

import java.util.ArrayList;
import java.util.List;

public class VSM {

    private Lexicon lexicon;

    public VSM(List<List<String>> files){
        lexicon = new Lexicon(files);
    }

    public double[] getScores(List<String> file){
        SubLexicon subLexicon = new SubLexicon(file);

        double[] scores = new double[lexicon.getFileNum()];
        // 在并行的同时保持subs与files对应顺序
        List<Integer> indexes = new ArrayList<>();
        for(int i=0; i<lexicon.getFileNum(); ++i){
            indexes.add(i);
        }
        indexes.parallelStream().forEach(index -> scores[index]= getSimilarity(subLexicon, index));
        return scores;
    }

    public double getSimilarity(SubLexicon subLexicon, int index){
        double innerProduct = 0;
        double lengthOfVectorX = 0;
        double lengthOfVectorY = 0;
        for (String word : subLexicon.getWords()) {
            double idf = lexicon.getIDF(word);
            double tfIdfX = idf * subLexicon.getTF(word);
            double tfIdfY = idf * lexicon.getTF(index, word);
            innerProduct += tfIdfX * tfIdfY;
            lengthOfVectorX += tfIdfX * tfIdfX;
            lengthOfVectorY += tfIdfY * tfIdfY;
        }

        double score;
        if(lengthOfVectorX == 0 || lengthOfVectorY == 0){
            score = 0;
        }else {
            score = innerProduct / (Math.sqrt(lengthOfVectorX) * Math.sqrt(lengthOfVectorY));
        }
        return score;
    }
}
