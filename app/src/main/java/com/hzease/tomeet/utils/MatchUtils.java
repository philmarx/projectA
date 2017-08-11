package com.hzease.tomeet.utils;

import com.hzease.tomeet.PTApplication;

/**
 * Created by Key on 2017/3/20 19:48
 * email: MrKey.K@gmail.com
 * description:
 */

public class MatchUtils {
    /**
     *
     * @param phoneNumber 手机号码
     * @return 检测结果
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        switch (phoneNumber.length()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                ToastUtils.getToast(PTApplication.getInstance(), "手机号码太短");
                return false;
            case 11:
                // 检测正确与否
                String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0-1]|[6-8]))|(18[0-9])|(888))\\d{8}$";
                if (phoneNumber.matches(regex)) {
                    return true;
                }
                ToastUtils.getToast(PTApplication.getInstance(), "手机号码不正确");
                return false;
            default:
                ToastUtils.getToast(PTApplication.getInstance(), "手机号码太长");
                return false;
        }
    }

    /**
     * 检查是否是身份证号码
     */
    public static boolean isIDCard(String IDCard){
        switch (IDCard.length()){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                ToastUtils.getToast(PTApplication.getInstance(), "身份证号码太短");
                return false;
            case 18:
                String regex="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
                if (IDCard.matches(regex)){
                    return true;
                }
                ToastUtils.getToast(PTApplication.getInstance(),"身份证号码格式不正确");
                return  false;
            default:
                ToastUtils.getToast(PTApplication.getInstance(),"身份证号码过长");
                return  false;
        }
    }

    /**
     * 检验姓名
     */
    public static boolean isRealName(String realName){
        switch (realName.length()){
            case 0:
                ToastUtils.getToast(PTApplication.getInstance(),"姓名格式不正确");
                return false;
            default:
                String regex = "^[\\u4e00-\\u9fa5]+$";
                if (realName.matches(regex)){
                    return  true;
                }
                ToastUtils.getToast(PTApplication.getInstance(),"姓名格式不正确");
                return false;
        }
    }
}
