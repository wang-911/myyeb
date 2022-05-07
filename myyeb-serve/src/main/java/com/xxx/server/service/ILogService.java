package com.xxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.Department;
import com.xxx.server.pojo.LogDO;
import com.xxx.server.pojo.RespPageBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface ILogService extends IService<LogDO> {


    RespPageBean getLogsByPage(Integer currentPage, Integer size, LogDO logDO);
}
