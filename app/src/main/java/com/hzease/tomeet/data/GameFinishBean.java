package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/8.
 */

public class GameFinishBean {


    /**
     * success : true
     * msg :
     * data : {"members":[{"id":10000005358,"nickname":"QQQQ","ready":true,"avatarSignature":"1506401264223","point":0,"globalRanking":1,"badge":3,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005564,"nickname":"大叔","ready":true,"avatarSignature":"1506584227047","point":0,"globalRanking":1,"badge":2,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005565,"nickname":"ink","ready":true,"avatarSignature":"1506584610684","point":0,"globalRanking":1,"badge":2,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005601,"nickname":"yyyy","ready":true,"avatarSignature":"1506930520221","point":0,"globalRanking":1,"badge":4,"vip":false,"online":false,"signed":false,"attend":false}],"room":{"id":1000000000844,"name":"密室逃脱","place":"杭州电子科技大学招生就业处","manager":{"id":10000005358,"nickname":"QQQQ","avatarSignature":null,"labels":[],"point":0},"open":true,"beginTime":"2017-10-02 16:00","endTime":"2017-10-02 17:00","createTime":"2017-10-02 15:49","state":4,"locked":false,"game":{"id":18,"name":"密室"},"money":0,"joinMember":4,"joinManMember":3,"joinWomanMember":1,"memberCount":0,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.345339,"latitude":30.315393,"prepareTime":"2017-10-02 15:53:08","joinMembers":[],"city":"杭州市"}}
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
         * members : [{"id":10000005358,"nickname":"QQQQ","ready":true,"avatarSignature":"1506401264223","point":0,"globalRanking":1,"badge":3,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005564,"nickname":"大叔","ready":true,"avatarSignature":"1506584227047","point":0,"globalRanking":1,"badge":2,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005565,"nickname":"ink","ready":true,"avatarSignature":"1506584610684","point":0,"globalRanking":1,"badge":2,"vip":false,"online":false,"signed":false,"attend":false},{"id":10000005601,"nickname":"yyyy","ready":true,"avatarSignature":"1506930520221","point":0,"globalRanking":1,"badge":4,"vip":false,"online":false,"signed":false,"attend":false}]
         * room : {"id":1000000000844,"name":"密室逃脱","place":"杭州电子科技大学招生就业处","manager":{"id":10000005358,"nickname":"QQQQ","avatarSignature":null,"labels":[],"point":0},"open":true,"beginTime":"2017-10-02 16:00","endTime":"2017-10-02 17:00","createTime":"2017-10-02 15:49","state":4,"locked":false,"game":{"id":18,"name":"密室"},"money":0,"joinMember":4,"joinManMember":3,"joinWomanMember":1,"memberCount":0,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.345339,"latitude":30.315393,"prepareTime":"2017-10-02 15:53:08","joinMembers":[],"city":"杭州市"}
         */

        private RoomBean room;
        private List<MembersBean> members;

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public static class RoomBean {
            /**
             * id : 1000000000844
             * name : 密室逃脱
             * place : 杭州电子科技大学招生就业处
             * manager : {"id":10000005358,"nickname":"QQQQ","avatarSignature":null,"labels":[],"point":0}
             * open : true
             * beginTime : 2017-10-02 16:00
             * endTime : 2017-10-02 17:00
             * createTime : 2017-10-02 15:49
             * state : 4
             * locked : false
             * game : {"id":18,"name":"密室"}
             * money : 0
             * joinMember : 4
             * joinManMember : 3
             * joinWomanMember : 1
             * memberCount : 0
             * manCount : 0
             * womanCount : 0
             * description : 暂无介绍
             * longitude : 120.345339
             * latitude : 30.315393
             * prepareTime : 2017-10-02 15:53:08
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
            private String prepareTime;
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

            public String getPrepareTime() {
                return prepareTime;
            }

            public void setPrepareTime(String prepareTime) {
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
                 * id : 10000005358
                 * nickname : QQQQ
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

        public static class MembersBean {
            /**
             * id : 10000005358
             * nickname : QQQQ
             * ready : true
             * avatarSignature : 1506401264223
             * point : 0
             * globalRanking : 1
             * badge : 3
             * vip : false
             * online : false
             * signed : false
             * attend : false
             */

            private long id;
            private String nickname;
            private boolean ready;
            private String avatarSignature;
            private int point;
            private int globalRanking;
            private int badge;
            private boolean vip;
            private boolean online;
            private boolean signed;
            private boolean attend;
            private boolean isShowInfo;

            public boolean isShowInfo() {
                return isShowInfo;
            }

            public void setShowInfo(boolean showInfo) {
                isShowInfo = showInfo;
            }

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

            public int getGlobalRanking() {
                return globalRanking;
            }

            public void setGlobalRanking(int globalRanking) {
                this.globalRanking = globalRanking;
            }

            public int getBadge() {
                return badge;
            }

            public void setBadge(int badge) {
                this.badge = badge;
            }

            public boolean isVip() {
                return vip;
            }

            public void setVip(boolean vip) {
                this.vip = vip;
            }

            public boolean isOnline() {
                return online;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }

            public boolean isSigned() {
                return signed;
            }

            public void setSigned(boolean signed) {
                this.signed = signed;
            }

            public boolean isAttend() {
                return attend;
            }

            public void setAttend(boolean attend) {
                this.attend = attend;
            }
        }
    }
}
