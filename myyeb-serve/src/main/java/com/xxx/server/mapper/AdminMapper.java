package com.xxx.server.mapper;

import com.xxx.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhanglishen
 * @since 2022-02-22
 */
public interface AdminMapper extends BaseMapper<Admin> {


    List<Admin> getAllAdmins(@Param("id") Integer id,@Param("keywords") String keywords);
}
