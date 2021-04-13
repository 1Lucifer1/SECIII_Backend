package team.software.irbl.core.domain;

import team.software.irbl.domain.CodeFile;

import java.util.ArrayList;
import java.util.List;

public class StructuredCodeFile extends CodeFile {
    private List<String> types;
    private List<String> methods;
    private List<String> fields;
    private List<String> comments;
    private List<String> contexts;

    public StructuredCodeFile(){};
    public StructuredCodeFile(String fileName, String filePath, int projectIndex){
        super(fileName, filePath, projectIndex);
    }

    public void addType(String type){
        if(types == null){
            types = new ArrayList<>();
        }
        types.add(type);
    }

    public void addMethod(String method){
        if(methods == null){
            methods = new ArrayList<>();
        }
        methods.add(method);
    }

    public void addField(String field){
        if(fields == null){
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    public void addComment(String comment){
        if(comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

    public void addContext(String context){
        if(contexts == null){
            contexts = new ArrayList<>();
        }
        contexts.add(context);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }
}
