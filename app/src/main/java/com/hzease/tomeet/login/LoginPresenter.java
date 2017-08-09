package com.hzease.tomeet.login;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.utils.RongCloudInitUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class LoginPresenter implements ILoginContract.Presenter {

    private final PTRepository mPTRepository;

    private final ILoginContract.View mLoginView;

    @Inject
    public LoginPresenter(PTRepository mPTRepository, ILoginContract.View mLoginView) {
        this.mPTRepository = mPTRepository;
        this.mLoginView = mLoginView;
    }

    @Inject
    void setupListeners() {
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNumber 手机号码
     */
    @Override
    public void getSmsCode(String phoneNumber) {
        PTApplication.getRequestService().getSMSCode(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StringDataBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 也是获取失败
                        mLoginView.smsCodeCountdown(new StringDataBean(false, "网络错误获取失败", ""));
                        Logger.e("getSmsCode - onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(StringDataBean stringDataBean) {
                        Logger.e("LoginBean:" + stringDataBean.isSuccess() + "  msg: " + stringDataBean.getMsg() + "  data: " + stringDataBean.getData());
                        mLoginView.smsCodeCountdown(stringDataBean);
                    }
                });
    }

    /**
     * 判断验证码
     * to: loginSuccess
     * to: loginFailed
     *
     * @param phoneNumber 手机号码
     * @param smsCode     验证码
     */
    @Override
    public void smsCodeSignIn(String phoneNumber, String smsCode) {
        // TODO 通过服务器接口判断success, 成功走loginSuccess,失败走loginFailed
        PTApplication.getRequestService().login4sms(phoneNumber,smsCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoginView.loginFailed("网络连接失败，请重试");
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        checkSuccess(loginBean, AppConstants.LOGIN_PHONE);
                    }
                });
    }

    /**
     * 判断手机密码
     * to: loginSuccess
     * to: loginFailed
     *
     * @param phoneNumber 手机号码
     * @param password    密码
     */
    @Override
    public void phonePasswordSignIn(String phoneNumber, String password) {
        // TODO 通过服务器接口判断success, 成功走loginSuccess,失败走loginFailed
        PTApplication.getRequestService().login(phoneNumber, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mLoginView.hideLoadingDialog();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mLoginView.showLoadingDialog();
                    }
                })
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                        //
                    }
                    @Override
                    public void onError(Throwable e) {
                        // 网络失败也是登录失败
                        Logger.e(e.getMessage());
                        mLoginView.loginFailed("网络连接失败，请重试");
                    }
                    @Override
                    public void onNext(LoginBean loginBean) {
                        checkSuccess(loginBean, AppConstants.LOGIN_PHONE);
                    }
                });
    }

    /**
     * 保存id token
     */
    @Override
    public void saveUserIdAndToken() {
        mPTRepository.saveUserIdAndToken();
    }

    /**
     * 完善用户信息,初始化
     */
    @Override
    public void finishInfo(String birthday,boolean gender, String nickName, String password) {
        PTApplication.getRequestService().finishInfo(birthday,gender, nickName, password, PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mLoginView.hideLoadingDialog();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mLoginView.showLoadingDialog();
                    }
                })
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mLoginView.checkInitResult(false, "初始化失败，请检查网络是否通畅");
                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if (userInfoBean.isSuccess()) {
                            PTApplication.myInfomation = userInfoBean;
                            mLoginView.checkInitResult(true, "");
                        } else {
                            Logger.e("finishInfo(初始化返回): " + userInfoBean.getMsg());
                            mLoginView.checkInitResult(false, userInfoBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 验证成功,保存用户名密码
     */
    @Override
    public void checkSuccess(final LoginBean loginBean, final String loginType) {
        Logger.e(loginBean.toString());
        if (loginBean.isSuccess()) {
            PTApplication.userId = loginBean.getData().getId();
            PTApplication.userToken = loginBean.getData().getToken();

            // 登录成功后去获取个人信息bean
            PTApplication.getRequestService().getMyInfomation(PTApplication.userToken, PTApplication.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            // 关闭转圈
                            mLoginView.hideLoadingDialog();
                        }
                    })
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            // 转圈
                            mLoginView.showLoadingDialog();
                        }
                    })
                    .subscribe(new Subscriber<UserInfoBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                            ToastUtils.getToast(PTApplication.getInstance(), "加载用户信息失败，请检查网络");
                        }

                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean.isSuccess()) {
                                PTApplication.myInfomation = userInfoBean;
                                PTApplication.myLoadingStatus = AppConstants.YY_PT_LOGIN_SUCCEED;

                                // 拿到个人信息后再跳转，则可以确认token是否有效
                                if(userInfoBean.getData().isIsInit()){
                                    //如果初始化过，说明不是新用户，直接跳转到到进来的页面就可以
                                    mLoginView.loginSuccess();
                                }else{
                                    switch(loginType) {
                                        case AppConstants.LOGIN_PHONE:
                                            // 友盟登录方式统计(自有帐号)
                                            MobclickAgent.onProfileSignIn(PTApplication.userId);
                                            mLoginView.finishInfo();
                                            break;
                                        // default 为除了手机号以为的其他所有渠道
                                        default:
                                            // 友盟登录方式统计(第三方)
                                            MobclickAgent.onProfileSignIn(loginType, PTApplication.userId);
                                            mLoginView.finishInfo();
                                            break;
                                    }
                                }

                                // 登录成功,保存用户id token
                                saveUserIdAndToken();

                                // 融云初始化
                                new RongCloudInitUtils().RongCloudInit();
                            } else {
                                ToastUtils.getToast(PTApplication.getInstance(), userInfoBean.getMsg());
                            }
                        }
                    });
        } else {
            mLoginView.loginFailed(loginBean.getMsg());
        }
    }

    @Override
    public void authLogin(final String type, String uid) {
        PTApplication.getRequestService().authLogin(type,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mLoginView.hideLoadingDialog();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mLoginView.showLoadingDialog();
                    }
                })
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }
                    @Override
                    public void onNext(LoginBean loginBean) {
                        checkSuccess(loginBean, type);
                    }
                });
    }
}
