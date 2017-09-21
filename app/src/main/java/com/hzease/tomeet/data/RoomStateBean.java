package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/9/21.
 */

public class RoomStateBean {

    /**
     * success : true
     * msg :
     * data : {"evaluated":true,"state":4}
     */

    private boolean success;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * evaluated : true
         * state : 4
         */

        private boolean evaluated;
        private int state;

        public boolean isEvaluated() {
            return evaluated;
        }

        public void setEvaluated(boolean evaluated) {
            this.evaluated = evaluated;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
