package com.hzease.tomeet.data;

import java.util.Map;

/**
 * Created by Key on 2017/4/26 13:44
 * email: MrKey.K@gmail.com
 * description: 适用于任何map data返回值的接口
 */

public class MapDataBean {

    @Override
    public String toString() {
        return "NoDataBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    private boolean success;
    private String msg;
    private Map<Object, Object> data;

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

    public Map<Object, Object> getData() {
        return data;
    }

    public void setData(Map<Object, Object> data) {
        this.data = data;
    }
}
