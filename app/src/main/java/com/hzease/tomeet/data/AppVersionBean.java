package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/8/10 18:48
 * email: MrKey.K@gmail.com
 * description:
 */

public class AppVersionBean {

    @Override
    public String toString() {
        return "AppVersionBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

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

        @Override
        public String toString() {
            return "DataBean{" +
                    "version='" + version + '\'' +
                    ", downUrl='" + downUrl + '\'' +
                    ", message='" + message + '\'' +
                    ", force=" + force +
                    '}';
        }

        private String version;
        private String downUrl;
        private String message;
        private boolean force;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }
    }
}
