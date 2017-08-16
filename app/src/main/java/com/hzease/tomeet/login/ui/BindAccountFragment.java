package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.Circle;
import com.bumptech.glide.Glide;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.widget.CircleImageView;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/15.
 */

public class BindAccountFragment extends BaseFragment implements ILoginContract.View {

    @BindView(R.id.iv_bindaccout_avatar)
    CircleImageView iv_bindaccout_avatar;
    @BindView(R.id.tv_bindaccount_name)
    TextView tv_bindaccount_name;
    Unbinder unbinder;
    @BindView(R.id.bt_tocreate_accout_fmt)
    Button bt_tocreate_accout_fmt;
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
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(6));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    //去创建一个新的账号
    private void toCreateAccount() {
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
            if (!avatarUrl.isEmpty()) {
                try {
                    URL url = new URL(avatarUrl);
                    Glide.with(mContext)
                            .load(url)
                            .into(iv_bindaccout_avatar);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                tv_bindaccount_name.setText(nickName);
            }
        }
    }

    //其他fragmnet里的方法
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
