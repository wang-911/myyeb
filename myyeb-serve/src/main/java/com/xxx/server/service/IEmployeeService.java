package com.xxx.server.service;

import com.xxx.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface IEmployeeService extends IService<Employee> {

    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);


    RespBean insertEmployee(Employee employee);

    List<Employee> getEmployee(Integer id);
}
