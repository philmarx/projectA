package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateBean {
    /**
     * success : true
     * msg :
     * data : [{"id":10000000025,"nickname":"马健原","avatarSignature":"1494220981231","labels":["x"]}]
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
         * id : 10000000025
         * nickname : 马健原
         * avatarSignature : 1494220981231
         * labels : ["x"]
         */

        private long id;
        private String nickname;
        private String avatarSignature;
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

        public List<String> getLabels() {
            return labels;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }
    }
}
