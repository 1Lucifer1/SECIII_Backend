package team.software.irbl.core.utils;

public class Calculate {
    // private static final int IDF_BASE = 10;


    public static double calculateTf(int appearTimes){
        return 1 + Math.log(appearTimes) ;
    }

    public static double calculateIdf(int appearFiles, int fileTotal){
        return Math.log((fileTotal+1)/(double)appearFiles);
    }

}
