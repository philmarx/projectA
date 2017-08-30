package com.hzease.tomeet.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/7/28.
 */

public class ChangeNewPwdFragment extends BaseFragment implements ILoginContract.View {
    private ILoginContract.Presenter mPresenter;
    @BindView(R.id.et_newpwd_setpwd_fmt)
    EditText et_newpwd_setpwd_fmt;
    private String phone;
    private String smscode;

    public ChangeNewPwdFragment() {
        // Required empty public constructor
    }

    public static ChangeNewPwdFragment newInstance() {
        return new ChangeNewPwdFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @OnClick(R.id.bt_setpwd_finsh_fmt)
    public void onClick(View v) {
        Logger.e(smscode);
        String pwd = et_newpwd_setpwd_fmt.getText().toString().trim();
        if (pwd.length() >= 6 && pwd.length() <= 12) {
            PTApplication.getRequestService().forgetPwd(pwd, phone, smscode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NoDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError" + e.getMessage());
                        }

                        @Override
                        public void onNext(NoDataBean noDataBean) {
                            if (noDataBean.isSuccess()) {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                ToastUtils.getToast("修改成功");
                                getActivity().finish();
                            } else {
                                ToastUtils.getToast(noDataBean.getMsg());
                            }
                        }
                    });
        } else {
            ToastUtils.getToast("密码长度不正确");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_setnewpwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        smscode = bundle.getString("smscode");
        Logger.e("phone" + phone);
        Logger.e("smsCode" + smscode);
    }

    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

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
