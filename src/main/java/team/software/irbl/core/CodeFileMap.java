package team.software.irbl.core;

import team.software.irbl.domain.CodeFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodeFileMap {

    /**
     * 以包名建立对源代码文件(实际只需要用到源代码文件索引，故直接包装为Rank record)的映射
     */
    private HashMap<String, CodeFile> codeFileMap;

    private List<CodeFile> codeFiles;

    public CodeFileMap(List<CodeFile> codeFiles){
        this.codeFiles = codeFiles;
        createCodeFileMap(codeFiles);
    }

    public List<CodeFile> values(){
        return codeFiles;
    }

    /**
     * 以包名建立对源代码文件的映射
     * @param codeFiles
     */
    public void createCodeFileMap(List<CodeFile> codeFiles){
        codeFileMap = new HashMap<>();
        for(CodeFile codeFile: codeFiles) {
            // 对于包名冲突的文件，采用包名加后缀 @n 的方式保留
            if(codeFileMap.containsKey(codeFile.getPackageName())){
                int conflictCount = 1;
                String conflictPackage = codeFile.getPackageName() + "@" + conflictCount;
                while (codeFileMap.containsKey(conflictPackage)){
                    conflictCount++;
                    conflictPackage = codeFile.getPackageName() + "@" + conflictCount;
                }
                codeFileMap.put(conflictPackage, codeFile);
            }else {
                codeFileMap.put(codeFile.getPackageName(), codeFile);
            }
        }
    }

    /**
     * 从map中取出对应的包装fileIndex的rank record，考虑包名冲突的情况，结果为列表
     * @param packageName
     * @return
     */
    public List<CodeFile> getCodeFileFromMap(String packageName){
        if(!codeFileMap.containsKey(packageName)){
            return null;
        }

        List<CodeFile> codeFiles = new ArrayList<>();
        codeFiles.add(codeFileMap.get(packageName));

        int conflictCount = 1;
        String conflictPackageName = packageName + "@" + conflictCount;
        while (codeFileMap.containsKey(conflictPackageName)){
            codeFiles.add(codeFileMap.get(conflictPackageName));
            conflictCount++;
            conflictPackageName = packageName + "@" + conflictCount;
        }
        return codeFiles;
    }
}
