package com.antrain.his.service.impl;

public class Test {
    public static void main(String[] args){
        PermissionServiceImpl permissionServiceImpl = new PermissionServiceImpl();
        Object list = permissionServiceImpl.userPermissionList(1);
        System.out.println(list.toString());
    }

}
