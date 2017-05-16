package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.data.SimpleUserInfoBean;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 隐式意图启动的会话列表，融云自带的界面
 */
public class ChatConversationActivity extends NetActivity {

    private String targetId;
    private Realm mRealm;
    private UserInfo myInfo;
    private Map<String, UserInfo> userInfoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_conversation;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        userInfoMap = new HashMap<>();
        targetId = getIntent().getData().getQueryParameter("targetId");
        myInfo = new UserInfo(PTApplication.userId, PTApplication.myInfomation.getData().getNickname(), Uri.parse(AppConstants.YY_PT_OSS_USER_PATH_MYSELF + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL + "#" + PTApplication.myInfomation.getData().getAvatarSignature()));
        ((TextView) findViewById(R.id.conversation_title_nickName)).setText(getIntent().getData().getQueryParameter("title"));


        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                return getUserInfoObject(userId);
            }
        }, true);
    }


    public UserInfo getUserInfoObject(final String userId) {
        Logger.e("getUserInfo: " + userId);

        if (userId.equals(PTApplication.userId)) {
            return myInfo;
        } else {
            if (!userInfoMap.containsKey(userId)) {
                if (mRealm == null) {
                    mRealm = Realm.getDefaultInstance();
                }
                RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", Long.valueOf(targetId)).findFirst();
                if (friendBean != null) {
                    userInfoMap.put(userId, new UserInfo(userId, friendBean.getNickname(), Uri.parse(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL + "#" + friendBean.getAvatarSignature())));
                } else {
                    // 先只获取头像
                    userInfoMap.put(userId, new UserInfo(userId, "", Uri.parse(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)));

                    // 等回调回来后去更新昵称和ID
                    PTApplication.getRequestService().getOtherAvatar(userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<SimpleUserInfoBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(SimpleUserInfoBean simpleUserInfoBean) {
                                    userInfoMap.put(userId, new UserInfo(userId, simpleUserInfoBean.getData().getNickname(), Uri.parse(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL + "#" + simpleUserInfoBean.getData().getAvatarSignature())));
                                }
                            });
                }
            }

            return userInfoMap.get(userId);
        }
    }


    /**
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("request: " + requestCode + "   resultCode:    " + resultCode);
        Logger.e("request " + R.id.rc_ext_my_location);

        String uri = data.getStringExtra("thumb");
        double lat = data.getDoubleExtra("lat", 100.00);
        double lng = data.getDoubleExtra("lng", 100.00);
        String poi = data.getStringExtra("poi");
        Logger.e("uri" + uri + "\nlat" + lat + "\nlng" + lng + "\npoi" + poi);
        LocationMessage message = LocationMessage.obtain(lat, lng, poi, Uri.parse(uri));
        Message sendMessage = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, message);
        RongIM.getInstance().sendLocationMessage(sendMessage, null, null, new IRongCallback.ISendMediaMessageCallback() {
            @Override
            public void onProgress(Message message, int i) {
                Logger.e("onProgress" + message.getContent());
            }

            @Override
            public void onCanceled(Message message) {
                Logger.e("onCanceled" + message.getContent());
            }

            @Override
            public void onAttached(Message message) {
                Logger.e("onAttached" + new String(message.getContent().encode()));
            }

            @Override
            public void onSuccess(Message message) {
                Logger.e("onSuccess" + message.getContent());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                Logger.e("onError" + new String(message.getContent().encode()));
            }
        });
    }
}
