package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/4/10.
 */

public class HomeRoomsBean {

    /**
     * success : true
     * msg : test
     * data : [{"beginTime":"2017-05-16 14:00","endTime":"2017-05-16 15:00","game":{"id":6,"name":"狼人杀"},"id":1000000000094,"longitude":120.0761,"latitude":30.317027,"locked":false,"money":0,"name":"狼人杀1圈圈内房间二","place":"西城博司展示中心","joinMember":1,"joinWomanMember":0,"joinManMember":1,"joinMembers":[{"id":10000000023,"avatarSignature":"1494290577016"}],"manCount":10,"womanCount":12,"memberCount":22},{"beginTime":"2017-05-26 23:00","endTime":"2017-05-31 20:00","game":{"id":24,"name":"逛街"},"id":1000000000071,"longitude":120.075405,"latitude":30.317182,"locked":false,"money":0,"name":"的的","place":"行舟装饰","joinMember":1,"joinWomanMember":0,"joinManMember":1,"joinMembers":[{"id":10000000001,"avatarSignature":"1493193505823"}],"manCount":0,"womanCount":0,"memberCount":10},{"beginTime":"2017-05-16 14:40","endTime":"2017-05-16 15:40","game":{"id":6,"name":"狼人杀"},"id":1000000000090,"longitude":120.076193,"latitude":30.317012,"locked":false,"money":0,"name":"张国文狼人杀圈子一","place":"西城博司展示中心","joinMember":2,"joinWomanMember":0,"joinManMember":2,"joinMembers":[{"id":10000000001,"avatarSignature":"1493193505823"},{"id":10000000023,"avatarSignature":"1494290577016"}],"manCount":0,"womanCount":0,"memberCount":2},{"beginTime":"2017-05-17 15:10","endTime":"2017-05-17 18:10","game":{"id":6,"name":"狼人杀"},"id":1000000000095,"longitude":120.109329,"latitude":30.219829,"locked":true,"money":0,"name":"狼人杀一圈圈内房间三","place":"龙凤茶店","joinMember":1,"joinWomanMember":0,"joinManMember":1,"joinMembers":[{"id":10000000023,"avatarSignature":"1494290577016"}],"manCount":5,"womanCount":5,"memberCount":10},{"beginTime":"2017-05-17 07:50","endTime":"2017-05-17 10:00","game":{"id":6,"name":"狼人杀"},"id":1000000000096,"longitude":120.215181,"latitude":30.174667,"locked":true,"money":2,"name":"狼人杀一圈圈内房间四","place":"中国空分大厦","joinMember":1,"joinWomanMember":0,"joinManMember":1,"joinMembers":[{"id":10000000023,"avatarSignature":"1494290577016"}],"manCount":0,"womanCount":0,"memberCount":2},{"beginTime":"2017-05-15 22:50","endTime":"2017-05-15 23:50","game":{"id":9,"name":"麻将"},"id":1000000000056,"longitude":119.923204,"latitude":28.461148,"locked":false,"money":0,"name":"输的吃屎","place":"万地广场","joinMember":1,"joinWomanMember":1,"joinManMember":0,"joinMembers":[{"id":10000000046,"avatarSignature":"1493191059992"}],"manCount":0,"womanCount":0,"memberCount":10},{"beginTime":"2017-06-10 15:33","endTime":"2017-06-12 15:32","game":{"id":6,"name":"狼人杀"},"id":1000000000053,"longitude":119.92216,"latitude":28.458498,"locked":false,"money":0,"name":"狼人杀","place":"丽水华侨开元名都大酒店","joinMember":1,"joinWomanMember":1,"joinManMember":0,"joinMembers":[{"id":10000000046,"avatarSignature":"1493191059992"}],"manCount":5,"womanCount":5,"memberCount":10}]
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
         * beginTime : 2017-05-16 14:00
         * endTime : 2017-05-16 15:00
         * game : {"id":6,"name":"狼人杀"}
         * id : 1000000000094
         * longitude : 120.0761
         * latitude : 30.317027
         * locked : false
         * money : 0
         * name : 狼人杀1圈圈内房间二
         * place : 西城博司展示中心
         * joinMember : 1
         * joinWomanMember : 0
         * joinManMember : 1
         * joinMembers : [{"id":10000000023,"avatarSignature":"1494290577016"}]
         * manCount : 10
         * womanCount : 12
         * memberCount : 22
         */

        private String beginTime;
        private String endTime;
        private GameBean game;
        private long id;
        private double longitude;
        private double latitude;
        private boolean locked;
        private int money;
        private String name;
        private String place;
        private int state;
        private int joinMember;
        private int joinWomanMember;
        private int joinManMember;
        private int manCount;
        private int womanCount;
        private int memberCount;
        private List<JoinMembersBean> joinMembers;


        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
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

        public GameBean getGame() {
            return game;
        }

        public void setGame(GameBean game) {
            this.game = game;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
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

        public int getJoinMember() {
            return joinMember;
        }

        public void setJoinMember(int joinMember) {
            this.joinMember = joinMember;
        }

        public int getJoinWomanMember() {
            return joinWomanMember;
        }

        public void setJoinWomanMember(int joinWomanMember) {
            this.joinWomanMember = joinWomanMember;
        }

        public int getJoinManMember() {
            return joinManMember;
        }

        public void setJoinManMember(int joinManMember) {
            this.joinManMember = joinManMember;
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

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public List<JoinMembersBean> getJoinMembers() {
            return joinMembers;
        }

        public void setJoinMembers(List<JoinMembersBean> joinMembers) {
            this.joinMembers = joinMembers;
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

        public static class JoinMembersBean {
            /**
             * id : 10000000023
             * avatarSignature : 1494290577016
             */

            private long id;
            private String avatarSignature;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }
        }
    }
}
