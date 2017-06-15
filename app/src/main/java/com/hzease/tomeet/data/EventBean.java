package com.hzease.tomeet.data;

/**
 * Created by Key on 2017/5/31 14:58
 * email: MrKey.K@gmail.com
 * description: EventBus 发送用
 */

public class EventBean {

    /**
     * 登录失效
     */
    public static class LoginInvalid {}

    /**
     * 刷新用户信息
     */
    public static class reflushInfomation {}

    /**
     * 充值完成界面，返回我的钱包
     */
    public static class goOnRecharge {}
}
