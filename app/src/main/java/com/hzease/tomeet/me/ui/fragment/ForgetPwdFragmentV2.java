package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/18.
 */

public class ForgetPwdFragmentV2 extends BaseFragment {
    private FragmentTransaction transaction;
    private MeActivity meActivity;
    @BindView(R.id.et_phone_number_findpwd_fmt)
    EditText et_phone_number_findpwd_fmt;

    @OnClick({R.id.bt_findpwd_next_fmt})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_findpwd_next_fmt:
                final String phoneNum = et_phone_number_findpwd_fmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNum)) {
                    PTApplication.getRequestService().getSMSCode(phoneNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<StringDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(StringDataBean stringDataBean) {
                                    if (stringDataBean.isSuccess()){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone", phoneNum);
                                        bundle.putBoolean("modifitypwd",true);
                                        SmsCodeFragment smsCodeFragment = SmsCodeFragment.newInstance();
                                        smsCodeFragment.setArguments(bundle);
                                        transaction.replace(R.id.fl_content_me_activity, smsCodeFragment);
                                        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                                        //transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else{
                                        ToastUtils.getToast(mContext,stringDataBean.getMsg());
                                    }
                                }
                            });

                }
                break;
        }
    }
    public static ForgetPwdFragmentV2 newInstance() {
        return new ForgetPwdFragmentV2();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_forgetpwdv2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
    }
}
