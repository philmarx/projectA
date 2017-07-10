package com.hzease.tomeet.splash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xuq on 2017/7/10.
 */

public class NoviceGuideActivity extends NetActivity {
    @BindView(R.id.iv_guide_resouse)
    ImageView iv_guide_resouse;
    @BindView(R.id.v_next_guide)
    View v_next_guide;
    int i=0;
    private int[] img = {R.drawable.novice_2,R.drawable.novice_3,R.drawable.novice_4,R.drawable.novice_5,R.drawable.novice_6,R.drawable.novice_7,
            R.drawable.novice_8,R.drawable.novice_9,R.drawable.novice_10,R.drawable.novice_11,R.drawable.novice_12,R.drawable.novice_13,R.drawable.novice_14};
    @OnClick(R.id.v_next_guide)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.v_next_guide:
                if (i == 13){
                    SpUtils.saveBoolean(this,"novice",true);
                    startActivity(new Intent(this, HomeActivity.class));
                }else{
                    iv_guide_resouse.setImageResource(img[i++]);
                }
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_noviceguide;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
