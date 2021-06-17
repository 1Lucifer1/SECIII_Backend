package team.software.irbl.core.enums;

public enum ComponentType {
    STACK(0), STRUCTURE(1), VERSION(2), REPORTER(3), REPORT(4), total(5);

    private final int type;

    ComponentType(int type){
        this.type = type;
    }

    public int value() {
        return type;
    }
}
