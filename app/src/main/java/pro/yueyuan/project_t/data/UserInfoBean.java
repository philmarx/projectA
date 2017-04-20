package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by xuq on 2017/3/27.
 */

public class UserInfoBean {

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", 用户信息: " + data +
                '}';
    }

    /**
     * success : true
     * msg : 登录成功
     * data : {"id":10000000001,"nickname":"123456","amount":1000000,"badge":10,"lockAmount":0,"phone":"18698569593","email":"test@outlook.com","gender":true,"place":"测试地址","age":22,"isInit":false,"labels":["a","b"]}
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", 余额=" + amount +
                    ", 勋章=" + badge +
                    ", 冻结余额=" + lockAmount +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", gender=" + gender +
                    ", place='" + place + '\'' +
                    ", age=" + age +
                    ", 初始化: " + isInit +
                    ", 标签: " + labels +
                    '}';
        }

        /**
         * id : 10000000001
         * nickname : 123456
         * amount : 1000000 余额
         * badge : 10 勋章
         * lockAmount : 0  冻结金额
         * phone : 18698569593
         * email : test@outlook.com
         * gender : true  男,,,false 女
         * place : 测试地址
         * age : 22
         * isInit : false  是否初始化
         * labels : ["a","b"]  标签
         */

        private long id;
        private String nickname;
        private int amount;
        private int badge;
        private int lockAmount;
        private String phone;
        private String email;
        private boolean gender;
        private String place;
        private int age;
        private boolean isInit;
        private List<String> labels;

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

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public int getLockAmount() {
            return lockAmount;
        }

        public void setLockAmount(int lockAmount) {
            this.lockAmount = lockAmount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isIsInit() {
            return isInit;
        }

        public void setIsInit(boolean isInit) {
            this.isInit = isInit;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
    }
}
