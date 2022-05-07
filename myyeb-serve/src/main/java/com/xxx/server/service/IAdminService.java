package com.xxx.server.service;

import com.xxx.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.Menu;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登陆之后，返回token
     * @param username
     * @param password
     * @param request
     * @return
     */

    RespBean login(String username, String password,String code, HttpServletRequest request);

    Admin getAdminByUserName(String username);


    /**
     * 根据用户id查询角色
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);


    /**
     * 获取所有员工
     * @return
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    RespBean updatePassword(String oldPass, String pass, Integer adminId);

    RespBean updatePasswordWithoutPass(String pass, Integer adminId);
}
