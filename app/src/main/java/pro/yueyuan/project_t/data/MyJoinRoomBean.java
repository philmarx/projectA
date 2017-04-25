package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by xuq on 2017/3/28.
 */

public class MyJoinRoomBean {

    /**
     * data : [{"beginTime":"2017-03-29 15:28","createTime":"2017-03-27 15:51","creator":{"age":22,"amount":0,"email":"test@outlook.com","gender":true,"id":10000000000,"isInit":true,"labels":[{"id":1,"name":"90后"}],"nickname":"test4","phone":"17702525841","place":"测试地址"},"description":"打乒乓球","endTime":"2017-03-22 18:35","game":{"id":28,"name":"短途旅行"},"id":42,"joinManMember":1,"joinMember":1,"joinMemberIds":[10000000000,10000000020],"joinWomanMember":0,"latitude":33.33,"longitude":22.3,"manCount":0,"memberCount":5,"money":1001,"name":"乒乓球55","phone":"17702525841","place":"杭州西湖","state":0,"womanCount":0},{"beginTime":"2017-03-29 15:28","createTime":"2017-03-27 15:51","creator":{"age":22,"amount":0,"email":"test@outlook.com","gender":true,"id":10000000000,"isInit":true,"labels":[{"id":1,"name":"90后"}],"nickname":"test4","phone":"17702525841","place":"测试地址"},"description":"打乒乓球","endTime":"2017-03-22 18:35","game":{"id":28,"name":"短途旅行"},"id":41,"joinManMember":1,"joinMember":1,"joinMemberIds":[10000000000,10000000020],"joinWomanMember":0,"latitude":33.33,"longitude":22.3,"manCount":3,"memberCount":5,"money":2323,"name":"乒乓球44","phone":"17702525841","place":"杭州西湖","state":1,"womanCount":2}]
     * msg : 查找成功
     * success : true
     */

    private String msg;
    private boolean success;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "beginTime='" + beginTime + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", creator=" + creator +
                    ", description='" + description + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", game=" + game +
                    ", id=" + id +
                    ", joinManMember=" + joinManMember +
                    ", joinMember=" + joinMember +
                    ", joinWomanMember=" + joinWomanMember +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", manCount=" + manCount +
                    ", memberCount=" + memberCount +
                    ", money=" + money +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", place='" + place + '\'' +
                    ", state=" + state +
                    ", womanCount=" + womanCount +
                    ", joinMemberIds=" + joinMemberIds +
                    '}';
        }

        /**
         * beginTime : 2017-03-29 15:28
         * createTime : 2017-03-27 15:51
         * creator : {"age":22,"amount":0,"email":"test@outlook.com","gender":true,"id":10000000000,"isInit":true,"labels":[{"id":1,"name":"90后"}],"nickname":"test4","phone":"17702525841","place":"测试地址"}
         * description : 打乒乓球
         * endTime : 2017-03-22 18:35
         * game : {"id":28,"name":"短途旅行"}
         * id : 42
         * joinManMember : 1
         * joinMember : 1
         * joinMemberIds : [10000000000,10000000020]
         * joinWomanMember : 0
         * latitude : 33.33
         * longitude : 22.3
         * manCount : 0
         * memberCount : 5
         * money : 1001
         * name : 乒乓球55
         * phone : 17702525841
         * place : 杭州西湖
         * state : 0
         * womanCount : 0
         */

        private String beginTime;
        private String createTime;
        private CreatorBean creator;
        private String description;
        private String endTime;
        private GameBean game;
        private int id;
        private int joinManMember;
        private int joinMember;
        private int joinWomanMember;
        private double latitude;
        private double longitude;
        private int manCount;
        private int memberCount;
        private int money;
        private String name;
        private String phone;
        private String place;
        private int state;
        private int womanCount;
        private List<Long> joinMemberIds;

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getJoinManMember() {
            return joinManMember;
        }

        public void setJoinManMember(int joinManMember) {
            this.joinManMember = joinManMember;
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

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getManCount() {
            return manCount;
        }

        public void setManCount(int manCount) {
            this.manCount = manCount;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getWomanCount() {
            return womanCount;
        }

        public void setWomanCount(int womanCount) {
            this.womanCount = womanCount;
        }

        public List<Long> getJoinMemberIds() {
            return joinMemberIds;
        }

        public void setJoinMemberIds(List<Long> joinMemberIds) {
            this.joinMemberIds = joinMemberIds;
        }

        public static class CreatorBean {
            /**
             * age : 22
             * amount : 0
             * email : test@outlook.com
             * gender : true
             * id : 10000000000
             * isInit : true
             * labels : [{"id":1,"name":"90后"}]
             * nickname : test4
             * phone : 17702525841
             * place : 测试地址
             */

            private int age;
            private int amount;
            private String email;
            private boolean gender;
            private long id;
            private boolean isInit;
            private String nickname;
            private String phone;
            private String place;
            private List<LabelsBean> labels;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public boolean isGender() {
                return gender;
            }

            public void setGender(boolean gender) {
                this.gender = gender;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public boolean isIsInit() {
                return isInit;
            }

            public void setIsInit(boolean isInit) {
                this.isInit = isInit;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
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
