package com.xxx.server.service;

import com.xxx.server.pojo.Menu;
import com.xxx.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface IMenuRoleService extends IService<MenuRole> {


    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
