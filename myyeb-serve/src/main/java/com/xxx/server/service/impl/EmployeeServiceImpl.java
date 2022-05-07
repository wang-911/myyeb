package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.server.pojo.Employee;
import com.xxx.server.mapper.EmployeeMapper;
import com.xxx.server.pojo.RespBean;
import com.xxx.server.pojo.RespPageBean;
import com.xxx.server.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        //开启分页
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(), employeeIPage.getRecords());
        return respPageBean;
    }

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */


    @Override
    public RespBean insertEmployee(Employee employee) {
        // 处理合同期限，保留2位小数
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if (1==employeeMapper.insert(employee)){

            Employee emp=employeeMapper.getEmployee(employee.getId()).get(0);
            sendMailtoPerson(emp);
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }



    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }

    public void sendMailtoPerson(Employee employee) {
        //一个复杂的邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);

            helper.setSubject("航摄公司欢迎您！");

            helper.setText("欢迎您加入航摄公司的大家庭！");

            String email=employee.getEmail();
            helper.setTo(email);
            helper.setFrom("1648192086@qq.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);

    }


}
