package com.hzease.tomeet.home.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.game.ui.ChatRoomSetActivity;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.utils.AndroidBug5497Workaround;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.ChatRoomMemberListAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.IExtensionClickListener;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.manager.IAudioPlayListener;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.Event;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/11/2.
 */

public class ChatRoomFragment extends BaseFragment implements IHomeContract.View, IExtensionClickListener, HomeActivity.OnBackPressedListener {

    // 底部栏
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.rc_extension_gamechatroom_fmt)
    RongExtension mRongExtension;
    //聊天室名称
    @BindView(R.id.tv_chatroom_name_fmt)
    TextView tv_chatroom_name_fmt;
    //聊天室名称
    @BindView(R.id.tv_chatroom_membernum_fmt)
    TextView tv_chatroom_membernum_fmt;
    //聊天室设置
    @BindView(R.id.iv_chatroom_setting_fmt)
    ImageView iv_chatroom_setting_fmt;
    //聊天室成员列表
    @BindView(R.id.rv_chatroom_memberlist_fmt)
    RecyclerView rv_chatroom_memberlist_fmt;
    //聊天室会话页面
    @BindView(R.id.rv_chatroom_conversationlist_fmt)
    RecyclerView rv_chatroom_conversationlist_fmt;

    private IHomeContract.Presenter mPresenter;
    private Conversation.ConversationType mConversationType;
    private String chatRoomId;
    private long mJoinedRoomTime;
    private MultiItemTypeAdapter messageMultiItemTypeAdapter;
    private FragmentTransaction transaction;
    private HomeActivity homeActivity;
    private List<Message> mConversationList;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mJoinRoomMembersList;
    private boolean isAnonymity;
    private LinearLayoutManager linearLayoutManager;
    private ChatRoomMemberListAdapter adapter;

    // 聊天室信息
    private Bundle roomInfo = new Bundle();

    @OnClick({
            R.id.iv_back_chatroom_fmt,
            R.id.iv_chatroom_setting_fmt
    })
    public void onClick(View view) {
        switch (view.getId()) {
            //退出房间
            case R.id.iv_back_chatroom_fmt:
                leaveChatroom();
                //getActivity().onBackPressed();
                homeActivity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.iv_chatroom_setting_fmt:
                startActivity(new Intent(getContext(), ChatRoomSetActivity.class).putExtras(roomInfo));
                break;
        }
    }

    public void leaveChatroom() {
        PTApplication.getRequestService().leaveRoom(PTApplication.userToken, PTApplication.userId, chatRoomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.d("离开聊天室: " + noDataBean.toString());
                        if (!noDataBean.isSuccess()) {
                            ToastUtils.getToast("离开聊天室失败：" + noDataBean.getMsg());
                        }
                    }
                });
    }

    public static ChatRoomFragment newInstance() {
        return new ChatRoomFragment();
    }

    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {

        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_chatroom;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mConversationList = new ArrayList<>();
        mJoinRoomMembersList = new ArrayList<>();


        Bundle arguments = getArguments();
        chatRoomId = arguments.getString("chatroomId");
        homeActivity = (HomeActivity) getActivity();
        transaction = homeActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        //隐藏底部布局
        if (bottomNavigationView.getVisibility() == View.VISIBLE) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        mConversationType = Conversation.ConversationType.CHATROOM;
        mRongExtension.setExtensionClickListener(this);
        mRongExtension.setFragment(this);
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {
                EditText inputEditText = mRongExtension.getInputEditText();
                inputEditText.getText().insert(inputEditText.getSelectionStart(), emoji);
            }

            @Override
            public void onDeleteClick() {
                mRongExtension.getInputEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        mRongExtension.addEmoticonTab(emojiTab, "1");

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus: " + getClass());
        }


        // 加入cmd聊天室
        RongIMClient.getInstance().joinChatRoom("cmd" + chatRoomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.i("加入cmd聊天室 第一次  加入成功： " + "cmd" + chatRoomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("加入cmd聊天室 第一次 errorCode: " + errorCode.getMessage());
                SystemClock.sleep(1000);
                RongIMClient.getInstance().joinChatRoom("cmd" + chatRoomId, -1, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Logger.i("加入cmd聊天室 第二次  加入成功： " + "cmd" + chatRoomId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Logger.e("加入cmd聊天室 第二次 errorCode: " + errorCode.getMessage());
                        ToastUtils.getToast("加入聊天室失败");
                    }
                });
            }
        });

        // 加入融云聊天室
        RongIMClient.getInstance().joinChatRoom(chatRoomId, 50, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                mJoinedRoomTime = System.currentTimeMillis();
                Logger.i("加入成功（第一次）： " + chatRoomId + "   加入成功之后：" + mJoinedRoomTime);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("加入融云聊天室 第一次 失败 errorCode: " + errorCode.getMessage());
                SystemClock.sleep(1000);
                RongIMClient.getInstance().joinChatRoom(chatRoomId, 50, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        mJoinedRoomTime = System.currentTimeMillis();
                        Logger.i("加入成功（第二次）： " + chatRoomId + "   加入成功之后：" + mJoinedRoomTime);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Logger.e("加入融云聊天室 第二次 失败 errorCode: " + errorCode.getMessage());
                        ToastUtils.getToast("加入聊天室失败!");
                    }
                });
            }
        });

        // 会话列表adapter
        messageMultiItemTypeAdapter = new MultiItemTypeAdapter<>(mContext, mConversationList);
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgInfoItemDelagate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingVcItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendVcItemDelegate());
        rv_chatroom_conversationlist_fmt.setAdapter(messageMultiItemTypeAdapter);
        rv_chatroom_conversationlist_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置rv为横向滑动
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_chatroom_memberlist_fmt.setLayoutManager(linearLayoutManager);

        // 初始化完的最后一步加载数据
        if (mPresenter != null) {
            mPresenter.loadChatRoom(chatRoomId);
        } else {
            ToastUtils.getToast("数据加载失败，请重新进入");
        }

    }


    // 接收到消息的event
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Logger.i("聊天室  接收  Content: " + new String(event.getMessage().getContent().encode())
                + "\ngetTargetId: " + event.getMessage().getTargetId() + "   Left: " + event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName() + "\nSenderUserId: " + event.getMessage().getSenderUserId()
                + "    MessageDirection: " + event.getMessage().getMessageDirection() + "   ConversationType: " + event.getMessage().getConversationType());
        if (event.getMessage().getConversationType().equals(Conversation.ConversationType.CHATROOM)
                && (event.getMessage().getTargetId().equals(chatRoomId) || event.getMessage().getTargetId().equals("cmd" + chatRoomId))
                //&& (event.getMessage().getObjectName().equals("RC:TxtMsg") || event.getMessage().getObjectName().equals("RC:InfoNtf"))) {
                && (event.getMessage().getObjectName().equals("RC:TxtMsg") || event.getMessage().getObjectName().equals("RC:InfoNtf") || event.getMessage().getObjectName().equals("RC:VcMsg"))) {
            // 插入数据源
            mConversationList.add(event.getMessage());
            // 更新界面
            messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
            rv_chatroom_conversationlist_fmt.smoothScrollToPosition(mConversationList.size());
        }
    }

    // 发出消息的event 和 cmdMsg
    public void onEventMainThread(Message message) {
        Logger.i("聊天室发出消息和CMD的event: " + message.getSentStatus() + "    TargetId: " + message.getTargetId() + "   Sender: " + message.getSenderUserId() + "\n" + new String(message.getContent().encode()) + "  Object: " + message.getObjectName() + "  Type: " + message.getConversationType());
        if (message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
            if ("RC:CmdMsg".equals(message.getObjectName()) && ("cmd" + chatRoomId).equals(message.getTargetId())) {
                if (message.getSentTime() > mJoinedRoomTime) {
                    CommandMessage cmdMsg = new CommandMessage(message.getContent().encode());
                    Logger.e(cmdMsg.getName());
                    switch (cmdMsg.getName()) {
                        // 刷新房间
                        case "refreshRoom":
                            //mPresenter.getGameChatRoomInfo(roomId);
                            mPresenter.loadChatRoom(chatRoomId);
                            break;
                        // 房间被解散
                        case "roomDissolve":
                            //this.getActivity().finish();
                            ToastUtils.getToast("该聊天室已被解散！");
                            break;
                    }
                }
            } else if (chatRoomId.equals(message.getTargetId()) && PTApplication.userId.equals(message.getSenderUserId())) {
                if ("RC:TxtMsg".equals(message.getObjectName()) || "RC:VcMsg".equals(message.getObjectName())) {
                    if (Message.SentStatus.SENDING.equals(message.getSentStatus())) {
                        mConversationList.add(message);
                        messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
                        rv_chatroom_conversationlist_fmt.smoothScrollToPosition(mConversationList.size());
                    } else {
                        for (int i = mConversationList.size() - 1; i >= 0; i--) {
                            if (mConversationList.get(i).getMessageId() == message.getMessageId()) {
                                mConversationList.get(i).setSentStatus(message.getSentStatus());
                                messageMultiItemTypeAdapter.notifyItemChanged(i);
                            }
                        }
                    }
                } else if (message.getObjectName().equals("RC:VcMsg")) {
                    Logger.e("发出一条语音消息");
                }
            }
        }
    }

    /**
     * 加载聊天室数据
     *
     * @param gameChatRoomBean
     */
    @Override
    public void loadChatRoomInfo(GameChatRoomBean gameChatRoomBean) {
        roomInfo.putString("gameChatRoomBean", new Gson().toJson(gameChatRoomBean));
        Logger.e("刷新人数" + gameChatRoomBean.getData().getJoinMembers().size());
        tv_chatroom_name_fmt.setText(gameChatRoomBean.getData().getName());
        tv_chatroom_membernum_fmt.setText(gameChatRoomBean.getData().getJoinMembers().size() + "人在线");
        isAnonymity = gameChatRoomBean.getData().isAnonymous();

        if (mJoinRoomMembersList.size() == 0){
            mJoinRoomMembersList.addAll(gameChatRoomBean.getData().getJoinMembers());
        }else{
            mJoinRoomMembersList.clear();
            mJoinRoomMembersList.addAll(gameChatRoomBean.getData().getJoinMembers());
        }

        if (adapter == null) {
            adapter = new ChatRoomMemberListAdapter(mContext, gameChatRoomBean.getData().getJoinMembers());
            rv_chatroom_memberlist_fmt.setAdapter(adapter);
        }else{
            adapter.addNewDatas(gameChatRoomBean.getData().getJoinMembers());
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mPresenter.exitChatRoom(PTApplication.userToken,PTApplication.userId,chatRoomId);

        leaveChatroom();

        // 关闭聊天室
        RongIMClient.getInstance().quitChatRoom(chatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.d("退出聊天室：" + chatRoomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("error: " + errorCode.getMessage());
            }
        });
        RongIMClient.getInstance().quitChatRoom("cmd" + chatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.d("退出CMD聊天室");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("error: " + errorCode.getMessage());
            }
        });
        // 解除监听

        EventBus.getDefault().unregister(this);
    }

    //其他fragment中的方法
    @Override
    public void initRoomsList(boolean isSuccess, List<HomeRoomsBean.DataBean> date, boolean isLoadMore) {

    }

    @Override
    public void setAvatarAndNickname() {

    }

    @Override
    public void joinTheRoom(NoDataBean noDataBean, String roomId, String password) {

    }

    @Override
    public void changeLoadView(boolean isShown) {
        if (isShown) {
            homeActivity.showLoadingDialog();
        } else {
            homeActivity.hideLoadingDialog();
        }
    }

    @Override
    public void onSendToggleClick(View view, String text) {
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
            TextMessage textMessage = TextMessage.obtain(text);
            MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
            if (mentionedInfo != null) {
                textMessage.setMentionedInfo(mentionedInfo);
            }

            Message message = Message.obtain(chatRoomId, this.mConversationType, textMessage);
            RongIM.getInstance().sendMessage(message, null, null, (IRongCallback.ISendMessageCallback) null);
        } else {
            RLog.e("ConversationFragment", "text content must not be null");
        }
    }

    @Override
    public void onImageResult(List<Uri> list, boolean b) {

    }

    @Override
    public void onLocationResult(double v, double v1, String s, Uri uri) {

    }

    @Override
    public void onSwitchToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onSwitchToggleClick");
    }

    // 语音
    private float mLastTouchY;
    private boolean mUpDirection;
    private float mOffsetLimit;

    @Override
    public void onVoiceInputToggleTouch(View v, MotionEvent event) {
        String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
        if (!PermissionCheckUtil.checkPermissions(this.getActivity(), permissions)) {
            if (event.getAction() == 0) {
                PermissionCheckUtil.requestPermissions(this, permissions, 100);
            }
        } else {
            if (event.getAction() == 0) {
                AudioPlayManager.getInstance().stopPlay();
                AudioRecordManager.getInstance().startRecord(v.getRootView(), this.mConversationType, this.chatRoomId);
                this.mLastTouchY = event.getY();
                this.mUpDirection = false;
                ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
            } else if (event.getAction() == 2) {
                if (this.mLastTouchY - event.getY() > this.mOffsetLimit && !this.mUpDirection) {
                    AudioRecordManager.getInstance().willCancelRecord();
                    this.mUpDirection = true;
                    ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input);
                } else if (event.getY() - this.mLastTouchY > -this.mOffsetLimit && this.mUpDirection) {
                    AudioRecordManager.getInstance().continueRecord();
                    this.mUpDirection = false;
                    ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
                }
            } else if (event.getAction() == 1 || event.getAction() == 3) {
                AudioRecordManager.getInstance().stopRecord();
                ((Button) v).setText(io.rong.imkit.R.string.rc_audio_input);
            }

            if (this.mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.chatRoomId, "RC:VcMsg");
            }

        }
    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onEmoticonToggleClick");
        //TODO 发送表情
        rv_chatroom_conversationlist_fmt.smoothScrollToPosition(mConversationList.size());
        //rl_bottom_gamechatroom.setVisibility(View.VISIBLE);
        //rl_bottom_gamechatroom.setVisibility(View.GONE);
        //AndroidBug5497Workaround.assistActivity(mRootView);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(130);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_bottom_gamechatroom.setVisibility(View.GONE);
                    }
                });
            }
        }).start();*/
    }

    @Override
    public void onPluginToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onMenuClick(int i, int i1) {

    }

    @Override
    public void onEditTextClick(EditText editText) {
        rv_chatroom_conversationlist_fmt.smoothScrollToPosition(mConversationList.size());
        /*if (rl_bottom_gamechatroom.getVisibility() == View.VISIBLE) {
            rl_bottom_gamechatroom.setVisibility(View.GONE);
            // 解决键盘不上浮问题
            //AndroidBug5497Workaround.assistActivity(mRootView);
        }*/
        AndroidBug5497Workaround.assistActivity(mRootView);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onExtensionCollapsed() {

    }

    @Override
    public void onExtensionExpanded(int i) {

    }

    @Override
    public void onPluginClicked(IPluginModule iPluginModule, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean myOnBackPressed() {
        if (mRongExtension != null && mRongExtension.isExtensionExpanded()) {
            mRongExtension.collapseExtension();
            return false;
        } else {
            return true;
        }
    }

    //发送和接受的内部类

    /**
     * 收到的文本消息
     */
    public class MsgComingItemDelegate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_coming_chatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:TxtMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.RECEIVE);
        }

        @Override
        public void convert(ViewHolder holder, final Message message, int position) {
            if (isAnonymity) {
                //匿名
                ((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)).setImageResource(R.drawable.person_default_icon);
                holder.setText(R.id.tv_name_item_coming_chatroom, new TextMessage("匿名").getContent());
            } else {
                //非匿名
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)));
                holder.setText(R.id.tv_name_item_coming_chatroom, new TextMessage("别人的名字").getContent());

            }
            (holder.getView(R.id.iv_avatar_item_coming_gamechatroom)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    /*Bundle bundle = new Bundle();
                    bundle.putLong("userId",Long.valueOf(message.getSenderUserId()));
                    intent.putExtras(bundle);*/
                    intent.putExtra("userId", Long.valueOf(message.getSenderUserId()));
                    startActivity(intent);
                }
            });
            holder.setText(R.id.tv_msg_item_coming_gamechatroom, new TextMessage(message.getContent().encode()).getContent());

        }
    }

    /**
     * 收到的语音消息
     */
    public class MsgComingVcItemDelegate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_coming_vc_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:VcMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.RECEIVE);
        }

        @Override
        public void convert(ViewHolder holder, final Message message, int position) {
            final VoiceMessage voiceMessage = (VoiceMessage) message.getContent();
            final View anim = holder.getView(R.id.v_anim_msg_item_coming_gamechatroom);

            holder.setOnClickListener(R.id.tv_msg_item_coming_gamechatroom, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 播放语音和动画
                    anim.setBackgroundResource(R.drawable.rc_an_voice_receive);
                    final AnimationDrawable animationDrawable = (AnimationDrawable) anim.getBackground();

                    AudioPlayManager.getInstance().stopPlay();
                    AudioPlayManager.getInstance().startPlay(mContext, voiceMessage.getUri(), new IAudioPlayListener() {
                        @Override
                        public void onStart(Uri uri) {
                            // 播放动画
                            animationDrawable.start();
                        }

                        @Override
                        public void onStop(Uri uri) {
                            // 结束动画
                            ToastUtils.getToast("播放失败");
                            animationDrawable.stop();
                            anim.setBackgroundResource(R.drawable.rc_ic_voice_receive);
                        }

                        @Override
                        public void onComplete(Uri uri) {
                            // 结束动画
                            animationDrawable.stop();
                            anim.setBackgroundResource(R.drawable.rc_ic_voice_receive);
                        }
                    });
                }
            });
            if (isAnonymity) {
                ((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)).setImageResource(R.drawable.person_default_icon);
                holder.setText(R.id.tv_name_item_coming_chatroom, new TextMessage("匿名").getContent());
            } else {
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)));
                holder.setText(R.id.tv_name_item_coming_chatroom, new TextMessage("别人的名字").getContent());
            }

            (holder.getView(R.id.iv_avatar_item_coming_gamechatroom)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    /*Bundle bundle = new Bundle();
                    bundle.putLong("userId",Long.valueOf(message.getSenderUserId()));
                    intent.putExtras(bundle);*/

                    intent.putExtra("userId", Long.valueOf(message.getSenderUserId()));
                    startActivity(intent);
                }
            });
            TextView textView = holder.getView(R.id.tv_msg_item_coming_gamechatroom);
            textView.setText(voiceMessage.getDuration() + "\"");
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.width = (int) (rv_chatroom_memberlist_fmt.getLayoutParams().height * 2 * (1.0 + voiceMessage.getDuration() / 60.0));
            textView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 发送的文本消息
     */
    public class MsgSendItemDelegate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_send_chatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:TxtMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.SEND);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            if (isAnonymity) {
                ((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)).setImageResource(R.drawable.person_default_icon);
                holder.setText(R.id.tv_name_item_send_chatroom, new TextMessage("匿名").getContent());
            } else {
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .placeholder(R.drawable.person_default_icon)
                        .error(R.drawable.person_default_icon)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)));
                holder.setText(R.id.tv_name_item_send_chatroom, new TextMessage(PTApplication.myInfomation.getData().getNickname()).getContent());
            }

            holder.setText(R.id.tv_msg_item_send_gamechatroom, new TextMessage(message.getContent().encode()).getContent());
            if (message.getSentStatus().equals(Message.SentStatus.SENT)) {
                holder.getView(R.id.pb_status_item_send_gamechatroom).setVisibility(View.GONE);
            } else if (message.getSentStatus().equals(Message.SentStatus.FAILED)) {
                holder.getView(R.id.pb_status_item_send_gamechatroom).setVisibility(View.GONE);
                holder.getView(R.id.iv_failed_item_send_gamechatroom).setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * 发送的语音消息
     */
    public class MsgSendVcItemDelegate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_send_vc_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:VcMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.SEND);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            final VoiceMessage voiceMessage = (VoiceMessage) message.getContent();
            final View anim = holder.getView(R.id.v_anim_msg_item_send_gamechatroom);

            holder.setOnClickListener(R.id.tv_msg_item_send_gamechatroom, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 播放语音和动画
                    anim.setBackgroundResource(R.drawable.rc_an_voice_sent);
                    final AnimationDrawable animationDrawable = (AnimationDrawable) anim.getBackground();

                    AudioPlayManager.getInstance().stopPlay();
                    AudioPlayManager.getInstance().startPlay(mContext, voiceMessage.getUri(), new IAudioPlayListener() {
                        @Override
                        public void onStart(Uri uri) {
                            // 播放动画
                            animationDrawable.start();
                        }

                        @Override
                        public void onStop(Uri uri) {
                            // 结束动画
                            ToastUtils.getToast("播放失败");
                            animationDrawable.stop();
                            anim.setBackgroundResource(R.drawable.rc_ic_voice_sent);
                        }

                        @Override
                        public void onComplete(Uri uri) {
                            // 结束动画
                            animationDrawable.stop();
                            anim.setBackgroundResource(R.drawable.rc_ic_voice_sent);
                        }
                    });
                }
            });
            if (isAnonymity) {
                ((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)).setImageResource(R.drawable.person_default_icon);
                holder.setText(R.id.tv_name_item_send_chatroom, new TextMessage("匿名").getContent());
            } else {
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .placeholder(R.drawable.person_default_icon)
                        .error(R.drawable.person_default_icon)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)));
                holder.setText(R.id.tv_name_item_send_chatroom, new TextMessage(PTApplication.myInfomation.getData().getNickname()).getContent());
            }

            TextView textView = holder.getView(R.id.tv_msg_item_send_gamechatroom);
            textView.setText(voiceMessage.getDuration() + "\"");
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.width = (int) (rv_chatroom_memberlist_fmt.getLayoutParams().height * 2 * (1.0 + voiceMessage.getDuration() / 60.0));
            textView.setLayoutParams(layoutParams);
            if (message.getSentStatus().equals(Message.SentStatus.SENT)) {
                holder.getView(R.id.pb_status_item_send_gamechatroom).setVisibility(View.GONE);
            } else if (message.getSentStatus().equals(Message.SentStatus.FAILED)) {
                holder.getView(R.id.pb_status_item_send_gamechatroom).setVisibility(View.GONE);
                holder.getView(R.id.iv_failed_item_send_gamechatroom).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * info消息
     */
    public class MsgInfoItemDelagate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_info_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:InfoNtf".equals(item.getObjectName());
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            holder.setText(R.id.tv_msg_item_info_gamechatroom, new InformationNotificationMessage(message.getContent().encode()).getMessage());
        }
    }
}
