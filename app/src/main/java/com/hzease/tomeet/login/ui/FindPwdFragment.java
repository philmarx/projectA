package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/21.
 */

public class FindPwdFragment extends BaseFragment implements ILoginContract.View {

    @BindView(R.id.et_phone_number_findpwd_fmt)
    EditText et_phone_number_findpwd_fmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;

    LoginActivity activity;
    public FindPwdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static FindPwdFragment newInstance() {
        return new FindPwdFragment();
    }


    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
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

    /**
     * @deprecated 完善信息界面专用
     */
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
        return R.layout.fragment_forgetpwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activity = (LoginActivity) getActivity();
    }

    @OnClick({
            R.id.bt_findpwd_next_fmt
    })
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_findpwd_next_fmt:
                final String phoneNumber = et_phone_number_findpwd_fmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNumber)) {
                    PTApplication.getRequestService().getSMSCode(phoneNumber)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<StringDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("onError" + e.getMessage());
                                }

                                @Override
                                public void onNext(StringDataBean stringDataBean) {
                                    if (stringDataBean.isSuccess()){
                                        //设置新密码
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        // 将 fragment_container View 中的内容替换为此 Fragment ，
                                        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone",phoneNumber);
                                        bundle.putBoolean("isfindpwd",true);
                                        activity.mFragmentList.get(4).setArguments(bundle);
                                        transaction.replace(R.id.fl_content_login_activity,activity.mFragmentList.get(4));
                                        transaction.addToBackStack(null);
                                        // 执行事务
                                        transaction.commit();
                                    }else{
                                        ToastUtils.getToast(stringDataBean.getMsg());
                                    }
                                }
                            });
                }

                break;
        }
    }
}
