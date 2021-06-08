package team.software.irbl.core.structureComponent;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.enums.CodeWordsType;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.core.vsm.VSM;

import java.util.*;

public class StructureRank {

    // 结构数量
    private final static int CODE_PART_NUM = 5;
    private final static int REPORT_PART_NUM = 2;

    // 默认权重, 顺序依照(summary, description) x (type, method, field, comment, context)
    private final static double[]DEFAULT_WEIGHT = {1,1,1,1,1,1,1,1,1,1};

    private VSM[] vsmList;

    private List<StructuredCodeFile> codeFiles;

    public StructureRank(List<StructuredCodeFile> codeFiles){
        this.codeFiles = codeFiles;
        initial();
    }

    public List<RankRecord> rank(StructuredBugReport report){
        return rank(report, DEFAULT_WEIGHT);
    }

    public List<RankRecord> rank(StructuredBugReport report, double[] weights){
        double[][] scores = new double[CODE_PART_NUM*REPORT_PART_NUM][];

        // 计算各部分相似度
        for(int index=0; index<CODE_PART_NUM; ++index){
            double[] scoreSummary = vsmList[index].getScores(report.getSummaryWords());
            scores[index] = scoreSummary;
            double[] scoreDesc = vsmList[index].getScores(report.getDescriptionWords());
            scores[index + CODE_PART_NUM] = scoreDesc;
        }

        // 使用归一化后的权重对各部分的相似得分加权
        List<RankRecord> records = new ArrayList<>();
        weights = normalize(weights);
        for(int fileIndex=0; fileIndex<codeFiles.size(); ++fileIndex){
            double score = 0;
            for(int partIndex=0; partIndex<CODE_PART_NUM*REPORT_PART_NUM; ++partIndex){
                score += weights[partIndex] * scores[partIndex][fileIndex];
            }
            records.add(new RankRecord(report.getReportIndex(), codeFiles.get(fileIndex).getFileIndex(), -1, score));
        }
        return records;
    }

    /**
     * 权重归一化（不修改传入的权重）
     * @param weights
     * @return
     */
    private double[] normalize(double[] weights){
        double[] newWeights = new double[weights.length];
        double total = 0;
        for(double weight: weights){
            total+=weight;
        }
        for(int i=0; i<weights.length; ++i){
            newWeights[i] = weights[i]/total;
        }
        return newWeights;
    }

    private void initial(){
        List<List<List<String>>> parts = new ArrayList<>();
        for(int i=0; i<CODE_PART_NUM ;++i){
            parts.add(new ArrayList<>());
        }

        for(StructuredCodeFile codeFile: codeFiles){
            parts.get(CodeWordsType.TYPES.value()).add(codeFile.getTypes());
            parts.get(CodeWordsType.METHODS.value()).add(codeFile.getMethods());
            parts.get(CodeWordsType.FIELDS.value()).add(codeFile.getFields());
            parts.get(CodeWordsType.COMMENTS.value()).add(codeFile.getComments());
            parts.get(CodeWordsType.CONTEXTS.value()).add(codeFile.getContexts());
        }

        // 为源代码文件的各部分都创建一个vsm
        vsmList = new VSM[CODE_PART_NUM];
        for(int i=0; i<CODE_PART_NUM ;++i){
            vsmList[i] = new VSM(parts.get(i));
        }
    }
}
