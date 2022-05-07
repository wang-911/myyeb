package com.xxx.server.controller;


import com.xxx.server.aspect.annotation.Log;
import com.xxx.server.pojo.Joblevel;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "查询所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @Log("添加职称")
    @ApiOperation(value = "添加职称")
    @PostMapping("/")
    public RespBean addJoblevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if (joblevelService.save(joblevel)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @Log("修改职称")
    @ApiOperation(value = "修改职称")
    @PutMapping("/")
    public RespBean updateJoblevel(@RequestBody Joblevel joblevel){
        System.out.println(joblevel);
        if (joblevelService.updateById(joblevel)){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");

    }

    @Log("删除职称")
    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id){
        if (joblevelService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @Log("批量删除职称")
    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJoblevelByIds(Integer[] ids){
        if (joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

}
