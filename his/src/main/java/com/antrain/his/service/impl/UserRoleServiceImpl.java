package com.antrain.his.service.impl;

import com.antrain.his.entity.UserRole;
import com.antrain.his.mapper.UserRoleMapper;
import com.antrain.his.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户角色 服务实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
