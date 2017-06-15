package com.hzease.tomeet.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hzease.tomeet.R;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.ScreenUtils;

/**
 * Created by xuq on 2017/6/14.
 */

public class GuideUtil {
    private Context context;
    private ImageView imgView;
    private WindowManager windowManager;
    private static GuideUtil instance = null;
    /** 是否第一次进入该程序 **/
    private boolean isFirst = true;

    private int i =0;


    int img[] = new int[]{R.drawable.guide_friend_gold,R.drawable.guide_friend_blue,R.drawable.guide_friend_green,R.drawable.guide_friend_gray,R.drawable.guide_friend_red};



    private GuideUtil() {
    }

    public static GuideUtil getInstance() {
        synchronized (GuideUtil.class) {
            if (null == instance) {
                instance = new GuideUtil();
            }
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    // 设置LayoutParams参数
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    // 设置显示格式
                    params.format = PixelFormat.RGBA_8888;
                    // 设置对齐方式
                    params.gravity = Gravity.LEFT | Gravity.TOP;
                    // 设置宽高
                    params.width = MyScreeUtils.getScreenWidth(context);
                    params.height = MyScreeUtils.getScreenHeight(context);
                    // 设置动画
                    //params.windowAnimations = R.style.view_anim;

                    // 添加到当前的窗口上
                    windowManager.addView(imgView, params);
                    break;
            }
        };
    };

    public void initGuide(Activity context, final int[] pics) {
        if (!isFirst) {
            return;
        }
        this.context = context;
        windowManager = context.getWindowManager();

        // 动态初始化图层
        imgView = new ImageView(context);
        imgView.setLayoutParams(new WindowManager.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setImageResource(pics[0]);
        imgView.setAlpha(0.8f);
        handler.sendEmptyMessage(1);

        // 点击图层之后，将图层移除
        imgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //windowManager.removeView(imgView);//这句话是移除当前ImageView的
                Logger.e("i   " +  i);
                if (i<pics.length-1){
                    i++;
                    imgView.setImageResource(pics[i]);
                }else if (i==pics.length-1){
                    i=0;
                    windowManager.removeView(imgView);
                    setFirst(false);
                }
            }
        });
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

}
