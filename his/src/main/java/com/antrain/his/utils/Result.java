package com.antrain.his.utils;

import java.io.Serializable;

import lombok.Data;

//结果信息对象(做登录、查询各种返回信息用)
@Data
public class Result implements Serializable{
    private static final long serialVersionUID = -6691656851198107463L;
    private int status;
    private String message;
    private Object data;
}