package com.hzease.tomeet.data;

import java.util.List;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositBean {
    /**
     * success : true
     * msg :
     * data : [{"amount":"0.01","out_trade_no":"10000000003a1496644217145","trade_no":"2017060521001004610230966314","id":10,"time":1496644225000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496644385356","trade_no":"2017060521001004610230991305","id":11,"time":1496644393000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496644444663","trade_no":"2017060521001004610230972681","id":12,"time":1496644451000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496645033831","trade_no":"2017060521001004610230967118","id":14,"time":1496645042000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496645933386","trade_no":"2017060521001004610231016391","id":15,"time":1496645950000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496646246970","trade_no":"2017060521001004610231021622","id":16,"time":1496646253000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496646526878","trade_no":"2017060521001004610231035860","id":17,"time":1496646533000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496649036952","trade_no":"2017060521001004610231110650","id":18,"time":1496649044000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496649076597","trade_no":"2017060521001004610231084789","id":19,"time":1496649082000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496649435505","trade_no":"2017060521001004610231095222","id":20,"time":1496649442000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496650172376","trade_no":"2017060521001004610231116768","id":22,"time":1496650179000,"refundAmount":"0"},{"amount":"0.01","out_trade_no":"10000000003a1496650198171","trade_no":"2017060521001004610231111686","id":23,"time":1496650205000,"refundAmount":"0"}]
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
         * amount : 0.01
         * out_trade_no : 10000000003a1496644217145
         * trade_no : 2017060521001004610230966314
         * id : 10
         * time : 1496644225000
         * refundAmount : 0
         */

        private String amount;
        private String out_trade_no;
        private String trade_no;
        private int id;
        private long time;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(String refundAmount) {
            this.refundAmount = refundAmount;
        }
    }
}
