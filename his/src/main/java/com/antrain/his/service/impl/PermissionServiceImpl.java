package com.antrain.his.service.impl;

import java.awt.*;
import java.util.List;

import javax.annotation.Resource;

import com.antrain.his.entity.Permission;
import com.antrain.his.entity.RolePermission;
import com.antrain.his.mapper.PermissionMapper;
import com.antrain.his.service.IPermissionService;
import com.antrain.his.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 权限 服务实现类
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

  @Resource
  private IRolePermissionService roles;

  @Override
  public Object listByTree() {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id", 0);
    List<Permission> list = this.list(wrapper);
    for (Permission i : list) {
      getChildren(i);
    }
    return list;
  }

  /**
   * 参数为父权限对象 通过父权限获取子权限, 这里假设为2级
   *
   */
  private void getChildren(Permission permission) {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id", permission.getId());
    // List<Permission> list =this.list(wrapper) ;
    // for (Permission i : list) {
    //   if (permission.getType() != 0) {
    //     getChildren(permission);
    //   }
    // }
    permission.setChildren(this.list(wrapper));
  }

  @Override
  public Object userPermissionList(int id) { //构建权限树
    QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", id);
    RolePermission rop = roles.getOne(wrapper,false);
    if (rop == null) return null;
    QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
    //mybatis-plus中inSql用法，相当于mysql嵌套查询
    //inSql("age", "1,2,3,4,5,6")--->age in (1,2,3,4,5,6)
    System.out.println("----------------------------------------------~~~~~~---------");
    System.out.println(rop.getPermissioinId().toString());
    System.out.println("----------------------------------------------~~~~~~---------");

    queryWrapper.eq("parent_id", 0).inSql("id", rop.getPermissioinId());
    List<Permission> list = this.list(queryWrapper);
    for (Permission i : list) {
      queryWrapper.clear();
      queryWrapper.eq("parent_id",i.getId()).inSql("id", rop.getPermissioinId());
      i.setChildren(this.list(queryWrapper));
    }
    System.out.println("----------------------------------2------------~~~~~~---------");
    System.out.println(list.toString());
    System.out.println("----------------------------------2------------~~~~~~---------");
    return list;
  }

}
