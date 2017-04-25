package pro.yueyuan.project_t.data;

import java.util.List;

public class UserInfoBean {

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                '}';
    }

    /**
     * data : {"amount":1000,"avatarSignature":"","badge":10,"gender":true,"id":10000000001,"isInit":true,"labels":["a","b"],"lockAmount":11,"nickname":"😏😏","phone":"18698569593","realName":"计奇良"}
     * msg : 登录成功
     * success : true
     */

    private DataBean data;
    private String msg;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {

        @Override
        public String toString() {
            return "DataBean{" +
                    "amount=" + amount +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", badge=" + badge +
                    ", gender=" + gender +
                    ", id=" + id +
                    ", isInit=" + isInit +
                    ", lockAmount=" + lockAmount +
                    ", nickname='" + nickname + '\'' +
                    ", phone='" + phone + '\'' +
                    ", realName='" + realName + '\'' +
                    ", labels=" + labels +
                    '}';
        }

        /**
         * amount : 1000
         * avatarSignature :
         * badge : 10
         * gender : true
         * id : 10000000001
         * isInit : true
         * labels : ["a","b"]
         * lockAmount : 11
         * nickname : 😏😏
         * phone : 18698569593
         * realName : 计奇良
         */

        private int amount;
        private String avatarSignature;
        private int badge;
        private boolean gender;
        private long id;
        private boolean isInit;
        private int lockAmount;
        private String nickname;
        private String phone;
        private String realName;
        private List<String> labels;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
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

        public int getLockAmount() {
            return lockAmount;
        }

        public void setLockAmount(int lockAmount) {
            this.lockAmount = lockAmount;
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

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
    }
}
