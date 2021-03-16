package team.software.irbl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SelfBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<T> entityList);

    /**
     * 批量插入并更新重复（实际用于批量更新）
     *
     * @param entityList 实体列表
     * @return 影响行数（实际为 总行数 + 有修改行数 ）
     */
    Integer insertOrUpdateBatch(List<T> entityList);

}
