package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
    private double myAmount;
    private double thisAmount;

    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;

    @OnClick({
            R.id.tv_deposit_alldeposit_fmt,
            R.id.bt_deposit_apply_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_deposit_alldeposit_fmt:
                Logger.e("thisAmount" + thisAmount);
                Logger.e("myAmount" + myAmount);
                et_deposit_money_fmt.setText(String.valueOf(thisAmount < myAmount ? thisAmount : myAmount));
                break;
            case R.id.bt_deposit_apply_fmt:
                final String money = String.valueOf((Double.valueOf(et_deposit_money_fmt.getText().toString().trim())*100)).split("\\.")[0];
                if (!money.equals("0")) {
                    Double inputAmount = Double.valueOf(et_deposit_money_fmt.getText().toString().trim());
                    Logger.e("inputAmount: " + inputAmount + "\n" + String.valueOf(inputAmount * 100) + "\n" + String.valueOf(inputAmount * 100).split("\\.")[0]);
                    if (inputAmount > myAmount) {
                        ToastUtils.getToast("退款金额不足！");
                    } else if (inputAmount > thisAmount) {
                        ToastUtils.getToast("退款金额不能大于可退金额！");
                    } else {
                        PTApplication.getRequestService().applyDeposit(mData.getId(), String.valueOf(inputAmount * 100).split("\\.")[0], PTApplication.userToken, PTApplication.myInfomation.getData().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        // 转圈
                                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et_deposit_money_fmt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                        load_View.setVisibility(View.VISIBLE);
                                        bt_deposit_apply_fmt.setEnabled(false);
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        // 关闭转圈
                                        load_View.setVisibility(View.GONE);
                                        bt_deposit_apply_fmt.setEnabled(true);
                                    }
                                })
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
                                            ToastUtils.getToast("退款成功，请注意查收！");
                                            //重新获取一遍我的余额
                                            PTApplication.getRequestService().getMyInfomation(PTApplication.userToken,PTApplication.userId)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Subscriber<UserInfoBean>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {

                                                        }

                                                        @Override
                                                        public void onNext(UserInfoBean userInfoBean) {
                                                            if (userInfoBean.isSuccess()){
                                                                PTApplication.myInfomation = userInfoBean;
                                                                getActivity().getSupportFragmentManager().popBackStack();
                                                            }
                                                        }
                                                    });

                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                            Logger.e(noDataBean.getMsg());
                                        }
                                    }
                                });
                    }
                } else {
                    ToastUtils.getToast("请输入正确的金额");
                }
                break;
        }
    }

    public static DepositMoneyFragment newInstance(DepositBean.DataBean mData) {
        DepositMoneyFragment depositMoneyFragment = new DepositMoneyFragment();
        depositMoneyFragment.mData = mData;
        return depositMoneyFragment;
    }

    public DepositMoneyFragment() {
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_depositmoney;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 总余额和本单余额，已转换为元
        myAmount = PTApplication.myInfomation.getData().getAmount() / 100.0;
        thisAmount = (Long.valueOf(mData.getAmount()) - Long.valueOf(mData.getRefundAmount())) / 100.0;

        tv_deposit_desc_fmt.setText("可用余额" + myAmount + "元，可退款余额" + thisAmount + "元");
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
                    if (Double.valueOf(et_deposit_money_fmt.getText().toString()) > Double.valueOf(mData.getAmount())/100.0) {
                        tv_deposit_isOut_fmt.setVisibility(View.VISIBLE);
                    } else {
                        tv_deposit_isOut_fmt.setVisibility(View.GONE);
                    }
                }
            }
        });

        et_deposit_money_fmt.requestFocusFromTouch();
    }
}
