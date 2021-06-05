package team.software.irbl.core.vsm;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SubLexicon {

    private HashMap<String, TF> wordMap = new HashMap<>();

    // 经典多线程下单例模式双检查锁的化用，保证calculateTF方法的正确性以及不会被重复调用(实际上不用也并没有什么影响)
    // private volatile boolean hasCalculateTF = false;
    // 不使用多线程，因为我是傻逼

    public SubLexicon(){}

    public SubLexicon(List<String> words){
        words.forEach(this::putWord);
        calculateTF();
    }


    private void putWord(String word){
        //hasCalculateTF = false;
        if(wordMap.containsKey(word)){
            wordMap.get(word).add();
        }else {
            wordMap.put(word, new TF());
        }
    }

    public double getTF(String word){
        if(wordMap.containsKey(word)){
//            if (!hasCalculateTF) {
//                synchronized (this){
//                    if(!hasCalculateTF) calculateTF();
//                }
//            }
            return wordMap.get(word).getTf();
        }else {
            return 0;
        }
    }

    public Set<String> getWords(){
        return wordMap.keySet();
    }

    private void calculateTF(){
        for(TF tf: wordMap.values()){
            tf.setTf(Calculate.calculateTf(tf.getCount()));
        }
    }
}

class TF{
    private int count;
    private double tf;

    public TF(){
        count = 1;
        tf = -1;
    }

    public void add(){
        count++;
    }

    public int getCount() {
        return count;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf){
        this.tf = tf;
    }

    public void setCount(int count){
        this.count = count;
    }
}
