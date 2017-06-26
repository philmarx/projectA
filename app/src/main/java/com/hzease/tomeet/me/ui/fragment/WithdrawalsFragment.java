package com.hzease.tomeet.me.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 提现界面
 * Created by xuq on 2017/6/21.
 */

public class WithdrawalsFragment extends BaseFragment {
    @BindView(R.id.et_withdrawals_alipayAmount_fmt)
    EditText et_withdrawals_alipayAmount_fmt;
    @BindView(R.id.tv_withdrawals_isRealName_fmt)
    TextView tv_withdrawals_isRealName_fmt;
    @BindView(R.id.et_withdrawals_money_fmt)
    EditText et_withdrawals_money_fmt;
    @BindView(R.id.tv_deposit_isOut_fmt)
    TextView tv_deposit_isOut_fmt;
    @BindView(R.id.tv_withdrawals_desc_fmt)
    TextView tv_withdrawals_desc_fmt;
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;

    @OnClick({
            R.id.bt_withdrawals_apply_fmt,
            R.id.tv_withdrawals_all_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_withdrawals_apply_fmt:
                if (et_withdrawals_alipayAmount_fmt.getText().toString().trim().isEmpty()){
                    ToastUtils.getToast(mContext,"请输入支付宝账号");
                }else if (et_withdrawals_money_fmt.getText().toString().trim().isEmpty()){
                    ToastUtils.getToast(mContext,"请输入提现金额");
                }else{
                    String alipayAmount = et_withdrawals_alipayAmount_fmt.getText().toString().trim();
                    final String money = String.valueOf((Double.valueOf(et_withdrawals_money_fmt.getText().toString().trim())*100)).split("\\.")[0];
                    PTApplication.getRequestService().withdrawals(alipayAmount,money,PTApplication.myInfomation.getData().getRealName(),PTApplication.userToken,PTApplication.userId)
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
                                        ToastUtils.getToast(mContext,"提现成功！");
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }else{
                                        ToastUtils.getToast(mContext,noDataBean.getMsg());
                                    }
                                }
                            });
                }

                break;
            case R.id.tv_withdrawals_all_fmt:
                et_withdrawals_money_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getAmount()/100.0));
                break;
        }
    }
    public static WithdrawalsFragment newInstance() {
        return new WithdrawalsFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_withdrawals;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (PTApplication.myInfomation.getData().isAuthorized()){
            tv_withdrawals_isRealName_fmt.setText(PTApplication.myInfomation.getData().getRealName());
            tv_withdrawals_isRealName_fmt.setTextColor(Color.rgb(184,184,184));
        }
        tv_withdrawals_desc_fmt.setText("可提现金额"+String.valueOf(PTApplication.myInfomation.getData().getAmount()/100.0)+"元");
        et_withdrawals_money_fmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_withdrawals_money_fmt.getText().toString().isEmpty()) {
                    if (Float.valueOf(et_withdrawals_money_fmt.getText().toString()) > Float.valueOf(PTApplication.myInfomation.getData().getAmount())/100.0) {
                        tv_deposit_isOut_fmt.setVisibility(View.VISIBLE);
                    } else {
                        tv_deposit_isOut_fmt.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
