package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/5/22 17:16
 * email: MrKey.K@gmail.com
 * description:
 */

public class CreateRoomBean {

    /**
     * success : true
     * msg :
     * data : {"id":1000000000049}
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
         * id : 1000000000049
         */

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
