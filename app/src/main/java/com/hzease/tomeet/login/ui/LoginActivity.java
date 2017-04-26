package com.hzease.tomeet.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

import javax.inject.Inject;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.login.DaggerILoginComponent;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.login.LoginPresenter;
import com.hzease.tomeet.login.LoginPresenterModule;
import com.hzease.tomeet.utils.ActivityUtils;

public class LoginActivity extends NetActivity {


    @Inject
    LoginPresenter mLoginPresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;



    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            /**
             * 登录fragment 0
             */
            LoginFragment loginFragment = LoginFragment.newInstance();
            /**
             * 忘记密码fragment 1
             */
            FindPwdFragment findPwdFragment = FindPwdFragment.newInstance();
            /**
             * 完善信息fragment 2
             */
            FinishInfoFragment finishInfoFragment = FinishInfoFragment.newInstance();
            /**
             * 设置新密码
            SetNewPwdFragment setNewPwdFragment = SetNewPwdFragment.newInstance();*/
            mFragmentList.add(loginFragment);
            mFragmentList.add(findPwdFragment);
            mFragmentList.add(finishInfoFragment);
            //mFragmentList.add(setNewPwdFragment);
            //放到contentFrame_first这个容器中
            if (PTApplication.myInfomation == null) {
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_login_activity);
            } else {
                if (!PTApplication.myInfomation.getData().isIsInit()) {
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(2), R.id.fl_content_login_activity);
                }
            }
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0;i<size;i++){
            DaggerILoginComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    // .loginPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                    .loginPresenterModule(new LoginPresenterModule(((ILoginContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
