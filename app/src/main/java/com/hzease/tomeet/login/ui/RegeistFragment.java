package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/15.
 */

public class RegeistFragment extends BaseFragment implements ILoginContract.View {
    @BindView(R.id.et_regeist_phoen_fmt)
    EditText et_regeist_phoen_fmt;
    @BindView(R.id.bt_regeist_next_fmt)
    Button bt_regeist_next_fmt;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;
    private ILoginContract.Presenter mPresenter;
    private String phoneNumber;

    public RegeistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static RegeistFragment newInstance() {
        return new RegeistFragment();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick(R.id.bt_regeist_next_fmt)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regeist_next_fmt:
                phoneNumber = et_regeist_phoen_fmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNumber)) {
                    mPresenter.getSmsCode(phoneNumber);
                }
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_regeist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        loginActivity = (LoginActivity) getActivity();
        transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        et_regeist_phoen_fmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_regeist_phoen_fmt.getText().toString().trim().length() ==11){
                    bt_regeist_next_fmt.setEnabled(true);
                }else{
                    bt_regeist_next_fmt.setEnabled(false);
                }
            }
        });
    }


    //其他fragment的方法
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
        if (data.isSuccess()) {
            Bundle bundle = new Bundle();
            bundle.putString("phone",phoneNumber);
            loginActivity.mFragmentList.get(4).setArguments(bundle);
            transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(4));
            transaction.addToBackStack(null);
            // 执行事务
            transaction.commit();
        } else {
            ToastUtils.getToast(mContext, data.getMsg());
        }
    }

    @Override
    public void toBindAccout() {

    }
}
