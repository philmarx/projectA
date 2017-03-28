package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by xuq on 2017/3/28.
 */

public class MyJionRoomBean {
    /**
     * success : true
     * msg : 查找成功
     * data : [{"id":41,"name":"乒乓球44","place":"杭州西湖","creator":{"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]},"beginTime":"2017-03-27 17:20","endTime":"2017-03-22 18:35","createTime":"2017-03-27 15:51","state":1,"game":{"id":28,"name":"短途旅行","children":null},"money":0,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":0,"manCount":0,"womanCount":0,"description":"28","longitude":0,"latitude":0,"joinMemberIds":[10000000020]},{"id":42,"name":"乒乓球55","place":"杭州西湖","creator":{"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]},"beginTime":"2017-03-27 16:38","endTime":"2017-03-22 18:35","createTime":"2017-03-27 15:51","state":0,"game":{"id":28,"name":"短途旅行","children":null},"money":0,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":0,"manCount":0,"womanCount":0,"description":"28","longitude":0,"latitude":0,"joinMemberIds":[10000000020]},{"id":40,"name":"乒乓球33","place":"杭州西湖","creator":{"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]},"beginTime":"2017-03-27 17:20","endTime":"2017-03-22 18:35","createTime":"2017-03-27 15:50","state":2,"game":{"id":28,"name":"短途旅行","children":null},"money":0,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":0,"manCount":0,"womanCount":0,"description":"28","longitude":0,"latitude":0,"joinMemberIds":[10000000020]},{"id":38,"name":"乒乓球11","place":"杭州西湖","creator":{"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]},"beginTime":"2017-03-27 17:20","endTime":"2017-03-23 17:33","createTime":"2017-03-27 15:46","state":3,"game":{"id":28,"name":"短途旅行","children":null},"money":0,"joinMember":0,"joinManMember":0,"joinWomanMember":0,"phone":null,"memberCount":0,"manCount":0,"womanCount":0,"description":"28","longitude":0,"latitude":0,"joinMemberIds":[10000000020]}]
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
         * id : 41
         * name : 乒乓球44
         * place : 杭州西湖
         * creator : {"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]}
         * beginTime : 2017-03-27 17:20
         * endTime : 2017-03-22 18:35
         * createTime : 2017-03-27 15:51
         * state : 1
         * game : {"id":28,"name":"短途旅行","children":null}
         * money : 0
         * joinMember : 0
         * joinManMember : 0
         * joinWomanMember : 0
         * phone : null
         * memberCount : 0
         * manCount : 0
         * womanCount : 0
         * description : 28
         * longitude : 0
         * latitude : 0
         * joinMemberIds : [10000000020]
         */

        private int id;
        private String name;
        private String place;
        private CreatorBean creator;
        private String beginTime;
        private String endTime;
        private String createTime;
        private int state;
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
        private int longitude;
        private int latitude;
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

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public int getLatitude() {
            return latitude;
        }

        public void setLatitude(int latitude) {
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
             * id : 10000000000
             * nickname : test4
             * amount : 0
             * phone : 17702525841
             * email : null
             * gender : null
             * location : null
             * place : null
             * age : null
             * isInit : null
             * labels : [{"id":1,"name":"90后"}]
             */

            private long id;
            private String nickname;
            private int amount;
            private String phone;
            private Object email;
            private Object gender;
            private Object location;
            private Object place;
            private Object age;
            private Object isInit;
            private List<LabelsBean> labels;

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

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public Object getEmail() {
                return email;
            }

            public void setEmail(Object email) {
                this.email = email;
            }

            public Object getGender() {
                return gender;
            }

            public void setGender(Object gender) {
                this.gender = gender;
            }

            public Object getLocation() {
                return location;
            }

            public void setLocation(Object location) {
                this.location = location;
            }

            public Object getPlace() {
                return place;
            }

            public void setPlace(Object place) {
                this.place = place;
            }

            public Object getAge() {
                return age;
            }

            public void setAge(Object age) {
                this.age = age;
            }

            public Object getIsInit() {
                return isInit;
            }

            public void setIsInit(Object isInit) {
                this.isInit = isInit;
            }

            public List<LabelsBean> getLabels() {
                return labels;
            }

            public void setLabels(List<LabelsBean> labels) {
                this.labels = labels;
            }

            public static class LabelsBean {
                /**
                 * id : 1
                 * name : 90后
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

        public static class GameBean {
            /**
             * id : 28
             * name : 短途旅行
             * children : null
             */

            private int id;
            private String name;
            private Object children;

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

            public Object getChildren() {
                return children;
            }

            public void setChildren(Object children) {
                this.children = children;
            }
        }
    }
}
