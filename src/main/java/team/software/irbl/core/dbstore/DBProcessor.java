package team.software.irbl.core.dbstore;

import team.software.irbl.domain.*;

import java.util.List;

public interface DBProcessor {
    int saveCodeFiles(List<CodeFile> codeFiles);

    int saveBugReports(List<BugReport> bugReports);

    int saveFixedFiles(List<FixedFile> fixedFiles);

    int saveProject(Project project);

    int updateProject(Project project);

    int cleanProject(int projectIndex);

    int saveRankRecord(List<RankRecord> records);

    Project getProjectByIndex(int projectIndex);

    Project getProjectByName(String projectName);
}
