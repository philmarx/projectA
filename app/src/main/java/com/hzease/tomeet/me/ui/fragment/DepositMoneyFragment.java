package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositMoneyFragment extends BaseFragment {

    //金额
    @BindView(R.id.et_deposit_money_fmt)
    EditText et_deposit_money_fmt;
    //描述
    @BindView(R.id.tv_deposit_desc_fmt)
    TextView tv_deposit_desc_fmt;
    //全部退款
    @BindView(R.id.tv_deposit_alldeposit_fmt)
    TextView tv_deposit_alldeposit_fmt;
    //是否超出可退金额
    @BindView(R.id.tv_deposit_isOut_fmt)
    TextView tv_deposit_isOut_fmt;
    //申请退款
    @BindView(R.id.bt_deposit_apply_fmt)
    Button bt_deposit_apply_fmt;
    private DepositBean.DataBean mData;

    @OnClick({
            R.id.tv_deposit_alldeposit_fmt,
            R.id.bt_deposit_apply_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_deposit_alldeposit_fmt:
                et_deposit_money_fmt.setText(mData.getAmount());
                break;
            case R.id.bt_deposit_apply_fmt:
                if (!et_deposit_money_fmt.getText().toString().isEmpty()) {
                    if (Float.valueOf(et_deposit_money_fmt.getText().toString()) > Float.valueOf(mData.getAmount())) {
                        ToastUtils.getToast(mContext, "退款金额不足！");
                    } else {
                        PTApplication.getRequestService().applyDeposit(mData.getId(),mData.getOut_trade_no(), Float.valueOf(et_deposit_money_fmt.getText().toString()), PTApplication.userToken, mData.getTrade_no(), PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            ToastUtils.getToast(mContext, "退款成功，请注意查收！");
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        } else {
                                            ToastUtils.getToast(mContext, noDataBean.getMsg());
                                            Logger.e(noDataBean.getMsg());
                                        }
                                    }
                                });
                    }
                }
                break;
        }
    }

    public DepositMoneyFragment(DepositBean.DataBean mData) {
        this.mData = mData;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_depositmoney;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_deposit_desc_fmt.setText("可用余额" + PTApplication.myInfomation.getData().getAmount() / 100.0 + "元，可退款余额" + mData.getAmount() + "元");
        et_deposit_money_fmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_deposit_money_fmt.getText().toString().isEmpty()) {
                    if (Float.valueOf(et_deposit_money_fmt.getText().toString()) > Float.valueOf(mData.getAmount())) {
                        tv_deposit_isOut_fmt.setVisibility(View.VISIBLE);
                    } else {
                        tv_deposit_isOut_fmt.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

}
