package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/6/8 14:09
 * email: MrKey.K@gmail.com
 * description:
 */

public class WxpayOrderInfoBean {

    /**
     * success : true
     * msg :
     * data : {"timeStamp":"1497256922","sign":"BEDE157927D85F523681F32118F49D97","prepayId":"wx2017061216420210c9a9d7fb0090377862","nonceStr":"1e9d9a2219eb462d9744ae08b5bee4a1"}
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
         * timeStamp : 1497256922
         * sign : BEDE157927D85F523681F32118F49D97
         * prepayId : wx2017061216420210c9a9d7fb0090377862
         * nonceStr : 1e9d9a2219eb462d9744ae08b5bee4a1
         */

        private String timeStamp;
        private String sign;
        private String prepayId;
        private String nonceStr;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }
    }
}
