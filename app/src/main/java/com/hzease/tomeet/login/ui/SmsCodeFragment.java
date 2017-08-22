package com.hzease.tomeet.login.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.CountDownButtonHelper;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.IdentifyingCodeView;
import com.orhanobut.logger.Logger;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/15.
 */

public class SmsCodeFragment extends BaseFragment implements ILoginContract.View {
    private ILoginContract.Presenter mPresenter;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;
    private String phone;
    @BindView(R.id.tv_smscode_cutdown_fmt)
    TextView tv_smscode_cutdown_fmt;
    @BindView(R.id.tv_smscode_phone_fmt)
    TextView tv_smscode_phone_fmt;
    @BindView(R.id.icv_smscode_fmt)
    IdentifyingCodeView icv_smscode_fmt;
    @BindView(R.id.bt_smscode_next_fmt)
    Button bt_smscode_next_fmt;
    private boolean isfindpwd;
    private String smsCode;


    @OnClick({R.id.tv_smscode_cutdown_fmt,
            R.id.bt_smscode_next_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_smscode_cutdown_fmt:
                cutdownTimer();
                break;
            case R.id.bt_smscode_next_fmt:
                if (isfindpwd) {
                    PTApplication.getRequestService().validateSmsCode(phone, smsCode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    showLoadingDialog();
                                }
                            })
                            .doAfterTerminate(new Action0() {
                                @Override
                                public void call() {
                                    hideLoadingDialog();
                                }
                            })
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    if (noDataBean.isSuccess()) {
                                        bt_smscode_next_fmt.setEnabled(true);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("phone", phone);
                                        bundle1.putString("smscode", smsCode);
                                        loginActivity.mFragmentList.get(7).setArguments(bundle1);
                                        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(7));
                                        transaction.addToBackStack(null);
                                        // 执行事务
                                        transaction.commit();
                                    } else {
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });
                } else {
                    Logger.e("true");
                    mPresenter.smsCodeSignIn(phone, icv_smscode_fmt.getTextContent());
                }
                break;
        }

    }

    public SmsCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static SmsCodeFragment newInstance() {
        return new SmsCodeFragment();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_inputsmscode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(icv_smscode_fmt, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300); //设置300毫秒的时长
        final Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        isfindpwd = bundle.getBoolean("isfindpwd", false);
        tv_smscode_phone_fmt.setText("验证码已发送至+86 " + phone);
        loginActivity = (LoginActivity) getActivity();
        transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        cutdownTimer();
        icv_smscode_fmt.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                smsCode = icv_smscode_fmt.getTextContent();
                if (smsCode.length() == 6) {
                    bt_smscode_next_fmt.setEnabled(true);
                    if (isfindpwd) {
                        PTApplication.getRequestService().validateSmsCode(phone, smsCode)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        showLoadingDialog();
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        hideLoadingDialog();
                                    }
                                })
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("phone", phone);
                                            bundle1.putString("smscode", smsCode);
                                            loginActivity.mFragmentList.get(7).setArguments(bundle1);
                                            transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(7));
                                            transaction.addToBackStack(null);
                                            // 执行事务
                                            transaction.commit();
                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                    } else {
                        Logger.e("true");
                        mPresenter.smsCodeSignIn(phone, icv_smscode_fmt.getTextContent());
                    }
                }
            }

            @Override
            public void deleteContent() {
                bt_smscode_next_fmt.setEnabled(false);
            }
        });
    }

    private void cutdownTimer() {
        CountDownButtonHelper helper = new CountDownButtonHelper(tv_smscode_cutdown_fmt, "秒后可重新获取", 60, 1);
        tv_smscode_cutdown_fmt.setEnabled(false);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                tv_smscode_cutdown_fmt.setText("重新发送");
                tv_smscode_cutdown_fmt.setEnabled(true);
                tv_smscode_cutdown_fmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PTApplication.getRequestService().getSMSCode(phone)
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
                                        if (stringDataBean.isSuccess()) {
                                            ToastUtils.getToast("发送验证码成功");
                                        } else {
                                            ToastUtils.getToast(stringDataBean.getMsg());
                                        }
                                    }
                                });
                    }
                });
            }
        });
        helper.start();
    }

    //其他fragment的方法
    @Override
    public void loginSuccess() {
        //changeLoadView(false);
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
    public void finishInfo() {
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(2));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    @Override
    public void checkInitResult(boolean isSuccess, String msg) {
        Logger.i("LoginFragment.view:  " + isSuccess + "  --  (" + msg + ")" + "\nflags:  " + getActivity().getIntent().getFlags());
        if (isSuccess) {
            EventBus.getDefault().post(new UserInfoBean());
            getActivity().finish();
        } else {
            ToastUtils.getToast(msg);
        }
    }

    @Override
    public void showLoadingDialog() {
        ((LoginActivity) getActivity()).showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        ((LoginActivity) getActivity()).hideLoadingDialog();
    }

    @Override
    public void SmsCodeResult(StringDataBean data) {

    }

    @Override
    public void toBindAccout() {

    }
}
