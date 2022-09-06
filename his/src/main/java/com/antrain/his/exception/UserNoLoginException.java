package com.antrain.his.exception;

import com.antrain.his.utils.Constants;

//登录异常
public class UserNoLoginException extends MyException{
  private static final long serialVersionUID = 6445638039342655171L;
  public UserNoLoginException(int code, String message){
    super(code, message);
  }
  public UserNoLoginException(){
    super(Constants.NOT_LOGIN_MSG);
  }
}