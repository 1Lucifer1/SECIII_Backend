package team.software.irbl.util;

import java.io.File;

public class SavePath {
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + File.separator +
            "src" + File.separator + File.separator + "main" + File.separator + File.separator +
            "resources" + File.separator + File.separator + "data" + File.separator + File.separator;

    public static String getAbsolutePath(String source){
        return ROOT_PATH + source;
    }

    public static String getPathFromPackage(String pack){
        if(File.separator.equals("\\")) return pack.replace(".", "\\\\").replaceFirst("\\\\java$", ".java");
        return pack.replace(".", File.separator).replaceFirst(File.separator + "java$", ".java");
    }
}
