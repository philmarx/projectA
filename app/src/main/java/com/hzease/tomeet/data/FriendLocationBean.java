package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/8/22.
 */

public class FriendLocationBean {
    /**
     * success : true
     * msg :
     * data : [{"userId":10000000001,"nickname":"卧床不起","avatarSignature":"1503049492208","longitude":120.075847,"latitude":30.317202},{"userId":10000000003,"nickname":"徐强","avatarSignature":"1503053177600","longitude":120.076047,"latitude":30.316722},{"userId":10000000006,"nickname":"普通村民","avatarSignature":"1502771090525","longitude":120.076229,"latitude":30.317237},{"userId":10000000010,"nickname":"丸子","avatarSignature":"1501235212267","longitude":120.076078,"latitude":30.316695},{"userId":10000000016,"nickname":"Jjun","avatarSignature":"1502174873495","longitude":120.073689,"latitude":30.314691},{"userId":10000000024,"nickname":"扎心","avatarSignature":"1502447526746","longitude":120.075548,"latitude":30.316733},{"userId":10000001610,"nickname":"恩","avatarSignature":"","longitude":118.698772,"latitude":32.161837}]
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
         * userId : 10000000001
         * nickname : 卧床不起
         * avatarSignature : 1503049492208
         * longitude : 120.075847
         * latitude : 30.317202
         */

        private long userId;
        private String nickname;
        private String avatarSignature;
        private double longitude;
        private double latitude;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
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

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
