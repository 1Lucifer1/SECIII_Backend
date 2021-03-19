package team.software.irbl.util;

import java.io.File;

public class SavePath {
    private static final String ROOT_PATH = "src/main/resources/data/";

    public static String getAbsolutePath(String source){
        return ROOT_PATH + source;
    }

    public static String getPathFromPackage(String pack){
        return pack.replace(".", "/").replaceFirst("/" + "java$", ".java");
    }
}
