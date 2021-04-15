package team.software.irbl.enums;

public enum ReportWordsType {
    SUMMARY_WORDS(0), DESCRIPTION_WORDS(1);

    private final int type;

    ReportWordsType(int type){
        this.type = type;
    }

    public int value() {
        return type;
    }
}
