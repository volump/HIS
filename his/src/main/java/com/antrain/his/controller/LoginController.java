package com.antrain.his.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.antrain.his.entity.User;
import com.antrain.his.service.IUserService;
import com.antrain.his.utils.JwtUtil;
import com.antrain.his.utils.Result;
import com.antrain.his.utils.ResultGenerator;
import com.antrain.his.utils.ShaUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  
  @Resource
  private IUserService userService;

  /**
   * 登陆
   * @param param
   * @return
   */
  @PostMapping("/login")
  public Result login(@RequestBody Map<String,Object> param){
    System.out.println(param);
    QueryWrapper<User> wrapper = new QueryWrapper<>();//条件(mybatis-plus中)，相当于sql中的where
    //登陆时加密检验
    //wrapper.eq("telephone", param.get("telephone")).eq("password", ShaUtil.getSHA256(param.get("password").toString()));//带加密的等值查询
    wrapper.eq("telephone", param.get("telephone")).eq("password", param.get("password").toString());//不带加密的等值查询
    User user =  userService.getOne(wrapper);
    if (user == null){
      return ResultGenerator.getFailResult(null,"手机号或密码错误");
    }
    if (user.getActive() == 0) {
      return ResultGenerator.getFailResult(null,"请联系管理员小哥哥帮你激活");
    }
    user.setLastlogin(LocalDateTime.now());//设置最后一次登陆时间，对应数据库中sys_user表中的lastlogin
    updateLoginTime(user.getId());//更新时间
    JSONObject jsonObject = new JSONObject();//创建一个json对象
    jsonObject.put("token", JwtUtil.create(user));//创建token放入json对象中
    jsonObject.put("id",user.getId());//id放入json对象中
    jsonObject.put("deptId",user.getDeptId());//deptId(sys_user的外码)放入json对象中
    return ResultGenerator.getSuccessResult(jsonObject,"登录成功");
  }

  //获得用户id，更新最后一次登录时间为当前时间
  private void updateLoginTime(int id){
    User user = new User();
    user.setId(id);
    user.setLastlogin(LocalDateTime.now());
    userService.updateById(user);
  }

}