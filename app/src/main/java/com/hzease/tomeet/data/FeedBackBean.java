package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/4/24.
 */

public class FeedBackBean {
    /**
     * success : false
     */
    private boolean success;
    private String msg;

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
}
