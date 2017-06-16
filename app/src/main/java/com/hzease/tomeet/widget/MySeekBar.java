package com.hzease.tomeet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xw.repo.BubbleSeekBar;

/**
 * Created by Key on 2017/6/16 21:48
 * email: MrKey.K@gmail.com
 * description: 重写onTouchEvent
 */

public class MySeekBar extends BubbleSeekBar {

    private boolean flag;

    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                flag = false;
                break;
        }
        getParent().getParent().requestDisallowInterceptTouchEvent(flag);
        return super.onTouchEvent(event);
    }
}
