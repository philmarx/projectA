package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/5.
 */

public class CircleInfoBean {

    /**
     * success : true
     * msg :
     * data : [{"id":14,"name":"洗衣服","city":"杭州市","place":"邬凌减肥养生馆","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1493260835310"},"longitude":120.075779,"latitude":30.3167,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":5,"name":"狼人杀5圈","city":"杭州市","place":"西城博司展示中心","notice":"","manager":{"id":10000000025,"nickname":"马健原","avatarSignature":"1493191059992"},"longitude":120.076085,"latitude":30.31708,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":10,"name":"OverWatch","city":"杭州市","place":"西城博司","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1493260835310"},"longitude":120.19,"latitude":30.26,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":1,"name":"狼人杀1圈","city":"杭州","place":"西城波斯","notice":"","manager":{"id":10000000023,"nickname":"张国文","avatarSignature":"1493342425239"},"longitude":120.076152,"latitude":30.317012,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":1},{"id":15,"name":"车友会2","city":"杭州市","place":"蜜菓(紫萱路店)","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1493260835310"},"longitude":120.075776,"latitude":30.3167,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":6,"name":"狼人杀6圈","city":"杭州市","place":"西城博司展示中心","notice":"","manager":{"id":10000000025,"nickname":"马健原","avatarSignature":"1493191059992"},"longitude":120.076085,"latitude":30.31708,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":11,"name":"杭州车友会","city":"杭州车友会","place":"妙手足疗","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1493260835310"},"longitude":120.075493,"latitude":30.317125,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":1,"roomCount":0},{"id":2,"name":"狼人杀2圈","city":"杭州市","place":"西城博司展示中心","notice":"","manager":{"id":10000000025,"nickname":"马健原","avatarSignature":"1493191059992"},"longitude":120.076085,"latitude":30.31708,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":3,"roomCount":0}]
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
         * id : 14
         * name : 洗衣服
         * city : 杭州市
         * place : 邬凌减肥养生馆
         * notice :
         * manager : {"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1493260835310"}
         * longitude : 120.075779
         * latitude : 30.3167
         * hot : 0
         * avatarSignature :
         * bgSignature :
         * memberCount : 1
         * roomCount : 0
         */

        private long id;
        private String name;
        private String city;
        private String place;
        private String notice;
        private ManagerBean manager;
        private double longitude;
        private double latitude;
        private int hot;
        private String avatarSignature;
        private String bgSignature;
        private int memberCount;
        private int roomCount;

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

        public ManagerBean getManager() {
            return manager;
        }

        public void setManager(ManagerBean manager) {
            this.manager = manager;
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

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public String getBgSignature() {
            return bgSignature;
        }

        public void setBgSignature(String bgSignature) {
            this.bgSignature = bgSignature;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getRoomCount() {
            return roomCount;
        }

        public void setRoomCount(int roomCount) {
            this.roomCount = roomCount;
        }

        public static class ManagerBean {
            /**
             * id : 10000000020
             * nickname : 我才是徐强
             * avatarSignature : 1493260835310
             */
            private long id;
            private String nickname;
            private String avatarSignature;

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

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }
        }
    }
}
