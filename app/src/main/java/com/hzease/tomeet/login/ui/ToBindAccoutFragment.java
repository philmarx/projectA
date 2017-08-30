package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/15.
 */

public class ToBindAccoutFragment extends BaseFragment implements ILoginContract.View {
    @BindView(R.id.et_phone_number_bind_fmt)
    EditText et_phone_number_bind_fmt;
    @BindView(R.id.et_password_bind_fmt)
    EditText et_password_bind_fmt;
    @BindView(R.id.bt_bind_next_fmt)
    Button bt_bind_next_fmt;

    private ILoginContract.Presenter mPresenter;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;
    private String phoneNumber;

    public ToBindAccoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static ToBindAccoutFragment newInstance() {
        return new ToBindAccoutFragment();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    @OnClick({
            R.id.bt_bind_next_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_bind_next_fmt:
                String phone = et_phone_number_bind_fmt.getText().toString().trim();
                String pwd = et_password_bind_fmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phone)){
                    if (pwd.length() > 5) {
                        PTApplication.getRequestService().mergeAccout(pwd,phone,PTApplication.userToken,PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<LoginBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError" + e.getMessage());
                                        ToastUtils.getToast("网络原因导致初始化失败，请重试");
                                    }

                                    @Override
                                    public void onNext(LoginBean loginBean) {
                                        if (loginBean.isSuccess()){
                                            mPresenter.checkSuccess(loginBean, AppConstants.LOGIN_PHONE);
                                        }else{
                                            ToastUtils.getToast(loginBean.getMsg());
                                        }
                                    }
                                });
                    } else {
                        ToastUtils.getToast("密码不会小于6位哦");
                    }
                }
                break;
        }
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_tobindaccout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public void loginSuccess(String loginType) {
        // 跳转到转进来的页面
        EventBus.getDefault().post(new UserInfoBean());
        getActivity().setResult(AppConstants.YY_PT_LOGIN_SUCCEED);
        getActivity().finish();
        Logger.d("登录成功");
    }

    @Override
    public void loginFailed(String info) {
        ToastUtils.getToast(info);
        getActivity().setResult(AppConstants.YY_PT_LOGIN_FAILED);
    }

    @Override
    public void finishInfo(String loginType) {
        loginActivity.mFragmentList.get(2).setArguments(getArguments());
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(2));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    @Override
    public void checkInitResult(boolean isSuccess, String msg) {

    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void SmsCodeResult(StringDataBean data) {

    }

    @Override
    public void toBindAccout() {
        // 绑定，绑定成功后不可能再走到这个方法了
    }
}
