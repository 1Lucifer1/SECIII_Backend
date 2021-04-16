package team.software.irbl.core.store;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.*;

import java.util.List;

public interface DBProcessor {
    public int saveCodeFiles(List<StructuredCodeFile> codeFiles);

    public int saveBugReports(List<StructuredBugReport> bugReports);

    public int saveFixedFiles(List<FixedFile> fixedFiles);

    public int saveProject(Project project);

    public int saveRankRecord(List<RankRecord> records);
}
