package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/24.
 */

public class SearchCircleBean {
    /**
     * success : true
     * msg :
     * data : [{"id":1,"name":"狼人杀1圈","city":"杭州","place":"西城波斯","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076152,"latitude":30.317012},{"id":2,"name":"狼人杀2圈","city":"杭州市","place":"西城博司展示中心","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076085,"latitude":30.31708},{"id":3,"name":"狼人杀3圈","city":"杭州市","place":"西城博司展示中心","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076085,"latitude":30.31708},{"id":4,"name":"狼人杀4圈","city":"杭州市","place":"西城博司展示中心","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076085,"latitude":30.31708},{"id":5,"name":"狼人杀5圈","city":"杭州市","place":"西城博司展示中心","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076085,"latitude":30.31708},{"id":6,"name":"狼人杀4圈","city":"杭州市","place":"西城博司展示中心","notice":"","creator":{"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]},"longitude":120.076085,"latitude":30.31708}]
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
         * id : 1
         * name : 狼人杀1圈
         * city : 杭州
         * place : 西城波斯
         * notice :
         * creator : {"id":10000000023,"nickname":"张国文","amount":50000,"badge":4,"lockAmount":0,"phone":"15516517017","email":null,"gender":true,"place":"","age":null,"isInit":true,"labels":["a","b"]}
         * longitude : 120.076152
         * latitude : 30.317012
         */

        private long id;
        private String name;
        private String city;
        private String place;
        private String notice;
        private CreatorBean creator;
        private double longitude;
        private double latitude;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    ", place='" + place + '\'' +
                    ", notice='" + notice + '\'' +
                    ", creator=" + creator +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    '}';
        }

        public static class CreatorBean {
            /**
             * id : 10000000023
             * nickname : 张国文
             * amount : 50000
             * badge : 4
             * lockAmount : 0
             * phone : 15516517017
             * email : null
             * gender : true
             * place :
             * age : null
             * isInit : true
             * labels : ["a","b"]
             */

            private long id;
            private String nickname;
            private int amount;
            private int badge;
            private int lockAmount;
            private String phone;
            private Object email;
            private boolean gender;
            private String place;
            private Object age;
            private boolean isInit;
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

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public Object getAge() {
                return age;
            }

            public void setAge(Object age) {
                this.age = age;
            }

            public boolean isIsInit() {
                return isInit;
            }

            public void setIsInit(boolean isInit) {
                this.isInit = isInit;
            }

            public List<String> getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }
        }
    }
}
