package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.eventbus.EventBus;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.fragment.BeforeChangePhoneFragment;
import com.hzease.tomeet.me.ui.fragment.BindOthersFragment;
import com.hzease.tomeet.splash.ui.NoviceGuideActivity;
import com.hzease.tomeet.utils.GlideCatchUtil;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

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
     * 绑定手机号
     */
    @BindView(R.id.rl_setting_bindphone_fmt)
    RelativeLayout rl_setting_bindphone_fmt;
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
     * 接收新消息通知
     */
    /*@BindView(R.id.sv_setting_jpush_isopen_fmt)
    Switch sv_setting_jpush_isopen_fmt;*/
    @BindView(R.id.tv_setting_bindingphone_fmt)
    TextView tv_setting_bindingphone_fmt;
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
    private String phoneMum;
    private boolean isBind = false;

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
            R.id.rl_setting_bindphone_fmt,
            R.id.rl_setting_feedback_fmt,
            R.id.bt_setting_logout_fmt,
            R.id.arl_setting_clear,
            R.id.arl_setting_aboutus_fmt,
            R.id.rl_setting_bindothers_fmt,
            R.id.arl_setting_novice_fmt,
            R.id.rl_setting_use_fmt
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
            case R.id.rl_setting_bindphone_fmt:
                if (isBind) {
                    transaction.replace(R.id.fl_content_me_activity, BeforeChangePhoneFragment.newInstance());
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(5));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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
                getActivity().finish();
                break;
            case R.id.arl_setting_clear:
                GlideCatchUtil.getInstance().cleanCatchDisk();
                tv_setting_filesize_fmt.setText(GlideCatchUtil.getInstance().getCacheSize());
                ToastUtils.getToast("清除缓存");
                break;
            case R.id.arl_setting_aboutus_fmt:
                startActivity(new Intent(meActivity, AboutUsActivity.class));
                break;
            case R.id.rl_setting_bindothers_fmt:
                //进入绑定三方账号
                transaction.replace(R.id.fl_content_me_activity, BindOthersFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.arl_setting_novice_fmt:
                startActivity(new Intent(mContext, NoviceGuideActivity.class));
                break;
            case R.id.rl_setting_use_fmt:
                //进入绑定三方账号
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(6));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
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
    public void showMyRooms(boolean isSuccess,MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

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

    @Override
    public int getContentViewId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");

        }
        Logger.e("isReal" + PTApplication.myInfomation.getData().isAuthorized() + "RealName" + PTApplication.myInfomation.getData().getRealName());
        if (PTApplication.myInfomation.getData().isAuthorized()) {
            tv_setting_authentication_fmt.setText(PTApplication.myInfomation.getData().getRealName());
            rl_setting_authentication_fmt.setClickable(false);
        }
        if (!TextUtils.isEmpty(PTApplication.myInfomation.getData().getPhone())) {
            tv_setting_bindingphone_fmt.setText("已绑定");
            isBind = true;
        }
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        tv_setting_filesize_fmt.setText(GlideCatchUtil.getInstance().getCacheSize());
        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        /*sv_setting_jpush_isopen_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SpUtils.saveBoolean(mContext,"isOpenJpush",isChecked);
                    JPushInterface.resumePush(mContext);
                }else{
                    SpUtils.saveBoolean(mContext,"isOpenJpush",isChecked);
                    JPushInterface.stopPush(mContext);
                }
            }
        });
        boolean isOpenJpush = SpUtils.getBooleanValue(mContext, "isOpenJpush");
        sv_setting_jpush_isopen_fmt.setChecked(isOpenJpush);*/
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
    public void onEventMainThread(String s) {
        String msglog = "----onEventMainThread收到了消息：" + s;
        Logger.e(msglog);
        if (s.length() != 11){
            Logger.e(s);
            PTApplication.myInfomation.getData().setAuthorized(true);
            PTApplication.myInfomation.getData().setRealName(s);
        }else{
            Logger.e(s);
            PTApplication.myInfomation.getData().setPhone(s);
        }
    }
}
