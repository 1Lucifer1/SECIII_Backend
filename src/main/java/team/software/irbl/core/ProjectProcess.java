package team.software.irbl.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.core.Preprocess;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FileWord;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.ProjectWord;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.FileWordMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.ProjectWordMapper;
import team.software.irbl.util.SavePath;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class ProjectProcess {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private FileWordMapper fileWordMapper;

    @Autowired
    private ProjectWordMapper projectWordMapper;


    public Project preprocess(String projectName){
        return null;
    }

    public Project traverseAndSaveProject(){
        return traverseAndSaveProject("swt-3.1");
    }


    public Project traverseAndSaveProject(String projectName) {
        String projectPath = SavePath.getAbsolutePath(projectName);
        File root = new File(projectPath);
        if (root.exists()) {
            Project project = new Project(projectName);

            // insert方法会自动修改project.projectIndex
            projectMapper.insert(project);
            int projectIndex = project.getProjectIndex();
            System.out.println(projectIndex);
            List<CodeFile> codeFiles = new ArrayList<>();
            int codeFileCount = 0;

            LinkedList<File> dirs = new LinkedList<>();
            dirs.add(root);

            String suffix = ".+\\.java$";
            while (!dirs.isEmpty()) {
                File dir = dirs.removeFirst();
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            dirs.add(file);
                        } else if (Pattern.matches(suffix, file.getName())) {
                            codeFileCount++;
                            codeFiles.add(new CodeFile(file.getName(), file.getAbsolutePath().replaceFirst(projectPath, projectName), projectIndex));
                        }
                    }
                }
            }
            project.setCodeFileCount(codeFileCount);
            project.setCodeFiles(codeFiles);
            projectMapper.updateById(project);
            codeFileMapper.insertBatchSomeColumn(codeFiles);

            scanFile(project);
            return project;
        }
        return null;
    }

    public void scanFile(Project project){
        List<CodeFile> codeFiles = project.getCodeFiles();
        ConcurrentHashMap<String, ProjectWord> projectWordMap = new ConcurrentHashMap<>();
        codeFiles.forEach(codeFile -> {
            ConcurrentHashMap<String, FileWord> fileWordMap = new ConcurrentHashMap<>();
            int wordCount = 0;
            File file = Preprocess.preprocessCodeFile(codeFile);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                while (line != null) {
                    String[] words = line.split(" ");
                    for (String word : words) {
                        wordCount++;
                        if (fileWordMap.containsKey(word)) {
                            fileWordMap.get(word).addAppearTimes();
                        } else {
                            fileWordMap.put(word, new FileWord(word, codeFile.getFileIndex()));
                        }
                    }
                    line = reader.readLine();
                }
                // 避免lambda表达式报错
                final double finalCount = wordCount;
                fileWordMap.values().forEach(fileWord -> {
                    double tf = fileWord.getAppearTimes() / finalCount;
                    fileWord.setTf(tf);
                    String word = fileWord.getWord();
                    // 可能会出现线程并发不安全，改为使用ConcurrentHashMap
                    if(projectWordMap.containsKey(word)){
                        projectWordMap.get(word).addAppearFiles();
                    }else {
                        projectWordMap.put(word, new ProjectWord(word, project.getProjectIndex()));
                    }
                });
                List<FileWord> fileWords = new ArrayList<>(fileWordMap.values());
                fileWordMapper.insertBatchSomeColumn(fileWords);
                codeFile.setWordCount(wordCount);
                codeFile.setWordMap(fileWordMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        codeFileMapper.insertOrUpdateBatch(codeFiles);

        final double fileCount = project.getCodeFileCount();
        projectWordMap.values().forEach(word -> {
            double idf = fileCount/ word.getAppearFiles();
            word.setIdf(idf);
        });
        List<ProjectWord> projectWords = new ArrayList<>(projectWordMap.values());
        projectWordMapper.insertBatchSomeColumn(projectWords);
        project.setWordMap(projectWordMap);
    }
}
