package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/10.
 */

public class HomeRoomsBean {
    /**
     * success : true
     * msg : null
     * data : [{"id":43,"name":"张国文一篇","place":"西城博司展示中心","creator":{"id":10000000009,"nickname":null,"amount":0,"lockAmount":0,"phone":null,"email":null,"gender":null,"place":null,"age":0,"isInit":false,"labels":null},"beginTime":"2017-04-01 13:20","endTime":"2017-04-01 19:55","createTime":"2017-04-01 11:55","state":0,"locked":true,"game":{"id":6,"name":null,"children":null},"money":200,"joinMember":1,"joinManMember":1,"joinWomanMember":0,"phone":null,"memberCount":3,"manCount":2,"womanCount":2,"description":"西城都是","longitude":120.076471,"latitude":30.317019,"joinMemberIds":[10000000000,10000000001,10000000007,10000000008,10000000009,10000000010,10000000011,10000000012,10000000020]},{"id":50,"name":"张国文4月7号","place":"西城博司展示中心","creator":{"id":10000000009,"nickname":null,"amount":0,"lockAmount":0,"phone":null,"email":null,"gender":null,"place":null,"age":0,"isInit":false,"labels":null},"beginTime":"2017-04-07 14:34","endTime":"2017-04-07 16:34","createTime":"2017-04-07 14:34","state":0,"locked":true,"game":{"id":6,"name":null,"children":null},"money":30000,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":10,"manCount":0,"womanCount":0,"description":"一个活动","longitude":120.076245,"latitude":30.317072,"joinMemberIds":[]},{"id":51,"name":"张国文4月7号第二","place":"西城博司展示中心","creator":{"id":10000000009,"nickname":null,"amount":0,"lockAmount":0,"phone":null,"email":null,"gender":null,"place":null,"age":0,"isInit":false,"labels":null},"beginTime":"2017-04-07 14:37","endTime":"2017-04-07 18:37","createTime":"2017-04-07 14:37","state":0,"locked":false,"game":{"id":6,"name":null,"children":null},"money":0,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":4,"manCount":2,"womanCount":2,"description":"西城","longitude":120.076217,"latitude":30.317068,"joinMemberIds":[10000000000,10000000001,10000000007,10000000008,10000000009,10000000010,10000000011,10000000012,10000000020]}]
     */

    private boolean success;
    private Object msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * id : 43
         * name : 张国文一篇
         * place : 西城博司展示中心
         * creator : {"id":10000000009,"nickname":null,"amount":0,"lockAmount":0,"phone":null,"email":null,"gender":null,"place":null,"age":0,"isInit":false,"labels":null}
         * beginTime : 2017-04-01 13:20
         * endTime : 2017-04-01 19:55
         * createTime : 2017-04-01 11:55
         * state : 0
         * locked : true
         * game : {"id":6,"name":null,"children":null}
         * money : 200
         * joinMember : 1
         * joinManMember : 1
         * joinWomanMember : 0
         * phone : null
         * memberCount : 3
         * manCount : 2
         * womanCount : 2
         * description : 西城都是
         * longitude : 120.076471
         * latitude : 30.317019
         * joinMemberIds : [10000000000,10000000001,10000000007,10000000008,10000000009,10000000010,10000000011,10000000012,10000000020]
         */

        private int id;
        private String name;
        private String place;
        private CreatorBean creator;
        private String beginTime;
        private String endTime;
        private String createTime;
        private int state;
        private boolean locked;
        private GameBean game;
        private int money;
        private int joinMember;
        private int joinManMember;
        private int joinWomanMember;
        private String phone;
        private int memberCount;
        private int manCount;
        private int womanCount;
        private String description;
        private double longitude;
        private double latitude;
        private List<Long> joinMemberIds;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getJoinMember() {
            return joinMember;
        }

        public void setJoinMember(int joinMember) {
            this.joinMember = joinMember;
        }

        public int getJoinManMember() {
            return joinManMember;
        }

        public void setJoinManMember(int joinManMember) {
            this.joinManMember = joinManMember;
        }

        public int getJoinWomanMember() {
            return joinWomanMember;
        }

        public void setJoinWomanMember(int joinWomanMember) {
            this.joinWomanMember = joinWomanMember;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getManCount() {
            return manCount;
        }

        public void setManCount(int manCount) {
            this.manCount = manCount;
        }

        public int getWomanCount() {
            return womanCount;
        }

        public void setWomanCount(int womanCount) {
            this.womanCount = womanCount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public List<Long> getJoinMemberIds() {
            return joinMemberIds;
        }

        public void setJoinMemberIds(List<Long> joinMemberIds) {
            this.joinMemberIds = joinMemberIds;
        }

        public static class CreatorBean {
            /**
             * id : 10000000009
             * nickname : null
             * amount : 0
             * lockAmount : 0
             * phone : null
             * email : null
             * gender : null
             * place : null
             * age : 0
             * isInit : false
             * labels : null
             */

            private long id;
            private String nickname;
            private int amount;
            private int lockAmount;
            private String phone;
            private String email;
            private Boolean gender;
            private String place;
            private int age;
            private boolean isInit;
            private List<String> labels;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public Object getNickname() {
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

            public int getLockAmount() {
                return lockAmount;
            }

            public void setLockAmount(int lockAmount) {
                this.lockAmount = lockAmount;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Object getGender() {
                return gender;
            }

            public void setGender(Boolean gender) {
                this.gender = gender;
            }

            public Object getPlace() {
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

            public Object getLabels() {
                return labels;
            }

            public void setLabels(List<String> labels) {
                this.labels = labels;
            }
        }

        public static class GameBean {
            /**
             * id : 6
             * name : null
             * children : null
             */

            private int id;
            private Object name;
            private Object children;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getChildren() {
                return children;
            }

            public void setChildren(Object children) {
                this.children = children;
            }
        }
    }
}
