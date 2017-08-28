package com.hzease.tomeet.data;

import java.util.List;

public class UserInfoBean {
    @Override
    public String toString() {
        return "UserInfoBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

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
        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", amount=" + amount +
                    ", realName='" + realName + '\'' +
                    ", isVip=" + isVip +
                    ", badge=" + badge +
                    ", lockAmount=" + lockAmount +
                    ", phone='" + phone + '\'' +
                    ", authorized=" + authorized +
                    ", gender=" + gender +
                    ", isInit=" + isInit +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", birthday=" + birthday +
                    ", labels=" + labels +
                    '}';
        }

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
        private String birthday;
        private List<String> labels;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        private String account;

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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String  birthday) {
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
