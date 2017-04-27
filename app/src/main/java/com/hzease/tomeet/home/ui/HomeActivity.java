package com.hzease.tomeet.home.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.home.DaggerIHomeComponent;
import com.hzease.tomeet.home.HomePresenter;
import com.hzease.tomeet.home.HomePresenterModule;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.home.ui.fragment.CreateRoomFragment;
import com.hzease.tomeet.utils.ActivityUtils;

public class HomeActivity extends NavigationActivity {

    //presenter 注入fragment中
    @Inject
    HomePresenter mHomePresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;



    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
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
        return R.layout.activity_home;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {

        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            HomeFragment homeFragment = HomeFragment.newInstance();
            /**
             * 创建房间fragment 1
             */
            CreateRoomFragment createRoomFragment = CreateRoomFragment.newInstance();
            mFragmentList.add(homeFragment);
            mFragmentList.add(createRoomFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_home_activity);
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerIHomeComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    // .homePresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                    .homePresenterModule(new HomePresenterModule(((IHomeContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
            navigation_bottom.getMenu().findItem(R.id.navigation_home).setChecked(true).setEnabled(false);
        }

    }

}
