package com.hzease.tomeet.chat.ui;

import android.os.Bundle;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.utils.ActivityUtils;

/**
 * Created by xuq on 2017/6/14.
 * 好友通过
 */

public class AddFriendActivity extends NetActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), AddFriendFragment.newInstance(), R.id.fl_content_add_friend_activity);
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }
}
