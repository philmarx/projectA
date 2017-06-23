package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/23.
 */

public class HavaBZmoneyRoomBean {
    /**
     * success : true
     * msg :
     * data : [{"id":1000000000050,"name":"这个活动有保证金","place":"汉承装饰","manager":{"id":10000000003,"nickname":"徐强","avatarSignature":null,"labels":[],"point":0},"open":true,"beginTime":"2017-06-23 10:00","endTime":"2017-06-23 15:00","createTime":"2017-06-23 09:16","state":0,"locked":false,"game":{"id":18,"name":"密室"},"money":100,"joinMember":1,"joinManMember":1,"joinWomanMember":0,"memberCount":2,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.075989,"latitude":30.316461,"prepareTime":null,"joinMembers":[],"city":"杭州市"}]
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
         * id : 1000000000050
         * name : 这个活动有保证金
         * place : 汉承装饰
         * manager : {"id":10000000003,"nickname":"徐强","avatarSignature":null,"labels":[],"point":0}
         * open : true
         * beginTime : 2017-06-23 10:00
         * endTime : 2017-06-23 15:00
         * createTime : 2017-06-23 09:16
         * state : 0
         * locked : false
         * game : {"id":18,"name":"密室"}
         * money : 100
         * joinMember : 1
         * joinManMember : 1
         * joinWomanMember : 0
         * memberCount : 2
         * manCount : 0
         * womanCount : 0
         * description : 暂无介绍
         * longitude : 120.075989
         * latitude : 30.316461
         * prepareTime : null
         * joinMembers : []
         * city : 杭州市
         */

        private long id;
        private String name;
        private String place;
        private ManagerBean manager;
        private boolean open;
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
        private int memberCount;
        private int manCount;
        private int womanCount;
        private String description;
        private double longitude;
        private double latitude;
        private Object prepareTime;
        private String city;
        private List<?> joinMembers;

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

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public ManagerBean getManager() {
            return manager;
        }

        public void setManager(ManagerBean manager) {
            this.manager = manager;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
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

        public Object getPrepareTime() {
            return prepareTime;
        }

        public void setPrepareTime(Object prepareTime) {
            this.prepareTime = prepareTime;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<?> getJoinMembers() {
            return joinMembers;
        }

        public void setJoinMembers(List<?> joinMembers) {
            this.joinMembers = joinMembers;
        }

        public static class ManagerBean {
            /**
             * id : 10000000003
             * nickname : 徐强
             * avatarSignature : null
             * labels : []
             * point : 0
             */

            private long id;
            private String nickname;
            private Object avatarSignature;
            private int point;
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

            public Object getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(Object avatarSignature) {
                this.avatarSignature = avatarSignature;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public List<?> getLabels() {
                return labels;
            }

            public void setLabels(List<?> labels) {
                this.labels = labels;
            }
        }

        public static class GameBean {
            /**
             * id : 18
             * name : 密室
             */

            private int id;
            private String name;

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
        }
    }
}
