package com.hzease.tomeet.chat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.model.Event;
import io.rong.imlib.model.Message;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.chat.IChatContract;
import com.hzease.tomeet.widget.adapters.ConversationAdapter;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatFragment extends BaseFragment implements IChatContract.View {

    @BindView(R.id.rv_conversation_list_chat_fmt)
    RecyclerView rv_conversation_list_chat_fmt;


    private IChatContract.Presenter mPresenter;
    private ConversationAdapter conversationAdapter;

    private String mChatingId = "";


    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void setPresenter(@NonNull IChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_chat;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        // 注册event
        EventBus.getDefault().register(this);
        InternalModuleManager.getInstance().onLoaded();


        //
        conversationAdapter = new ConversationAdapter(mContext);
        conversationAdapter.setOnItemClickListener(new ConversationAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, final String tag) {
                // 点击后进入会话
                RongIM.getInstance().startPrivateChat(mContext, tag, "标题");
                //RongIM.getInstance().startChatRoomChat(mContext,"100000",true);
                //Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase()).appendQueryParameter("targetId", tag).appendQueryParameter("title", "标题").build();
                //mContext.startActivity(new Intent("android.intent.action.VIEW", uri));
                /*Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon().appendPath("conversation").appendPath("chatroom").appendQueryParameter("targetId", "10000").build();
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.putExtra("createIfNotExist", true);
                mContext.startActivity(intent);*/
                // 记录打开会话的ID
                mChatingId = tag;
            }
        });
        rv_conversation_list_chat_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_conversation_list_chat_fmt.setAdapter(conversationAdapter);
    }

    // 发出消息的event
    public void onEventMainThread(Message message) {
        Logger.w("发出消息的event: " + message.getSentStatus() + "  " + new String(message.getContent().encode()) + "  发送时间: " + message.getSentTime());
        // TODO: 2017/3/31 发送失败的效果还没处理
        switch(message.getSentStatus()) {
            case SENDING:
                mPresenter.sendMessage(message);
                break;
        }
    }

    // 接收到消息的event
    public void onEventMainThread(final Event.OnReceiveMessageEvent event) {
        Logger.d("onEventMainThread   MessageContentEncode: " + new String(event.getMessage().getContent().encode())
                + "   getTargetId: " + event.getMessage().getTargetId() + "   Left: " +event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName());

        // 更新好友数据(刷新列表)
        mPresenter.updateFriendsDate();
    }

    @OnClick({R.id.rb_gold_chat_fmt,
            R.id.rb_blue_chat_fmt,
            R.id.rb_green_chat_fmt,
            R.id.rb_gray_chat_fmt,
            R.id.rb_red_chat_fmt})
    public void onClick(View view) {
        // 默认金色,注释掉金色,那没有符合的case就是金色
        int[] type = AppConstants.GOLD_POINT;
        switch (view.getId()) {
            /*case R.id.rb_gold_chat_fmt:
                Logger.i("切换到金色");
                type = AppConstants.GOLD_POINT;
                break;*/
            case R.id.rb_blue_chat_fmt:
                // Logger.i("切换到蓝色");
                type = AppConstants.BLUE_POINT;
                break;
            case R.id.rb_green_chat_fmt:
                // Logger.i("切换到绿色");
                type = AppConstants.GREEN_POINT;
                break;
            case R.id.rb_gray_chat_fmt:
                // Logger.i("切换到灰色");
                type = AppConstants.GRAY_POINT;
                break;
            case R.id.rb_red_chat_fmt:
                // Logger.i("切换到红色");
                type = AppConstants.RED_POINT;
                break;
        }
        conversationAdapter.switchFriends(type);
    }

    /**
     * 更新好友列表
     */
    @Override
    public void updateFriendList() {
        conversationAdapter.updateFriends();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 如果是从打开的会话中回来.就清空未读
        if (!TextUtils.isEmpty(mChatingId)) {
            Logger.d(mChatingId);
            mPresenter.clearUnread(mChatingId);
            mChatingId = "";
        }

        mPresenter.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
