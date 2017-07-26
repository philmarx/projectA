package com.hzease.tomeet;

import java.util.List;
import java.util.Map;

/**
 * Created by Key on 2017/7/25 20:29
 * email: MrKey.K@gmail.com
 * description:
 */

public class UserInfoBeanUnit {

    @Override
    public String toString() {
        return "UserInfoBeanUnit{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : {"birthday":"1918-07-25","avatarSignature":"1494588234118","gender":true,"nickname":"你猜我叫什么名字呢？🙃","imageSignatures":{"image1Signature":"1500947824656","image2Signature":"1498817137942","image3Signature":"","image4Signature":"1497945941757","image5Signature":"1497945794061"},"circles":[{"id":100000000000,"name":"张国文的圈子一","experience":572},{"id":100000000001,"name":"人民大会堂","experience":668},{"id":100000000008,"name":"杭州逸时网络科技有限公司","experience":798}],"orders":[{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1030,"ranking":2,"gameId":6,"gameName":"狼人杀","avatarSignature":"1494588234118","count":4,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1015,"ranking":2,"gameId":7,"gameName":"三国杀","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1055,"ranking":1,"gameId":8,"gameName":"扑克","avatarSignature":"1494588234118","count":3,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":4,"gameId":9,"gameName":"麻将","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1010,"ranking":4,"gameId":10,"gameName":"象棋","avatarSignature":"1494588234118","count":5,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1030,"ranking":1,"gameId":11,"gameName":"围棋","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":7,"gameId":12,"gameName":"篮球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1015,"ranking":3,"gameId":13,"gameName":"足球","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":1,"gameId":14,"gameName":"排球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1035,"ranking":2,"gameId":15,"gameName":"网球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1035,"ranking":1,"gameId":17,"gameName":"羽毛球","avatarSignature":"1494588234118","count":2,"gender":true}],"id":10000000000,"isVip":true,"labels":["神准时","萌妹纸","1234"]}
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
         * birthday : 1918-07-25
         * avatarSignature : 1494588234118
         * gender : true
         * nickname : 你猜我叫什么名字呢？🙃
         * imageSignatures : {"image1Signature":"1500947824656","image2Signature":"1498817137942","image3Signature":"","image4Signature":"1497945941757","image5Signature":"1497945794061"}
         * circles : [{"id":100000000000,"name":"张国文的圈子一","experience":572},{"id":100000000001,"name":"人民大会堂","experience":668},{"id":100000000008,"name":"杭州逸时网络科技有限公司","experience":798}]
         * orders : [{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1030,"ranking":2,"gameId":6,"gameName":"狼人杀","avatarSignature":"1494588234118","count":4,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1015,"ranking":2,"gameId":7,"gameName":"三国杀","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1055,"ranking":1,"gameId":8,"gameName":"扑克","avatarSignature":"1494588234118","count":3,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":4,"gameId":9,"gameName":"麻将","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1010,"ranking":4,"gameId":10,"gameName":"象棋","avatarSignature":"1494588234118","count":5,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1030,"ranking":1,"gameId":11,"gameName":"围棋","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":7,"gameId":12,"gameName":"篮球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1015,"ranking":3,"gameId":13,"gameName":"足球","avatarSignature":"1494588234118","count":1,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1000,"ranking":1,"gameId":14,"gameName":"排球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1035,"ranking":2,"gameId":15,"gameName":"网球","avatarSignature":"1494588234118","count":2,"gender":true},{"userId":10000000000,"nickname":"你猜我叫什么名字呢？🙃","point":1035,"ranking":1,"gameId":17,"gameName":"羽毛球","avatarSignature":"1494588234118","count":2,"gender":true}]
         * id : 10000000000
         * isVip : true
         * labels : ["神准时","萌妹纸","1234"]
         */

        private String birthday;
        private String avatarSignature;
        private boolean gender;
        private String nickname;
        private Map imageSignatures;
        private long id;
        private boolean isVip;
        private List<CirclesBean> circles;
        private List<OrdersBean> orders;
        private List<String> labels;

        public Map getImageSignatures() {
            return imageSignatures;
        }

        public void setImageSignatures(Map imageSignatures) {
            this.imageSignatures = imageSignatures;
        }

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean vip) {
            isVip = vip;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public boolean isGender() {
            return gender;
        }

        public void setGender(boolean gender) {
            this.gender = gender;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<CirclesBean> getCircles() {
            return circles;
        }

        public void setCircles(List<CirclesBean> circles) {
            this.circles = circles;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public static class CirclesBean {
            /**
             * id : 100000000000
             * name : 张国文的圈子一
             * experience : 572
             */

            private long id;
            private String name;
            private int experience;

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

            public int getExperience() {
                return experience;
            }

            public void setExperience(int experience) {
                this.experience = experience;
            }
        }

        public static class OrdersBean {
            /**
             * userId : 10000000000
             * nickname : 你猜我叫什么名字呢？🙃
             * point : 1030
             * ranking : 2
             * gameId : 6
             * gameName : 狼人杀
             * avatarSignature : 1494588234118
             * count : 4
             * gender : true
             */

            private long userId;
            private String nickname;
            private int point;
            private int ranking;
            private int gameId;
            private String gameName;
            private String avatarSignature;
            private int count;
            private boolean gender;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public int getRanking() {
                return ranking;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public int getGameId() {
                return gameId;
            }

            public void setGameId(int gameId) {
                this.gameId = gameId;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public String getAvatarSignature() {
                return avatarSignature;
            }

            public void setAvatarSignature(String avatarSignature) {
                this.avatarSignature = avatarSignature;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public boolean isGender() {
                return gender;
            }

            public void setGender(boolean gender) {
                this.gender = gender;
            }
        }
    }
}
