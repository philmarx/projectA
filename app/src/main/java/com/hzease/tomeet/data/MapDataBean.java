package com.hzease.tomeet.data;

import java.util.Map;

/**
 * Created by Key on 2017/4/26 13:44
 * email: MrKey.K@gmail.com
 * description: 适用于任何map data返回值的接口
 */

public class MapDataBean<T> {

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
    private Map<T, T> data;

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

    public Map<T, T> getData() {
        return data;
    }

    public void setData(Map<T, T> data) {
        this.data = data;
    }
}
