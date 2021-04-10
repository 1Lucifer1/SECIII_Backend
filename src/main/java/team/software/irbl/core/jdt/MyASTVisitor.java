package team.software.irbl.core.jdt;

import org.eclipse.jdt.core.dom.*;
import team.software.irbl.util.Logger;


public class MyASTVisitor extends ASTVisitor {

    private StringBuilder types;
    private StringBuilder methods;
    private StringBuilder fields;

    CompilationUnit compilationUnit;

    public MyASTVisitor(CompilationUnit compilationUnit){
        this.compilationUnit = compilationUnit;
    }

    /**
     * 获得局部变量名与成员变量名
     * @param node
     * @return
     */
    @Override
    public boolean visit(VariableDeclarationFragment node) {
        if(fields == null){
            fields = new StringBuilder();
        }

        SimpleName name = node.getName();
        fields.append(name.getIdentifier());
        fields.append(' ');
        Logger.devLog("Field in line " + compilationUnit.getLineNumber(node.getStartPosition()) +": " + name);
        return true;
    }

    /**
     * 获得形参名
     * @param node
     * @return
     */
    @Override
    public boolean visit(SingleVariableDeclaration node) {
        if(fields == null){
            fields = new StringBuilder();
        }

        SimpleName name = node.getName();
        fields.append(name.getIdentifier());
        fields.append(' ');
        Logger.devLog("Field in line " + compilationUnit.getLineNumber(node.getStartPosition()) +": " + name);
        return true;
    }


    /**
     * 获得方法名
     * @param node
     * @return
     */
    @Override
    public boolean visit(MethodDeclaration node) {
        if(methods == null){
            methods = new StringBuilder();
        }

        if(!node.getName().getIdentifier().equals("run")) {
            methods.append(node.getName().getIdentifier());
            methods.append(' ');
            Logger.devLog("Method in line " + compilationUnit.getLineNumber(node.getStartPosition()) + ": " + node.getName());
        }
        return true;
    }

    /**
     * 获得类名（包括内部类）
     * @param node
     * @return
     */
    @Override
    public boolean visit(TypeDeclaration node) {
        if(types == null){
            types = new StringBuilder();
        }

        types.append(node.getName().toString());
        types.append(' ');
        Logger.devLog("Class in line " + compilationUnit.getLineNumber(node.getStartPosition()) + ": " + node.getName());
        return true;
    }


    public String getFields(){
        if(fields == null) return "";
        else return fields.toString();
    }

    public String getTypes(){
        if(types == null) return "";
        else return types.toString();
    }

    public String getMethods(){
        if(methods == null) return "";
        else return methods.toString();
    }

}
