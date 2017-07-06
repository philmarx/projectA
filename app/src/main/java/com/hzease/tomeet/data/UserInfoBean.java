package com.hzease.tomeet.data;

import java.util.List;

public class UserInfoBean {


    /**
     * success : true
     * msg : 登录成功
     * data : {"id":10000000003,"nickname":"徐强","amount":7064,"realName":"徐强","isVip":true,"badge":69,"lockAmount":3900,"phone":"18758183175","authorized":true,"gender":true,"isInit":true,"labels":["国服第一假女巫","颜值爆表","漂亮的小姐姐","漂亮的小哥哥","超级大帅哥","宇宙无敌美男子","贼JB帅","无敌","超准时"],"avatarSignature":"1498566498795","birthday":null}
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
         * amount : 7064
         * realName : 徐强
         * isVip : true
         * badge : 69
         * lockAmount : 3900
         * phone : 18758183175
         * authorized : true
         * gender : true
         * isInit : true
         * labels : ["国服第一假女巫","颜值爆表","漂亮的小姐姐","漂亮的小哥哥","超级大帅哥","宇宙无敌美男子","贼JB帅","无敌","超准时"]
         * avatarSignature : 1498566498795
         * birthday : null
         */

        private long id;
        private String nickname;
        private int amount;
        private String realName;
        private boolean isVip;
        private int badge;
        private int lockAmount;
        private String phone;
        private boolean authorized;
        private boolean gender;
        private boolean isInit;
        private String avatarSignature;
        private Object birthday;
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

        public boolean isIsVip() {
            return isVip;
        }

        public void setIsVip(boolean isVip) {
            this.isVip = isVip;
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

        public Object getBirthday() {
            return birthday;
        }

        public void setBirthday(Object birthday) {
            this.birthday = birthday;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
    }
}
