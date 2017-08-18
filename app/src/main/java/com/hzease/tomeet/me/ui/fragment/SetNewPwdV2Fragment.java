package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/18.
 */

public class SetNewPwdV2Fragment extends BaseFragment {

    @BindView(R.id.et_newpwd_setpwd_fmt)
    EditText et_newpwd_setpwd_fmt;
    private String phone;
    private String smscode;

    @OnClick(R.id.bt_setpwd_finsh_fmt)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setpwd_finsh_fmt:
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
                                        ToastUtils.getToast(mContext,"修改成功");
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    } else {
                                        ToastUtils.getToast(mContext, noDataBean.getMsg());
                                    }
                                }
                            });
                } else {
                    ToastUtils.getToast(mContext, "密码长度不正确");
                }
                break;
        }
    }

    public static SetNewPwdV2Fragment newInstance() {
        return new SetNewPwdV2Fragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_setnewpwdv2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        smscode = bundle.getString("smscode");
        Logger.e("phone" + phone);
        Logger.e("smsCode" + smscode);
    }
}
