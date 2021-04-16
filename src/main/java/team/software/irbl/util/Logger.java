package team.software.irbl.util;

public class Logger {

    public static void log(String info){
        //System.out.println("Log: " + info);
    }

    public static void devLog(String info){
        //System.out.println("DevLog: " + info);
    }

    public static void debugLog(String info){
        //System.out.println("DebugLog: " + info);
    }

    public static void errorLog(String info){
        System.out.println("Error: " + info);
    }
}
