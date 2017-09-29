package com.hzease.tomeet.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuq on 2017/9/20.
 */

public class HomeActivityBean {
    /**
     * success : true
     * msg :
     * data : [{"id":1,"url":"https://hzease.com/activity9/planetRise.html","message":"星球崛起3","photoUrl":"https://hzease.com/ads/xqjq.png","expiryDate":"2017-10-01","force":true}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * url : https://hzease.com/activity9/planetRise.html
         * message : 星球崛起3
         * photoUrl : https://hzease.com/ads/xqjq.png
         * expiryDate : 2017-10-01
         * force : true
         */

        private int id;
        private String url;
        private String message;
        private String photoUrl;
        private String expiryDate;
        private boolean force;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }
    }
}
