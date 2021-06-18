package team.software.irbl.core.common.dbstore;

import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.domain.*;

import java.util.List;

public interface DBProcessor {
    int saveCodeFiles(List<CodeFile> codeFiles);

    int saveBugReports(List<BugReport> bugReports, CodeFileMap codeFileMap);

    int saveFixedFiles(List<FixedFile> fixedFiles);

    int saveProject(Project project);

    int updateProject(Project project);

    int cleanProject(int projectIndex);

    int saveRankRecord(List<RankRecord> records);

    int saveIndicator(Indicator indicator);

    Project getProjectByIndex(int projectIndex);

    Project getProjectByName(String projectName);

    List<CodeFile> getCodeFilesByProjectIndex(int projectIndex);

    List<BugReport> getBugReportsByProjectIndex(int projectIndex);

}
