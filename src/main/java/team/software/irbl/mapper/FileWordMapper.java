package team.software.irbl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import team.software.irbl.domain.FileWord;

import java.util.Collection;

public interface FileWordMapper extends BaseMapper<FileWord> {
    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<FileWord> entityList);
}
