package com.hzease.tomeet.data;

import java.util.List;

public class UserInfoBean {
    /**
     * success : true
     * msg : 登录成功
     * data : {"id":10000000003,"nickname":"徐强","amount":10000,"realName":null,"badge":0,"lockAmount":0,"phone":"18758183175","authorized":false,"gender":true,"isInit":true,"labels":[],"avatarSignature":"1495691275776"}
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
         * id : 10000000003
         * nickname : 徐强
         * amount : 10000
         * realName : null
         * badge : 0
         * lockAmount : 0
         * phone : 18758183175
         * authorized : false
         * gender : true
         * isInit : true
         * labels : []
         * avatarSignature : 1495691275776
         */

        private long id;
        private String nickname;
        private int amount;
        private String realName;
        private int badge;
        private int lockAmount;
        private String phone;
        private boolean authorized;
        private boolean gender;
        private boolean isInit;
        private String avatarSignature;
        private List<String> labels;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public int getLockAmount() {
            return lockAmount;
        }

        public void setLockAmount(int lockAmount) {
            this.lockAmount = lockAmount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isAuthorized() {
            return authorized;
        }

        public void setAuthorized(boolean authorized) {
            this.authorized = authorized;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public boolean isIsInit() {
            return isInit;
        }

        public void setIsInit(boolean isInit) {
            this.isInit = isInit;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
    }
}
