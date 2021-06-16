package team.software.irbl.core.component;

import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.RankRecord;

import java.util.List;

public interface ComponentRank {
    List<RankRecord> rank(BugReport report);
}
