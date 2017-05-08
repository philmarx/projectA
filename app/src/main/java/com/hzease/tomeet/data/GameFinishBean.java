package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/8.
 */

public class GameFinishBean {
    /**
     * success : true
     * msg :
     * data : {"members":[{"id":10000000001,"nickname":"😏😏😏","ready":true,"avatarSignature":"1493193505823","point":0},{"id":10000000007,"nickname":"test2","ready":true,"avatarSignature":"1493191059992","point":0},{"id":10000000025,"nickname":"马健原","ready":true,"avatarSignature":"1494220981231","point":0}],"room":{"id":1000000000023,"name":"马建院的圈内房间二","place":"西城博司展示中心","manager":{"id":10000000025,"nickname":"马健原","avatarSignature":null,"labels":null},"open":false,"belongCircle":null,"beginTime":"2017-05-05 19:02","endTime":"2017-05-05 23:02","createTime":"2017-05-05 19:02","state":4,"locked":false,"game":{"id":6,"name":"狼人杀"},"money":0,"joinMember":1,"joinManMember":1,"joinWomanMember":0,"phone":null,"memberCount":4,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.076103,"latitude":30.317022,"prepareTime":null,"joinMembers":[],"city":"杭州市"}}
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
         * members : [{"id":10000000001,"nickname":"😏😏😏","ready":true,"avatarSignature":"1493193505823","point":0},{"id":10000000007,"nickname":"test2","ready":true,"avatarSignature":"1493191059992","point":0},{"id":10000000025,"nickname":"马健原","ready":true,"avatarSignature":"1494220981231","point":0}]
         * room : {"id":1000000000023,"name":"马建院的圈内房间二","place":"西城博司展示中心","manager":{"id":10000000025,"nickname":"马健原","avatarSignature":null,"labels":null},"open":false,"belongCircle":null,"beginTime":"2017-05-05 19:02","endTime":"2017-05-05 23:02","createTime":"2017-05-05 19:02","state":4,"locked":false,"game":{"id":6,"name":"狼人杀"},"money":0,"joinMember":1,"joinManMember":1,"joinWomanMember":0,"phone":null,"memberCount":4,"manCount":0,"womanCount":0,"description":"暂无介绍","longitude":120.076103,"latitude":30.317022,"prepareTime":null,"joinMembers":[],"city":"杭州市"}
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
             * id : 1000000000023
             * name : 马建院的圈内房间二
             * place : 西城博司展示中心
             * manager : {"id":10000000025,"nickname":"马健原","avatarSignature":null,"labels":null}
             * open : false
             * belongCircle : null
             * beginTime : 2017-05-05 19:02
             * endTime : 2017-05-05 23:02
             * createTime : 2017-05-05 19:02
             * state : 4
             * locked : false
             * game : {"id":6,"name":"狼人杀"}
             * money : 0
             * joinMember : 1
             * joinManMember : 1
             * joinWomanMember : 0
             * phone : null
             * memberCount : 4
             * manCount : 0
             * womanCount : 0
             * description : 暂无介绍
             * longitude : 120.076103
             * latitude : 30.317022
             * prepareTime : null
             * joinMembers : []
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
            private int joinMember;
            private int joinManMember;
            private int joinWomanMember;
            private Object phone;
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

            public List<?> getJoinMembers() {
                return joinMembers;
            }

            public void setJoinMembers(List<?> joinMembers) {
                this.joinMembers = joinMembers;
            }

            public static class ManagerBean {
                /**
                 * id : 10000000025
                 * nickname : 马健原
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
                 * id : 6
                 * name : 狼人杀
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
