package com.hzease.tomeet.home.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.home.DaggerIHomeComponent;
import com.hzease.tomeet.home.HomePresenter;
import com.hzease.tomeet.home.HomePresenterModule;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.utils.ActivityUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import cn.magicwindow.mlink.annotation.MLinkDefaultRouter;

@MLinkDefaultRouter
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
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }*/

        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            HomeFragment homeFragment = HomeFragment.newInstance();
            /**
             * 创建房间fragment 1
             */
            mFragmentList.add(homeFragment);
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

    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        // 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 设置状态栏颜色
        // 由于setStatusBarColor()这个API最低版本支持21, 本人的是15,所以如果要设置颜色,自行到style中通过配置文件设置
        window.setStatusBarColor(getResources().getColor(R.color.transparent0));
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            // 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusBarHeight();
        int statusColor = getResources().getColor(R.color.colorPrimary);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null && mTopView.getLayoutParams() != null &&
                mTopView.getLayoutParams().height == statusBarHeight) {
            // 避免重复添加 View
            mTopView.setBackgroundColor(statusColor);
            return;
        }
        // 使 ChildView 预留空间
        if (mTopView != null) {
            ViewCompat.setFitsSystemWindows(mTopView, true);
        }

        //添加假 View
        mTopView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        mTopView.setBackgroundColor(statusColor);
        mContentView.addView(mTopView, 0, lp);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

}
