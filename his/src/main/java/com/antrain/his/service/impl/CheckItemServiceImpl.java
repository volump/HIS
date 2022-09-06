package com.antrain.his.service.impl;

import com.antrain.his.entity.CheckItem;
import com.antrain.his.mapper.CheckItemMapper;
import com.antrain.his.service.ICheckItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 检查项目 服务实现类
 */
@Service
public class CheckItemServiceImpl extends ServiceImpl<CheckItemMapper, CheckItem> implements ICheckItemService {

}
