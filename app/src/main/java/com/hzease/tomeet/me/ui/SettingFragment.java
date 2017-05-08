package com.hzease.tomeet.me.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.GlideCatchUtil;
import com.hzease.tomeet.utils.ToastUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class SettingFragment extends BaseFragment implements IMeContract.View {

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    /**
     * 实名认证
     */
    @BindView(R.id.rl_setting_authentication_fmt)
    RelativeLayout rl_setting_authentication_fmt;
    /**
     * 实名用户
     */
    @BindView(R.id.tv_setting_authentication_fmt)
    TextView tv_setting_authentication_fmt;
    /**
     * 修改登录密码
     */
    @BindView(R.id.rl_setting_changepwd_fmt)
    RelativeLayout rl_setting_changepwd_fmt;
    /**
     * 修改支付宝
     */
    @BindView(R.id.rl_setting_changealipay_fmt)
    RelativeLayout rl_setting_changealipay_fmt;
    /**
     * 使用设置
     */
    @BindView(R.id.rl_setting_use_fmt)
    RelativeLayout rl_setting_use_fmt;
    /**
     * 清除缓存
     */
    @BindView(R.id.arl_setting_clear)
    AutoRelativeLayout arl_setting_clear;
    /**
     * 缓存大小
     */
    @BindView(R.id.tv_setting_filesize_fmt)
    TextView tv_setting_filesize_fmt;
    /**
     * 意见反馈
     */
    @BindView(R.id.rl_setting_feedback_fmt)
    RelativeLayout rl_setting_feedback_fmt;
    @BindView(R.id.arl_setting_aboutus_fmt)
    AutoRelativeLayout arl_setting_aboutus_fmt;
    /**
     * 退出当前账号
     */
    @BindView(R.id.bt_setting_logout_fmt)
    Button bt_setting_logout_fmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    private boolean isAuthorizedSuccess;
    private String authorizedName;

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    /**
     * 点击事件
     *
     * @return
     */
    @OnClick({
            R.id.rl_setting_authentication_fmt,
            R.id.rl_setting_changepwd_fmt,
            R.id.rl_setting_changealipay_fmt,
            R.id.rl_setting_use_fmt,
            R.id.rl_setting_feedback_fmt,
            R.id.bt_setting_logout_fmt,
            R.id.arl_setting_clear,
            R.id.arl_setting_aboutus_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_authentication_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(3));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_changepwd_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(4));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_changealipay_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(5));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_use_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(6));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_feedback_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(7));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bt_setting_logout_fmt:
                mPresenter.logoutUser();
                startActivity(new Intent(getContext(), HomeActivity.class));
                break;
            case R.id.arl_setting_clear:
                GlideCatchUtil.getInstance().cleanCatchDisk();
                tv_setting_filesize_fmt.setText(GlideCatchUtil.getInstance().getCacheSize());
                ToastUtils.getToast(PTApplication.getInstance(),"清除缓存");
                break;
            case R.id.arl_setting_aboutus_fmt:
                startActivity(new Intent(meActivity,AboutUsActivity.class));
                break;
        }
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }
    @Override
    public void showMyRooms(HomeRoomsBean myJoinRoomBean,boolean isLoadMore) {

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
    public int getContentViewId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        SharedPreferences sp = meActivity.getSharedPreferences("game_name", Context.MODE_PRIVATE);
        isAuthorizedSuccess = sp.getBoolean("isAuthorizedSuccess", false);
        authorizedName = sp.getString("authorizedName", "");
        if (isAuthorizedSuccess){
            rl_setting_authentication_fmt.setClickable(false);
        }else{
            rl_setting_authentication_fmt.setClickable(true);
        }
        tv_setting_filesize_fmt.setText(GlideCatchUtil.getInstance().getCacheSize());
        tv_setting_authentication_fmt.setText(authorizedName);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
