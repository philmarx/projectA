package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/5.
 */

public class MoneyDetailsBean {
    /**
     * success : true
     * msg :
     * data : [{"id":29,"user":{"id":10000000003,"nickname":"徐强啊","amount":10012,"realName":"徐强","badge":0,"lockAmount":0,"phone":"18758183175","authorized":true,"gender":true,"isInit":true,"labels":[],"avatarSignature":"1495691275776"},"money":2000,"description":"取消准备，解冻保证金。","createTime":"2017-06-05 16:20"},{"id":28,"user":{"id":10000000003,"nickname":"徐强啊","amount":10012,"realName":"徐强","badge":0,"lockAmount":0,"phone":"18758183175","authorized":true,"gender":true,"isInit":true,"labels":[],"avatarSignature":"1495691275776"},"money":-2000,"description":"成为房主，冻结保证金。","createTime":"2017-06-05 16:11"}]
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
         * id : 29
         * user : {"id":10000000003,"nickname":"徐强啊","amount":10012,"realName":"徐强","badge":0,"lockAmount":0,"phone":"18758183175","authorized":true,"gender":true,"isInit":true,"labels":[],"avatarSignature":"1495691275776"}
         * money : 2000
         * description : 取消准备，解冻保证金。
         * createTime : 2017-06-05 16:20
         */

        private int id;
        private UserBean user;
        private int money;
        private String description;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public static class UserBean {
            /**
             * id : 10000000003
             * nickname : 徐强啊
             * amount : 10012
             * realName : 徐强
             * badge : 0
             * lockAmount : 0
             * phone : 18758183175
             * authorized : true
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
            private List<?> labels;

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

            public List<?> getLabels() {
                return labels;
            }

            public void setLabels(List<?> labels) {
                this.labels = labels;
            }
        }
    }
}
