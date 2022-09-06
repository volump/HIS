package com.antrain.his.service.impl;

import com.antrain.his.entity.User;
import com.antrain.his.mapper.UserMapper;
import com.antrain.his.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
