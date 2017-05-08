package com.hzease.tomeet.splash.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.home.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends NetActivity {


    @BindView(R.id.tp_guide_pic_aty)
    ViewPager tp_guide_pic_aty;
    @BindView(R.id.bt_guide_immediately_aty)
    Button bt_guide_immediately_aty;
    private List<ImageView> list;
    //引导页图片
    int[] guides = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    @OnClick({
            R.id.bt_guide_immediately_aty
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_guide_immediately_aty:
                startActivity(new Intent(GuideActivity.this, HomeActivity.class));
                SharedPreferences sp = getSharedPreferences("game_name", Context.MODE_PRIVATE);
                sp.edit().putBoolean("isGuide", true).apply();
                finish();
                break;
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        // 缩放动画
        final ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scale.setDuration(1000);
        //监听动画的执行状态，如果动画正在执行，不能进行滑动操作
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tp_guide_pic_aty.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tp_guide_pic_aty.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        initDatas();
        //viewpager设置适配器
        tp_guide_pic_aty.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((ImageView) object);
            }
        });
        //viewpager设置监听
        tp_guide_pic_aty.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    bt_guide_immediately_aty.setVisibility(View.VISIBLE);
                    bt_guide_immediately_aty.startAnimation(scale);
                } else {
                    bt_guide_immediately_aty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDatas() {
        list = new ArrayList<>();
        for (int i = 0; i < guides.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(guides[i]);// 注意设置背景, 才可以填充屏幕
            list.add(image);
        }
    }

}
