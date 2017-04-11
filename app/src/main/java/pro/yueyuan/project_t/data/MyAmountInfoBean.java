package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by xuq on 2017/3/27.
 */

public class MyAmountInfoBean {
    /**
     * success : true
     * msg : 登录成功
     * data : {"id":10000000000,"nickname":"test4","amount":0,"phone":"17702525841","email":null,"gender":null,"location":null,"place":null,"age":null,"isInit":null,"labels":[{"id":1,"name":"90后"}]}
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
}
