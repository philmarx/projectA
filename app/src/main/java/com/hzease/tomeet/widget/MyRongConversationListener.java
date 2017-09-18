package com.hzease.tomeet.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/23.
 */

public class MyRongConversationListener implements RongIM.ConversationBehaviorListener {

    public MyRongConversationListener() {
    }

    /**
     * 点击头像后的处理
     *
     * @param context
     * @param conversationType
     * @param userInfo
     * @return
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        // 用户ID 不为11位的都不进个人中心
        if (userInfo.getUserId().length() != 11) {
            return false;
        } else {
            Intent intent = new Intent(context, PersonOrderInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("userId", Long.valueOf(userInfo.getUserId()));
            intent.putExtras(bundle);
            context.startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        // Logger.e("message: " + message.getObjectName());
        if (message.getObjectName().equals("RC:ImgTextMsg")) {
            RichContentMessage richContentMessage = new RichContentMessage(message.getContent().encode());
            if (!TextUtils.isEmpty(richContentMessage.getExtra())) {
                // 跳转到房间
                Uri uri = Uri.parse(richContentMessage.getExtra());
                Logger.e("uri: " + uri.toString() + "   action: " + uri.getQueryParameter("action"));
                switch (uri.getQueryParameter("action")) {
                    case "invited":
                        // roomId
                        final String roomId = uri.getQueryParameter("key1");
                        if (PTApplication.myInfomation != null) {
                            PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e(e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            Logger.e(noDataBean.toString());
                                            if (noDataBean.isSuccess()) {
                                                PTApplication.getInstance().startActivity(new Intent(PTApplication.getInstance(), GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            } else {
                                                ToastUtils.getToast(noDataBean.getMsg());
                                            }
                                        }
                                    });
                        } else {
                            // 如果用户没登录
                            ToastUtils.getToast("请先登陆后再加入房间");
                        }
                        break;
                    case "share":
                        // userId
                        final String userId = uri.getQueryParameter("key1");
                        if (PTApplication.myInfomation != null) {
                            PTApplication.getRequestService().becameFriend(PTApplication.userToken, PTApplication.userId, userId, uri.getQueryParameter("key2"))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e(e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            Logger.e(noDataBean.toString());
                                            if (!TextUtils.isEmpty(noDataBean.getMsg()))
                                                ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    });
                        } else {
                            // 如果用户没登录
                            ToastUtils.getToast("请先登陆后再点此链接");
                        }
                        break;
                }
                return true;
            } else {
                return false;
            }
        } else if (message.getObjectName().equals("RC:TxtMsg")) {
            String extra = ((TextMessage) message.getContent()).getExtra();
            if (!TextUtils.isEmpty(extra)) {
                try {
                    JSONObject jsonObject = new JSONObject(extra);
                    String type = jsonObject.getString("type");
                    switch(type) {
                        case "note":
                            // 点击小纸条
                            Logger.e("note");
                            // TODO: 2017/9/15 显示小纸条
                            break;
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
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
