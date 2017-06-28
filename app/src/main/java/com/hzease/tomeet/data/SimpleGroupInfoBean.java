package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/6/28 11:29
 * email: MrKey.K@gmail.com
 * description:
 */

public class SimpleGroupInfoBean {

    /**
     * success : true
     * msg :
     * data : {"avatarSignature":"1498457832312","name":"张国文的圈子一","id":100000000000}
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
         * avatarSignature : 1498457832312
         * name : 张国文的圈子一
         * id : 100000000000
         */

        private String avatarSignature;
        private String name;
        private long id;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
