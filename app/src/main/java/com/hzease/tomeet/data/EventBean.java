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

    /**
     * 清除小红点
     */
    public static class clearUnreadRedBadge {}

    /**
     * 系统未读+1
     */
    public static class systemUnreadAddOne {}

    /**
     * 更新检查完成
     */
    public static class updateCheckFinish {}
}
