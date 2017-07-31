package com.hzease.tomeet.login.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/28.
 */

public class ChangeNewPwdFragment extends BaseFragment {
    @BindView(R.id.et_me_olderpwd_fmt)
    EditText et_me_olderpwd_fmt;
    @BindView(R.id.et_me_newpwd_fmt)
    EditText et_me_newpwd_fmt;
    @BindView(R.id.bt_me_changpwd_fmt)
    Button bt_me_changpwd_fmt;
    Unbinder unbinder;
    private String phone;
    private String smsCode;

    public static ChangeNewPwdFragment newInstance(){
        return new ChangeNewPwdFragment();
    }
    @OnClick(
            R.id.bt_me_changpwd_fmt
    )
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_me_changpwd_fmt:
                PTApplication.getRequestService().forgetPwd(et_me_newpwd_fmt.getText().toString().trim(),phone,smsCode)
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
                                if (noDataBean.isSuccess()){
                                    ToastUtils.getToast(mContext,"修改成功");
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }else{
                                    ToastUtils.getToast(mContext,noDataBean.getMsg());
                                }
                            }
                        });
                break;
        }
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_changepwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        et_me_olderpwd_fmt.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        smsCode = bundle.getString("smsCode");

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
