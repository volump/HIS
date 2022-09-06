package com.antrain.his.controller;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.antrain.his.annotation.UserLoginToken;
import com.antrain.his.entity.Department;
import com.antrain.his.entity.User;
import com.antrain.his.mapper.UserMapper;
import com.antrain.his.service.IDepartmentService;
import com.antrain.his.service.IUserService;
import com.antrain.his.utils.InitUtil;
import com.antrain.his.utils.Result;
import com.antrain.his.utils.ResultGenerator;
import com.antrain.his.utils.ShaUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 用户 前端控制器
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * 1、GET：
     * GET请求会向数据库发送数据的请求，从而来获取信息，该请求就像数据库的select操作一样，只是用来查询数据，不会修改，增加数据，不会影响资源内容，即该请求不会产生副作用。无论进行多少次操作，结果都是一样的。
     * 2、PUT：
     * PUT与GET不同的是，PUT请求是想服务器端发送数据的，从而改变信息，该请求就像数据库的update操作一样，用来修改数据的内容，但是不会增加数据的种类等，也就是说无论进行多少次PUT操作，其结果并没有不同。
     * 3、POST：
     * POST请求同PUT请求类似，都是向服务器端发送数据的，但是该请求会改变数据的种类等资源，就像数据库的insert操作一样，会创建新的内容。几乎模前所有的提交操作都是用POST请求的。
     * 4、DELETE：
     * DELETE求情顾名思义，就是用来删除某一行的，该请求就像数据库的delete操作一样。
     */
    @Resource
    private IUserService userService;
    @Resource
    private IDepartmentService departmentService;

    @GetMapping("/Num")
    public List<Map<String, Object>> getNum(){
        //需求： 以部门id进行分组查询，查每个部门员工个数， 将大于3人的部门过滤出来
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("dept_id,count(id) count");
        wrapper.groupBy("dept_id");
        //wrapper.having("count>3");//GROUP BY dept_id HAVING count>3
        wrapper.having("count>{0}",0);//GROUP BY dept_id HAVING count>?
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> maps = userService.listMaps(wrapper);
        Map<String, Object> res = new HashMap<>();
        for(Integer i = 0; i < maps.size(); i++){
            Map<String, Object> item = maps.get(i);
            String value = item.get("dept_id").toString();
            Integer d_id = Integer.parseInt(value);
            if(d_id == 0){
                System.out.println("管理员");
                String nameManage = "管理";
                res.put(i.toString(), nameManage);
                continue;
            }
            Department department = departmentService.getById(d_id);
            res.put(i.toString(), department.getName());
            System.out.println(department.getName());
        }
        Result result = new Result();
        result.setData(maps);
        return maps;
    }
    @GetMapping("/Num2")
    public Result getNum2(){
        //需求： 以部门id进行分组查询，查每个部门员工个数， 将大于3人的部门过滤出来
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("dept_id,count(id) count");
        wrapper.groupBy("dept_id");
        //wrapper.having("count>3");//GROUP BY dept_id HAVING count>3
        wrapper.having("count>{0}",0);//GROUP BY dept_id HAVING count>?
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> maps = userService.listMaps(wrapper);
        Map<String, Object> res = new HashMap<>();
        for(Integer i = 0; i < maps.size(); i++){
            Map<String, Object> item = maps.get(i);
            String value = item.get("dept_id").toString();
            Integer d_id = Integer.parseInt(value);
            if(d_id == 0){
                System.out.println("管理员");
                String nameManage = "管理";
                res.put(i.toString(), nameManage);
                continue;
            }
            Department department = departmentService.getById(d_id);
            res.put(i.toString(), department.getName());
            System.out.println(department.getName());
        }
        maps.add(res);
        return ResultGenerator.getSuccessResult(maps);
    }
    @GetMapping("/Num3")
    public List<Map<String, Object>> getNum3(){
        //需求： 以部门id进行分组查询，查每个部门员工个数， 将大于3人的部门过滤出来
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("dept_id,count(id) count");
        wrapper.groupBy("dept_id");
        //wrapper.having("count>3");//GROUP BY dept_id HAVING count>3
        wrapper.having("count>{0}",0);//GROUP BY dept_id HAVING count>?
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> maps = userService.listMaps(wrapper);
        List<Map<String, Object>> result = userService.listMaps(wrapper);
        result.clear();
        for(Integer i = 0; i < maps.size(); i++){
            Map<String, Object> item = maps.get(i);
            String value = item.get("dept_id").toString();
            Integer d_id = Integer.parseInt(value);
            Map<String, Object> res = new HashMap<>();
            if(d_id == 0){
                System.out.println("管理员");
                String nameManage = "管理";
                res.put(i.toString(), nameManage);
                result.add(res);
                continue;
            }
            Department department = departmentService.getById(d_id);
            res.put(i.toString(), department.getName());
            result.add(res);
            System.out.println(department.getName());
        }
        return result;
    }

    @GetMapping
    public Result getList(@RequestParam Map<String, Object> param) {
        InitUtil.initPage(param);//初始化分页
        int num = Integer.parseInt(param.get("page").toString());//第几页
        int limit = Integer.parseInt(param.get("limit").toString());//该页记录数
        QueryWrapper<User> wrapper = new QueryWrapper<User>();//查询条件
        Object name = param.get("name");//获取传入对象的姓名
        if (!StringUtils.isEmpty(name)){
            //根据姓名模糊查询用户
            wrapper.like("username", name).or().like("realname", name);
        }
//        Object id = param.get("id");//获取传入对象的id
//        if (!StringUtils.isEmpty(id)){
//            //根据id查询用户
//            wrapper.eq("id", id);
//        }
        InitUtil.initEq(param, wrapper, "active");//未激活用户是搜索不到的
        IPage<User> page = new Page<>(num, limit);//参数一是当前页，参数二是每页个数
        return ResultGenerator.getSuccessResult(userService.page(page, wrapper));//返回查询成功信息
    }

    @GetMapping("/{id}")
    public Result getUser(@PathVariable int id) {
        User user = userService.getById(id);
        if (user == null) return ResultGenerator.getFailResult("","无用户记录");
        return ResultGenerator.getSuccessResult(user);
    }

    /**
     * 检查用户名
     */
    @GetMapping("/check")
    public Result checkUserName(@RequestParam String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        if (userService.getOne(wrapper) != null)
            return ResultGenerator.getFailResult("", "该用户名已存在");
        return ResultGenerator.getSuccessResult();
    }

    @GetMapping("/list")
    public Result getListBy(@RequestParam Map<String, Object> param) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        InitUtil.initEq(param, wrapper, "user_type","dept_id","active");
        JSONObject jsonObject = new JSONObject();
        List<User> list = userService.list(wrapper);
        for (User i : list) {
            jsonObject.put(i.getId().toString(),Map.of("name", i.getRealname()));
        }
        return ResultGenerator.getSuccessResult(jsonObject);
    }

    /**
     * 前端通过/users 获取后端的post接口
     */
    @PostMapping()
    @UserLoginToken
    public Result save(@RequestBody User user){
        //System.out.println(user);
        user.setPassword(ShaUtil.getSHA256(user.getPassword()));//注册时密码加密
        //user.setPassword(user.getPassword());
        if(userService.save(user)) return ResultGenerator.getSuccessResult("","添加成功");
        return ResultGenerator.getFailResult("","添加失败");
    }

    @PutMapping("/{id}")
    @UserLoginToken
    public Result update(@RequestBody User user,@PathVariable int id){
        user.setId(id);
        //System.out.println(user);
        if(userService.updateById(user)) return ResultGenerator.getSuccessResult("","更新成功");
        return ResultGenerator.getFailResult("","更新失败");
    }

    /**
     * 修改用户的状态，相当于修改用户的激活状态
     * 传入id，active(0或1)
     */
    @PutMapping("/{id}/state/{active}")
    @UserLoginToken
    public Result changeActive(@PathVariable int id, @PathVariable int active) {
      User user = new User();
      user.setActive(active);
      user.setId(id);
      if (userService.updateById(user))
        return ResultGenerator.getSuccessResult("", "激活状态修改成功");
      return ResultGenerator.getFailResult("", "激活状态修改失败");
    }

    @DeleteMapping("/{id}")
    @UserLoginToken
    public Result del(@PathVariable int id) {
        if(userService.removeById(id)) return ResultGenerator.getSuccessResult("","删除成功");
        return ResultGenerator.getFailResult("","删除失败");
    }

    @DeleteMapping("/batchdel")
    @UserLoginToken
    public Result batchDel(@RequestParam String ids) {
        String[] idList = ids.split(",");//前端传过来的批量删除id必须是以","分隔的字符串
        List<Integer> list = new ArrayList<>(idList.length);
        for (String id : idList) {
            list.add(Integer.parseInt(id));
        }
        if (userService.removeByIds(list))
            return ResultGenerator.getSuccessResult("", "删除成功");
        return ResultGenerator.getFailResult("", "删除失败");
    }
}
