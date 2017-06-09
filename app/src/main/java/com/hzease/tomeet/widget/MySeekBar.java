package com.hzease.tomeet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by xuq on 2017/6/9.
 */

public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar {

    private Context context;
    private  int oldsign;

    public MySeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO 自动生成的方法存根
                if(progress>oldsign+3||progress<oldsign-3){
                    seekBar.setProgress(oldsign);
                    return;
                }
                seekBar.setProgress(progress);
                oldsign = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO 自动生成的方法存根
                seekBar.setProgress(oldsign);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO 自动生成的方法存根

            }

        });
    }

}
