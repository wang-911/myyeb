package com.xxx.server.controller;

import com.xxx.server.aspect.annotation.Log;
import com.xxx.server.pojo.Employee;
import com.xxx.server.pojo.LogDO;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.RespPageBean;
import com.xxx.server.service.ILogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/log")
public class LogController {


    @Autowired
    private ILogService logService;

    @ApiOperation(value = "获取所有操作日志（分页）")
    @GetMapping("/")
    public RespPageBean getLogsByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      LogDO logDO){
        System.out.println(logDO);
       return logService.getLogsByPage(currentPage,size,logDO);
   }


    @Log("删除一条日志")
    @ApiOperation(value = "删除一条日志")
    @DeleteMapping("/{id}")
    public RespBean deleteLog(@PathVariable Long id){
        if (logService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }


}
