package com.antrain.his.service.impl;

import com.antrain.his.entity.Department;
import com.antrain.his.mapper.DepartmentMapper;
import com.antrain.his.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 科室 服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
