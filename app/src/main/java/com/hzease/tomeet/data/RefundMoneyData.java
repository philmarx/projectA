package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/8/16.
 */

public class RefundMoneyData {
    /**
     * success : true
     * msg :
     * data : 5099
     */

    private boolean success;
    private String msg;
    private int data;

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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
