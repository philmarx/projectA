package com.hzease.tomeet.me.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.AlipayOrderInfoBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.PropsShopCenter;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.data.WxpayOrderInfoBean;
import com.hzease.tomeet.home.ui.CreateRoomBeforeActivity;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.me.ui.MySmallPaperActivity;
import com.hzease.tomeet.me.ui.ShareWebViewActivity;
import com.hzease.tomeet.splash.ui.SplashActivity;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.MyPropsAdapterV2;
import com.hzease.tomeet.widget.adapters.PropShopCenterAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/10/18.
 */

public class PropsCenterFragmentV2 extends BaseFragment implements IMeContract.View {
    @BindView(R.id.gv_propcenter_myprops_fmt)
    GridView gv_propcenter_myprops_fmt;
    @BindView(R.id.gv_propcenter_shop_fmt)
    GridView gv_propcenter_shop_fmt;

    //自己的叶子数量
    @BindView(R.id.tv_propcenter_badgenum_fmt)
    TextView tv_propcenter_badgenum_fmt;

    FragmentTransaction transaction;
    MeActivity meActivity;

    private List<PropsShopCenter.DataBean> mData;
    BottomNavigationView bottomNavigationView;
    private IMeContract.Presenter mPresenter;
    private MyPropsAdapterV2 myPropAdapter;
    private PropShopCenterAdapter propShopAdapter;


