package pro.yueyuan.project_t.data;

import java.util.List;

/**
 * Created by Key on 2017/3/21 17:33
 * email: MrKey.K@gmail.com
 * description:
 */

public class ShowGameListBean {
    /**
     * success : true
     * msg : null
     * data : [{"id":1,"name":"桌游棋牌","children":[{"id":6,"name":"狼人杀","children":null},{"id":7,"name":"三国杀","children":null},{"id":8,"name":"扑克","children":null},{"id":9,"name":"麻将","children":null},{"id":10,"name":"象棋","children":null},{"id":11,"name":"围棋","children":null}]},{"id":2,"name":"体育运动","children":[{"id":12,"name":"篮球","children":null},{"id":13,"name":"足球","children":null},{"id":14,"name":"排球","children":null},{"id":15,"name":"网球","children":null},{"id":16,"name":"乒乓球","children":null},{"id":17,"name":"羽毛球","children":null}]},{"id":3,"name":"聚会交友","children":[{"id":18,"name":"密室","children":null},{"id":19,"name":"KTV","children":null},{"id":20,"name":"酒吧","children":null},{"id":21,"name":"派对","children":null},{"id":22,"name":"电影","children":null},{"id":23,"name":"美食","children":null},{"id":24,"name":"逛街","children":null}]},{"id":4,"name":"结伴出游","children":[{"id":25,"name":"爬山","children":null},{"id":26,"name":"骑车","children":null},{"id":27,"name":"郊游","children":null},{"id":28,"name":"短途旅行","children":null},{"id":29,"name":"长途旅行","children":null}]},{"id":5,"name":"其它活动","children":null}]
     */

    private boolean success;
    private Object msg;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * id : 1
         * name : 桌游棋牌
         * children : [{"id":6,"name":"狼人杀","children":null},{"id":7,"name":"三国杀","children":null},{"id":8,"name":"扑克","children":null},{"id":9,"name":"麻将","children":null},{"id":10,"name":"象棋","children":null},{"id":11,"name":"围棋","children":null}]
         */

        private int id;
        private String name;
        private List<ChildrenBean> children;

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

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean {
            /**
             * id : 6
             * name : 狼人杀
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
