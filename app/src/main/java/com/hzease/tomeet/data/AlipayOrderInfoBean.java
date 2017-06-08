package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/6/2 13:12
 * email: MrKey.K@gmail.com
 * description:
 */

public class AlipayOrderInfoBean {

    @Override
    public String toString() {
        return "AlipayOrderInfoBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    /**
     * success : true
     * msg :
     * data : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017042506958849&biz_content=%7B%22body%22%3A%22%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%2214963802639863fd18620-c236-4c83-9dca-828b50b77285%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22Key%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Ftomeet-app.hzease.com%2Fbeta%2Falipay%2Fcall&sign=dH2hP5FQD9GHHijvQqMcR4Yh5V%2FYRzBReSdv%2Bky%2BVI3l29icbRXpg49zO1Tp3aSdWSSX3HajgKNVz1oRFqXoNRahvl1xO9LTJ8yMei72O0nCB8FcwY1ONEdJqqVgOVckB7fXT0nv%2FYdWBuUfiMu94j58Pif24CGwb76B9t28q1OaE%2Bw7DEGMPQ49zHh%2FhXoJgbiB8fnrF5heHkoM76lb8qmJQXc5IH74LkmRFcJQ1HSxUertgoBFlsYGsqBdiNl4KgKHNeoQd5s2SG%2FnrOt4wHHrFV4nXSzDZyqjiBaULKwSUHIzGOXk1nI5cuFatRIFA1qnlJpobSIOt7sif%2FttZA%3D%3D&sign_type=RSA2&timestamp=2017-06-02+13%3A11%3A04&version=1.0
     */

    private boolean success;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
