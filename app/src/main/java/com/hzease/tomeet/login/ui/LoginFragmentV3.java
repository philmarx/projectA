package com.hzease.tomeet.login.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/28.
 */

public class LoginFragmentV3 extends BaseFragment implements ILoginContract.View {
    @BindView(R.id.et_phone_number_login_fmt)
    EditText et_phone_number_login_fmt;
    @BindView(R.id.et_password_login_fmt)
    EditText et_password_login_fmt;
    @BindView(R.id.tv_login4sms_login_fmt)
    TextView tv_login4sms_login_fmt;
    @BindView(R.id.tv_regesist_account_fmt)
    TextView tv_regesist_account_fmt;
    private ILoginContract.Presenter mPresenter;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;

    public static LoginFragmentV3 newInstance(){
        return new LoginFragmentV3();
    }
    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFailed(String info) {

    }

    @Override
    public void finishInfo() {

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

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_loginv3;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loginActivity = (LoginActivity) getActivity();
        transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        tv_regesist_account_fmt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
