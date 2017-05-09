package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/5/9.
 */

public class EnterCircleInfoBean {
    /**
     * success : true
     * msg :
     * data : {"createCount":0,"joinCount":0,"circle":{"id":10,"name":"OverWatch","city":"杭州市","place":"西城博司","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1494207144605","labels":null},"longitude":120.19,"latitude":30.26,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":0,"roomCount":0},"experience":-1}
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
         * createCount : 0
         * joinCount : 0
         * circle : {"id":10,"name":"OverWatch","city":"杭州市","place":"西城博司","notice":"","manager":{"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1494207144605","labels":null},"longitude":120.19,"latitude":30.26,"hot":0,"avatarSignature":"","bgSignature":"","memberCount":0,"roomCount":0}
         * experience : -1
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
             * id : 10
             * name : OverWatch
             * city : 杭州市
             * place : 西城博司
             * notice :
             * manager : {"id":10000000020,"nickname":"我才是徐强","avatarSignature":"1494207144605","labels":null}
             * longitude : 120.19
             * latitude : 30.26
             * hot : 0
             * avatarSignature :
             * bgSignature :
             * memberCount : 0
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
                 * avatarSignature : 1494207144605
                 * labels : null
                 */

                private long id;
                private String nickname;
                private String avatarSignature;
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

                public String getAvatarSignature() {
                    return avatarSignature;
                }

                public void setAvatarSignature(String avatarSignature) {
                    this.avatarSignature = avatarSignature;
                }

                public Object getLabels() {
                    return labels;
                }

                public void setLabels(Object labels) {
                    this.labels = labels;
                }
            }
        }
    }
}
