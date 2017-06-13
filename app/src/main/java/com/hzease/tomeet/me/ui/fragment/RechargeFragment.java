package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
        String totalAmount = tv_recharge_money_fmt.getText().toString().trim() + "00";
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

        arl_alipay_recharge_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay = "alipay";
                cb_recharge_wxpay_fmt.setChecked(false);
                cb_recharge_alipay_fmt.setChecked(true);
            }
        });
        arl_wxpay_recharge_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay = "wxpay";
                cb_recharge_alipay_fmt.setChecked(false);
                cb_recharge_wxpay_fmt.setChecked(true);
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
