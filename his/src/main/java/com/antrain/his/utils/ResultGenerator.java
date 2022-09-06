package com.antrain.his.utils;

public class ResultGenerator {

    /**
     * 
     * @return 默认成功信息 （data=null, msg = SUCCESS , status = 200)
     */
    public static Result getSuccessResult() {
        return getSuccessResult("", Constants.SUCCESS_MSG,Constants.RESULT_CODE_SUCCESS);
    }

    /**
     * 
     * @return 带数据的成功信息 （data=data, msg = SUCCESS , status = 200)
     */
    public static Result getSuccessResult(Object data) {
        //打印树形数据
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(data);
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        return getSuccessResult(data,Constants.SUCCESS_MSG,Constants.RESULT_CODE_SUCCESS);
    }

    /**
     * 
     * @return （data=data, msg = msg , status = 200)
     */
    public static Result getSuccessResult(Object data,String msg) {
        return getSuccessResult(data,msg,Constants.RESULT_CODE_SUCCESS);
    }

     /**
     * 
     * @return （data=data, msg = msg , status = 200)
     */
    public static Result getSuccessResult(Object data,String msg,int code) {
        Result result = new Result();
        result.setData(data);
        result.setStatus(code);
        result.setMessage(msg);
        return result;
    }

     /**
     * 
     * @return 默认成功信息 （data=null, msg = FAIL , status = 400)
     */
    public static Result getFailResult() {
        return getFailResult("", Constants.FAIL_MSG,Constants.RESULT_CODE_FAIL);
    }

    /**
     * 
     * @return 带数据的成功信息 （data=data, msg = FAIL , status = 400)
     */
    public static Result getFailResult(Object data) {
        return getFailResult(data,Constants.FAIL_MSG,Constants.RESULT_CODE_FAIL);
    }

    /**
     * 
     * @return （data=data, msg = msg , status = 400)
     */
    public static Result getFailResult(Object data,String msg) {
        return getFailResult(data,msg,Constants.RESULT_CODE_FAIL);
    }

     /**
     * 
     * @return （data=data, msg = msg , status = 400)
     */
    public static Result getFailResult(Object data,String msg,int code) {
        Result result = new Result();
        result.setData(data);
        result.setStatus(code);
        result.setMessage(msg);
        return result;
    }
}