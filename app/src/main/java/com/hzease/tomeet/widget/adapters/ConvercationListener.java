package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hzease.tomeet.PersonOrderInfoActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by xuq on 2017/6/23.
 */

public class ConvercationListener  implements RongIM.ConversationBehaviorListener{
    private Activity activity;
    public ConvercationListener(Activity activity) {
        this.activity = activity;
    }

    /**
     * 点击头像后的处理
     * @param context
     * @param conversationType
     * @param userInfo
     * @return
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Intent intent = new Intent(activity, PersonOrderInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("userId",Long.valueOf(userInfo.getUserId()));
        intent.putExtras(bundle);
        activity.startActivity(intent);
        return true;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
}
