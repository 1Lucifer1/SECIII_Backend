package team.software.irbl.core.utils.jdt;

import org.eclipse.jdt.core.dom.*;
import team.software.irbl.util.Logger;

public class MyCommentVisitor extends ASTVisitor {

    CompilationUnit compilationUnit;

    private String[] source;

    private StringBuilder comments;

    public MyCommentVisitor(CompilationUnit compilationUnit, String[] source) {
        super();
        this.compilationUnit = compilationUnit;
        this.source = source;
    }

    public boolean visit(LineComment node) {
        if(comments == null){
            comments = new StringBuilder();
        }

        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        String[] line = source[startLineNumber].split("//");
        if(line.length == 2){
            comments.append(line[1].trim());
            comments.append('\n');

            Logger.devLog("Line comment in line " + compilationUnit.getLineNumber(node.getStartPosition()) + ":\n" + line[1]);
        }else {
            Logger.debugLog(source[startLineNumber]);
        }

        return false;
    }

    public boolean visit(BlockComment node) {
        if(comments == null){
            comments = new StringBuilder();
        }

        int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition()) - 1;
        int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength()) - 1;

        StringBuilder sb = new StringBuilder();
        for (int lineCount = startLineNumber ; lineCount<= endLineNumber; lineCount++) {

            String blockCommentLine = source[lineCount].trim();
            sb.append(blockCommentLine);
            sb.append('\n');
        }
        Logger.devLog("Block comment in line " + compilationUnit.getLineNumber(node.getStartPosition()) + ":\n" + sb.toString());
        comments.append(sb);

        return false;
    }

    /**
     * 获得符合javadoc的注释
     * @param node
     * @return
     */
    @Override
    public boolean visit(Javadoc node) {
        if(comments == null){
            comments = new StringBuilder();
        }

        comments.append(node.toString());
        comments.append('\n');
        Logger.devLog("Doc in line " + compilationUnit.getLineNumber(node.getStartPosition()) + ":\n" + node);
        return false;
    }

    public String getComments(){
        if(comments == null) return "";
        else return comments.toString();
    }


    public void preVisit(ASTNode node) {

    }
}