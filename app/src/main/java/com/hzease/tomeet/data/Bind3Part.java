package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/9/13.
 */

public class Bind3Part {
    /**
     * success : false
     * msg : 可强制解除，请登录新版操作
     * data : {"userId2":10000002817,"nickname2":"。。！！","avatarSignature2":"1505297275236"}
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
         * userId2 : 10000002817
         * nickname2 : 。。！！
         * avatarSignature2 : 1505297275236
         */

        private long userId2;
        private String nickname2;
        private String avatarSignature2;

        public long getUserId2() {
            return userId2;
        }

        public void setUserId2(long userId2) {
            this.userId2 = userId2;
        }

        public String getNickname2() {
            return nickname2;
        }

        public void setNickname2(String nickname2) {
            this.nickname2 = nickname2;
        }

        public String getAvatarSignature2() {
            return avatarSignature2;
        }

        public void setAvatarSignature2(String avatarSignature2) {
            this.avatarSignature2 = avatarSignature2;
        }
    }
}
