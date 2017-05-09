package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by Key on 2017/5/8 20:02
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomBean {
    @Override
    public String toString() {
        return "GameChatRoomBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : {"id":1000000000036,"name":"呵呵😊","place":"西城博司","manager":{"id":10000000001,"nickname":"😏😏😏","avatarSignature":null,"labels":null},"open":true,"belongCircle":null,"beginTime":"2017-05-08 20:50","endTime":"2017-05-08 21:50","createTime":"2017-05-08 20:04","state":0,"locked":true,"game":{"id":7,"name":"三国杀"},"money":100,"joinMemberCount":1,"joinManMemberCount":1,"joinWomanMemberCount":0,"phone":null,"memberCount":2,"manCount":0,"womanCount":0,"description":"哈哈😄","longitude":120.075389,"latitude":30.316934,"prepareTime":null,"joinMembers":[{"id":10000000001,"nickname":"😏😏😏","ready":true,"avatarSignature":"1493193505823","point":0}],"city":"杭州市"}
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
        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", place='" + place + '\'' +
                    ", manager=" + manager +
                    ", open=" + open +
                    ", belongCircle=" + belongCircle +
                    ", beginTime='" + beginTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", state=" + state +
                    ", locked=" + locked +
                    ", game=" + game +
                    ", money=" + money +
                    ", joinMemberCount=" + joinMemberCount +
                    ", joinManMemberCount=" + joinManMemberCount +
                    ", joinWomanMemberCount=" + joinWomanMemberCount +
                    ", phone=" + phone +
                    ", memberCount=" + memberCount +
                    ", manCount=" + manCount +
                    ", womanCount=" + womanCount +
                    ", description='" + description + '\'' +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", prepareTime=" + prepareTime +
                    ", city='" + city + '\'' +
                    ", joinMembers=" + joinMembers.toString() +
                    '}';
        }

        /**
         * id : 1000000000036
         * name : 呵呵😊
         * place : 西城博司
         * manager : {"id":10000000001,"nickname":"😏😏😏","avatarSignature":null,"labels":null}
         * open : true
         * belongCircle : null
         * beginTime : 2017-05-08 20:50
         * endTime : 2017-05-08 21:50
         * createTime : 2017-05-08 20:04
         * state : 0
         * locked : true
         * game : {"id":7,"name":"三国杀"}
         * money : 100
         * joinMemberCount : 1
         * joinManMemberCount : 1
         * joinWomanMemberCount : 0
         * phone : null
         * memberCount : 2
         * manCount : 0
         * womanCount : 0
         * description : 哈哈😄
         * longitude : 120.075389
         * latitude : 30.316934
         * prepareTime : null
         * joinMembers : [{"id":10000000001,"nickname":"😏😏😏","ready":true,"avatarSignature":"1493193505823","point":0}]
         * city : 杭州市
         */

        private long id;
        private String name;
        private String place;
        private ManagerBean manager;
        private boolean open;
        private Object belongCircle;
        private String beginTime;
        private String endTime;
        private String createTime;
        private int state;
        private boolean locked;
        private GameBean game;
        private int money;
        private int joinMemberCount;
        private int joinManMemberCount;
        private int joinWomanMemberCount;
        private Object phone;
        private int memberCount;
        private int manCount;
        private int womanCount;
        private String description;
        private double longitude;
        private double latitude;
        private Object prepareTime;
        private String city;
        private List<JoinMembersBean> joinMembers;

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

        public Object getBelongCircle() {
            return belongCircle;
        }

        public void setBelongCircle(Object belongCircle) {
            this.belongCircle = belongCircle;
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

        public int getJoinMemberCount() {
            return joinMemberCount;
        }

        public void setJoinMemberCount(int joinMemberCount) {
            this.joinMemberCount = joinMemberCount;
        }

        public int getJoinManMemberCount() {
            return joinManMemberCount;
        }

        public void setJoinManMemberCount(int joinManMemberCount) {
            this.joinManMemberCount = joinManMemberCount;
        }

        public int getJoinWomanMemberCount() {
            return joinWomanMemberCount;
        }

        public void setJoinWomanMemberCount(int joinWomanMemberCount) {
            this.joinWomanMemberCount = joinWomanMemberCount;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
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

        public List<JoinMembersBean> getJoinMembers() {
            return joinMembers;
        }

        public void setJoinMembers(List<JoinMembersBean> joinMembers) {
            this.joinMembers = joinMembers;
        }

        public static class ManagerBean {
            /**
             * id : 10000000001
             * nickname : 😏😏😏
             * avatarSignature : null
             * labels : null
             */

            private long id;
            private String nickname;
            private Object avatarSignature;
            private Object labels;

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

            public Object getLabels() {
                return labels;
            }

            public void setLabels(Object labels) {
                this.labels = labels;
            }
        }

        public static class GameBean {
            /**
             * id : 7
             * name : 三国杀
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

        public static class JoinMembersBean {
            @Override
            public String toString() {
                return "JoinMembersBean{" +
                        "id=" + id +
                        ", nickname='" + nickname + '\'' +
                        ", ready=" + ready +
                        ", avatarSignature='" + avatarSignature + '\'' +
                        ", point=" + point +
                        '}';
            }

            /**
             * id : 10000000001
             * nickname : 😏😏😏
             * ready : true
             * avatarSignature : 1493193505823
             * point : 0
             */

            private long id;
            private String nickname;
            private boolean ready;
            private String avatarSignature;
            private int point;

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

            public boolean isReady() {
                return ready;
            }

            public void setReady(boolean ready) {
                this.ready = ready;
            }

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }
        }
    }
}
