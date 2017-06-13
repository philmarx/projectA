package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositBean {

    /**
     * success : true
     * msg :
     * data : [{"amount":"1","out_trade_no":"10000000000a1497320107014","trade_no":"4001272001201706135505748676","id":8111111111111111,"time":1497320117000,"type":"weixin","refundAmount":"0"},{"amount":"100","out_trade_no":"10000000000a1496903302107","trade_no":"2017060821001004450236401751","id":78,"time":1496903309000,"type":"alipay","refundAmount":"0"}]
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
         * amount : 1
         * out_trade_no : 10000000000a1497320107014
         * trade_no : 4001272001201706135505748676
         * id : 8111111111111111
         * time : 1497320117000
         * type : weixin
         * refundAmount : 0
         */

        private String amount;
        private String out_trade_no;
        private String trade_no;
        private long id;
        private long time;
        private String type;
        private String refundAmount;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(String refundAmount) {
            this.refundAmount = refundAmount;
        }
    }
}
