package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/9/8.
 */

public class ThreePartBean {
    /**
     * success : true
     * msg :
     * data : {"photoUrl":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKav1ib8qG43xzbQALQ8oAkMsDzrhNXXtGsia6Fz8BLlgOOPbicpPkQqOMic1ZpT6XbgW1LorvHm0iabgw/0","nickname":"得得得得"}
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
         * photoUrl : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKav1ib8qG43xzbQALQ8oAkMsDzrhNXXtGsia6Fz8BLlgOOPbicpPkQqOMic1ZpT6XbgW1LorvHm0iabgw/0
         * nickname : 得得得得
         */

        private String photoUrl;
        private String nickname;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
