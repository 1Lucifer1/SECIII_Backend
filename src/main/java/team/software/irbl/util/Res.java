package team.software.irbl.util;

public class Res {

    public Object data;
    public boolean success;
    public String msg;

    public static Res success(Object data) {
        var r = new Res();
        r.data = data;
        r.success = true;
        return r;
    }

    public static Res success() {
        return success(null);
    }

    public static Res failure(String msg) {
        var r = new Res();
        r.data = null;
        r.msg = msg;
        r.success = false;
        return r;
    }

}
