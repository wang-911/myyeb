package com.xxx.server.controller;


import com.xxx.server.aspect.annotation.Log;
import com.xxx.server.pojo.Department;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.service.IDepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;


    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartment(){
        List<Department> list = departmentService.list();
        return list;
    }


    @Log("添加一个部门")
    @ApiOperation(value = "添加一个部门")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department){
        boolean save = departmentService.save(department);
        if(save){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @Log("删除一个部门")
    @ApiOperation(value = "删除一个部门")
    @DeleteMapping("/{id}")
    public RespBean deleteDepartmrnt(@PathVariable Integer id){
        boolean b = departmentService.removeById(id);
        if(b){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @Log("更新一个部门")
    @ApiOperation(value="更新一个部门")
    @PutMapping("/")
    public RespBean updateDepartment(@RequestBody Department department){
        boolean b = departmentService.updateById(department);
        if(b){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");

    }

    @Log("批量删除部门")
    @ApiOperation(value="批量删除部门")
    @DeleteMapping("/")
    public RespBean deleteDepByIds(Integer[] ids){
        if (departmentService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }


}
