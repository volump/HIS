package com.antrain.his.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.antrain.his.annotation.UserLoginToken;
import com.antrain.his.entity.Permission;
import com.antrain.his.entity.User;
import com.antrain.his.service.IPermissionService;
import com.antrain.his.utils.InitUtil;
import com.antrain.his.utils.JwtUtil;
import com.antrain.his.utils.Result;
import com.antrain.his.utils.ResultGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 权限 前端控制器
 */
@RestController
@RequestMapping("/permissions")
public class PermissionController {

  private Logger log = LoggerFactory.getLogger(PermissionController.class);
  
  @Resource
  private IPermissionService permissionService;

  @GetMapping("/lists")
  public Result getlist(@RequestParam Map<String, Object> param) {
    InitUtil.initPage(param);
    int num = Integer.parseInt(param.get("page").toString());
    int limit = Integer.parseInt(param.get("limit").toString());
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    InitUtil.initLike(param, wrapper, "name");
    InitUtil.initEq(param, wrapper, "active");
    IPage<Permission> page = new Page<>(num, limit);
    return ResultGenerator.getSuccessResult(permissionService.page(page, wrapper));
  }

  /**
   * 获取树形的权限树
   */
  /**
   * 1. @GetMapping的标准语法是有Value的。
   * 2. 如果@GetMapping没有指定Value出现了一次，那当前的Control中有且就只能存在这样的一个空Value的@GetMapping。否则会编译报错。
   * 3. 当有一个路由指向了当前的Control，并且到了Control的路由就结束了指向，没有更进一步Handle寻找，此时如果存在这样一个空的Value路由，则会默认指向。
   * 4. 对于有具体Mapping路由的则返回对应的路由数据，没有则404.
   * 一般配合@RequestMapping
   */
  @GetMapping
  public Result getlistByTree() {
//    System.out.println("--------------------------------------------------------");
//    System.out.println(ResultGenerator.getSuccessResult(permissionService.listByTree()));
//    System.out.println("--------------------------------------------------------");
    return ResultGenerator.getSuccessResult(permissionService.listByTree());
  }

  @GetMapping("/parent")
  public Result getParent(){
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("parent_id",0);
    List<Permission> list =  permissionService.list(wrapper);
    List<Integer> ids = new ArrayList<>();
    for(Permission i : list){
      ids.add(i.getId());
    }
    return ResultGenerator.getSuccessResult(ids);
  }

  @GetMapping("/all")
  public Result getAll() {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("active", 1);
    JSONObject jsonObject = new JSONObject();
    List<Permission> list = permissionService.list(wrapper);
    for (Permission i : list) {
      jsonObject.put(i.getId().toString(), Map.of("name",i.getName()));
    }
    return ResultGenerator.getSuccessResult(jsonObject);
  }

  @GetMapping("/{id}")
  public Result getPermission(@PathVariable int id) {
    Permission permission = permissionService.getById(id);
    if (permission == null)
      return ResultGenerator.getFailResult("", "无该角色记录");
    return ResultGenerator.getSuccessResult(permission);
  }

  @GetMapping("/check")
  public Result checkUserName(@RequestParam String name) {
    QueryWrapper<Permission> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    if (permissionService.getOne(wrapper) != null)
      return ResultGenerator.getFailResult("", "该权限名已存在");
    return ResultGenerator.getSuccessResult();
  }

  /**
   * 根据用户的角色类型获取菜单(登录后前端马上调这个)
   */
  @GetMapping("/userPermissionList")
  @UserLoginToken
  public Result userPermissionList(HttpServletRequest request){
    String token = request.getHeader("Authorization");//从http请求头中取出token
    log.debug("/userPermissionList:  "+token);
    User user = JwtUtil.getUserById(token);
    //测试输出
//    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//    System.out.println(ResultGenerator.getSuccessResult(permissionService.userPermissionList(user.getUserType())).getData());
//    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    return ResultGenerator.getSuccessResult(permissionService.userPermissionList(user.getUserType()));
  }

  @PostMapping()
  @UserLoginToken
  public Result save(@RequestBody Permission permission) {
    // System.out.println(permission);
    if (permissionService.save(permission))
      return ResultGenerator.getSuccessResult("", "添加成功");
    return ResultGenerator.getFailResult("", "添加失败");
  }

  @PutMapping("/{id}")
  @UserLoginToken
  public Result update(@RequestBody Permission permission, @PathVariable int id) {
    permission.setId(id);
    // System.out.println(permission);
    if (permissionService.updateById(permission))
      return ResultGenerator.getSuccessResult("", "更新成功");
    return ResultGenerator.getFailResult("", "更新失败");
  }

  @PutMapping("/{id}/state/{active}")
  @UserLoginToken
  public Result changeActive(@PathVariable int id, @PathVariable int active) {
    Permission permission = new Permission();
    permission.setActive(active);
    permission.setId(id);
    if (permissionService.updateById(permission))
      return ResultGenerator.getSuccessResult("", "激活状态修改成功");
    return ResultGenerator.getFailResult("", "激活状态修改失败");
  }

  @DeleteMapping("/{id}")
  @UserLoginToken
  public Result del(@PathVariable int id) {
    if (permissionService.removeById(id))
      return ResultGenerator.getSuccessResult("", "删除成功");
    return ResultGenerator.getFailResult("", "删除失败");
  }

  @DeleteMapping("/batchdel")
  @UserLoginToken
  public Result batchDel(@RequestParam String ids) {
    String[] idList = ids.split(",");
    List<Integer> list = new ArrayList<>(idList.length);
    for (String id : idList) {
      list.add(Integer.parseInt(id));
    }
    if (permissionService.removeByIds(list))
      return ResultGenerator.getSuccessResult("", "删除成功");
    return ResultGenerator.getFailResult("", "删除失败");
  }
}
