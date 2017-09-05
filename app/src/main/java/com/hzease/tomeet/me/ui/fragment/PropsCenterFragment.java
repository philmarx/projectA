package com.hzease.tomeet.me.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.AlipayOrderInfoBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.PropsShopBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.data.WxpayOrderInfoBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.me.ui.MySmallPaperActivity;
import com.hzease.tomeet.me.ui.ShareWebViewActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemProps;
import com.hzease.tomeet.widget.adapters.PropsShopAdapter;
import com.hzease.tomeet.widget.adapters.PropsShopAdapterV2;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class PropsCenterFragment extends BaseFragment implements IMeContract.View {

    //道具商城
    @BindView(R.id.rv_propsShop_fmt)
    RecyclerView rv_propsShop_fmt;
    //小纸条
    @BindView(R.id.all_props_small_paper_fmt)
    AutoRelativeLayout all_props_small_paper_fmt;
    //标签消除卡
    @BindView(R.id.all_props_lables_dismiss_fmt)
    AutoRelativeLayout all_props_lables_dismiss_fmt;
    //改名卡
    @BindView(R.id.all_props_name_change_fmt)
    AutoRelativeLayout all_props_name_change_fmt;
    //vip
    @BindView(R.id.all_props_vip_fmt)
    AutoRelativeLayout all_props_vip_fmt;
    //小纸条数量
    @BindView(R.id.tv_props_small_pager_mum)
    TextView tv_props_small_pager_mum;
    //标签消除卡数量
    @BindView(R.id.tv_props_labels_dismiss_mum)
    TextView tv_props_labels_dismiss_mum;
    //改名卡数量
    @BindView(R.id.tv_props_chang_name_mum)
    TextView tv_props_chang_name_mum;
    //会员剩余时间
    @BindView(R.id.tv_props_vip_time)
    TextView tv_props_vip_time;
    //补签卡数量
    @BindView(R.id.tv_props_buqian_mum)
    TextView tv_props_buqian_mum;
    @BindView(R.id.tv_props_torechgre_fmt)
    TextView tv_props_torechgre_fmt;
    //如何获得叶子
    @BindView(R.id.tv_props_getcurrency)
    TextView tv_props_getcurrency;
    //观影券数量
    @BindView(R.id.tv_props_movie_ticket_mum)
    TextView tv_props_movie_ticket_mum;
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;
    //道具名称
    private String[] propsName = {"小纸条", "标签消除卡", "改名卡", "补签卡", "VIP1个月", "VIP3个月", "VIP1年"};
    //道具图片
    private int[] propsIcons = {R.drawable.propsshop_small_paper, R.drawable.propsshop_labels_dismiss, R.drawable.propsshop_name_change,
            R.drawable.propsshop_buqian_card, R.drawable.propsshop_vip, R.drawable.propsshop_vip, R.drawable.propsshop_vip};
    //道具背景
    private int[] propsbg = {R.drawable.props_small_paper_bg, R.drawable.props_labels_dismiss_bg, R.drawable.props_name_change_bg,
            R.drawable.props_buqian_bg, R.drawable.props_ticket_bg, R.drawable.props_vip_1_bg, R.drawable.props_vip_3_bg, R.drawable.props_vip_12_bg,R.drawable.props_movie_ticket};
    //道具描述
    private String[] propsDic = {"有些话想和你说", "这是个误会", "当初品味成谜", "小小的迟到一下是可以被原谅的", "来一场华丽的变身", "来一场华丽的变身", "来一场华丽的变身"};
    //道具价格
    private String[] propMoney = {"5", "5", "60", "5", "18元", "50元", "188元"};
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    MeActivity meActivity;
    private List<PropsShopBean> mDatas;
    private int changeNameCount;
    private int labelsmember;


    @OnClick({
            R.id.all_props_small_paper_fmt,
            R.id.all_props_lables_dismiss_fmt,
            R.id.all_props_name_change_fmt,
            R.id.tv_props_torechgre_fmt,
            R.id.all_props_buqian_fmt,
            R.id.tv_props_getcurrency,
            R.id.tv_prop_leaf_detailed,
            R.id.all_props_ticket_fmt,
            R.id.all_props_movie_ticket_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_props_small_paper_fmt:
                //initPopupWindow(v, 0, false);
                startActivity(new Intent(getActivity(), MySmallPaperActivity.class));
                break;
            case R.id.all_props_lables_dismiss_fmt:
                initPopupWindow(v, 1, false);
                break;
            case R.id.all_props_name_change_fmt:
                initPopupWindow(v, 2, false);
                break;
            case R.id.all_props_buqian_fmt:
                initPopupWindow(v, 3, false);
                break;
            case R.id.all_props_ticket_fmt:
                initPopupWindow(v, 4, false);
                break;
            case R.id.tv_props_torechgre_fmt:
                //跳转到徽章充值界面
                //将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(10));
                //然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.tv_props_getcurrency:
                initRulePopWindow(v);
                break;
            case R.id.tv_prop_leaf_detailed:
                transaction.replace(R.id.fl_content_me_activity, LeafDetailed.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.all_props_movie_ticket_fmt:
                initPopupWindow(v, 8, false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static PropsCenterFragment newInstance() {
        return new PropsCenterFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }

    @Override
    public void showMyRooms(boolean isSuccess, MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

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
        changeNameCount = data.getChangeNicknameCount();
        tv_props_small_pager_mum.setText("X" + data.getNoteCount());
        tv_props_labels_dismiss_mum.setText("X" + data.getLabelClearCount());
        labelsmember = data.getLabelClearCount();
        tv_props_chang_name_mum.setText("X" + data.getChangeNicknameCount());
        tv_props_vip_time.setText(calculateTime(data.getVipExpireDate()));
        tv_props_buqian_mum.setText("X" + data.getSignCount());
        tv_props_movie_ticket_mum.setText("X" + data.getMovieTicket());

    }

    /**
     * 修改昵称成功
     */
    @Override
    public void showChangeNameSuccess(NoDataBean noDataBean) {
        if (noDataBean.isSuccess()) {
            ToastUtils.getToast("修改昵称成功!");
            mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
        }else{
            ToastUtils.getToast(noDataBean.getMsg());
        }
    }

    /**
     * 显示购买道具结果
     *
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index, boolean success, String msg) {
        if (success) {
            ToastUtils.getToast("购买成功");
            mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
            PTApplication.myInfomation.getData().setBadge(PTApplication.myInfomation.getData().getBadge() - Integer.valueOf(propMoney[index]));
            tv_props_torechgre_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getBadge()));
        } else {
            ToastUtils.getToast(msg);
        }
    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_propscenter;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_props_torechgre_fmt.setText(PTApplication.myInfomation.getData().getBadge() + "");
        mPresenter.findPropsMum(PTApplication.userToken, PTApplication.userId);
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        initDatas();
        rv_propsShop_fmt.addItemDecoration(new SpacesItemProps(15));
        rv_propsShop_fmt.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
        //PropsShopAdapter adapter = new PropsShopAdapter(mDatas, getContext());
        PropsShopAdapterV2 adapter = new PropsShopAdapterV2(mContext);
        adapter.setOnItemClickLitener(new PropsShopAdapterV2.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        initPopupWindow(view, position, true);
                        break;
                    default:
                        initPopupWindow(view, position + 1, true);
                        break;
                }


            }
        });
        //rv_propsShop_fmt.setNestedScrollingEnabled(false);
        rv_propsShop_fmt.setAdapter(adapter);
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
        final int index = position -1;
        switch (index) {
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
                PTApplication.getRequestService().buyVIPbyAlipay(1, String.valueOf(index), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                load_View.setVisibility(View.VISIBLE);
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭弹窗
                                load_View.setVisibility(View.GONE);
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
                                load_View.setVisibility(View.VISIBLE);
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭弹窗
                                popupWindow.dismiss();
                                load_View.setVisibility(View.GONE);
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

    //添加道具对象
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PropsShopBean shopBean = new PropsShopBean();
            shopBean.setPropsName(propsName[i]);
            shopBean.setPropsIcon(propsIcons[i]);
            shopBean.setPropsDic(propsDic[i]);
            shopBean.setPropsMoney(propMoney[i]);
            mDatas.add(shopBean);
        }
    }

    /**
     * 弹出popupwindow
     *
     * @param view
     * @param bgIndex
     * @param isBuy
     */
    private void initPopupWindow(final View view, final int bgIndex, final boolean isBuy) {
        Logger.e("角标" + bgIndex);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_myprops, null);
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
        final AutoLinearLayout all_buttongroup_pop = contentView.findViewById(R.id.all_buttongroup_pop);
        //pop背景
        final AutoLinearLayout bg = contentView.findViewById(R.id.all_props_bg_pop);
        //取消
        Button cancel = contentView.findViewById(R.id.bt_props_cancel_pop);
        //使用或购买
        Button buyoruse = contentView.findViewById(R.id.bt_props_buyoruse_pop);
        bg.setBackgroundResource(propsbg[bgIndex]);
        //判断显示购买还是显示使用
        if (isBuy) {
            buyoruse.setText("购买");
        } else {
            if (bgIndex == 3 || bgIndex == 4 || bgIndex==8) {
                all_buttongroup_pop.setVisibility(View.GONE);
            }
            buyoruse.setText("使用");
        }
        if (bgIndex == 8){
            buyoruse.setText("去获取");
        }
        //关闭pop
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //执行购买或者使用
        buyoruse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBuy) {
                    //获取电影票
                    if (bgIndex == 8){
                        startActivity(new Intent(meActivity, ShareWebViewActivity.class));
                        popupWindow.dismiss();
                        return;
                    }
                    // 执行购买方法
                    if (bgIndex == 5 || bgIndex == 6 || bgIndex == 7) {
                        initBuyVipPopWindow(view, bgIndex);
                        popupWindow.dismiss();
                    } else {
                        mPresenter.buyProps(bgIndex, 1, PTApplication.userToken, bgIndex, PTApplication.userId);
                        popupWindow.dismiss();
                    }

                } else {
                    // 执行使用方法
                    switch (bgIndex) {
                        //使用小纸条
                        case 0:
                            startActivity(new Intent(getActivity(), MySmallPaperActivity.class));
                            break;
                        //使用标签消除卡
                        case 1:
                            // 将 fragment_container View 中的内容替换为此 Fragment ，
                            Bundle bundle = new Bundle();
                            bundle.putInt("clearleabel", labelsmember);
                            transaction.replace(R.id.fl_content_me_activity, ClearLabelsFragment.newInstance(labelsmember));
                            // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                            transaction.addToBackStack(null);
                            // 执行事务
                            transaction.commit();
                            break;
                        //使用改名卡
                        case 2:
                            if (changeNameCount == 0) {
                                ToastUtils.getToast("改名卡不足,请购买");
                                popupWindow.dismiss();
                            } else {
                                popupWindow.dismiss();
                                initChangePop(v);
                            }
                            break;
                    }
                    popupWindow.dismiss();
                }
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void initChangePop(View v) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_changename, null);
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
        final EditText nickName = (EditText) contentView.findViewById(R.id.et_changename_pop);
        final Button dismiss = (Button) contentView.findViewById(R.id.bt_changename_cancel_pop);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Button success = (Button) contentView.findViewById(R.id.bt_changename_success_pop);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nickName.getText().toString().trim();
                mPresenter.changeNickName(newName, PTApplication.userToken, PTApplication.userId);
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 计算时间
     */
    private String calculateTime(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long now = (System.currentTimeMillis() + offSet) / 60000;
        long create = (time + offSet) / 60000;
        long diff = create - now;
        if (diff / 60 < 24) {
            return "(剩余0天)";
        } else {
            return "(剩余" + diff / 60 / 24 + "天)";
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


}
