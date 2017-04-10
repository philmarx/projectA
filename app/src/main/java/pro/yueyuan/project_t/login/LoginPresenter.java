package pro.yueyuan.project_t.login;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.FinishInfoBean;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.data.source.Login4SmsBean;
import pro.yueyuan.project_t.data.source.PTRepository;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
                        Logger.e("UserInfoBean:" + stringDataBean.isSuccess() + "  msg: " + stringDataBean.getMsg() + "  data: " + stringDataBean.getData());
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
                .subscribe(new Subscriber<Login4SmsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Login4SmsBean login4SmsBean) {
                        if (login4SmsBean.isSuccess()) {
                            PTApplication.userId = login4SmsBean.getData().getId();
                            PTApplication.userToken = login4SmsBean.getData().getToken();
                            Logger.d(login4SmsBean.isSuccess() + "id:" + String.valueOf(PTApplication.userId) + "token:" + PTApplication.userToken);
                            mLoginView.checkSuccess();
                            if(login4SmsBean.getData().isIsInit()){
                                //如果初始化过，说明不是新用户，直接跳转到到进来的页面就可以
                                mLoginView.loginSuccess();
                            }else{
                                mLoginView.finishInfo();
                            }
                        } else {
                            // TODO 登录失败
                        }
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
                .subscribe(new Subscriber<UserInfoBean>() {
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
                    public void onNext(UserInfoBean userInfoBean) {
                        Logger.d(userInfoBean.isSuccess());
                        if (userInfoBean.isSuccess()) {
                            PTApplication.userId = userInfoBean.getData().getId();
                            PTApplication.userToken = userInfoBean.getData().getToken();
                            Logger.i(PTApplication.userId + "---" + PTApplication.userToken);
                            mLoginView.loginSuccess();
                        } else {
                            Logger.e(userInfoBean.getData().getError());
                            mLoginView.loginFailed(userInfoBean.getMsg());
                        }
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
     * 完善用户信息
     */
    @Override
    public void finishInfo(Integer age,boolean gender,String nickname,String password,String place,String token,String userId) {
        PTApplication.getRequestService().finishInfo(age,gender,nickname,password,place,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FinishInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(FinishInfoBean finishInfoBean) {
                        if (finishInfoBean.isSuccess()){
                            mLoginView.registerSuccess();
                        }
                    }
                });
    }
}
