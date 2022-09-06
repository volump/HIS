package com.antrain.his.exception;

//查询信息不存在异常
public class NotExistException extends MyException{
  private static final long serialVersionUID = 6445638039852655171L;
  public NotExistException(int code, String message){
    super(code, message);
  }
  public NotExistException(){
    super("查询信息不存在");
  }
}
