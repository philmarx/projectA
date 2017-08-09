package com.hzease.tomeet.login;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.StringDataBean;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface ILoginContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 获取验证码后开始倒计时60秒
         * from: getSmsCode
         * @param stringDataBean 成功或失败,含原因
         */
        void smsCodeCountdown(StringDataBean stringDataBean);

        /**
         * 登录成功
         * from: smsCodeSignIn
         * from: phonePasswordSignIn
         */
        void loginSuccess();

        /**
         * 登录失败.返回原因
         * from: smsCodeSignIn
         * from: phonePasswordSignIn
         * @param info 失败原因
         */
        void loginFailed(String info);

        /**
         * 如果是新用户 需要完善用户信息
         */
        void finishInfo();

        /**
         * 检查初始化返回值，比如昵称重复之类的
         * 完善信息界面专用
         * {@link com.hzease.tomeet.login.ui.FinishInfoFragment}
         */
        void checkInitResult(boolean isSuccess, String msg);

        void showLoadingDialog();

        void hideLoadingDialog();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 获取手机验证码
         * to: smsCodeCountdown
         * @param phoneNumber 手机号码
         */
        void getSmsCode(String phoneNumber);

        /**
         * 判断验证码
         * to: loginSuccess
         * to: loginFailed
         * @param phoneNumber 手机号码
         * @param smsCode 验证码
         */
        void smsCodeSignIn(String phoneNumber, String smsCode);

        /**
         * 判断手机密码
         * to: loginSuccess
         * to: loginFailed
         * @param phoneNumber 手机号码
         * @param password 密码
         */
        void phonePasswordSignIn(String phoneNumber, String password);

        /**
         * 保存id token
         */
        void saveUserIdAndToken();

        /**
         * 完善新用户的信息
         */
        void finishInfo(String birthday,boolean gender, String nickName, String password);

        /**
         * 登录成功，保存信息，并上传友盟
         * @param loginBean 登录成功返回的bean
         * @param loginType 登录渠道
         */
        void checkSuccess(LoginBean loginBean, String loginType);

        /**
         * 第三方登录
         */
        void authLogin(String type,String uid);
    }
}
