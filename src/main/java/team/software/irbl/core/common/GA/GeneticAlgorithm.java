package team.software.irbl.core.common.GA;

import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.domain.RawResult;
import team.software.irbl.core.enums.ComponentType;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.domain.Indicator;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.ConcurrentHashMap;

public class GeneticAlgorithm {

    // 种群，以一个64位Long来作为5种权重的表示，其中每种权重被分配10位，取值范围（0.00~10.23）
    private Unity[] population;
    private static int PresentBit = 10;
    private static long UnityLimit = (long) Math.pow(2, 50);

    // 每次交叉替换会更换的位数
    private static int CrossSize = 10;
    // 每次突变会改变的位数
    private static int VariantSize = 2;
    // 突变概率
    private static double VariantP = 0.1;

    // 数据集路径
    private String[] dataSetPath;
    private int[] dataSize;
    private double[] ratio;

    private int batchSize;
    private int repeatTimes;
    private int populationSize;
    private String savePath;
    private String populationSave;

    public GeneticAlgorithm(String[] dataSetPath, int[] dataSize, double[] ratio, String savePopulationPath, String outPutPath){
        this(dataSetPath,dataSize, ratio, savePopulationPath, outPutPath,100, 300, 100);
    }

    public GeneticAlgorithm(String[] dataSetPath,int[] dataSize, double[] ratio, String savePopulationPath, String outPutPath, int populationSize, int batchSize, int repeatTimes){
        this.dataSetPath = dataSetPath;
        this.dataSize = dataSize;
        this.ratio = ratio;
        this.population = new Unity[populationSize];
        this.populationSize = populationSize;
        this.batchSize = batchSize;
        this.repeatTimes = repeatTimes;
        this.savePath = outPutPath;
        this.populationSave = savePopulationPath;
        initial();
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setPopulation(Unity[] population) {
        this.population = population;
        this.populationSize = population.length;
    }

    public void changeDataSet(String[] dataSetPath, int[] dataSize, double[] ratio) {
        this.dataSetPath = dataSetPath;
        this.dataSize = dataSize;
        this.ratio = ratio;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void train(){
        long begin = System.currentTimeMillis();
        for(int i=0; i<repeatTimes; ++i){
            long startTime = System.currentTimeMillis();
            population = newGeneration(population, 10);
            long processEndTime = System.currentTimeMillis();
            if(populationSave != null) savePopulation();
            Logger.log("Finish one generation in " + (processEndTime-startTime)/1000.0 + " seconds");
            for(int j=0; j<dataSetPath.length; ++j){
                evaluateWhole(population[0], savePath, dataSetPath[j], dataSize[j]);
            }
        }
        long end = System.currentTimeMillis();
        Logger.log("Finish train in " + (end - begin)/1000.0 + " seconds");
    }

    private void savePopulation(){
        FileTranslator.writeObject(population, populationSave);
    }

    private void initial(){
        // 随机初始化
        Random random = new Random();
        for(int i=0; i<populationSize; ++i){
            population[i] = new Unity((long)(random.nextDouble()*UnityLimit));
        }
    }

    private Unity[] newGeneration(Unity[] old, int time){
        List<List<RawResult>> batches = new ArrayList<>();
        for(int i=0; i<dataSetPath.length; ++i){
            batches.add(getRandomBatch(dataSetPath[i], dataSize[i]));
        }
        List<Unity> unities = new ArrayList<>(Arrays.asList(old));
        unities.parallelStream().forEach(unity -> {
            double score = 0;
            for (int i=0; i<batches.size(); ++i) score += evaluate(unity, batches.get(i)) * ratio[i];
            unity.score = score;
        });
        unities.sort(Collections.reverseOrder());

        Random random = new Random();
        for(int i=0; i<time; ++i){
            long startTime = System.currentTimeMillis();
            List<Integer> para = new ArrayList<>();
             for(int j=0; j<populationSize/2; ++j){
                 para.add(1);
             }
            List<Unity> newUnities = Collections.synchronizedList(new ArrayList<>());
            List<Unity> finalUnities = unities;
            para.parallelStream().forEach(integer -> {
                 int parent1 = random.nextInt(populationSize/2);
                 int parent2 = random.nextInt(populationSize/2);
                 Unity[] sons = cross(finalUnities.get(parent1), finalUnities.get(parent2), false);
                 for(Unity son:sons){
                     double score = 0;
                     for (int j=0; j<batches.size(); ++j) score += evaluate(son, batches.get(j)) * ratio[j];
                     son.score = score;
                     if(random.nextDouble() < VariantP) variant(son);
                     newUnities.add(son);
                 }
             });
            unities.addAll(newUnities);
            unities.sort(Collections.reverseOrder());
            unities = unities.subList(0 ,populationSize);
            long processEndTime = System.currentTimeMillis();
            Logger.log("Finish once in " + (processEndTime-startTime)/1000.0 + " seconds");
        }

        return unities.toArray(old);
    }

    /**
     * 互换部分片段
     * @param unity1
     * @param unity2
     * @param noRepeat 指定是否强制要求交换位置不重复
     * @return
     */
    private Unity[] cross(Unity unity1, Unity unity2, boolean noRepeat){
        Random random = new Random();
        long value1 = unity1.value, value2 = unity2.value;
        long son1 = value1, son2 = value2; // 女拳警告

        HashSet<Integer> positions = new HashSet<>();
        for(int i=0; i<CrossSize; ++i){
            int position = random.nextInt(PresentBit*ComponentType.total.value());
            if(noRepeat) {
                // 选取新位置
                while (positions.contains(position)) position = random.nextInt(PresentBit * ComponentType.total.value());
                positions.add(position);
            }
            long mask = (long)Math.pow(2, position);
            son1 = (son1 & ~mask) | (value2 & mask);
            son2 = (son2 & ~mask) | (value1 & mask);
        }
        return new Unity[]{new Unity(son1), new Unity(son2)};
    }

    /**
     * 变异算子，随机几位取反
     * @param unity
     * @return
     */
    private void variant(Unity unity){
        long value = unity.value;
        Random random = new Random();
        for(int i=0; i<VariantSize; ++i){
            int pos = random.nextInt(ComponentType.total.value()*PresentBit);
            long mask = (long)Math.pow(2, pos);
            //System.out.println(Long.toBinaryString(mask));
            //System.out.println(Long.toBinaryString(value));
            value = (value & ~mask) | (~(value & mask) & mask);
            //System.out.println(Long.toBinaryString(value));
        }
        unity.value = value;
    }

    private List<RawResult> getRandomBatch( String dataSetPath, int dataSize){
        Random random = new Random();
        List<RawResult> results = new ArrayList<>();
        HashSet<Integer> added = new HashSet<>();
        while (results.size() < batchSize && added.size() < dataSize){
            int pos = random.nextInt(dataSize) + 1;
            while (added.contains(pos)) pos = random.nextInt(dataSize) + 1;
            List<RawResult> result = FileTranslator.readRawResults(dataSetPath+pos);
            if(result != null) results.addAll(result);
            added.add(pos);
        }
        return results;
    }


    private double evaluate(Unity unity, List<RawResult> data){
        double[] weights = extractWeights(unity.value);

        List<BugReport> reports = processResult(data, weights);
        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        return indicator.getMAP() + indicator.getMRR();
        //indicator.print();
    }

    private void evaluateWhole(Unity unity, String savePath, String dataSetPath, int dataSize){
        double[] weights = extractWeights(unity.value);
        System.out.println(Arrays.toString(weights));
        List<BugReport> reports = new ArrayList<>();
        for(int i=1; i<=dataSize; ++i){
            List<RawResult> results = FileTranslator.readRawResults(dataSetPath+i);
            if(results != null) reports.addAll(processResult(results, weights));
        }
        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        indicator.print();
        try{
            FileWriter writer = new FileWriter(savePath, true);
            writer.write("Result evaluate using " + Arrays.toString(weights) +" :\n");
            writer.write("Top@1:  "+indicator.getTop1() + '\n');
            writer.write("Top@5:  "+indicator.getTop5() + '\n');
            writer.write("Top@10: "+indicator.getTop10() + '\n');
            writer.write("MRR:    "+indicator.getMRR() + '\n');
            writer.write("MAP:    "+indicator.getMAP() + '\n'+'\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从表示用的二进制中提取权重
     * @param unity
     * @return
     */
    private double[] extractWeights(long unity){
        // 从1-PresentBit位开始，每次取对应位数
        long mask = (long)Math.pow(2, PresentBit) - 1; //0x00000000000003ff

        double[] weights = new double[ComponentType.total.value()];
        for(int i=0; i< ComponentType.total.value(); ++i){
            long weight = unity & mask;
            weights[i] = weight / 100.0;
            unity >>>= PresentBit;
        }
        return weights;
    }

    private Unity presentWeight(double[] weights){
        long res = 0;
        for(int i=weights.length-1; i>=0; --i){
            res <<= PresentBit;
            res += weights[i]*100;
        }
        return new Unity(res);
    }

    public static List<BugReport> processResult(List<RawResult> results, double[] weights){
        List<BugReport> reports = Collections.synchronizedList(new ArrayList<>());
        results.parallelStream().forEach(result -> {
            BugReport bugReport = new BugReport();
            bugReport.setReportIndex(result.getReport().getReportIndex());
            bugReport.setFixedFiles(result.getReport().getFixedFiles());
            ConcurrentHashMap<Integer, Double> scoreMap = new ConcurrentHashMap<>();

            // 根据论文公式计算与合并各部分打分
            result.getRankResults(ComponentType.STRUCTURE.value()).forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), rankRecord.getScore() * weights[ComponentType.STRUCTURE.value()]));

            if (result.getRankResults(ComponentType.STACK.value()) != null)
                result.getRankResults(ComponentType.STACK.value()).forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), scoreMap.get(rankRecord.getFileIndex()) +rankRecord.getScore() * weights[ComponentType.STACK.value()]));

            for (int i = 0; i < ComponentType.total.value(); ++i) {
                if (i != ComponentType.STRUCTURE.value() && i != ComponentType.STACK.value()) {
                    for (RankRecord rankRecord : result.getRankResults(i)) {
                        double before = scoreMap.get(rankRecord.getFileIndex());
                        if (before != 0) {
                            scoreMap.put(rankRecord.getFileIndex(), before + rankRecord.getScore() * weights[i]);
                        }
                    }
                }
            }

            List<RankRecord> recordList = new ArrayList<>();
            Set<Entry<Integer, Double>> entrySet = scoreMap.entrySet();
            for(Entry<Integer, Double> entry: entrySet){
                recordList.add(new RankRecord(bugReport.getReportIndex(), entry.getKey(), -1, entry.getValue()));
            }

            recordList.sort(Collections.reverseOrder());
            for(int i=0; i<recordList.size(); ++i){
                recordList.get(i).setFileRank(i+1);
            }
            bugReport.setRanks(recordList);
            reports.add(bugReport);
        });
        return reports;
    }


    public static void main(String[] args) {
//        String dir = SavePath.getSourcePath("rawResult/");
//        List<RawResult> results = FileTranslator.readRawResults(dir+"eclipse-3.1-res");
//        assert results != null;
//        System.out.println(results.size());
        String[] dataSetPath = {SavePath.getSourcePath("rawResult/swt-3.1-res"),SavePath.getSourcePath("rawResult/eclipse-3.1-res"),SavePath.getSourcePath("rawResult/aspectj-res")};
        int[] dataSize = {10, 308, 29};
        double[] ratio = {0, 0.6, 0.4};
        GeneticAlgorithm ga = new GeneticAlgorithm(dataSetPath, dataSize, ratio, SavePath.getSourcePath("population"), SavePath.getSourcePath("weights10.txt"));
        //ga.setBatchSize(64);
        ga.setRepeatTimes(20);
        ga.setBatchSize(300);
        ga.train();

        //        long unity1 = 240 + (long)Math.pow(2, 10)*351 + (long)Math.pow(2, 20)*10 + (long)Math.pow(2, 30)*1023 + (long)Math.pow(2, 40)*555;
//        long unity2 = 255 + (long)Math.pow(2, 10)*457 + (long)Math.pow(2, 20)*1000 + (long)Math.pow(2, 30)*23 + (long)Math.pow(2, 40)*347;
//        Unity[] sons = ga.cross(new Unity(unity1), new Unity(unity2), true);
//        System.out.println((unity1|unity2)==(sons[0].value|sons[1].value));
//        System.out.println(Long.toBinaryString(unity1));
//        System.out.println(Long.toBinaryString(unity2));
//        System.out.println(Long.toBinaryString(sons[0].value));
//        System.out.println(Long.toBinaryString(sons[1].value));
//        List<BugReport> reports = new ArrayList<>();
//        double[] weights = {1, 2, 0.1, 0.1, 0.1};
//        for(int i=1; i<=308; ++i){
//            List<RawResult> results = FileTranslator.readRawResults(SavePath.getSourcePath("rawResult/eclipse-3.1-res")+i);
//            assert results != null;
//            reports.addAll(ga.processResult(results, weights));
//        }
//        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
//        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
//        indicator.print();
//        double[] weights = {1, 2, 0.1, 0.1, 0.1};
//        Unity unity = ga.presentWeight(weights);
//        ga.variant(unity);
//        System.out.println(Arrays.toString(ga.extractWeights(unity.value)));
//        List<RawResult> results = ga.getRandomBatch();
//        ga.evaluate(ga.presentWeight(weights), results);
//        long startTime = System.currentTimeMillis();
//        ga.initial();
//        Unity[] res = ga.newGeneration(ga.population, 10);
//        ga.evaluateWhole(res[0]);
//        long processEndTime = System.currentTimeMillis();
//        Logger.log("Finish all in " + (processEndTime-startTime)/1000.0 + " seconds");
    }
}

class Unity implements Comparable<Unity>{
    protected long value;
    protected double score;

    protected Unity(long value){
        this.value = value;
    }

    @Override
    public int compareTo(Unity o) {
        return Double.compare(score, o.score);
    }

    public Unity() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}