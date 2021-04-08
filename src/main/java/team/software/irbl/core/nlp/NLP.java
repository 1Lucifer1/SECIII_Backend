package team.software.irbl.core.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NLP {

    // 在标准lucene列表中添加几个额外的术语
    private static final String customStopWordList = "int,java,integer,string,public,class,import,void,a,an,and,are,as,at,be,but,by,for,if,in,into,is,it,no,not,of,on,or,such,that,the,their,then,there,these,they,this,to,was,will,with";

    private static final int wordLengthLimit = 3;

    // 注意：有两处决定是否最终都转换成小写的地方
    public static List<String> standfordNLP(String text){
        Properties props = new Properties();
        props.setProperty("customAnnotatorClass.stopword", "team.software.irbl.core.nlp.StopwordAnnotator");

        // 设置停用词的CoreNLP属性。注意自定义停用词列表属性
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, stopword");
        props.setProperty(StopwordAnnotator.STOPWORDS_LIST, customStopWordList);
        props.setProperty(StopwordAnnotator.CHECK_LEMMA, "true");
        props.setProperty(StopwordAnnotator.IGNORE_STOPWORD_CASE, String.valueOf(true));

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<String> wordList = new ArrayList<>();

        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);
        for (CoreLabel token: tokens) {
            String originalWord = token.get(CoreAnnotations.LemmaAnnotation.class);  // 获取对应上面word的词元信息，即我所需要的词形还原后的单词

            //get the stopword annotation
            Pair<Boolean, Boolean> stopword = token.get(StopwordAnnotator.class);

            if(originalWord.length()>=wordLengthLimit && !stopword.first()) {
                String word = token.word();
                // 全大写的复合词没能力分开
                // 全小写、全大写、仅开头大写
                if (word.toLowerCase().equals(word) || word.toUpperCase().equals(word) ||
                        word.substring(1).toLowerCase().equals(word.substring(1))) {
                    wordList.add(originalWord.toLowerCase());
                    // 经考虑后觉得非复合词下的大写可能具有其特殊含义，因此不全转小写（实际上StanfordCoreNLP会做一部分仅开头大写的单词的大小写转换）
                    // wordList.add(originalWord);
                }
                else {
                    int begin = 0;
                    String splitWord = "";
                    for (int i = 1; i < word.length() - 1; i++) {
                        if ((Character.isUpperCase(word.charAt(i)) && Character.isLowerCase(word.charAt(i + 1))) ||
                                (Character.isUpperCase(word.charAt(i)) && Character.isLowerCase(word.charAt(i - 1)))) {
                            splitWord = splitWord + word.substring(begin,i) + " ";
                            begin = i;
                        }
                    }
                    splitWord = splitWord + word.substring(begin) + " ";

                    Annotation d = new Annotation(splitWord);
                    pipeline.annotate(d);
                    List<CoreLabel> toks = d.get(CoreAnnotations.TokensAnnotation.class);
                    for(CoreLabel tok : toks){
                        String originalWord2 = tok.lemma();
                        Pair<Boolean, Boolean> stopword2 = tok.get(StopwordAnnotator.class);
                        if(originalWord2.length()>=wordLengthLimit && !stopword2.first()) {
                            wordList.add(originalWord2.toLowerCase());
                        }
                    }
                }
            }
        }
//        for (String word:wordList) {
//            System.out.println(word);
//        }
        return wordList;
    }
}
