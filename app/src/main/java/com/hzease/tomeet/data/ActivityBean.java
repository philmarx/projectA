package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/8/4.
 */

public class ActivityBean {
    /**
     * success : true
     * msg :
     * data : [{"id":1,"name":"八月女神","url":"https://hzease.com/activity/index.html","photoUrl":"https://hzease.com/activity/images/banner.png","beginTime":1502035200000,"endTime":1503244799000,"enable":true}]
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
         * id : 1
         * name : 八月女神
         * url : https://hzease.com/activity/index.html
         * photoUrl : https://hzease.com/activity/images/banner.png
         * beginTime : 1502035200000
         * endTime : 1503244799000
         * enable : true
         */

        private int id;
        private String name;
        private String url;
        private String photoUrl;
        private String shareUrl;
        private long beginTime;
        private long endTime;
        private boolean enable;

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
