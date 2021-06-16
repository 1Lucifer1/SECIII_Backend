package team.software.irbl.core.common.GA;

import com.baomidou.mybatisplus.extension.api.R;
import team.software.irbl.core.domain.RawResult;
import team.software.irbl.core.enums.ComponentType;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.util.SavePath;

import java.util.*;
import java.util.Map.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

public class GeneticAlgorithm {

    // 种群，以一个64位Long来作为5种权重的表示，其中每种权重被分配10位，取值范围（0.00~10.23）
    private Unity[] population;
    private static int PresentBit = 10;
    private static long UnityLimit = (long) Math.pow(2, 50);
    
    // 每次交叉替换会更换的位数
    private static int CrossSize = 10;

    // 数据集路径
    private String dataSetPath;
    private int dataSize;

    private int batchSize;
    private int repeatTimes;
    private int populationSize;


    public GeneticAlgorithm(String dataSetPath, int dataSize){
        this(dataSetPath, 100, 300, 1000);
    }

    public GeneticAlgorithm(String dataSetPath, int populationSize, int batchSize, int repeatTimes){
        this.dataSetPath = dataSetPath;
        this.population = new Unity[populationSize];
        this.populationSize = populationSize;
        this.batchSize = batchSize;
        this.repeatTimes = repeatTimes;
    }

    public void train(){

    }

    private void initial(){
        // 随机初始化
        Random random = new Random();
        for(int i=0; i<populationSize; ++i){
            population[i] = new Unity((long)(random.nextDouble()*UnityLimit));
        }
    }

    private Unity[] newGeneration(Unity[] old){
        return null;
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
     * 变异算子
     * @param unity
     * @return
     */
    private long variant(long unity){

        return unity;
    }

    private void evaluate(Unity unity, boolean useWhole, boolean print){
        double[] weights = extractWeights(unity.value);

        List<RawResult> results = new ArrayList<>();
        List<BugReport> reports = new ArrayList<>();

        if(useWhole){
            for(int i=0; i<dataSize; ++i){

            }
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

    private List<BugReport> processResult(List<RawResult> results, double[] weights){
        List<BugReport> reports = new ArrayList<>();
        for(RawResult result: results) {
            BugReport bugReport = result.getReport();
            ConcurrentHashMap<Integer, Double> scoreMap = new ConcurrentHashMap<>();

            // 根据论文公式计算与合并各部分打分
            result.getRankResults(ComponentType.STRUCTURE.value()).forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), rankRecord.getScore() * weights[ComponentType.STRUCTURE.value()]));

            if (result.getRankResults(ComponentType.STACK.value()) != null)
                result.getRankResults(ComponentType.STACK.value()).forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), scoreMap.get(rankRecord.getFileIndex()) + rankRecord.getScore() * weights[ComponentType.STACK.value()]));

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
        }
        return reports;
    }


    public static void main(String[] args) {
//        String dir = SavePath.getSourcePath("rawResult/");
//        List<RawResult> results = FileTranslator.readRawResults(dir+"eclipse-3.1-res");
//        assert results != null;
//        System.out.println(results.size());

        GeneticAlgorithm ga = new GeneticAlgorithm(null, 308);
        long unity1 = 240 + (long)Math.pow(2, 10)*351 + (long)Math.pow(2, 20)*10 + (long)Math.pow(2, 30)*1023 + (long)Math.pow(2, 40)*555;
        long unity2 = 255 + (long)Math.pow(2, 10)*457 + (long)Math.pow(2, 20)*1000 + (long)Math.pow(2, 30)*23 + (long)Math.pow(2, 40)*347;
        Unity[] sons = ga.cross(new Unity(unity1), new Unity(unity2), true);
        System.out.println((unity1|unity2)==(sons[0].value|sons[1].value));
        System.out.println(Long.toBinaryString(unity1));
        System.out.println(Long.toBinaryString(unity2));
        System.out.println(Long.toBinaryString(sons[0].value));
        System.out.println(Long.toBinaryString(sons[1].value));
    }
}

class Unity{
    protected long value;
    protected double score;

    protected Unity(long value){
        this.value = value;
    }
}