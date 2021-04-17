package team.software.irbl.core.dbstore;

import team.software.irbl.domain.*;

import java.util.List;

public interface DBProcessor {
    public int saveCodeFiles(List<CodeFile> codeFiles);

    public int saveBugReports(List<BugReport> bugReports);

    public int saveFixedFiles(List<FixedFile> fixedFiles);

    public int saveProject(Project project);

    public int updateProject(Project project);

    public int saveRankRecord(List<RankRecord> records);

    public Project getProjectByIndex(int projectIndex);
}
