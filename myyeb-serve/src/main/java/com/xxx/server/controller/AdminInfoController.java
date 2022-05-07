package com.xxx.server.controller;

import com.xxx.server.aspect.annotation.Log;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService adminService;

    @Log("更新当前用户信息")
    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        //更新成功,重新构建Authentication对象
        if (adminService.updateById(admin)) {
            /**
             * 1.用户对象
             * 2.凭证（密码）
             * 3.用户角色
             */
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities()));
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @Log("更新用户密码")
    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updatePassword(oldPass, pass, adminId);
    }
    @ApiOperation(value = "不要原密码更新用户密码")
    @PutMapping("/admin/pass2")
    public RespBean updateAdminPasswordWithoutPass(@RequestBody Map<String, Object> info){

        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updatePasswordWithoutPass( pass, adminId);

    }

//    @ApiOperation(value = "更新用户头像")
//    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "头像", dataType = "MultipartFile")})
//    @PutMapping("/admin/userface")
//    public RespBean updateUserFace(MultipartFile file, Integer id, Authentication authentication) {
//        //获取文件上传地址
//        String[] uploadPath = FastDFSUtils.upload(file);
//        String url = FastDFSUtils.getTrackerUrl() + uploadPath[0] + "/" + uploadPath[1];
//        return adminService.updateAdminUserFace(url,id,authentication);
//    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/admin/avatar")
    public RespBean updateUserFace(@RequestParam("avatar") MultipartFile uploadFile) throws IOException {
        System.out.println("更新用户头像");
        //获得项目的类路径
        //使用ClassUtils
        String path = ResourceUtils.getURL("classpath:").getPath();
       // String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        //空文件夹在编译时不会打包进入target中
        File uploadDir = new File(path+"/static/admin/userFace");
        if (!uploadDir.exists()) {
            System.out.println("上传头像路径不存在，正在创建...");
            uploadDir.mkdir();
        }
        if ( uploadFile != null) {
            //获得上传文件的文件名
            String oldName = uploadFile.getOriginalFilename();
            System.out.println("[上传的文件名]：" + oldName);
            //我的文件保存在static目录下的admin/userFace
            File avatar = new File(path + "/static/admin/userFace/" , oldName);
            try {
                //保存图片
                uploadFile.transferTo(avatar);
                //返回成功结果，附带文件的相对路径
                return RespBean.success("上传成功","/admin/userFace/"+oldName);
            }catch (IOException e) {
                e.printStackTrace();
                return RespBean.error("上传失败");
            }
        }else {
            System.out.println("上传的文件为空");
            return RespBean.error("文件传输错误");
        }

    }
}
