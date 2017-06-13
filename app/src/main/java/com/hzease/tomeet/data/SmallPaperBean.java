package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/13.
 */

public class SmallPaperBean {
    /**
     * success : true
     * msg :
     * data : [{"avatarSignature":"1495160353657","senderId":10000000001,"nickname":"我是新修改的昵称","id":10,"state":0,"content":"n你好啊啊啊啊啊"},{"avatarSignature":"1496989201921","senderId":10000000141,"nickname":"得得得得","id":9,"state":0,"content":"哈哈哈哈哈哈"}]
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
         * avatarSignature : 1495160353657
         * senderId : 10000000001
         * nickname : 我是新修改的昵称
         * id : 10
         * state : 0
         * content : n你好啊啊啊啊啊
         */

        private String avatarSignature;
        private long senderId;
        private String nickname;
        private int id;
        private int state;
        private String content;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public long getSenderId() {
            return senderId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
