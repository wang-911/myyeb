package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.server.config.security.compoment.JwtTokenUtil;
import com.xxx.server.mapper.AdminRoleMapper;
import com.xxx.server.mapper.RoleMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.mapper.AdminMapper;
import com.xxx.server.pojo.AdminRole;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.Role;
import com.xxx.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public RespBean login(String username, String password, String code,HttpServletRequest request) {


        UserDetails userDetails=userDetailsService.loadUserByUsername(username);

        if(null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用，请联系管理员");
        }

        String captcha=(String)request.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(code)||!captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误，请重新输入");
        }

        //更新security登陆用户对象
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMAp=new HashMap<>();
        tokenMAp.put("token",token);
        tokenMAp.put("tokenHead",tokenHead);

        return RespBean.success("登陆成功",tokenMAp);
    }

    @Override
    public Admin getAdminByUserName(String username) {

        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    /**
     * 根据用户id查询角色
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }


    /**
     * 获取所有员工
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        Integer id = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return adminMapper.getAllAdmins(id,keywords);

    }


    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if (rids.length == result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updatePassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int i = adminMapper.updateById(admin);
            if (i == 1){
                return RespBean.success("更新成功");
            }
        }
        return RespBean.error("更新失败");
    }

    @Override
    public RespBean updatePasswordWithoutPass(String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode(pass));
        int i = adminMapper.updateById(admin);
        if (i == 1){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }


}
