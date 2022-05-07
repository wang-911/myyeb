package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.server.mapper.SystemMysqlBackupsMapper;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.SystemMysqlBackups;
import com.xxx.server.service.SystemMysqlBackupsService;
import com.xxx.server.utils.Constants;
import com.xxx.server.utils.DateUtil;
import com.xxx.server.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Date: 2021/9/16 15:19
 * ClassName:SystemMysqlBackupsService
 * 类描述： MySQL备份实现
 */
@Slf4j
@Service
public class SystemMysqlBackupsServiceImpl extends ServiceImpl<SystemMysqlBackupsMapper, SystemMysqlBackups> implements SystemMysqlBackupsService {

    @Resource
    private SystemMysqlBackupsMapper systemMysqlBackupsMapper;

    @Override
    public List<SystemMysqlBackups> selectBackupsList() {
        return systemMysqlBackupsMapper.selectBackupsList();
    }

    @Override
    public Object mysqlBackups(String filePath, String url, String userName, String password) {
        System.out.println(url);
        // 获取ip
        final String ip = url.substring(13, 26);
        // 获取端口号
        final String port = url.substring(27, 31);
        // 获取数据库名称
        final String database_name = url.substring(32, 39);
        // 数据库文件名称

        StringBuilder mysqlFileName = new StringBuilder()
                .append(Constants.DATA_BASE_NAME)
                .append("_")
                .append(DateUtil.parseDateToStr(new Date(), "yyyy-MM-dd-HH-mm-ss"))
                .append(Constants.FILE_SUFFIX);
        // 备份命令
        StringBuilder cmd = new StringBuilder()
                .append("mysqldump ")
                .append("--no-tablespaces ")
                .append("-h")
                .append(ip)
                .append(" -u")
                .append(userName)
                .append(" -p")
                .append(password)
                // 排除MySQL备份表
                .append(" --ignore-table ")
                .append(database_name)
                .append(".mysql_backups ")
                .append(database_name)
                .append(" > ")
                .append(filePath)
                .append(mysqlFileName);
        // 判断文件是否保存成功
        if (!FileUtil.isFileExists(filePath)) {
            FileUtil.isDir(filePath);
            return  RespBean.error("备份失败，文件保存异常，请查看文件内容后重新尝试！",HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.value());
        }
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        String[] command = new String[0];
        if (Constants.isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        SystemMysqlBackups smb = new SystemMysqlBackups();
        // 备份信息存放到数据库
        smb.setMysqlIp(ip);
        smb.setMysqlPort(port);
        smb.setBackupsName(String.valueOf(mysqlFileName));
        smb.setDatabaseName(database_name);
        smb.setMysqlCmd(String.valueOf(cmd));
        smb.setBackupsPath(filePath);
        smb.setCreateTime(new Date());
        smb.setStatus(1);
        smb.setOperation(0);
        systemMysqlBackupsMapper.insert(smb);
        log.error("数据库备份命令为：{}", cmd);
        // 获取Runtime实例
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.info("Mysql 数据库备份成功,备份文件名：{}", mysqlFileName);
            } else {
                return  RespBean.error("网络异常，数据库备份失败",HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  RespBean.error("网络异常，数据库备份失败",HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return smb;
    }

    @Override
    public SystemMysqlBackups selectListId(Long id) {
        return systemMysqlBackupsMapper.selectListId(id);
    }

    @Override
    public Object rollback(SystemMysqlBackups smb, String userName, String password) {
        // 备份路径和文件名
        StringBuilder realFilePath = new StringBuilder().append(smb.getBackupsPath()).append(smb.getBackupsName());
        if (!FileUtil.isFileExists(String.valueOf(realFilePath))) {
            return RespBean.error("文件不存在，恢复失败，请查看目录内文件是否存在后重新尝试！",HttpStatus.NOT_FOUND.value());
        }
        StringBuilder cmd = new StringBuilder()
                .append("mysql -h")
                .append(smb.getMysqlIp())
                .append(" -u")
                .append(userName)
                .append(" -p")
                .append(password)
                .append(" ")
                .append(smb.getDatabaseName())
                .append(" < ")
                .append(realFilePath);
        String[] command = new String[0];
        log.error("数据库恢复命令为：{}", cmd);
        // 获取操作系统名称
        String osName = System.getProperty("os.name").toLowerCase();
        if (Constants.isSystem(osName)) {
            // Windows
            command = new String[]{"cmd", "/c", String.valueOf(cmd)};
        } else {
            // Linux
            command = new String[]{"/bin/sh", "-c", String.valueOf(cmd)};
        }
        // 恢复指令写入到数据库
        smb.setMysqlBackCmd(String.valueOf(cmd));
        // 更新操作次数
        smb.setRecoveryTime(new Date());
        smb.setOperation(smb.getOperation() + 1);
        // 获取Runtime实例
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {
                log.error("Mysql 数据库恢复成功,恢复文件名：{}", realFilePath);
            } else {
                return RespBean.error("网络异常，恢复失败，请稍后重新尝试！",HttpStatus.GATEWAY_TIMEOUT.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error( "网络异常，恢复失败，请稍后重新尝试！",HttpStatus.GATEWAY_TIMEOUT.value());
        }
        return smb;
    }
}