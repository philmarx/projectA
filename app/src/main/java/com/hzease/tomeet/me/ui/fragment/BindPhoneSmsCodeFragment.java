package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.me.ui.MeActivity;
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
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/15.
 */

public class BindPhoneSmsCodeFragment extends BaseFragment {
    private FragmentTransaction transaction;
    private MeActivity meActivity;
    private String phone;
    @BindView(R.id.tv_smscode_cutdown_fmt)
    TextView tv_smscode_cutdown_fmt;
    @BindView(R.id.tv_smscode_phone_fmt)
    TextView tv_smscode_phone_fmt;
    @BindView(R.id.icv_smscode_fmt)
    IdentifyingCodeView icv_smscode_fmt;

    @OnClick(R.id.tv_smscode_cutdown_fmt)
    public void onClick(View v) {
        cutdownTimer();
    }

    public BindPhoneSmsCodeFragment() {
        // Required empty public constructor
    }

    public static BindPhoneSmsCodeFragment newInstance() {
        return new BindPhoneSmsCodeFragment();
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
        tv_smscode_phone_fmt.setText("验证码已发送至+86 " + phone);
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        cutdownTimer();
        icv_smscode_fmt.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                final String smsCode = icv_smscode_fmt.getTextContent();
                if (smsCode.length() == 6) {
                    PTApplication.getRequestService().bindPhone(phone, smsCode, PTApplication.userToken, PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
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
                                        EventBus.getDefault().post(phone);
                                        ToastUtils.getToast("绑定成功！");
                                        meActivity.getSupportFragmentManager().popBackStack();
                                    } else {
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });
                }
            }

            @Override
            public void deleteContent() {

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
}
