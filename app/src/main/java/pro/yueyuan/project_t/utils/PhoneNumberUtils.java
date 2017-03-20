package pro.yueyuan.project_t.utils;

import pro.yueyuan.project_t.PTApplication;

/**
 * Created by Key on 2017/3/20 19:48
 * email: MrKey.K@gmail.com
 * description:
 */

public class PhoneNumberUtils {
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
                String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0-1]|[6-8]))|(18[0-9]))\\d{8}$";
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
}
