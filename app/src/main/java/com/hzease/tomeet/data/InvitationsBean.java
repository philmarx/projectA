package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/20.
 */

public class InvitationsBean {
    /**
     * success : true
     * msg :
     * data : [{"id":4,"nickname":"徐强","createTime":1497945418000,"gender":true,"avatarSignature":"1495175242030","state":0,"userId":10000000003}]
     */

    private boolean success;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4
         * nickname : 徐强
         * createTime : 1497945418000
         * gender : true
         * avatarSignature : 1495175242030
         * state : 0
         * userId : 10000000003
         */

        private int id;
        private String nickname;
        private long createTime;
        private boolean gender;
        private String avatarSignature;
        private int state;
        private long userId;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        private String origin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }
}
