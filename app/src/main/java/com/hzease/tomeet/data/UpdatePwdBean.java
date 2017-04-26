package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/4/24.
 */

public class UpdatePwdBean {
    /**
     * success : false
     * msg : java.lang.Exception:新密码不能为空
     * data : null
     */
    private boolean success;
    private String msg;
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
