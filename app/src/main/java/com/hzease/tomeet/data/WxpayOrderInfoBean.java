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
     * data : {"nonce_str":"szzINfyJ2JC3yefC","time_stamp":"1496974596","appid":"wxc9ba3b479546a874","sign":"23D9AE917C907703B3F98547E6B4DF59","trade_type":"APP","return_msg":"OK","result_code":"SUCCESS","mch_id":"1480291952","return_code":"SUCCESS","prepay_id":"wx20170609101636d2e0ca88200033075198"}
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
         * nonce_str : szzINfyJ2JC3yefC
         * time_stamp : 1496974596
         * appid : wxc9ba3b479546a874
         * sign : 23D9AE917C907703B3F98547E6B4DF59
         * trade_type : APP
         * return_msg : OK
         * result_code : SUCCESS
         * mch_id : 1480291952
         * return_code : SUCCESS
         * prepay_id : wx20170609101636d2e0ca88200033075198
         */

        private String nonce_str;
        private String time_stamp;
        private String appid;
        private String sign;
        private String trade_type;
        private String return_msg;
        private String result_code;
        private String mch_id;
        private String return_code;
        private String prepay_id;

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }
    }
}
