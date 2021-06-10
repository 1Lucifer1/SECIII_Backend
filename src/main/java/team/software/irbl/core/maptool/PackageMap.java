package team.software.irbl.core.maptool;

import team.software.irbl.domain.CodeFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PackageMap extends CodeFileMap {

    /**
     * 以包名建立对源代码文件的映射
     */
    private HashMap<String, CodeFile> codeFileMap;


    public PackageMap(List<CodeFile> codeFiles){
        super(codeFiles);
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
     * 根据包名取CodeFile
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
