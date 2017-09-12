package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/15.
 */

public class BindAccountFragment extends BaseFragment implements ILoginContract.View {

    @BindView(R.id.iv_bindaccout_avatar)
    CircleImageView iv_bindaccout_avatar;
    @BindView(R.id.tv_bindaccount_name)
    TextView tv_bindaccount_name;
    @BindView(R.id.bt_tocreate_accout_fmt)
    TextView bt_tocreate_accout_fmt;
    @BindView(R.id.tv_tobind_accout_fmt)
    TextView tv_tobind_accout_fmt;
    private ILoginContract.Presenter mPresenter;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;
    private String avatarUrl;
    private String nickName;
    private boolean gender;

    public BindAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static BindAccountFragment newInstance() {
        return new BindAccountFragment();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    @OnClick({
            R.id.bt_tocreate_accout_fmt,
            R.id.tv_tobind_accout_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_tocreate_accout_fmt:
                //TODO 去创建一个新的账户 跳转到信息界面
                toCreateAccount();
                break;
            case R.id.tv_tobind_accout_fmt:
                //TODO 去绑定一个之前的账户，跳转到账号密码界面
                toBindAccount();
                break;
        }
    }

    //去绑定老账号
    private void toBindAccount() {
        loginActivity.mFragmentList.get(6).setArguments(getArguments());
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(6));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    //去创建一个新的账号
    private void toCreateAccount() {
        loginActivity.mFragmentList.get(2).setArguments(getArguments());
        Bundle bundle = new Bundle();
        bundle.putString("avatarUrl", avatarUrl);
        bundle.putString("nickName", nickName);
        bundle.putBoolean("gender", gender);
        loginActivity.mFragmentList.get(2).setArguments(bundle);
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(2));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_accout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loginActivity = (LoginActivity) getActivity();
        transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        Bundle bundle = getArguments();
        if (bundle != null) {
            avatarUrl = bundle.getString("avatarUrl", "");
            nickName = bundle.getString("nickName", "");
            gender = bundle.getBoolean("gender");
            String avatarUrl = bundle.getString("avatarUrl", "");
            if (!avatarUrl.isEmpty()) {
                Glide.with(mContext)
                        .load(avatarUrl)
                        .into(iv_bindaccout_avatar);
                tv_bindaccount_name.setText(bundle.getString("nickName", ""));
            }
        }
    }

    //其他fragmnet里的方法
    @Override
    public void loginSuccess(String loginType) {

    }

    @Override
    public void loginFailed(String info) {

    }

    @Override
    public void finishInfo(String loginType) {

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
}
