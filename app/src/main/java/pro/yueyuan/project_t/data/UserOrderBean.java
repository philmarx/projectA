package pro.yueyuan.project_t.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuq on 2017/4/6.
 */

public class UserOrderBean implements Serializable {


    /**
     * success : true
     * msg :
     * data : {"avatarSignature":"","gender":true,"nickname":"我才是徐强","imageSignatures":{"image1Signature":"","image2Signature":"","image3Signature":"","image4Signature":"","image5Signature":""},"orders":[{"point":42153,"ranking":4,"gameId":10,"gameName":"象棋","count":1},{"point":17283,"ranking":2,"gameId":13,"gameName":"足球","count":1}],"id":10000000020,"labels":["a","b"]}
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
         * avatarSignature :
         * gender : true
         * nickname : 我才是徐强
         * imageSignatures : {"image1Signature":"","image2Signature":"","image3Signature":"","image4Signature":"","image5Signature":""}
         * orders : [{"point":42153,"ranking":4,"gameId":10,"gameName":"象棋","count":1},{"point":17283,"ranking":2,"gameId":13,"gameName":"足球","count":1}]
         * id : 10000000020
         * labels : ["a","b"]
         */

        private String avatarSignature;
        private boolean gender;
        private String nickname;
        private ImageSignaturesBean imageSignatures;
        private long id;
        private List<OrdersBean> orders;
        private List<String> labels;

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

        public ImageSignaturesBean getImageSignatures() {
            return imageSignatures;
        }

        public void setImageSignatures(ImageSignaturesBean imageSignatures) {
            this.imageSignatures = imageSignatures;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public static class ImageSignaturesBean {
            /**
             * image1Signature :
             * image2Signature :
             * image3Signature :
             * image4Signature :
             * image5Signature :
             */

            private String image1Signature;
            private String image2Signature;
            private String image3Signature;
            private String image4Signature;
            private String image5Signature;

            public String getImage1Signature() {
                return image1Signature;
            }

            public void setImage1Signature(String image1Signature) {
                this.image1Signature = image1Signature;
            }

            public String getImage2Signature() {
                return image2Signature;
            }

            public void setImage2Signature(String image2Signature) {
                this.image2Signature = image2Signature;
            }

            public String getImage3Signature() {
                return image3Signature;
            }

            public void setImage3Signature(String image3Signature) {
                this.image3Signature = image3Signature;
            }

            public String getImage4Signature() {
                return image4Signature;
            }

            public void setImage4Signature(String image4Signature) {
                this.image4Signature = image4Signature;
            }

            public String getImage5Signature() {
                return image5Signature;
            }

            public void setImage5Signature(String image5Signature) {
                this.image5Signature = image5Signature;
            }
        }

        public static class OrdersBean {
            /**
             * point : 42153
             * ranking : 4
             * gameId : 10
             * gameName : 象棋
             * count : 1
             */

            private int point;
            private int ranking;
            private int gameId;
            private String gameName;
            private int count;

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

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }
}
