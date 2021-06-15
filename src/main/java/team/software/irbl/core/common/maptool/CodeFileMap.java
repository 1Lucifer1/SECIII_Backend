package team.software.irbl.core.common.maptool;

import team.software.irbl.domain.CodeFile;

import java.util.List;

public abstract class CodeFileMap {

    protected List<CodeFile> codeFiles;

    public CodeFileMap(List<CodeFile> codeFiles){
        this.codeFiles = codeFiles;
        createCodeFileMap(codeFiles);
    }

    public List<CodeFile> values(){
        return codeFiles;
    }

    /**
     * 建立对源代码文件的映射
     * @param codeFiles
     */
    public abstract void createCodeFileMap(List<CodeFile> codeFiles);

    /**
     * 从map中取出对应的源代码，不存在则会返回null
     * @param packageName
     * @return
     */
    public abstract List<CodeFile> getCodeFileFromMap(String packageName);
}
