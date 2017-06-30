package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/8.
 */

public class GameFinishBean {

    /**
     * success : true
     * msg :
     * data : {"members":[{"id":10000000006,"nickname":"天天","ready":true,"avatarSignature":"1497950761139","point":0,"badge":0,"vip":false,"signed":false,"attend":false,"online":false},{"id":10000000019,"nickname":"陈属","ready":true,"avatarSignature":"","point":0,"badge":0,"vip":false,"signed":false,"attend":false,"online":false}],"room":{"id":1000000000072,"name":"一起哈啤","place":"天屿山顶别墅(北山街)","manager":{"id":10000000019,"nickname":"陈属","avatarSignature":null,"labels":[],"point":0},"open":true,"beginTime":"2017-06-26 17:30","endTime":"2017-06-27 14:00","createTime":"2017-06-26 17:05","state":4,"locked":false,"game":{"id":21,"name":"派对"},"money":0,"joinMember":2,"joinManMember":2,"joinWomanMember":0,"memberCount":2,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.138846,"latitude":30.253121,"prepareTime":"2017-06-26 17:09:47","joinMembers":[],"city":"杭州"}}
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
         * members : [{"id":10000000006,"nickname":"天天","ready":true,"avatarSignature":"1497950761139","point":0,"badge":0,"vip":false,"signed":false,"attend":false,"online":false},{"id":10000000019,"nickname":"陈属","ready":true,"avatarSignature":"","point":0,"badge":0,"vip":false,"signed":false,"attend":false,"online":false}]
         * room : {"id":1000000000072,"name":"一起哈啤","place":"天屿山顶别墅(北山街)","manager":{"id":10000000019,"nickname":"陈属","avatarSignature":null,"labels":[],"point":0},"open":true,"beginTime":"2017-06-26 17:30","endTime":"2017-06-27 14:00","createTime":"2017-06-26 17:05","state":4,"locked":false,"game":{"id":21,"name":"派对"},"money":0,"joinMember":2,"joinManMember":2,"joinWomanMember":0,"memberCount":2,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.138846,"latitude":30.253121,"prepareTime":"2017-06-26 17:09:47","joinMembers":[],"city":"杭州"}
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
             * id : 1000000000072
             * name : 一起哈啤
             * place : 天屿山顶别墅(北山街)
             * manager : {"id":10000000019,"nickname":"陈属","avatarSignature":null,"labels":[],"point":0}
             * open : true
             * beginTime : 2017-06-26 17:30
             * endTime : 2017-06-27 14:00
             * createTime : 2017-06-26 17:05
             * state : 4
             * locked : false
             * game : {"id":21,"name":"派对"}
             * money : 0
             * joinMember : 2
             * joinManMember : 2
             * joinWomanMember : 0
             * memberCount : 2
             * manCount : 0
             * womanCount : 0
             * description : 暂无介绍
             * longitude : 120.138846
             * latitude : 30.253121
             * prepareTime : 2017-06-26 17:09:47
             * joinMembers : []
             * city : 杭州
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
                 * id : 10000000019
                 * nickname : 陈属
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
                 * id : 21
                 * name : 派对
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
             * id : 10000000006
             * nickname : 天天
             * ready : true
             * avatarSignature : 1497950761139
             * point : 0
             * badge : 0
             * vip : false
             * signed : false
             * attend : false
             * online : false
             */

            private long id;
            private String nickname;
            private boolean ready;
            private String avatarSignature;
            private int point;
            private int badge;
            private boolean vip;
            private boolean signed;
            private boolean attend;
            private boolean online;

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

            public boolean isOnline() {
                return online;
            }

            public void setOnline(boolean online) {
                this.online = online;
            }
        }
    }
}
