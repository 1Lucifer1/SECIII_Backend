package team.software.irbl.util;

public class SavePath {
    private static final String ROOT_PATH = "D:\\\\大三下\\\\软工三\\\\IRBL-基于信息检索的缺陷定位（迭代一与迭代二）\\\\data\\\\";

    public static String getAbsolutePath(String source){
        return ROOT_PATH + source;
    }
}
