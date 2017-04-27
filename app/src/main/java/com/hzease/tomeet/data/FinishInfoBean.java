package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/3/28.
 */

public class FinishInfoBean {
    /**
     * success : true
     * msg : 保存成功
     * data : {"id":10000000014,"nickname":"test","amount":0,"phone":"18758183175","email":null,"gender":true,"location":null,"place":"null","age":0,"isInit":true,"labels":[]}
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
         * id : 10000000014
         * nickname : test
         * amount : 0
         * phone : 18758183175
         * email : null
         * gender : true
         * location : null
         * place : null
         * age : 0
         * isInit : true
         * labels : []
         */

        private long id;
        private String nickname;
        private int amount;
        private String phone;
        private Object email;
        private boolean gender;
        private Object location;
        private String place;
        private int age;
        private boolean isInit;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isIsInit() {
            return isInit;
        }

        public void setIsInit(boolean isInit) {
            this.isInit = isInit;
        }

        public List<?> getLabels() {
            return labels;
        }

        public void setLabels(List<?> labels) {
            this.labels = labels;
        }
    }
}
