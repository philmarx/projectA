package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.AlipayOrderInfoBean;
import com.hzease.tomeet.data.BadgeBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.data.WxpayOrderInfoBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * 叶子充值界面
 * Created by xuq on 2017/5/18.
 */

public class BadgeRechgreFragment extends BaseFragment implements IMeContract.View {

    @BindView(R.id.tfl_badge_regchre_fmt)
    TagFlowLayout tfl_badge_regchre_fmt;
    @BindView(R.id.cb_badge_alipay_fmt)
    CheckBox cb_badge_alipay_fmt;
    @BindView(R.id.cb_badge_wxpay_fmt)
    CheckBox cb_badge_wxpay_fmt;
    @BindView(R.id.bt_me_changpwd_fmt)
    Button bt_me_changpwd_fmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    private String[] badgeCount = {"6", "18", "38", "68", "150", "500"};
    private String[] badgeMoneys = {"6元", "18元", "38元", "68元", "128元", "388元"};
    private List<BadgeBean> mDatas;
    private String pay;
    private String count;
    private String money;
    private String newmoney;

    @OnClick({
            R.id.bt_me_changpwd_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_me_changpwd_fmt:
                //TODO 执行充值操作
                newmoney = money.substring(0,money.length()-1);
                if (cb_badge_alipay_fmt.isChecked()) {
                    PTApplication.getRequestService().createAlipayBadgeOrder(PTApplication.userToken,Integer.valueOf(newmoney) * 100,PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Subscriber<AlipayOrderInfoBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("onError" + e.getMessage());
                                }

                                @Override
                                public void onNext(final AlipayOrderInfoBean orderInfoBean) {
                                    if (orderInfoBean.isSuccess()){
                                        PayTask payTask = new PayTask(getActivity());
                                        Map<String, String> payV2Result = payTask.payV2(orderInfoBean.getData(), true);
                                        final String resultStatus = payV2Result.get("resultStatus");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                switch (resultStatus) {
                                                    case "9000":
                                                        // 充值成功
                                                        ToastUtils.getToast("充值成功");
                                                        mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
                                                        break;
                                                    case "8000":
                                                        // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                                                        ToastUtils.getToast("正在处理中");
                                                        break;
                                                    case "4000":
                                                        // 订单支付失败
                                                        ToastUtils.getToast("订单支付失败");
                                                        break;
                                                    case "5000":
                                                        // 重复请求
                                                        ToastUtils.getToast("订单支付失败");
                                                        break;
                                                    case "6001":
                                                        // 用户中途取消
                                                        ToastUtils.getToast("取消充值");
                                                        break;
                                                    case "6002":
                                                        // 网络连接出错
                                                        ToastUtils.getToast("网络连接出错");
                                                        break;
                                                    case "6004":
                                                        // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                                                        ToastUtils.getToast("支付结果未知，请查询余额或明细");
                                                        break;
                                                    default:
                                                        // 其它支付错误
                                                        ToastUtils.getToast("其它支付错误");
                                                        break;
                                                }
                                            }
                                        });
                                    }else{
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtils.getToast(orderInfoBean.getMsg());
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    PTApplication.getRequestService().createWxBadgeOrder(PTApplication.userToken,Integer.valueOf(newmoney) * 100,PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<WxpayOrderInfoBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(WxpayOrderInfoBean wxpayOrderInfoBean) {
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
                                        wxapi.sendReq(wxpayRequest);
                                    } else {
                                        ToastUtils.getToast(wxpayOrderInfoBean.getMsg());
                                    }
                                }
                            });
                }
                //getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static BadgeRechgreFragment newInstance() {
        return new BadgeRechgreFragment();
    }

    /**
     * 显示 或 刷新 我的信息
     */
    @Override
    public void showMyInfo() {

    }

    /**
     * 显示我的房间
     *
     * @param myJoinRoomBean
     * @param isLoadMore
     */
    @Override
    public void showMyRooms(MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {
    }

    /**
     * 更新密码成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 提交反馈成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 认证成功
     */
    @Override
    public void authorizedSuccess() {

    }

    /**
     * 显示结束房间信息
     *
     * @param data
     */
    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    /**
     * 显示待评价成员
     *
     * @param data
     */
    @Override
    public void showWaitEvaluateMember(WaitEvaluateV2Bean data) {

    }

    /**
     * 显示道具数量
     *
     * @param data
     */
    @Override
    public void showPropsMum(PropsMumBean.DataBean data) {

    }

    /**
     * 修改昵称成功
     */
    @Override
    public void showChangeNameSuccess() {

    }

    /**
     * 显示购买道具结果
     *
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index, boolean success, String msg) {

    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_badgerechgre;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        initDatas();
        tfl_badge_regchre_fmt.setAdapter(new TagAdapter<BadgeBean>(mDatas) {
            @Override
            public View getView(FlowLayout parent, int position, BadgeBean badgeBean) {
                AutoLinearLayout view = (AutoLinearLayout) View.inflate(getContext(), R.layout.item_taglayout, null);
                if (position == 0) {
                    view.setPadding(80, 24, 80, 20);
                }
                if (position == 5) {
                    view.setPadding(56, 24, 56, 20);
                }
                TextView badgeCount = view.findViewById(R.id.tv_badge_count_fmt);
                TextView badgeMoney = view.findViewById(R.id.tv_badge_money_fmt);
                badgeCount.setText(mDatas.get(position).getBadgeCount());
                badgeMoney.setText(mDatas.get(position).getBadgeMoney());
                return view;
            }
        });
        tfl_badge_regchre_fmt.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                count = mDatas.get(position).getBadgeCount();
                money = mDatas.get(position).getBadgeMoney();
                Logger.e(position + "\ncount " + count + "\nmoney" + money);
                return true;
            }
        });
        cb_badge_alipay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_badge_wxpay_fmt.setChecked(false);
                    cb_badge_alipay_fmt.setChecked(isChecked);
                    pay = "支付宝充值";
                }
            }
        });
        cb_badge_wxpay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cb_badge_alipay_fmt.setChecked(false);
                cb_badge_wxpay_fmt.setChecked(isChecked);
                pay = "微信充值";
            }
        });
    }
    //加载对象
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            BadgeBean bean = new BadgeBean();
            bean.setBadgeCount(badgeCount[i]);
            bean.setBadgeMoney(badgeMoneys[i]);
            mDatas.add(bean);
        }
    }
}
