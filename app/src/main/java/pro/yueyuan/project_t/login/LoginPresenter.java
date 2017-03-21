package pro.yueyuan.project_t.login;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.data.source.PTRepository;
import rx.Observer;
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

        // TODO: 成功的话,把token和id存到application中,销毁时自动存到SP中

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

        // TODO: 成功的话,把token和id存到application中,销毁时自动存到SP中
    }
}
