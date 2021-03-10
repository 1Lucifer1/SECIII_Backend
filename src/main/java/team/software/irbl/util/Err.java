package team.software.irbl.util;

public class Err extends Exception {

    public String msg;
    public Err(String msg) {
        super();
        this.msg = "[Error] " + msg;
    }

}
