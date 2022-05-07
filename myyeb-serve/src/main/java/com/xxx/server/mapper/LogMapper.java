package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.server.pojo.Department;
import com.xxx.server.pojo.LogDO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface LogMapper extends BaseMapper<LogDO> {

    IPage<LogDO> getLogsByPage(Page<LogDO> page, @Param("logDO") LogDO logDO);
}
