package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ui.CircleInfoActivity;
import com.hzease.tomeet.widget.MyRongConversationListener;
import com.orhanobut.logger.Logger;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.LocationMessage;

/**
 * 隐式意图启动的会话列表，融云自带的界面
 */
public class  ChatConversationActivity extends NetActivity {

    private String targetId;


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
        //AndroidBug5497WorkaroundActivity.assistActivity(this);
        targetId = getIntent().getData().getQueryParameter("targetId");
        if ("888888".equals(targetId)){
            findViewById(R.id.conversation_person_space).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.conversation_title_nickName)).setText(getIntent().getData().getQueryParameter("title"));
        findViewById(R.id.conversation_person_space).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetId.length() == 11) {
                    Intent intent = new Intent(ChatConversationActivity.this, PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", Long.valueOf(targetId));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ChatConversationActivity.this, CircleInfoActivity.class);
                    intent.putExtra("circleId", Long.valueOf(targetId));
                    startActivity(intent);
                }
            }
        });
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
        if (requestCode == 66306 && resultCode == RESULT_OK) {
            Logger.e("request: " + requestCode + "   resultCode:    " + resultCode);
            Logger.e("request " + R.id.rc_ext_my_location);
            String uri = data.getStringExtra("thumb");
            double lat = data.getDoubleExtra("lat", 100.00);
            double lng = data.getDoubleExtra("lng", 100.00);
            String poi = data.getStringExtra("poi");
            Logger.e("uri" + uri + "\nlat" + lat + "\nlng" + lng + "\npoi" + poi);
            LocationMessage message = LocationMessage.obtain(lat, lng, poi, Uri.parse(uri));
            Message sendMessage = null;
            if (targetId.length() == 12) {
                sendMessage = Message.obtain(targetId, Conversation.ConversationType.GROUP, message);
            } else if (targetId.length() == 11) {
                sendMessage = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, message);
            }
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
}