    @OnClick({
            R.id.ll_tv_props_torechgre_fmt,
            R.id.tv_prop_leaf_detailed,
            R.id.tv_props_getcurrency
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_tv_props_torechgre_fmt:
                //跳转到徽章充值界面
                //将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(10));
                //然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.tv_prop_leaf_detailed:
                transaction.replace(R.id.fl_content_me_activity, LeafDetailed.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.tv_props_getcurrency:
                initRulePopWindow(v);
                break;
        }
    }

    private int[] propsShopPic = {R.drawable.props_shop_buy_smallpaper, R.drawable.props_shop_buy_label_dismiss, R.drawable.props_shop_buy_label_changname,
            R.drawable.props_shop_buy_sign};
    private int buy_Props_Num;

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static PropsCenterFragmentV2 newInstance() {
        return new PropsCenterFragmentV2();
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_propscenterv2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        tv_propcenter_badgenum_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getBadge()));
        //我的道具
        myPropAdapter = new MyPropsAdapterV2(mContext);
        gv_propcenter_myprops_fmt.setAdapter(myPropAdapter);
        gv_propcenter_myprops_fmt.setNumColumns(4);
        initPropShopCenter();
        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
        gv_propcenter_myprops_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Logger.e("postion" + position);
            }
        });


    }

    //加载道具商城
    private void initPropShopCenter() {
        gv_propcenter_shop_fmt.setNumColumns(2);
        String propShopCenter = SpUtils.getString(mContext, AppConstants.TOMEET_SP_PROP_SHOP);
        if (TextUtils.isEmpty(propShopCenter)) {
            PTApplication.getRequestService().findPropShop()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Subscriber<PropsShopCenter>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(PropsShopCenter propsShopCenter) {
                            if (propsShopCenter.isSuccess()) {
                                mData = propsShopCenter.getData();
                                for (int i = 0; i < mData.size(); i++) {
                                    if (!mData.get(i).isShow()) {
                                        mData.remove(i);
                                        i--;
                                    }
                                }
                                propShopAdapter = new PropShopCenterAdapter(mContext, propsShopCenter.getData());
                                gv_propcenter_shop_fmt.setAdapter(propShopAdapter);
                                SpUtils.saveString(mContext, AppConstants.TOMEET_SP_PROP_SHOP, new Gson().toJson(propsShopCenter));
                            } else {
                                ToastUtils.getToast(propsShopCenter.getMsg());
                            }
                        }
                    });
        } else {
            mData = new Gson().fromJson(propShopCenter, PropsShopCenter.class).getData();
            for (int i = 0; i < mData.size(); i++) {
                if (!mData.get(i).isShow()) {
                    mData.remove(i);
                    i--;
                }
            }
            propShopAdapter = new PropShopCenterAdapter(mContext, mData);
            gv_propcenter_shop_fmt.setAdapter(propShopAdapter);
            gv_propcenter_shop_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            initBuyPropPop(view, i);
                            break;
                        case 4:
                        case 5:
                        case 6:
                            initBuyVipPopWindow(view, i);
                            break;
                        default:
                            initBuyPropPop(view, i);
                            break;
                    }

                }
            });
        }


    }

    //显示如何获得叶子
    private void initRulePopWindow(View v) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_getcurrency, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }


    //购买会员
    private void initBuyVipPopWindow(View view, final int position) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_buy_vip, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        TextView tv_prop_vip_price = contentView.findViewById(R.id.tv_prop_vip_price);
        AutoRelativeLayout arl_wechat = contentView.findViewById(R.id.arl_wechat);
        AutoRelativeLayout arl_alipay = contentView.findViewById(R.id.arl_ali_pay);
        switch (position) {
            case 4:
                Logger.e("一个月");
                tv_prop_vip_price.setText("¥18.00");
                break;
            case 5:
                Logger.e("3个月");
                tv_prop_vip_price.setText("¥50.00");
                break;
            case 6:
                Logger.e("一年");
                tv_prop_vip_price.setText("¥188.00");
                break;
        }
        //支付宝
        arl_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().buyVIPbyAlipay(1, String.valueOf(position), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                showLoadingDialog();
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭弹窗
                                hideLoadingDialog();
                                popupWindow.dismiss();
                            }
                        })
                        .subscribe(new Subscriber<AlipayOrderInfoBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(AlipayOrderInfoBean alipayOrderInfoBean) {
                                if (alipayOrderInfoBean.isSuccess()) {
                                    PayTask payTask = new PayTask(getActivity());
                                    Map<String, String> payV2Result = payTask.payV2(alipayOrderInfoBean.getData(), true);
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
                                            popupWindow.dismiss();
                                        }
                                    });
                                } else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.getToast("网络连接失败");
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        //微信
        arl_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().buyVIPbyWechat(1, String.valueOf(position), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                showLoadingDialog();
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭弹窗
                                popupWindow.dismiss();
                                hideLoadingDialog();
                            }
                        })
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
                                    mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
                                } else {
                                    ToastUtils.getToast("网络连接失败");
                                }
                            }
                        });
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    //弹出购买道具的弹窗
    private void initBuyPropPop(final View view, final int position) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_buyprop, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });

        //道具数量的父布局
        LinearLayout ll_plusorreduce = contentView.findViewById(R.id.ll_plusorreduce);
        //道具价格的父布局
        LinearLayout ll_propsprice = contentView.findViewById(R.id.ll_propsprice);
        //道具的背景图片
        ImageView iv_Props_Picture = contentView.findViewById(R.id.iv_props_shop_picture);
        //减少购买道具数量
        ImageView iv_Props_Reduce = contentView.findViewById(R.id.iv_buyProp_reduce);
        //增加购买道具数量
        ImageView iv_Props_Plus = contentView.findViewById(R.id.iv_buyProp_plus);
        //显示购买道具数量
        final EditText et_Props_Num = contentView.findViewById(R.id.et_buyPorp_num);
        //显示购买道具需要的叶子
        TextView tv_Props_Price = contentView.findViewById(R.id.tv_buyProp_price);
        //取消
        Button bt_Props_cancel = contentView.findViewById(R.id.bt_props_cancel_pop);
        //确认购买
        Button bt_Props_buy = contentView.findViewById(R.id.bt_props_buyoruse_pop);
        //设置道具的背景图片
        if (position == mData.size() - 1) {
            ll_plusorreduce.setVisibility(View.GONE);
            ll_propsprice.setVisibility(View.GONE);
            iv_Props_Picture.setImageResource(R.drawable.props_movie_ticket);
            bt_Props_buy.setText("去获取");
        } else {
            iv_Props_Picture.setImageResource(propsShopPic[position]);
        }
        //设置道具的叶子
        tv_Props_Price.setText(String.valueOf(mData.get(position).getBadge()));
        //减少购买道具数量
        iv_Props_Reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int props_Num = Integer.valueOf(et_Props_Num.getText().toString().trim());
                if (props_Num != 0) {
                    et_Props_Num.setText(String.valueOf(--props_Num));
                }
            }
        });
        //增加购买道具数量
        iv_Props_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int props_Num = Integer.valueOf(et_Props_Num.getText().toString().trim());
                if (props_Num != 99) {
                    et_Props_Num.setText(String.valueOf(++props_Num));
                }
            }
        });
        //取消
        bt_Props_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        //确认购买
        bt_Props_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == mData.size() - 1) {
                    Intent intent = new Intent(getActivity(), ShareWebViewActivity.class);
                    intent.putExtra("url", "https://hzease.com/activityList/movieList.html");
                    startActivity(intent);
                    popupWindow.dismiss();
                } else {
                    buy_Props_Num = Integer.valueOf(et_Props_Num.getText().toString().trim());
                    mPresenter.buyProps(position, buy_Props_Num, PTApplication.userToken, position, PTApplication.userId);
                    popupWindow.dismiss();
                }

            }
        });

        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 购买结束的回掉
     *
     * @param index
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index, boolean success, String msg) {
        if (success) {
            ToastUtils.getToast("购买成功");
            mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
            PTApplication.myInfomation.getData().setBadge(PTApplication.myInfomation.getData().getBadge() - mData.get(index).getBadge() * buy_Props_Num);
            tv_propcenter_badgenum_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getBadge()));
        } else {
            ToastUtils.getToast(msg);
        }
    }

    //显示我的道具的数量
    @Override
    public void showPropsMum(PropsMumBean.DataBean data) {
        myPropAdapter.setPropNum(data);
    }

    public void showLoadingDialog() {
        ((MeActivity) getActivity()).showLoadingDialog();
    }

    public void hideLoadingDialog() {
        ((MeActivity) getActivity()).hideLoadingDialog();
    }


    //其他fragment的方法
    @Override
    public void showMyInfo() {

    }

    @Override
    public void showMyRooms(boolean isSuccess, MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

    }

    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void authorizedSuccess() {

    }

    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    @Override
    public void showWaitEvaluateMember(WaitEvaluateV2Bean data) {

    }

    @Override
    public void showChangeNameSuccess(NoDataBean noDataBean) {

    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }
}