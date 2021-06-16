package team.software.irbl.core.common.GA;

import com.baomidou.mybatisplus.extension.api.R;
import team.software.irbl.core.domain.RawResult;
import team.software.irbl.core.enums.ComponentType;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.domain.BugReport;
import team.software.irbl.util.SavePath;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class GeneticAlgorithm {

    // 种群，以一个64位Long来作为5种权重的表示，其中每种权重被分配10位，取值范围（0.00~10.23）
    private long[] population;
    private static int PresentBit = 10;
    private static long UnityLimit = (long) Math.pow(2, 50);
    
    // 每次交叉替换会更换的位数
    private static int CrossSize = 10;

    // 数据集路径
    private String dataSetPath;

    private int batchSize;
    private int repeatTimes;
    private int populationSize;


    public GeneticAlgorithm(String dataSetPath){
        this(dataSetPath, 100, 300, 1000);
    }

    public GeneticAlgorithm(String dataSetPath, int populationSize, int batchSize, int repeatTimes){
        this.dataSetPath = dataSetPath;
        this.population = new long[populationSize];
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
            population[i] = (long)(random.nextDouble()*UnityLimit);
        }
    }

    private long[] newGeneration(long[] old){
        return null;
    }

    /**
     * 互换部分片段
     * @param unity1
     * @param unity2
     * @param noRepeat 指定是否强制要求交换位置不重复
     * @return
     */
    private long[] cross(long unity1, long unity2, boolean noRepeat){
        Random random = new Random();
        long son1 = unity1, son2 = unity2;

        HashSet<Integer> positions = new HashSet<>();
        for(int i=0; i<CrossSize; ++i){
            int position = random.nextInt(PresentBit*ComponentType.total.value());
            if(noRepeat) {
                // 选取新位置
                while (positions.contains(position)) position = random.nextInt(PresentBit * ComponentType.total.value());
                positions.add(position);
            }
            long mask = (long)Math.pow(2, position);
            son1 = (son1 & ~mask) | (unity2 & mask);
            son2 = (son2 & ~mask) | (unity1 & mask);
        }
        return new long[]{son1, son2};
    }

    private long variant(long unity){
        return unity;
    }

    private double evaluate(long unity){
        return 0.0;
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


    public static void main(String[] args) {
//        String dir = SavePath.getSourcePath("rawResult/");
//        List<RawResult> results = FileTranslator.readRawResults(dir+"eclipse-3.1-res");
//        assert results != null;
//        System.out.println(results.size());

        GeneticAlgorithm ga = new GeneticAlgorithm(null);
        long unity = 240 + (long)Math.pow(2, 10)*351 + (long)Math.pow(2, 20)*10 + (long)Math.pow(2, 30)*1023 + (long)Math.pow(2, 40)*1024;
        System.out.println(Arrays.toString(ga.extractWeights(unity)));
    }
}
