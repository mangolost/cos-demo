package com.mangolost.cos.common;

/**
 *
 */
public class CommonResult {

    /**
     * 状态码
     */
    private int code = ApiStatusCode.SUCCESS; //200

    /**
     * 描述
     */
    private String message = "OK";

    /**
     * 响应数据
     */
    private Object data = null;

    public CommonResult() {

    }

    public CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult setCodeAndMessage(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public CommonResult setData(Object data) {
        this.data = data;
        return this;
    }

}
