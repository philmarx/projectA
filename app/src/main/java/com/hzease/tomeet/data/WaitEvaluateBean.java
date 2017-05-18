package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateBean {

    /**
     * success : true
     * msg :
     * data : [{"id":10000000001,"nickname":"😏😏😏","avatarSignature":"1493193505823","labels":["yaaaaaaaaaaa","daaaaaaa","zaaaaaaaaaa","caaaaaaa","iaaaaa"],"point":5},{"id":10000000025,"nickname":"马健原","avatarSignature":"1494220981231","labels":["daaaaaaa","gaaaaaaaaaaa","iaaaaa","qaaaaaaaaaa","oaaaaa"],"point":0}]
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
         * id : 10000000001
         * nickname : 😏😏😏
         * avatarSignature : 1493193505823
         * labels : ["yaaaaaaaaaaa","daaaaaaa","zaaaaaaaaaa","caaaaaaa","iaaaaa"]
         * point : 5
         */

        private long id;
        private String nickname;
        private String avatarSignature;
        private int point;
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

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", nickname='" + nickname + '\'' +
                    ", avatarSignature='" + avatarSignature + '\'' +
                    ", point=" + point +
                    ", labels=" + labels +
                    '}';
        }
    }
}
