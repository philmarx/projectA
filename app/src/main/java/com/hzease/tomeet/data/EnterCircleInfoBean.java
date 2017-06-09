package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/9.
 */

public class EnterCircleInfoBean {
    /**
     * success : true
     * msg :
     * data : {"createCount":4,"joinCount":0,"circle":{"id":100000000000,"name":"张国文的圈子一","city":"杭州市","place":"西城博司展示中心","notice":"","manager":{"id":10000000001,"nickname":"我是新修改的昵称","avatarSignature":"1495160353657","labels":[],"point":0},"longitude":120.075936,"latitude":30.317123,"hot":0,"avatarSignature":"1495160261984","bgSignature":"1495160275395","memberCount":5,"roomCount":0,"sign":true},"experience":31}
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
         * createCount : 4
         * joinCount : 0
         * circle : {"id":100000000000,"name":"张国文的圈子一","city":"杭州市","place":"西城博司展示中心","notice":"","manager":{"id":10000000001,"nickname":"我是新修改的昵称","avatarSignature":"1495160353657","labels":[],"point":0},"longitude":120.075936,"latitude":30.317123,"hot":0,"avatarSignature":"1495160261984","bgSignature":"1495160275395","memberCount":5,"roomCount":0,"sign":true}
         * experience : 31
         */

        private int createCount;
        private int joinCount;
        private CircleBean circle;
        private int experience;

        public int getCreateCount() {
            return createCount;
        }

        public void setCreateCount(int createCount) {
            this.createCount = createCount;
        }

        public int getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(int joinCount) {
            this.joinCount = joinCount;
        }

        public CircleBean getCircle() {
            return circle;
        }

        public void setCircle(CircleBean circle) {
            this.circle = circle;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public static class CircleBean {
            /**
             * id : 100000000000
             * name : 张国文的圈子一
             * city : 杭州市
             * place : 西城博司展示中心
             * notice :
             * manager : {"id":10000000001,"nickname":"我是新修改的昵称","avatarSignature":"1495160353657","labels":[],"point":0}
             * longitude : 120.075936
             * latitude : 30.317123
             * hot : 0
             * avatarSignature : 1495160261984
             * bgSignature : 1495160275395
             * memberCount : 5
             * roomCount : 0
             * sign : true
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
            private boolean sign;

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

            public boolean isSign() {
                return sign;
            }

            public void setSign(boolean sign) {
                this.sign = sign;
            }

            public static class ManagerBean {
                /**
                 * id : 10000000001
                 * nickname : 我是新修改的昵称
                 * avatarSignature : 1495160353657
                 * labels : []
                 * point : 0
                 */

                private long id;
                private String nickname;
                private String avatarSignature;
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

                public List<?> getLabels() {
                    return labels;
                }

                public void setLabels(List<?> labels) {
                    this.labels = labels;
                }
            }
        }
    }
}
