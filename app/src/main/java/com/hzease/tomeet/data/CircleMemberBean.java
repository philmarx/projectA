package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/10.
 */

public class CircleMemberBean {
    /**
     * success : true
     * msg :
     * data : [{"avatarSignature":"1494220981231","createCount":0,"joinCount":0,"nickname":"马健原","id":10000000025,"experience":0},{"avatarSignature":"1494290577016","createCount":0,"joinCount":0,"nickname":"张国文","id":10000000023,"experience":0}]
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
         * avatarSignature : 1494220981231
         * createCount : 0
         * joinCount : 0
         * nickname : 马健原
         * id : 10000000025
         * experience : 0
         */

        private String avatarSignature;
        private int createCount;
        private int joinCount;
        private String nickname;
        private long id;
        private int experience;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public int getCreateCount() {
            return createCount;
        }

        public void setCreateCount(int createCount) {
            this.createCount = createCount;
        }

        public int getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(int joinCount) {
            this.joinCount = joinCount;
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

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }
    }
}
