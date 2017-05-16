package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/5/16 15:22
 * email: MrKey.K@gmail.com
 * description:
 */

public class SimpleUserInfoBean {

    /**
     * success : true
     * msg :
     * data : {"avatarSignature":"1493193505823","nickname":"😏😏😏","id":10000000001}
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
         * avatarSignature : 1493193505823
         * nickname : 😏😏😏
         * id : 10000000001
         */

        private String avatarSignature;
        private String nickname;
        private long id;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
