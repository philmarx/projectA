package com.hzease.tomeet.game.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.ModitfyRoomInfoActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.game.IGameChatRoomContract;
import com.hzease.tomeet.game.MemberDiffCallback;
import com.hzease.tomeet.utils.AndroidBug5497Workaround;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.GameChatRoomMembersAdapter;
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
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.Event;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/5/4 14:19
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomFragment extends BaseFragment implements IGameChatRoomContract.View, IExtensionClickListener, GameChatRoomActivity.OnBackPressedListener {

    // 会话内容
    @BindView(R.id.rv_conversation_list_gamechatroom_fmt)
    RecyclerView rv_conversation_list_gamechatroom_fmt;
    // 成员列表
    @BindView(R.id.rv_members_gamechatroom_fmt)
    RecyclerView rv_members_gamechatroom_fmt;
    // 房间名字
    @BindView(R.id.tv_room_name_gamechatroom_fmg)
    TextView tv_room_name_gamechatroom_fmg;
    // 融云扩展框
    @BindView(R.id.rc_extension_gamechatroom_fmt)
    RongExtension mRongExtension;
    // 退出
    @BindView(R.id.ib_exit_gamechatroom_fmt)
    ImageButton ib_exit_gamechatroom_fmt;
    // 准备 或者 取消
    @BindView(R.id.ib_ready_gamechatroom_fmt)
    ImageButton ib_ready_gamechatroom_fmt;
    // 邀请
    @BindView(R.id.ib_invite_gamechatroom_fmt)
    ImageButton ib_invite_gamechatroom_fmt;
    //
    @BindView(R.id.ll_bottom_gamechatroom)
    LinearLayout ll_bottom_gamechatroom;

    private Conversation.ConversationType mConversationType;

    private IGameChatRoomContract.Presenter mPresenter;
    private String roomId;
    private boolean amIReady = false;
    private boolean amIManager = false;
    private int mRoomStatus = 0;

    // 公告
    private String mNotice;

    private List<Message> mConversationList = new ArrayList<>();
    private MultiItemTypeAdapter messageMultiItemTypeAdapter;
    private GameChatRoomMembersAdapter gameChatRoomMembersAdapter;
    private boolean isLeaveRoom = true;

    public static GameChatRoomFragment newInstance() {
        return new GameChatRoomFragment();
    }

    @Override
    public void setPresenter(@NonNull IGameChatRoomContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_gamechatroom;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {


        roomId = getActivity().getIntent().getStringExtra(AppConstants.TOMEET_ROOM_ID);

        // 注册event
        //EventBus.getDefault().register(this);
        //InternalModuleManager.getInstance().onLoaded();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
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

        //mRongExtension.setCurrentEmoticonTab(new EmojiTab(), "2");


        RongIMClient.getInstance().joinChatRoom(roomId, 50, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                // 插入一条房间信息
                Logger.i("加入成功： " + roomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("errorCode: " + errorCode.getMessage());
            }
        });

        // 会话列表adapter
        messageMultiItemTypeAdapter = new MultiItemTypeAdapter<>(mContext, mConversationList);
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingItemDelagate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendItemDelagate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgInfoItemDelagate());
        rv_conversation_list_gamechatroom_fmt.setAdapter(messageMultiItemTypeAdapter);
        rv_conversation_list_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));


        // 初始化完的最后一步加载数据
        mPresenter.getGameChatRoomInfo(roomId);
    }

    // 接收到消息的event
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Logger.i("聊天室   onEventMainThread   MessageContentEncode: " + new String(event.getMessage().getContent().encode())
                + "\ngetTargetId: " + event.getMessage().getTargetId() + "   Left: " + event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName() + "\nSenderUserId: " + event.getMessage().getSenderUserId()
                + "    MessageDirection: " + event.getMessage().getMessageDirection() + "   ConversationType: " + event.getMessage().getConversationType());
        if (event.getMessage().getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
            switch (event.getMessage().getObjectName()) {
                case "RC:TxtMsg":
                case "RC:InfoNtf":
                    // 插入数据源
                    mConversationList.add(event.getMessage());
                    // 更新界面
                    messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
                    rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
                    break;
            }
        }
    }

    // 发出消息的event 和 cmdMsg
    public void onEventMainThread(Message message) {
        Logger.w("发出消息的event: " + message.getSentStatus() + "  " + new String(message.getContent().encode()) + "  发送时间: " + message.getSentTime());
        if (message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
            if (message.getObjectName().equals("RC:CmdMsg")) {
                CommandMessage cmdMsg = new CommandMessage(message.getContent().encode());
                Logger.w(cmdMsg.getName());
                switch (cmdMsg.getName()) {
                    // 刷新房间
                    case "refreshRoom":
                        mPresenter.getGameChatRoomInfo(roomId);
                        break;
                    // 收到踢人消息
                    case "outMan":
                        if (PTApplication.userId.equals(cmdMsg.getData())) {
                            // // TODO: 2017/5/10 用弹窗提醒，用全局mContext,在finish之后弹
                            isLeaveRoom = false;
                            mPresenter.exitRoom(roomId);
                            this.getActivity().finish();
                            ToastUtils.getToast(PTApplication.getInstance(), "您被踢出了房间");
                        } else {
                            mPresenter.getGameChatRoomInfo(roomId);
                        }
                        break;
                }
            } else {
                if (message.getSentStatus().equals(Message.SentStatus.SENDING)) {
                    mConversationList.add(message);
                    messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
                    rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
                } else {
                    for (int i = mConversationList.size() - 1; i >= 0; i--) {
                        if (mConversationList.get(i).getMessageId() == message.getMessageId()) {
                            mConversationList.get(i).setSentStatus(message.getSentStatus());
                            messageMultiItemTypeAdapter.notifyItemChanged(i);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 离开房间
        if (isLeaveRoom) {
            mPresenter.leaveRoom(roomId);
        }

        // 解除监听
        EventBus.getDefault().unregister(this);

        // 关闭聊天室
        RongIMClient.getInstance().quitChatRoom(roomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.d("退出聊天室：" + roomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("error: " + errorCode.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.e("1234");
    }

    @OnClick({R.id.ib_return_gamechatroom_fmg, R.id.ib_detail_gamechatroom_fmg
            , R.id.ib_exit_gamechatroom_fmt, R.id.ib_ready_gamechatroom_fmt, R.id.ib_invite_gamechatroom_fmt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 关闭聊天室
            case R.id.ib_return_gamechatroom_fmg:
                getActivity().finish();
                break;
            // 房间详情
            case R.id.ib_detail_gamechatroom_fmg:
                initPopupWindos(view);
                break;
            // 退出
            case R.id.ib_exit_gamechatroom_fmt:
                if (mRoomStatus == 0) {
                    isLeaveRoom = false;
                    mPresenter.exitRoom(roomId);
                    this.getActivity().finish();
                } else {
                    ToastUtils.getToast(mContext, "活动已就绪，无法退出");
                }
                break;
            // 准备
            case R.id.ib_ready_gamechatroom_fmt:
                if (mRoomStatus == 0) {
                    mPresenter.ReadyOrCancel(amIReady, roomId, amIManager);
                } else {
                    ToastUtils.getToast(mContext, "活动已就绪，不用重复点击");
                }
                break;
            // 邀请
            case R.id.ib_invite_gamechatroom_fmt:
                ToastUtils.getToast(mContext, "邀请好友来一起参加活动吧~");
                break;
        }
    }

    /**
     * 加载房间数据
     */
    @Override
    public void refreshGameChatRoomInfo(final GameChatRoomBean gameChatRoomBean) {
        GameChatRoomBean.DataBean roomData = gameChatRoomBean.getData();
        mRoomStatus = roomData.getState();
        if (mRoomStatus == 0) {
            ll_bottom_gamechatroom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom_gamechatroom.setVisibility(View.GONE);
        }
        if (PTApplication.userId.equals(String.valueOf(roomData.getManager().getId()))) {
            amIManager = true;
            ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_begin);
        }

        // 获取公告
        mNotice = new StringBuilder("开始时间：").append(roomData.getBeginTime()).append("\n结束时间：")
                .append(roomData.getEndTime()).append("\n活动地点：").append(roomData.getPlace())
                .append("\n保证金：").append(roomData.getMoney()).append("\n活动介绍：").append(roomData.getDescription()).toString();
        // 设置房间名
        tv_room_name_gamechatroom_fmg.setText(roomData.getName());

        // adapter
        if (gameChatRoomMembersAdapter == null) {
            // 第一次进来检查房间状态
            for (GameChatRoomBean.DataBean.JoinMembersBean joinMembersBean : roomData.getJoinMembers()) {
                if (PTApplication.userId.equals(String.valueOf(joinMembersBean.getId()))) {
                    if (amIReady = joinMembersBean.isReady()) {
                        ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
                    }
                }
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(3000);
                    // 插入一条公告
                    InformationNotificationMessage informationNotificationMessage = InformationNotificationMessage.obtain(mNotice);
                    Message message = Message.obtain(roomId, GameChatRoomFragment.this.mConversationType, informationNotificationMessage);
                    message.setObjectName("RC:InfoNtf");
                    mConversationList.add(message);
                    GameChatRoomFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
                            rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
                        }
                    });

                }
            }).start();



            // 左边的成员列表
            gameChatRoomMembersAdapter = new GameChatRoomMembersAdapter(mContext, roomData.getJoinMembers(), roomData.getManager().getId(), roomData.getId(), roomData.getGame().getId());
            rv_members_gamechatroom_fmt.setAdapter(gameChatRoomMembersAdapter);
            rv_members_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            // 设置房主ID
            gameChatRoomMembersAdapter.setmManagerId(roomData.getManager().getId());

            // 刷新数据
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MemberDiffCallback(gameChatRoomMembersAdapter.getDate(), roomData.getJoinMembers()), true);
            gameChatRoomMembersAdapter.setDate(roomData.getJoinMembers());
            diffResult.dispatchUpdatesTo(gameChatRoomMembersAdapter);
        }

    }

    /**
     * 改变准备按钮状态
     */
    @Override
    public void changeReadyOrCancel() {
        // 先赋值后判断，如果为true，则说明点击了准备，改成取消按钮
        if (amIReady = !amIReady) {
            ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
        } else {
            ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_ready);
        }
    }

    @Override
    public void onSendToggleClick(View v, String text) {
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
            TextMessage textMessage = TextMessage.obtain(text);
            MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
            if (mentionedInfo != null) {
                textMessage.setMentionedInfo(mentionedInfo);
            }

            Message message = Message.obtain(roomId, this.mConversationType, textMessage);
            RongIM.getInstance().sendMessage(message, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
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

    @Override
    public void onVoiceInputToggleTouch(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onEmoticonToggleClick");
    }

    @Override
    public void onPluginToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onMenuClick(int i, int i1) {
        Logger.e("onMenuClick: " + i + "     " + i1);
    }

    @Override
    public void onEditTextClick(EditText editText) {
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        if (mRoomStatus == 0) {
            ll_bottom_gamechatroom.setVisibility(View.GONE);
        }
        // 解决键盘不上浮问题
        AndroidBug5497Workaround.assistActivity(mRootView);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onExtensionCollapsed() {
        // 扩展框关闭
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        if (mRoomStatus == 0) {
            ll_bottom_gamechatroom.setVisibility(View.VISIBLE);
        }
        // 解决键盘不上浮问题
        AndroidBug5497Workaround.assistActivity(mRootView);
    }

    @Override
    public void onExtensionExpanded(int i) {
        // 扩展框打开
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        if (mRoomStatus == 0) {
            ll_bottom_gamechatroom.setVisibility(View.GONE);
        }
        // 解决键盘不上浮问题
        AndroidBug5497Workaround.assistActivity(mRootView);
    }

    @Override
    public void onPluginClicked(IPluginModule iPluginModule, int i) {
        Logger.e("onPluginClicked: " + i);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * activity中的返回键
     */
    @Override
    public boolean myOnBackPressed() {
        if (mRongExtension.isExtensionExpanded()) {
            mRongExtension.collapseExtension();
            return false;
        } else {
            return true;
        }
    }


    //发送和接受的内部类

    /**
     * 收到的消息
     */
    public class MsgComingItemDelagate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_coming_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:TxtMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.RECEIVE);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .placeholder(R.drawable.person_default_icon)
                    .error(R.drawable.person_default_icon)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)));
            holder.setText(R.id.tv_msg_item_coming_gamechatroom, new TextMessage(message.getContent().encode()).getContent());

        }
    }

    /**
     * 发送的消息
     */
    public class MsgSendItemDelagate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_send_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:TxtMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.SEND);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .placeholder(R.drawable.person_default_icon)
                    .error(R.drawable.person_default_icon)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)));
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


    private void initPopupWindos(View v) {
        Logger.e("initPopupWindows");
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_roominfo, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button cancel = (Button) contentView.findViewById(R.id.cancelforowner);
        Button modtify = (Button) contentView.findViewById(R.id.moditfyroominfo);
        TextView startTime = (TextView) contentView.findViewById(R.id.tv_roominfo_starttime_pop);
 /*       TextView endTime = (TextView) contentView.findViewById(R.id.tv_roominfo_endtime_pop);
        TextView place = (TextView) contentView.findViewById(R.id.tv_roominfo_place_pop);
        TextView money = (TextView) contentView.findViewById(R.id.tv_roominfo_money_pop);
        TextView disc = (TextView) contentView.findViewById(R.id.tv_roominfo_disc_pop);*/
        startTime.setText(mNotice);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        modtify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ModitfyRoomInfoActivity.class));
            }
        });
    }
}
