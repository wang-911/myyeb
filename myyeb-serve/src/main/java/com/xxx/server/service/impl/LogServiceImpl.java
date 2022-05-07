package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.server.mapper.DepartmentMapper;
import com.xxx.server.mapper.LogMapper;
import com.xxx.server.pojo.Department;
import com.xxx.server.pojo.LogDO;
import com.xxx.server.pojo.RespPageBean;
import com.xxx.server.service.IDepartmentService;
import com.xxx.server.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements ILogService {

    @Autowired
    LogMapper logMapper;

    @Override
    public RespPageBean getLogsByPage(Integer currentPage, Integer size, LogDO logDO) {
        //开启分页
        Page<LogDO> page=new Page<>(currentPage,size);
        IPage<LogDO> logIPage=logMapper.getLogsByPage(page,logDO);
        RespPageBean respPageBean=new RespPageBean(logIPage.getTotal(),logIPage.getRecords());

        return respPageBean;
    }
}
