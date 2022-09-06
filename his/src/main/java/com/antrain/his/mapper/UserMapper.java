package com.antrain.his.mapper;

import com.antrain.his.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户 Mapper 接口
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT dept_id,count(id) count FROM sys_user GROUP BY dept_id")
    List<Map<String, Object>> getDataGroup();

    @Select("select * from sys_user")
    List<User> getAllUser();
}
