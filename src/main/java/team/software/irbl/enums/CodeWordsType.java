package team.software.irbl.enums;

public enum CodeWordsType {
    TYPES(0), METHODS(1), FIELDS(2), COMMENTS(3), CONTEXTS(4);

    private final int type;

    CodeWordsType(int type){
        this.type = type;
    }

    public int value() {
        return type;
    }
}
