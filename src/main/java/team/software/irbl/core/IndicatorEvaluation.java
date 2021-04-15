package team.software.irbl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.mapper.*;
import team.software.irbl.util.Err;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IndicatorEvaluation {

    private final static String NO_REPORT = "该项目下没有缺陷报告";
    private final static String OTHER_ERROR = "其他问题";

    public double Top(Integer K, int reportNum, List<List<Integer>> expectedList,
                      List<List<RankRecord>> rankRecordLists) throws Err {
//        int reportNum = projectMapper.selectById(projectIndex).getReportCount();
        if (reportNum == 0){
//            throw new Err(NO_REPORT);
            return 0.0;
        }
        int countOfSuccessLocalization = 0;
//        Map<String, Object> conditions = new HashMap<>();
//        conditions.put("project_index", projectIndex);
//        List<BugReport> bugReportList = bugReportMapper.selectByMap(conditions);

        for(int i=0; i<reportNum; i++){
//        for(BugReport bugReport: bugReportList){
//            int reportIndex = bugReport.getReportIndex();
            // 得到正确的文件号列表
//            List<Integer> expected = getExpectedFileIndexes(reportIndex);
            List<Integer> expected = expectedList.get(i);

//            List list = new ArrayList(K);
//            for (int i=1;i<=K;i++){
//                list.add(i);
//            }
//            QueryWrapper<RankRecord> queryWrapper = new QueryWrapper<>();
//            queryWrapper.in("file_rank", list)
//                    .eq("report_index", reportIndex);
//            List<RankRecord> rankRecordList = rankRecordMapper.selectList(queryWrapper);
            List<RankRecord> rankRecordList = rankRecordLists.get(i);
//            for (RankRecord record : rankRecordList) {
//                if(expected.contains(record.getFileIndex())){
//                    countOfSuccessLocalization++;
//                    break;
//                }
            for (int j=0; j<K; j++) {
                if(expected.contains(rankRecordList.get(j).getFileIndex())){
                    countOfSuccessLocalization++;
                    break;
                }
                // 可替换使用方式，fixedFileList见 getExpectedFileIndexes 方法
//                if (fixedFileList.stream().filter(w->w.getFileIndex()==record.getFileIndex()).findAny().isPresent()){
//                    countOfSuccessLocalization++;
//                    break;
//                }
            }
        }
        return (double) countOfSuccessLocalization/reportNum;
    }


    public double MRR(int reportNum, List<List<Integer>> expectedList,
                      List<List<RankRecord>> rankRecordLists) throws Err {
//        int reportNum = projectMapper.selectById(projectIndex).getReportCount();
        if (reportNum == 0){
//            throw new Err(NO_REPORT);
            return 0.0;
        }
        double result = 0;
//        Map<String, Object> conditions = new HashMap<>();
//        conditions.put("project_index", projectIndex);
//        List<BugReport> bugReportList = bugReportMapper.selectByMap(conditions);

        for(int i=0; i<reportNum; i++){
//        for (BugReport bugReport : bugReportList){
//            int reportIndex = bugReport.getReportIndex();
            // 得到正确的文件号列表
            List<Integer> expected = expectedList.get(i);
            // 得到排序好的指定报告对应的所有文件列表
            List<RankRecord> rankRecordList = rankRecordLists.get(i);

            for(RankRecord rankRecord : rankRecordList){
                if(expected.contains(rankRecord.getFileIndex())){
                    result+=(double)1/rankRecord.getFileRank();
                    break;
                }
            }
        }
        result/=reportNum;
        return result;
    }


    public double MAP(int reportNum, List<List<Integer>> expectedList,
                      List<List<RankRecord>> rankRecordLists) throws Err {
//        int reportNum = projectMapper.selectById(projectIndex).getReportCount();
        if (reportNum == 0){
//            throw new Err(NO_REPORT);
            return 0.0;
        }
        double MAP = 0;
//        Map<String, Object> conditions = new HashMap<>();
//        conditions.put("project_index", projectIndex);
//        List<BugReport> bugReportList = bugReportMapper.selectByMap(conditions);

        for(int i=0; i<reportNum; i++){
//        for (BugReport bugReport : bugReportList) {
            double AvgP = 0;
//            int reportIndex = bugReport.getReportIndex();
            // 得到正确的文件号列表
            List<Integer> expected = expectedList.get(i);
            // 得到排序好的指定报告对应的所有文件列表
            List<RankRecord> rankRecordList = rankRecordLists.get(i);
            // 得到排序好的指定报告对应的所有文件id列表
            List<Integer> actual = rankRecordList.stream().map(RankRecord::getFileIndex).collect(Collectors.toList());
            // target用于存放列表actual中所有与缺陷报告相关的源码文件位置集合
            List<Integer> targetLocation = new ArrayList<>();
            for (Integer fileIndex: expected){
                // 判断是否含有这个元素，实际上应该一定存在的吧
                if (actual.contains(fileIndex)){
                    targetLocation.add(actual.indexOf(fileIndex));
                }
                else {
                    throw new Err(OTHER_ERROR);
                }
            }
            Collections.sort(targetLocation);
            int len = targetLocation.size();
            for(int j=1;j<=len;j++){
                AvgP+=(double)j/(targetLocation.get(j-1)+1);
            }
            AvgP=AvgP/len;
            MAP+=AvgP;
        }
        MAP=MAP/reportNum;
        return MAP;
    }


//    /**
//     * 得到正确的文件号列表
//     * @param reportIndex
//     * @return
//     */
//    private List<Integer> getExpectedFileIndexes(int reportIndex){
//        List<FixedFile> fixedFileList = fixedFileMapper.selectList(new QueryWrapper<FixedFile>().eq("report_index", reportIndex));
//        List<Integer> expected = fixedFileList.stream().map(FixedFile::getFileIndex).collect(Collectors.toList());
//        return expected;
//    }
//
//    /**
//     * 得到排序好的指定报告对应的所有文件列表
//     * @param reportIndex
//     * @return
//     */
//    private List<RankRecord> getSortedRankRecordList(int reportIndex){
//        Map<String, Object> conditions2 = new HashMap<>();
//        conditions2.put("report_index", reportIndex);
//        List<RankRecord> rankRecordList = rankRecordMapper.selectByMap(conditions2);
//        rankRecordList.sort(Comparator.comparing(RankRecord::getFileRank));
//        return rankRecordList;
//    }

}
