package team.software.irbl.core.common.maptool;

import team.software.irbl.domain.CodeFile;
import team.software.irbl.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilePathMap extends CodeFileMap {

    /**
     * 以路径建立对源代码文件的映射
     */
    private HashMap<String, CodeFile> codeFileMap;

    public FilePathMap(List<CodeFile> codeFiles) {
        super(codeFiles);
    }

    /**
     * 以路径建立对源代码文件的映射
     * @param codeFiles
     */
    public void createCodeFileMap(List<CodeFile> codeFiles) {
        codeFileMap = new HashMap<>();
        for(CodeFile codeFile: codeFiles) {
            if(codeFileMap.containsKey(codeFile.getFilePath())){
                Logger.errorLog("Duplicated file path.");
            }else {
                codeFileMap.put(codeFile.getFilePath(), codeFile);
            }
        }
    }

    /**
     * 根据路径名取CodeFile
     * @param filePath
     * @return
     */
    public List<CodeFile> getCodeFileFromMap(String filePath) {
        if(codeFileMap.containsKey(filePath)){
            List<CodeFile> codeFiles = new ArrayList<>();
            codeFiles.add(codeFileMap.get(filePath));
            return codeFiles;
        }
        return null;
    }
}
