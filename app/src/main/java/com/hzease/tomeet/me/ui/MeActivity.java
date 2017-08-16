package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.me.DaggerIMeComponent;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.MePresenter;
import com.hzease.tomeet.me.MePresenterModule;
import com.hzease.tomeet.me.ui.fragment.AuthenticationFragment;
import com.hzease.tomeet.me.ui.fragment.BadgeRechgreFragment;
import com.hzease.tomeet.me.ui.fragment.ChangePhoneFragment;
import com.hzease.tomeet.me.ui.fragment.ChangePwdFragment;
import com.hzease.tomeet.me.ui.fragment.FeedBackFragment;
import com.hzease.tomeet.me.ui.fragment.GameEvaluateFragment;
import com.hzease.tomeet.me.ui.fragment.PropsCenterFragment;
import com.hzease.tomeet.me.ui.fragment.UseSettingFragment;
import com.hzease.tomeet.utils.ActivityUtils;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import javax.inject.Inject;

import io.rong.eventbus.EventBus;

/**
 * Created by Key on 2017/3/8 14:27
 * email: MrKey.K@gmail.com
 * description:
 */

public class MeActivity extends NavigationActivity {

    @Inject
    MePresenter mMePresenter;


    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;

    public void onEventMainThread(EventBean.LoginInvalid loginInvalid) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_me;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        navigation_bottom.getMenu().findItem(R.id.navigation_me).setChecked(true).setEnabled(false);
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            /**
             * 个人中心fragment 0
             */
            MeFragment meFragment = MeFragment.newInstance();
            /**
             * 我的余额fragment 1
             */
            MyWalletFragment myWalletFragment = MyWalletFragment.newInstance();
            /**
             * 设置界面fragment 2
             */
            SettingFragment settingFragment = SettingFragment.newInstance();
            /**
             * 实名认证fragment 3
             */
            AuthenticationFragment authenticationFragment = AuthenticationFragment.newInstance();
            /**
             * 修改登录密码fragment 4
             */
            ChangePwdFragment changePwdFragment = ChangePwdFragment.newInstance();
            /**
             * 修改手机号fragment 5
             */
            ChangePhoneFragment changePhoneFragment = ChangePhoneFragment.newInstance();
            /**
             * 使用设置fragment 6
             */
            UseSettingFragment useSettingFragment = UseSettingFragment.newInstance();
            /**
             * 意见反馈fragment 7
             */
            FeedBackFragment feedBackFragment = FeedBackFragment.newInstance();
            /**
             * 道具中心fragment 8
             */
            PropsCenterFragment propsCenterFragment = PropsCenterFragment.newInstance();
            /**
             * 房间结束界面 9
             */
            // GameFinishFragment gameFinishFragment = GameFinishFragment.newInstance();
            /**
             * 待评价页面 9
             */
            GameEvaluateFragment gameEvaluateFragment = GameEvaluateFragment.newInstance();
            /**
             * 保证金充值界面
             */
            //RechargeFragment rechargeFragment = RechargeFragment.newInstance();

            mFragmentList.add(meFragment);
            mFragmentList.add(myWalletFragment);
            mFragmentList.add(settingFragment);
            mFragmentList.add(authenticationFragment);
            mFragmentList.add(changePwdFragment);
            mFragmentList.add(changePhoneFragment);
            mFragmentList.add(useSettingFragment);
            mFragmentList.add(feedBackFragment);
            mFragmentList.add(propsCenterFragment);
            mFragmentList.add(gameEvaluateFragment);
            /**
             * 徽章充值界面 10
             */
            mFragmentList.add(BadgeRechgreFragment.newInstance());
            //mFragmentList.add(gameFinishFragment);


            //mFragmentList.add(rechargeFragment);
            //mFragmentList.add(badgeRechgreFragment);

            //放到contentFrame_first这个容器中
        }
        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerIMeComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .mePresenterModule(new MePresenterModule(((IMeContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra("bindPhone",false)){
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(5), R.id.fl_content_me_activity);
        }else{
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_me_activity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
