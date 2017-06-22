package com.hzease.tomeet.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuq on 2017/3/8.
 */

public class GameTypeBean {

    @Override
    public String toString() {
        return "GameTypeBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg : 1
     * data : [{"id":1,"name":"桌游棋牌","children":[{"id":6,"name":"狼人杀","children":null,"scoring":true,"show":true},{"id":7,"name":"三国杀","children":null,"scoring":true,"show":true},{"id":8,"name":"扑克","children":null,"scoring":true,"show":true},{"id":9,"name":"麻将","children":null,"scoring":true,"show":true},{"id":10,"name":"象棋","children":null,"scoring":true,"show":true},{"id":11,"name":"围棋","children":null,"scoring":true,"show":true}],"scoring":false,"show":false},{"id":2,"name":"体育运动","children":[{"id":12,"name":"篮球","children":null,"scoring":true,"show":true},{"id":13,"name":"足球","children":null,"scoring":true,"show":true},{"id":14,"name":"排球","children":null,"scoring":true,"show":true},{"id":15,"name":"网球","children":null,"scoring":true,"show":true},{"id":16,"name":"乒乓球","children":null,"scoring":true,"show":true},{"id":17,"name":"羽毛球","children":null,"scoring":true,"show":true}],"scoring":false,"show":false},{"id":3,"name":"聚会交友","children":[{"id":18,"name":"密室","children":null,"scoring":false,"show":false},{"id":19,"name":"KTV","children":null,"scoring":false,"show":false},{"id":20,"name":"酒吧","children":null,"scoring":false,"show":false},{"id":21,"name":"派对","children":null,"scoring":false,"show":false},{"id":22,"name":"电影","children":null,"scoring":false,"show":false},{"id":23,"name":"美食","children":null,"scoring":false,"show":false},{"id":24,"name":"逛街","children":null,"scoring":false,"show":false}],"scoring":false,"show":false},{"id":4,"name":"结伴出游","children":[{"id":25,"name":"爬山","children":null,"scoring":false,"show":false},{"id":26,"name":"骑车","children":null,"scoring":false,"show":false},{"id":27,"name":"郊游","children":null,"scoring":false,"show":false},{"id":28,"name":"短途旅行","children":null,"scoring":false,"show":false},{"id":29,"name":"长途旅行","children":null,"scoring":false,"show":false}],"scoring":false,"show":false},{"id":5,"name":"其它活动","children":null,"scoring":false,"show":false}]
     */

    private boolean success;
    private String msg;
    private List<ChildrenBean> data;

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

    public List<ChildrenBean> getData() {
        return data;
    }

    public void setData(List<ChildrenBean> data) {
        this.data = data;
    }

    public static class ChildrenBean implements Serializable {

        @Override
        public String toString() {
            return "ChildrenBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", scoring=" + scoring +
                    ", show=" + show +
                    ", children=" + children +
                    '}';
        }

        /**
         * id : 1
         * name : 桌游棋牌
         * children : [{"id":6,"name":"狼人杀","children":null,"scoring":true,"show":true},{"id":7,"name":"三国杀","children":null,"scoring":true,"show":true},{"id":8,"name":"扑克","children":null,"scoring":true,"show":true},{"id":9,"name":"麻将","children":null,"scoring":true,"show":true},{"id":10,"name":"象棋","children":null,"scoring":true,"show":true},{"id":11,"name":"围棋","children":null,"scoring":true,"show":true}]
         * scoring : false
         * show : false
         */

        private int id;
        private String name;
        private boolean scoring;
        private boolean show;
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

        public boolean isScoring() {
            return scoring;
        }

        public void setScoring(boolean scoring) {
            this.scoring = scoring;
        }

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }
    }
}
