package team.software.irbl.core.common.GA;

import team.software.irbl.core.domain.RawResult;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.domain.BugReport;

import java.util.List;

public class GeneticAlgorithm {

    // 种群，以一个64位Long来作为5种权重的表示，其中每种权重被分配10位，取值范围（0.00~10.24）
    private long[] population;

    // 数据集
    private List<RawResult> dataSet;

    private int batchSize;
    private int repeatTimes;
    private int populationSize;


    public GeneticAlgorithm(List<RawResult> dataSet){
        this(dataSet, 100, dataSet.size(), 1000);
    }

    public GeneticAlgorithm(List<RawResult> dataSet, int populationSize, int batchSize, int repeatTimes){
        this.dataSet = dataSet;
        this.population = new long[populationSize];
        this.populationSize = populationSize;
        this.batchSize = batchSize;
        this.repeatTimes = repeatTimes;
    }

    public void train(){

    }

    private long[] newGeneration(long[] old){
        return null;
    }

    private long[] cross(long unity1, long unity2){
        return null;
    }

    private long variant(long unity){
        return unity;
    }

    private double evaluate(long unity){
        return 0.0;
    }

    private double[] extractWeights(long unity){
        return null;
    }


    public static void main(String[] args) {
        String dir = "D:/大三下/软工三/resource/data/rawResult/";
        List<RawResult> results = FileTranslator.readRawResults(dir+"eclipse-3.1-res");
        assert results != null;
        System.out.println(results.size());
    }
}
