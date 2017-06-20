package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.AlipayOrderInfoBean;
import com.hzease.tomeet.data.WxpayOrderInfoBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/18.
 * 支付页面
 */

public class RechargeFragment extends BaseFragment {
    FragmentTransaction transaction;
    @BindView(R.id.cb_recharge_alipay_fmt)
    CheckBox cb_recharge_alipay_fmt;

    @BindView(R.id.cb_recharge_wxpay_fmt)
    CheckBox cb_recharge_wxpay_fmt;

    // 金额输入框
    @BindView(R.id.tv_recharge_money_fmt)
    EditText tv_recharge_money_fmt;

    @BindView(R.id.arl_alipay_recharge_fmt)
    AutoRelativeLayout arl_alipay_recharge_fmt;

    @BindView(R.id.arl_wxpay_recharge_fmt)
    AutoRelativeLayout arl_wxpay_recharge_fmt;
    // 确定按钮
    @BindView(R.id.bt_recharge_success_fmt)
    Button bt_recharge_success_fmt;

    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;

    private String pay = "";

    @OnClick({
            R.id.bt_recharge_success_fmt
    })
    public void onClick(View v) {
        String totalAmount = tv_recharge_money_fmt.getText().toString().trim().replace(".","");
        Logger.e("text: " + totalAmount);
        //getActivity().getSupportFragmentManager().popBackStack();
        if (!TextUtils.isEmpty(tv_recharge_money_fmt.getText().toString().trim())) {
            switch (pay) {
                case "alipay":
                    PTApplication.getRequestService().createAlipayOrder(PTApplication.userToken, PTApplication.userId, totalAmount)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    // 转圈
                                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tv_recharge_money_fmt.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                                    load_View.setVisibility(View.VISIBLE);
                                    bt_recharge_success_fmt.setEnabled(false);
                                }
                            })
                            .doAfterTerminate(new Action0() {
                                @Override
                                public void call() {
                                    // 关闭转圈
                                    load_View.setVisibility(View.GONE);
                                    bt_recharge_success_fmt.setEnabled(true);
                                }
                            })
                            .observeOn(Schedulers.io())
                            .subscribe(new Subscriber<AlipayOrderInfoBean>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("error: " + e.getMessage());
                                }

                                @Override
                                public void onNext(AlipayOrderInfoBean alipayOrderInfoBean) {
                                    Logger.e(alipayOrderInfoBean.toString());
                                    if (alipayOrderInfoBean.isSuccess()) {
                                        PayTask payTask = new PayTask(getActivity());
                                        Map<String, String> payV2Result = payTask.payV2(alipayOrderInfoBean.getData(), true);
                                        Logger.e(payV2Result.toString());
                                        final String resultStatus = payV2Result.get("resultStatus");
                                        startActivity(new Intent(mContext, WXPayEntryActivity.class).putExtra("alipayIsSuccess", resultStatus));
                                    } else {
                                        startActivity(new Intent(mContext, WXPayEntryActivity.class).putExtra("alipayIsSuccess", "6002"));
                                    }
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            });
                    break;
                case "wxpay":
                    PTApplication.getRequestService().createWXOrder(PTApplication.userToken, PTApplication.userId, totalAmount)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    // 转圈
                                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tv_recharge_money_fmt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    load_View.setVisibility(View.VISIBLE);
                                    bt_recharge_success_fmt.setEnabled(false);
                                }
                            })
                            .doAfterTerminate(new Action0() {
                                @Override
                                public void call() {
                                    // 关闭转圈
                                    load_View.setVisibility(View.GONE);
                                    bt_recharge_success_fmt.setEnabled(true);
                                }
                            })
                            .subscribe(new Subscriber<WxpayOrderInfoBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e(e.getMessage());
                                }

                                @Override
                                public void onNext(WxpayOrderInfoBean wxpayOrderInfoBean) {
                                    Logger.e(wxpayOrderInfoBean.toString());
                                    if (wxpayOrderInfoBean.isSuccess()) {
                                        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, AppConstants.TOMEET_WX_APP_ID);
                                        PayReq wxpayRequest = new PayReq();
                                        wxpayRequest.appId = AppConstants.TOMEET_WX_APP_ID;
                                        wxpayRequest.partnerId = AppConstants.TOMEET_WX_APP_PARTNER_ID;
                                        wxpayRequest.prepayId = wxpayOrderInfoBean.getData().getPrepayId();
                                        wxpayRequest.packageValue = "Sign=WXPay";
                                        wxpayRequest.nonceStr = wxpayOrderInfoBean.getData().getNonceStr();
                                        wxpayRequest.timeStamp = wxpayOrderInfoBean.getData().getTimeStamp();
                                        wxpayRequest.sign = wxpayOrderInfoBean.getData().getSign();
                                        Logger.e(wxapi.toString() + "\nappId: " + wxpayRequest.appId + "\n" + wxpayRequest.partnerId + "\npartnerId: " + wxpayRequest.prepayId + "\nnonceStr: " + wxpayRequest.nonceStr + "\ntimeStamp: " + wxpayRequest.timeStamp + "\nsign: " + wxpayRequest.sign);
                                        wxapi.sendReq(wxpayRequest);
                                    } else {
                                        startActivity(new Intent(mContext, WXPayEntryActivity.class).putExtra("alipayIsSuccess", "6002"));
                                    }
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            });
                    break;
                default:
                    ToastUtils.getToast(mContext, "请选择一种充值方式");
                    break;
            }
        } else {
            ToastUtils.getToast(mContext, "请输入充值金额");
        }
    }

    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_recharge;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        tv_recharge_money_fmt.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {// ----->如果字符未改变则返回
                    return;
                }
                String str = s.toString();

                isChanged = true;
                String cuttedStr = str;
                /* 删除字符串中的dot */
                for (int i = str.length() - 1; i >= 0; i--) {
                    char c = str.charAt(i);
                    if ('.' == c) {
                        cuttedStr = str.substring(0, i) + str.substring(i + 1);
                        break;
                    }
                }
                /* 删除前面多余的0 */
                int NUM = cuttedStr.length();
                int zeroIndex = -1;
                for (int i = 0; i < NUM - 2; i++) {
                    char c = cuttedStr.charAt(i);
                    if (c != '0') {
                        zeroIndex = i;
                        break;
                    }else if(i == NUM - 3){
                        zeroIndex = i;
                        break;
                    }
                }
                if(zeroIndex != -1){
                    cuttedStr = cuttedStr.substring(zeroIndex);
                }
                /* 不足3位补0 */
                if (cuttedStr.length() < 3) {
                    cuttedStr = "0" + cuttedStr;
                }
                /* 加上dot，以显示小数点后两位 */
                cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)
                        + "." + cuttedStr.substring(cuttedStr.length() - 2);

                tv_recharge_money_fmt.setText(cuttedStr);

                tv_recharge_money_fmt.setSelection(tv_recharge_money_fmt.length());
                isChanged = false;
            }
        });

        arl_alipay_recharge_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay = "wxpay";
                cb_recharge_wxpay_fmt.setChecked(true);
                cb_recharge_alipay_fmt.setChecked(false);
            }
        });
        arl_wxpay_recharge_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay = "alipay";
                cb_recharge_alipay_fmt.setChecked(true);
                cb_recharge_wxpay_fmt.setChecked(false);
            }
        });

        tv_recharge_money_fmt.requestFocusFromTouch();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(tv_recharge_money_fmt, InputMethodManager.SHOW_IMPLICIT);
            }
        }).start();
    }
}
