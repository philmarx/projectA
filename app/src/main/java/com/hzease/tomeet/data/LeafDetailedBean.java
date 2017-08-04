package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/8/3.
 */

public class LeafDetailedBean {
    /**
     * success : true
     * msg :
     * data : [{"id":1119,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"【周四早起爬山身体好】结束后赠送","createTime":1501725603000,"variation":4},{"id":1104,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"【周三打败阿尔法狗】结束后赠送","createTime":1501639201000,"variation":1},{"id":1100,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575936000,"variation":1},{"id":1095,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575935000,"variation":1},{"id":1096,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575935000,"variation":1},{"id":1097,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575935000,"variation":1},{"id":1098,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575935000,"variation":1},{"id":1099,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575935000,"variation":1},{"id":1090,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575934000,"variation":1},{"id":1091,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575934000,"variation":1},{"id":1092,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575934000,"variation":1},{"id":1093,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575934000,"variation":1},{"id":1094,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575934000,"variation":1},{"id":1085,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575933000,"variation":1},{"id":1086,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575933000,"variation":1},{"id":1087,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575933000,"variation":1},{"id":1088,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575933000,"variation":1},{"id":1089,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575933000,"variation":1},{"id":1082,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575932000,"variation":1},{"id":1083,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575932000,"variation":1},{"id":1084,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575932000,"variation":1},{"id":1080,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575931000,"variation":1},{"id":1081,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575931000,"variation":1},{"id":1075,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575930000,"variation":1},{"id":1076,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575930000,"variation":1},{"id":1077,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575930000,"variation":1},{"id":1078,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575930000,"variation":1},{"id":1079,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575930000,"variation":1},{"id":1074,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575554000,"variation":1},{"id":1073,"user":{"id":10000000003,"nickname":"江边泽国有民蛤"},"description":"使用1个叶子添加经验值10分","createTime":1501575550000,"variation":1}]
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
         * id : 1119
         * user : {"id":10000000003,"nickname":"江边泽国有民蛤"}
         * description : 【周四早起爬山身体好】结束后赠送
         * createTime : 1501725603000
         * variation : 4
         */

        private int id;
        private UserBean user;
        private String description;
        private long createTime;
        private int variation;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getVariation() {
            return variation;
        }

        public void setVariation(int variation) {
            this.variation = variation;
        }

        public static class UserBean {
            /**
             * id : 10000000003
             * nickname : 江边泽国有民蛤
             */

            private long id;
            private String nickname;

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
        }
    }
}
