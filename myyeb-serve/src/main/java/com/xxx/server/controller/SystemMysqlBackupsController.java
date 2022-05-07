package com.xxx.server.controller;


import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.SystemMysqlBackups;
import com.xxx.server.service.SystemMysqlBackupsService;
import com.xxx.server.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/9/16 14:45
 * ClassName:SystemMysqlBackupsController
 * 类描述： MySQL数据备份接口
 */
@RestController
@Api(description = "MySQL数据备份")
@RequestMapping(value = "/sys/data")
public class SystemMysqlBackupsController {

    /**
     * 数据库用户名
     */
    @Value("${spring.datasource.username}")
    private String userName;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 数据库url
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * Windows数据库备份地址
     */
    @Value("D:/")
    private String windowsPath;

    /**
     * Linux数据库备份地址
     */
    @Value("D:/")
    private String linuxPath;


    @Autowired
    private SystemMysqlBackupsService systemMysqlBackupsService;

    @ApiOperation(value = "获取所有备份数据列表")
    @GetMapping("/")
    public List<SystemMysqlBackups> backupsList() {
        List<SystemMysqlBackups> systemMysqlBackups = systemMysqlBackupsService.selectBackupsList();
        return systemMysqlBackups;
    }
    

    @ApiOperation(value = "MySQL备份")
    @PostMapping("/")
    public Object mysqlBackups() {
        String path = null;
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (Constants.isSystem(osName)) {
            // Windows
            path = this.windowsPath;
        } else {
            // Linux
            path = this.linuxPath;
        }
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 数据库地址
        String url = this.url;
        // 调用备份
        Object systemMysqlBackups = systemMysqlBackupsService.mysqlBackups(path, url, userName, password);
        return RespBean.success("操作成功",systemMysqlBackups);
    }


    @ApiOperation(value = "恢复数据库")
    @PutMapping("/rollback")
    public Object rollback( @ApiParam(value = "恢复数据库") @RequestBody Map<String, Object> map) {
        Long id = Long.valueOf(map.get("id").toString());
        if (id == null) {
            return RespBean.error("id不能为null，请重新尝试！",HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        // 数据库用户名
        String userName = this.userName;
        // 数据库密码
        String password = this.password;
        // 根据id查询查询已有的信息
        SystemMysqlBackups smb = systemMysqlBackupsService.selectListId(id);
        // 恢复数据库
        Object rollback = systemMysqlBackupsService.rollback(smb, userName, password);
        // 更新操作次数
        systemMysqlBackupsService.updateById(smb);
        return RespBean.success("操作成功",rollback);
    }
}