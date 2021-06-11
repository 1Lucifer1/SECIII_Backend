package team.software.irbl.core.vsm;

import java.util.ArrayList;
import java.util.List;

/**
 * 只处理分性预处理（分词、词性还原之类的）过的文本
 */
public class VSM {

    private Lexicon lexicon;

    public VSM(List<List<String>> files){
        lexicon = new Lexicon(files);
    }

    /**
     * 保证返回分数的顺序与构造函数接受的file列表的顺序，以及通过addFile方法添加file的顺序相同
     * @param file
     * @return
     */
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
    /**
     * 向vsm的词典添加新的文件
     * @param file
     */
    public void addFile(List<String> file){
        lexicon.addFile(file);
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
