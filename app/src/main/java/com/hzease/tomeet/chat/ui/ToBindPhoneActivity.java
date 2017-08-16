package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.me.ui.MeActivity;

import butterknife.OnClick;

/**
 * Created by xuq on 2017/8/15.
 */

public class ToBindPhoneActivity extends NetActivity {

    @OnClick(R.id.bt_tobindphone_act)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_tobindphone_act:
                Intent intent = new Intent(this, MeActivity.class);
                intent.putExtra("bindPhone",true);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bindphone;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }
}
