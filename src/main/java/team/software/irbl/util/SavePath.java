package team.software.irbl.util;


public class SavePath {
    /**
     * linux 上的文件间隔符在win上也可以用，但win默认返回 \\
     * 若路径实际为linux下路径，则不会做任何事
     * @param path
     * @return
     */
    public static String pathTransformFromWinToLinux(String path){
        return path.replace('\\', '/');
    }

    // 实验项目原文件（包括项目源代码与错误报告）所在路径
    private static String SOURCE_PATH = pathTransformFromWinToLinux(System.getProperty("user.dir")) + "/IRBL/data/";
    // 预处理文件存放路径
    private static String Preprocess_FILE_PATH = pathTransformFromWinToLinux(System.getProperty("user.dir")) + "/IRBL/data/preprocess/";
    // 测试所需文件存放路径，后面可能会与上述路径区分开
    private static String TEST_FILE_PATH = pathTransformFromWinToLinux(System.getProperty("user.dir")) + "/IRBL/data/test/";

    private static String LOG_FILE_PATH = pathTransformFromWinToLinux(System.getProperty("user.dir")) + "/IRBL/log/";

    public static String getSourcePath(String source){
        return SOURCE_PATH + source;
    }

    public static String getPreProcessReportPath(String projectName){
        return Preprocess_FILE_PATH + projectName + "_report";
    }

    public static String getPreProcessSourcePath(String projectName){
        return Preprocess_FILE_PATH + projectName + "_source";
    }

    public static String getTestFilePath(String fileName){
        return TEST_FILE_PATH + fileName;
    }

    public static String getLogFilePath(String fileName) {
        return LOG_FILE_PATH + fileName;
    }

    public static String getPathFromPackage(String pack){
        return pack.replace(".", "/").replaceFirst("/java$", ".java");
    }

}
