package com.hzease.tomeet.circle.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.CirclePresenter;
import com.hzease.tomeet.circle.CirclePresenterModule;
import com.hzease.tomeet.circle.DaggerICircleComponent;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.fragment.CircleFragment;
import com.hzease.tomeet.circle.fragment.CircleInfoFragment;
import com.hzease.tomeet.circle.fragment.CreateCircleFragmentFinish;
import com.hzease.tomeet.circle.fragment.CreateCircleFragmentFirst;
import com.hzease.tomeet.circle.fragment.MotifityCircleFragment;
import com.hzease.tomeet.circle.fragment.MyCircleFragment;
import com.hzease.tomeet.circle.fragment.MySpeachFragment;
import com.hzease.tomeet.utils.ActivityUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class CircleActivity extends NavigationActivity {

    private OnBackPressedListener mListener;

    @Override
    public void onBackPressed() {
        if (!((CircleFragment) mListener).isVisible() || mListener.myOnBackPressed()) {
            super.onBackPressed();
        }
    }

    public interface OnBackPressedListener {
        boolean myOnBackPressed();
    }

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    @Inject
    CirclePresenter mCriclePresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;
    @BindView(R.id.rb_friend_fmt)
    RadioButton rb_friend_fmt;
    @BindView(R.id.rb_circle_fmt)
    RadioButton rb_circle_fmt;
    @BindView(R.id.rg_circle_selector)
    RadioGroup rg_circle_selector;

    //设置第一个按钮是否选中
    private boolean isChecked = true;

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
        return R.layout.activity_circle;
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
            CircleFragment circleFragment = CircleFragment.newInstance();
            mListener = circleFragment;
            //圈子Fragment
            MyCircleFragment myCircleFragment = MyCircleFragment.newInstance();
            //圈子详情Fragment
            CircleInfoFragment circleInfoFragment = CircleInfoFragment.newInstance();
            //修改圈子公告
            MotifityCircleFragment motifityCircleFragment = MotifityCircleFragment.newInstance();
            //创建圈子第一步 4
            CreateCircleFragmentFirst createCircleFragmentFirst = CreateCircleFragmentFirst.newInstance();
            //创建圈子第二步 5
            CreateCircleFragmentFinish createCircleFragmentFinish = CreateCircleFragmentFinish.newInstance();
            //我的喊话 6
            MySpeachFragment mySpeachFragment = MySpeachFragment.newInstance();
            mFragmentList.add(circleFragment);
            mFragmentList.add(myCircleFragment);
            mFragmentList.add(circleInfoFragment);
            mFragmentList.add(motifityCircleFragment);
            mFragmentList.add(createCircleFragmentFirst);
            mFragmentList.add(createCircleFragmentFinish);
            mFragmentList.add(mySpeachFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_bidding_activity);
            //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(4), R.id.fl_content_bidding_activity);
        }
        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerICircleComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .circlePresenterModule(new CirclePresenterModule(((ICircleContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }
        rb_friend_fmt.setChecked(isChecked);
        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_friend_fmt:
                        //replaceFragment(new CircleFragment());
                        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
                        fragmentManager = getSupportFragmentManager();
                        // 2.开启一个事务，通过调用beginTransaction()方法开启
                        transaction = fragmentManager.beginTransaction();
                        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
                        transaction.replace(R.id.fl_content_bidding_activity, mFragmentList.get(0));  //fr_container不能为fragment布局，可使用线性布局相对布局等。
                        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
                        //transaction.addToBackStack(null);
                        // 5.提交事物,调用commit()方法来完成
                        transaction.commit();
                        break;
                    case R.id.rb_circle_fmt:
                        // replaceFragment(new MyCircleFragment());
                        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
                        fragmentManager = getSupportFragmentManager();
                        // 2.开启一个事务，通过调用beginTransaction()方法开启
                        transaction = fragmentManager.beginTransaction();
                        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
                        transaction.replace(R.id.fl_content_bidding_activity, mFragmentList.get(1));  //fr_container不能为fragment布局，可使用线性布局相对布局等。
                        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
                        //transaction.addToBackStack(null);
                        // 5.提交事物,调用commit()方法来完成
                        transaction.commit();
                        isChecked = false;
                        break;
                }
            }
        });
        navigation_bottom.getMenu().findItem(R.id.navigation_circle).setChecked(true).setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Logger.e("temp" + flag);
        if (flag == 4) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(4), R.id.fl_content_bidding_activity);
        }*/
    }
//Fragment启动方法：
    /*private void replaceFragment(Fragment fragment) {
        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager =getSupportFragmentManager();
        // 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fl_content_bidding_activity, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        //transaction.addToBackStack(null);
        // 5.提交事物,调用commit()方法来完成
        transaction.commit();
    }*/
}
