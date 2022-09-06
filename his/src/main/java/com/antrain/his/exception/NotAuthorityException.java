package com.antrain.his.exception;

//无权限异常处理
public class NotAuthorityException  extends MyException{
  private static final long serialVersionUID = 6445638039842655171L;
  public NotAuthorityException(int code, String message){
    super(code, message);
  }
  public NotAuthorityException(){
    super("没有该权限操作");
  }
}