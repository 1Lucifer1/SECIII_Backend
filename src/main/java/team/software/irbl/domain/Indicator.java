package team.software.irbl.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class Indicator {
    @TableId(value = "project_index")
    private Integer projectIndex;
    @TableField(value = "top_1")
    private double Top1;
    @TableField(value = "top_5")
    private double Top5;
    @TableField(value = "top_10")
    private double Top10;
    @TableField(value = "mrr")
    private double MRR;
    @TableField(value = "map")
    private double MAP;

    public Integer getProjectIndex() {
        return projectIndex;
    }

    public void setProjectIndex(Integer projectIndex) {
        this.projectIndex = projectIndex;
    }

    public double getTop1() {
        return Top1;
    }

    public void setTop1(double top1) {
        Top1 = top1;
    }

    public double getTop5() {
        return Top5;
    }

    public void setTop5(double top5) {
        Top5 = top5;
    }

    public double getTop10() {
        return Top10;
    }

    public void setTop10(double top10) {
        Top10 = top10;
    }

    public double getMRR() {
        return MRR;
    }

    public void setMRR(double MRR) {
        this.MRR = MRR;
    }

    public double getMAP() {
        return MAP;
    }

    public void setMAP(double MAP) {
        this.MAP = MAP;
    }

    public void print(){
        System.out.println("Top@1:  " + getTop1());
        System.out.println("Top@5:  " + getTop5());
        System.out.println("Top@10: " + getTop10());
        System.out.println("MRR:    " + getMRR());
        System.out.println("MAP:    " + getMAP());
    }
}
