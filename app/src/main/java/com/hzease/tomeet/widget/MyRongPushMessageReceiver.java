package com.hzease.tomeet.widget;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Key on 2017/8/1 14:58
 * email: MrKey.K@gmail.com
 * description:
 */

public class MyRongPushMessageReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        // 打开聊天界面
        //Logger.e(pushNotificationMessage.getExtra() + "  " + pushNotificationMessage.getPushContent() + "   " + pushNotificationMessage.getPushData() + "   " + pushNotificationMessage.getPushFlag());
        //RongIM.getInstance().startConversation(context, Conversation.ConversationType.setValue(pushNotificationMessage.getConversationType().getValue()), pushNotificationMessage.getTargetId(), pushNotificationMessage.getTargetUserName());
        return false;
    }
}
