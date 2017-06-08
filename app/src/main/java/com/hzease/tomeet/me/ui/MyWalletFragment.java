package com.hzease.tomeet.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.fragment.AllMoneyDetailsFragment;
import com.hzease.tomeet.me.ui.fragment.DepositFragment;
import com.hzease.tomeet.me.ui.fragment.RechargeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    public MyWalletFragment() {
        // Required empty public constructor
    }

    @OnClick({
            R.id.bt_mywallet_recharge_fmt,
            R.id.tv_me_mywallet_details_fmt,
            R.id.bt_mywallet_bzmoney_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mywallet_recharge_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, RechargeFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // mPresenter.start();
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

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mywallet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        mPresenter.loadMyInfo();
    }
}
