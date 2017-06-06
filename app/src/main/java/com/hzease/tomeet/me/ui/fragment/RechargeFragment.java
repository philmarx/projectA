package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.OrderInfoBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
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

    private String pay;


    @OnClick({
            R.id.bt_recharge_success_fmt
    })
    public void onClick(View v) {
        String totalAmount = tv_recharge_money_fmt.getText().toString().trim();
        Logger.e("text: " + totalAmount);
        switch (v.getId()) {
            case R.id.bt_recharge_success_fmt:
                //getActivity().getSupportFragmentManager().popBackStack();
                if ((cb_recharge_alipay_fmt.isChecked() || cb_recharge_wxpay_fmt.isChecked()) && (!tv_recharge_money_fmt.getText().toString().isEmpty())) {
                    switch (pay) {
                        case "alipay":
                            PTApplication.getRequestService().createAlipayOrder(PTApplication.userToken, PTApplication.userId, totalAmount)
                                    .doOnCompleted(new Action0() {
                                        @Override
                                        public void call() {
                                            // 转圈
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    //.observeOn(Schedulers.io())
                                    .subscribe(new Subscriber<OrderInfoBean>() {
                                        @Override
                                        public void onCompleted() {
                                            // 关闭转圈
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("error: " + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(OrderInfoBean orderInfoBean) {
                                            Logger.e(orderInfoBean.toString());
                                            PayTask payTask = new PayTask(getActivity());
                                            Map<String, String> payV2Result = payTask.payV2(orderInfoBean.getData(), true);
                                            Logger.e(payV2Result.toString());
                                            final String resultStatus = payV2Result.get("resultStatus");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    switch (resultStatus) {
                                                        case "9000":
                                                            // 支付成功
                                                            ToastUtils.getToast(mContext, "支付成功");
                                                            getActivity().getSupportFragmentManager().popBackStack();
                                                            // 将 fragment_container View 中的内容替换为此 Fragment ，
                                                            transaction.replace(R.id.fl_content_me_activity, new RechargeResultFragment(true));
                                                            // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                                                            transaction.addToBackStack(null);
                                                            // 执行事务
                                                            transaction.commit();
                                                            break;
                                                        case "8000":
                                                            // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                                                            break;
                                                        case "4000":
                                                            // 订单支付失败
                                                            break;
                                                        case "5000":
                                                            // 重复请求
                                                            break;
                                                        case "6001":
                                                            // 用户中途取消
                                                            break;
                                                        case "6002":
                                                            // 网络连接出错
                                                            break;
                                                        case "6004":
                                                            // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                                                            break;
                                                        default:
                                                            // 其它支付错误
                                                            break;
                                                    }
                                                }
                                            });
                                        }
                                    });
                            break;
                        case "wxpay":
                            IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, null);
                            wxapi.registerApp("");
                            PayReq wxpayRequest = new PayReq();
                            wxpayRequest.appId = "wxd930ea5d5a258f4f";
                            wxpayRequest.partnerId = "1900000109";
                            wxpayRequest.prepayId= "1101000000140415649af9fc314aa427";
                            wxpayRequest.packageValue = "Sign=WXPay";
                            wxpayRequest.nonceStr= "1101000000140429eb40476f8896f4c9";
                            wxpayRequest.timeStamp= "1398746574";
                            wxpayRequest.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
                            Logger.e(wxpayRequest.toString());
                            wxapi.sendReq(wxpayRequest);
                            break;
                    }
                } else {
                    ToastUtils.getToast(mContext, "请选择支付方式或者输入充值金额");
                }

                break;
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
        tv_recharge_money_fmt.setFocusable(true);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        cb_recharge_alipay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_recharge_wxpay_fmt.setChecked(false);
                    pay = "alipay";
                }
            }
        });
        cb_recharge_wxpay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_recharge_alipay_fmt.setChecked(false);
                    pay = "wxpay";
                }
            }
        });
    }

}
