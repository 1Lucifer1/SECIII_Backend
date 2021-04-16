package team.software.irbl.core.store;

import org.springframework.stereotype.Component;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;

import java.util.List;

@Component
public class DBProcessorImpl implements DBProcessor {
    @Override
    public int saveCodeFiles(List<StructuredCodeFile> codeFiles) {
        return 0;
    }

    @Override
    public int saveBugReports(List<StructuredBugReport> bugReports) {
        return 0;
    }

    @Override
    public int saveFixedFiles(List<FixedFile> fixedFiles) {
        return 0;
    }

    @Override
    public int saveProject(Project project) {
        return 0;
    }

    @Override
    public int saveRankRecord(List<RankRecord> records) {
        return 0;
    }
}
