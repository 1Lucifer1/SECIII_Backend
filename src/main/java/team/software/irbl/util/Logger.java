package team.software.irbl.util;

import java.util.Date;

public class Logger {

    public static void log(String info){
        Date date = new Date();
        System.out.println(date + "  Log: " + info);
    }

    public static void devLog(String info){
        //System.out.println("DevLog: " + info);
    }

    public static void debugLog(String info){
        //System.out.println("DebugLog: " + info);
    }

    public static void errorLog(String info){
        Date date = new Date();
        System.out.println(date + " Error: " + info);
    }
}
