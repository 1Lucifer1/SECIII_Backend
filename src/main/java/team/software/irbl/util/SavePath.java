package team.software.irbl.util;

import java.io.File;

public class SavePath {
    private static final String ROOT_PATH = "IRBL/data/";
    private static final String CLASS_PREPROCESS_PATH = "IRBL/data/class_preprocessed/";
    private static final String REPORT_PREPROCESS_PATH = "IRBL/data/report_preprocessed/";

    public static String getAbsolutePath(String source){
        return ROOT_PATH + source;
    }

    public static String getPathFromPackage(String pack){
        return pack.replace(".", "/").replaceFirst("/java$", ".java");
    }

    /**
     * linux 上的文件间隔符在win上也可以用，但win默认返回 \\
     * 若路径实际为linux下路径，则不会做任何事
     * @param path
     * @return
     */
    public static String pathTransformFromWinToLinux(String path){
        return path.replace('\\', '/');
    }

    public static String getPreProcessReportPath(String source){
        return REPORT_PREPROCESS_PATH + source;
    }

    public static String getPreProcessClassPath(String source){
        return CLASS_PREPROCESS_PATH + source;
    }
}
