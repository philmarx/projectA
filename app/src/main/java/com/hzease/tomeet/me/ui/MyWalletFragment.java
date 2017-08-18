package com.hzease.tomeet.me.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.RefundMoneyData;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.fragment.AllMoneyDetailsFragment;
import com.hzease.tomeet.me.ui.fragment.DepositFragment;
import com.hzease.tomeet.me.ui.fragment.RechargeFragment;
import com.hzease.tomeet.me.ui.fragment.WithdrawalsFragment;
import com.hzease.tomeet.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/23.
 */

public class MyWalletFragment extends BaseFragment implements IMeContract.View {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.tv_mewallet_amount_fmt)
    TextView tv_mewallet_amount_fmt;
    @BindView(R.id.tv_mewallet_lockamount_fmt)
    TextView tv_mewallet_lockamount_fmt;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private MeActivity meActivity;

    private boolean waitForRecharge = false;

    public MyWalletFragment() {
        // Required empty public constructor
    }


    public void onEventMainThread(EventBean.goOnRecharge goOnRecharge) {
        waitForRecharge = true;
    }

    @OnClick({
            R.id.bt_mywallet_recharge_fmt,
            R.id.tv_me_mywallet_details_fmt,
            R.id.bt_mywallet_bzmoney_fmt,
            R.id.iv_mywallet_rule_fmt,
            R.id.bt_mywallet_withdrawals_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            // 充值
            case R.id.bt_mywallet_recharge_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, RechargeFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            // 明细
            case R.id.tv_me_mywallet_details_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, AllMoneyDetailsFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.bt_mywallet_bzmoney_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, DepositFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.iv_mywallet_rule_fmt:
                //弹出钱包规则说明
                initRulePopWindow(v);
                break;
            case R.id.bt_mywallet_withdrawals_fmt:
                PTApplication.getRequestService().refundMoney(PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<RefundMoneyData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(RefundMoneyData refundMoneyData) {
                                if (refundMoneyData.isSuccess()){
                                    if (refundMoneyData.getData() == 0){
                                        // 将 fragment_container View 中的内容替换为此 Fragment ，
                                        transaction.replace(R.id.fl_content_me_activity, WithdrawalsFragment.newInstance());
                                        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                                        transaction.addToBackStack(null);
                                        // 执行事务
                                        transaction.commit();
                                    }else{
                                        ToastUtils.getToast("您还有可退还的保证金 请先退还保证金");
                                    }
                                }else{
                                    ToastUtils.getToast(refundMoneyData.getMsg());
                                }
                            }
                        });

                break;
        }
    }

    //显示规则说明图片
    private void initRulePopWindow(View v) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_mywalletrule, null);
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
        /*ImageView iv = (ImageView) contentView.findViewById(R.id.iv_rule);
        iv.setImageResource(R.drawable.wallet_rule);*/
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        if (waitForRecharge) {
            transaction.replace(R.id.fl_content_me_activity, RechargeFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
            waitForRecharge = false;
        }
    }

    public static MyWalletFragment newInstance() {
        return new MyWalletFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {
        tv_mewallet_amount_fmt.setText(String.format("%.2f", PTApplication.myInfomation.getData().getAmount() / 100.0));
        tv_mewallet_lockamount_fmt.setText(String.format("%.2f", PTApplication.myInfomation.getData().getLockAmount() / 100.0));
    }

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

    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

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
    public void showBuyPropsResult(int index,boolean success, String msg) {

    }

    @Override
    public void initResult(NoDataBean noDataBean) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mywallet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView =  getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        //mPresenter.loadMyInfo();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
