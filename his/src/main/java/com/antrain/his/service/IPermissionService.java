package com.antrain.his.service;

import com.antrain.his.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 权限 服务类
 */
public interface IPermissionService extends IService<Permission> {
  //调用PerssionServiceImpl中的方法
  Object listByTree();
  Object userPermissionList(int id);
}
