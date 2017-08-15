package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.newUserBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
    Unbinder unbinder;

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
                                .subscribe(new Subscriber<newUserBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError" + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(newUserBean newuserBean) {
                                        if (newuserBean.isSuccess()){
                                            EventBus.getDefault().post(new UserInfoBean());
                                            getActivity().setResult(AppConstants.YY_PT_LOGIN_SUCCEED);
                                            getActivity().finish();
                                            PTApplication.userId = newuserBean.getData().getUserId();
                                            PTApplication.userToken = newuserBean.getData().getToken();
                                            //TODO 重新获取一遍个人信息
                                            getActivity().finish();
                                        }else{
                                            ToastUtils.getToast(mContext,newuserBean.getMsg());
                                        }
                                    }
                                });
                    } else {
                        ToastUtils.getToast(getContext(), "密码不会小于6位哦");
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
}
