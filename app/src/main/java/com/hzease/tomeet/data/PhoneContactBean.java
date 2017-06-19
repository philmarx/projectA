package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by Key on 2017/6/19 21:07
 * email: MrKey.K@gmail.com
 * description:
 */

public class PhoneContactBean {

    @Override
    public String toString() {
        return "PhoneContactBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : [{"avatarSignature":"1495175242030","nickname":"鹏杰","friend":0,"id":10000000005}]
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "avatarSignature='" + avatarSignature + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", friend=" + friend +
                    ", id=" + id +
                    '}';
        }

        /**
         * avatarSignature : 1495175242030
         * nickname : 鹏杰
         * friend : 0
         * id : 10000000005
         */

        private String avatarSignature;
        private String nickname;
        private int friend;
        private long id;

        public String getAvatarSignature() {
            return avatarSignature;
        }

        public void setAvatarSignature(String avatarSignature) {
            this.avatarSignature = avatarSignature;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getFriend() {
            return friend;
        }

        public void setFriend(int friend) {
            this.friend = friend;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
