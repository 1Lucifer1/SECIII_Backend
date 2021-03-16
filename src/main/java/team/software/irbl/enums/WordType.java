package team.software.irbl.enums;

public enum WordType {
    CodeFileWord(0), BugReportWord(1);

    private int type;

    private WordType(int type){
        this.type = type;
    }

    public int value() {
        return type;
    }
}
