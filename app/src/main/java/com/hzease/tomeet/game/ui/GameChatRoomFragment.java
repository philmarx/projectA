package com.hzease.tomeet.game.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.ModitfyRoomInfoActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.RoomLocationActivity;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.game.IGameChatRoomContract;
import com.hzease.tomeet.game.MemberDiffCallback;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.AndroidBug5497Workaround;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.AlertDialog;
import com.hzease.tomeet.widget.adapters.GameChatRoomMembersAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    // 房主 开始 或 取消
    @BindView(R.id.ib_begin_gamechatroom_fmt)
    ImageButton ib_begin_gamechatroom_fmt;
    // 邀请
    @BindView(R.id.ib_invite_gamechatroom_fmt)
    ImageButton ib_invite_gamechatroom_fmt;
    // status0
    @BindView(R.id.ll_status0_bottom_gamechatroom)
    LinearLayout ll_status0_bottom_gamechatroom;
    @BindView(R.id.ib_exchange_gamechatroom_fmt)
    ImageButton ib_exchange_gamechatroom_fmt;
    // 状态
    @BindView(R.id.tv_status_gamechatroom_fmt)
    TextView tv_status_gamechatroom_fmt;

    // status1 的时候
    // 出发
    @BindView(R.id.ib_go_gamechatroom_fmt)
    ImageButton ib_go_gamechatroom_fmt;
    // 地图
    @BindView(R.id.ib_map_gamechatroom_fmt)
    ImageButton ib_map_gamechatroom_fmt;
    // 签到
    @BindView(R.id.ib_check_gamechatroom_fmt)
    ImageButton ib_check_gamechatroom_fmt;
    //status1
    @BindView(R.id.ll_status1_bottom_gamechatroom)
    LinearLayout ll_status1_bottom_gamechatroom;

    // 底部栏
    @BindView(R.id.rl_bottom_gamechatroom)
    RelativeLayout rl_bottom_gamechatroom;

    //人数比例
    @BindView(R.id.tv_room_male_fmt)
    TextView tv_room_male_fmt;
    @BindView(R.id.tv_room_female_fmt)
    TextView tv_room_female_fmt;
    @BindView(R.id.ll_nosex)
    LinearLayout ll_nosex;
    @BindView(R.id.ll_havesex)
    LinearLayout ll_havesex;
    @BindView(R.id.tv_room_nosex_fmt)
    TextView tv_room_nosex_fmt;
    // 进度框
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;
    @BindView(R.id.rl_load_View)
    RelativeLayout rl_load_View;


    private Conversation.ConversationType mConversationType;

    private IGameChatRoomContract.Presenter mPresenter;
    private String roomId;
    // 开始时间
    long mPrepareTimeMillis = 0;

    private boolean amIReady = false;
    private boolean amIManager = false;
    private boolean isBegin = false;
    private int mRoomStatus = 0;
    private long mJoinedRoomTime;

    // 公告
    private String mNotice;

    private List<Message> mConversationList = new ArrayList<>();
    // 聊天Adapter
    private MultiItemTypeAdapter messageMultiItemTypeAdapter;

    // 成员Adapter
    private GameChatRoomMembersAdapter gameChatRoomMembersAdapter;

    private boolean isLeaveRoom = true;
    private CountDownTimer countDownTimer = null;

    private PopupWindow itemMorePopup;
    private int popXoff;
    private String startTimeValue;
    private String endTimeValue;
    private String placeValue;
    private int moneyValue;
    private String discValue;
    private double roomLat;
    private double roomLong;
    private String roomCity;
    private String roomName;
    // 发送邀请的介绍文字
    private String invitedNotice;
    private int womanCount;
    private int manCount;
    private int memberCount;
    private boolean isOpen;
    private boolean firstLoading = true;
    private GameChatRoomBean.DataBean.JoinMembersBean myJoinBean;
    private int gameId;
    private int roomState;
    private Bundle mMapBundle;


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
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(mRootView);
        this.mOffsetLimit = 70.0F * this.getActivity().getResources().getDisplayMetrics().density;
        changeLoadView(true);

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

        Logger.i("加入之前： " + System.currentTimeMillis());

        // 加入cmd聊天室
        RongIMClient.getInstance().joinExistChatRoom("cmd" + roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.i("加入cmd聊天室  加入成功： " + "cmd" + roomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("errorCode: " + errorCode.getMessage());
            }
        });


        // 加入融云聊天室
        RongIMClient.getInstance().joinExistChatRoom(roomId, 50, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                mJoinedRoomTime = System.currentTimeMillis();
                Logger.i("加入成功： " + roomId + "   加入成功之后：" + mJoinedRoomTime);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("errorCode: " + errorCode.getMessage());
            }
        });


        // 会话列表adapter
        messageMultiItemTypeAdapter = new MultiItemTypeAdapter<>(mContext, mConversationList);
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgInfoItemDelagate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingVcItemDelegate());
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendVcItemDelegate());
        rv_conversation_list_gamechatroom_fmt.setAdapter(messageMultiItemTypeAdapter);
        rv_conversation_list_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化完的最后一步加载数据
        mPresenter.getGameChatRoomInfo(roomId);
        // 初始化右上角更多按钮
        initItemMorePop();
    }

    /**
     * 初始化右上角更多按钮
     */
    private void initItemMorePop() {
        View popupContent = View.inflate(mContext, R.layout.pop_room_more_item, null);

        popXoff = -popupContent.getWidth();
        Logger.e("MeasuredWidth: " + popupContent.getMeasuredWidth() + "   width: " + popupContent.getWidth());
        // 公告
        popupContent.findViewById(R.id.afl_notice_pop_room_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemMorePopup.dismiss();
                initPopupWindos(v);
            }
        });

        // 投诉
        popupContent.findViewById(R.id.afl_complaint_pop_room_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemMorePopup.dismiss();
                Intent intent = new Intent(getActivity(), ComplaintActivity.class);
                intent.putExtras(mMapBundle);
                startActivity(intent);
            }
        });

        // 我没迟到
        popupContent.findViewById(R.id.afl_not_late_pop_room_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemMorePopup.dismiss();
                /*if (myJoinBean.isSigned()) {
                    ToastUtils.getToast(mContext, "您已签到，无需再申诉");
                } else {
                    mPresenter.iAmNotLate(roomId);
                }*/
                if (roomState == 2) {
                    Intent intent = new Intent(getActivity(), NoLateActivity.class);
                    intent.putExtras(mMapBundle);
                    startActivity(intent);
                } else {
                    ToastUtils.getToast("活动还未开始");
                }

            }
        });

        //规则说明
        popupContent.findViewById(R.id.afl_rule_desc_pop_room_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemMorePopup.dismiss();
                initRulePopWindow(v);
            }
        });

        itemMorePopup = new PopupWindow(popupContent, -2, -2, true);
        itemMorePopup.setTouchable(true);
        itemMorePopup.setFocusable(true);
        itemMorePopup.setOutsideTouchable(true);
        itemMorePopup.setBackgroundDrawable(new ColorDrawable(0));
    }

    //显示规则说明图片
    private void initRulePopWindow(View v) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_roomrule, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    // 接收到消息的event
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Logger.i("聊天室  接收  Content: " + new String(event.getMessage().getContent().encode())
                + "\ngetTargetId: " + event.getMessage().getTargetId() + "   Left: " + event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName() + "\nSenderUserId: " + event.getMessage().getSenderUserId()
                + "    MessageDirection: " + event.getMessage().getMessageDirection() + "   ConversationType: " + event.getMessage().getConversationType());
        if (event.getMessage().getConversationType().equals(Conversation.ConversationType.CHATROOM)
                && (event.getMessage().getTargetId().equals(roomId) || event.getMessage().getTargetId().equals("cmd" + roomId))
                //&& (event.getMessage().getObjectName().equals("RC:TxtMsg") || event.getMessage().getObjectName().equals("RC:InfoNtf"))) {
                && (event.getMessage().getObjectName().equals("RC:TxtMsg") || event.getMessage().getObjectName().equals("RC:InfoNtf") || event.getMessage().getObjectName().equals("RC:VcMsg"))) {
            // 插入数据源
            mConversationList.add(event.getMessage());
            // 更新界面
            messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
            rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        }
    }

    // 发出消息的event 和 cmdMsg
    public void onEventMainThread(Message message) {
        Logger.i("聊天室发出消息和CMD的event: " + message.getSentStatus() + "    TargetId: " + message.getTargetId() + "   Sender: " + message.getSenderUserId() + "\n" + new String(message.getContent().encode()) + "  Object: " + message.getObjectName() + "  Type: " + message.getConversationType());
        if (message.getConversationType().equals(Conversation.ConversationType.CHATROOM)) {
            if ("RC:CmdMsg".equals(message.getObjectName()) && ("cmd" + roomId).equals(message.getTargetId())) {
                if (message.getSentTime() > mJoinedRoomTime) {
                    CommandMessage cmdMsg = new CommandMessage(message.getContent().encode());
                    Logger.w(cmdMsg.getName());
                    switch (cmdMsg.getName()) {
                        // 刷新房间
                        case "refreshRoom":
                            mPresenter.getGameChatRoomInfo(roomId);
                            break;
                        // 收到踢人消息
                        case "outMan":
                            String[] outMan = cmdMsg.getData().split(":&:", 2);
                            Logger.e(Arrays.toString(outMan));
                            if (PTApplication.userId.equals(outMan[0])) {
                                isLeaveRoom = false;
                                initOutManPop(getActivity(), outMan[1]);
                            } else {
                                mPresenter.getGameChatRoomInfo(roomId);
                            }
                            break;
                        // 房间被解散
                        case "roomDissolve":
                            this.getActivity().finish();
                            ToastUtils.getToast("该房间已被解散！");
                            break;
                    }
                }
            } else if (roomId.equals(message.getTargetId()) && PTApplication.userId.equals(message.getSenderUserId())) {
                if ("RC:TxtMsg".equals(message.getObjectName()) || "RC:VcMsg".equals(message.getObjectName())) {
                    if (Message.SentStatus.SENDING.equals(message.getSentStatus())) {
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
                }/* else if (message.getObjectName().equals("RC:VcMsg")){
                    Logger.e("发出一条语音消息");
                }*/
            }
        }
    }

    //弹出踢人理由
    private void initOutManPop(final Activity activity, String reason) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_outreason, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button istrue = contentView.findViewById(R.id.bt_outreason_true_fmt);
        Button cancel = contentView.findViewById(R.id.bt_outreason_cancel_fmt);
        TextView tv_outreason_reason_fmt = contentView.findViewById(R.id.tv_outreason_reason_fmt);
        tv_outreason_reason_fmt.setText("您已被管理员请离房间,理由: " + reason);
        istrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                activity.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                activity.finish();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 销毁倒计时
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

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
        RongIMClient.getInstance().quitChatRoom("cmd" + roomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Logger.d("退出CMD聊天室");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("error: " + errorCode.getMessage());
            }
        });
    }


    @OnClick({R.id.ib_return_gamechatroom_fmg, R.id.ib_detail_gamechatroom_fmg,
            R.id.ib_exit_gamechatroom_fmt, R.id.ib_ready_gamechatroom_fmt,
            R.id.ib_invite_gamechatroom_fmt, R.id.ib_begin_gamechatroom_fmt,
            R.id.ib_go_gamechatroom_fmt, R.id.ib_map_gamechatroom_fmt,
            R.id.ib_check_gamechatroom_fmt, R.id.iv_roominfo_location_one,
            R.id.iv_roominfo_location_two})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            //位置
            case R.id.iv_roominfo_location_one:
                Intent intent = new Intent(getActivity(), RoomLocationActivity.class);
                intent.putExtras(mMapBundle);
                startActivity(intent);
                break;
            case R.id.iv_roominfo_location_two:
                Intent intent1 = new Intent(getActivity(), RoomLocationActivity.class);
                intent1.putExtras(mMapBundle);
                startActivity(intent1);
                break;
            // 出发
            case R.id.ib_go_gamechatroom_fmt:
                mPresenter.memberGo(roomId);
                break;
            // 地图
            case R.id.ib_map_gamechatroom_fmt:
                Intent intent2 = new Intent(getActivity(), RoomLocationActivity.class);
                intent2.putExtras(mMapBundle);
                startActivity(intent2);
                break;
            // 签到
            case R.id.ib_check_gamechatroom_fmt:
                if (mRoomStatus == 1) {
                    ib_check_gamechatroom_fmt.setEnabled(false);
                    mPresenter.checkSendLocation(roomId);
                } else {
                    // TODO: 2017/6/30 使用补签卡
                    PTApplication.getRequestService().findPropsMum(PTApplication.userToken, PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<PropsMumBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(PropsMumBean propsMumBean) {
                                    initPopupWindow(view, propsMumBean.getData().getSignCount());
                                }
                            });
                }
                break;
            // 关闭聊天室
            case R.id.ib_return_gamechatroom_fmg:
                getActivity().finish();
                break;
            // 房间详情 .已改为更多。目前里面有三个选项
            case R.id.ib_detail_gamechatroom_fmg:
                itemMorePopup.showAsDropDown(view, popXoff, 15);
                break;
            // 退出
            case R.id.ib_exit_gamechatroom_fmt:
                if (mRoomStatus == 0) {
                    isLeaveRoom = false;
                    mPresenter.exitRoom(roomId);
                    this.getActivity().finish();
                } else {
                    ToastUtils.getToast("活动已就绪，无法退出");
                }
                break;
            // 房主点开始 或 取消
            case R.id.ib_begin_gamechatroom_fmt:
                if (mRoomStatus == 0 && amIManager) {
                    mPresenter.managerReadyOrCancel(roomId, isBegin);
                } else {
                    ToastUtils.getToast("活动已开始 或 您不是房主");
                }
                break;
            // 准备 或 取消
            case R.id.ib_ready_gamechatroom_fmt:
                if (mRoomStatus == 0 && (mPrepareTimeMillis == 0 || mPrepareTimeMillis > System.currentTimeMillis())) {
                    if (!amIReady) {
                        new AlertDialog(mContext).builder()
                                .setTitle("该房间有设有保证金哦，如果准备则需要冻结您相应的保证金，按时参加活动可退回")
                                .setPositiveButton("确认准备", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mPresenter.memberReadyOrCancel(amIReady, roomId);
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                }).show();
                    } else {
                        mPresenter.memberReadyOrCancel(amIReady, roomId);
                    }
                } else {
                    ToastUtils.getToast("活动已就绪，不用重复点击");
                }
                break;
            // 邀请
            case R.id.ib_invite_gamechatroom_fmt:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    requestPermissions(mPermissionList, 123);
                }
                new ShareAction(getActivity())
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .addButton("umeng_sharebutton_custom", "tomeet", "share_logo_200x200", "share_logo_200x200")
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media == null) {
                                    if (snsPlatform.mKeyword.equals("tomeet")) {
                                        Intent intent = new Intent(getActivity(), SelectFriendActivity.class);
                                        intent.putExtra("roomId", roomId);
                                        intent.putExtra("roomName", roomName);
                                        intent.putExtra("desc", invitedNotice);
                                        intent.putExtra("gameId", gameId);
                                        startActivity(intent);
                                    }
                                } else {
                                    UMWeb web = new UMWeb(AppConstants.TOMMET_SHARE_APP_ROOM + PTApplication.userId + "&roomId=" + roomId);
                                    web.setTitle("你的小伙伴喊你参加【" + roomName + "】啦!");
                                    web.setThumb(new UMImage(mContext, R.drawable.share_logo_200x200));
                                    web.setDescription(invitedNotice);

                                    new ShareAction(getActivity()).setPlatform(share_media).setCallback(new UMShareListener() {
                                        @Override
                                        public void onStart(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                        }

                                        @Override
                                        public void onResult(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                            //ToastUtils.getToast(mContext, "分享成功");
                                        }

                                        @Override
                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                            Logger.e(share_media.toString());
                                            //ToastUtils.getToast(mContext, "分享失败");
                                        }

                                        @Override
                                        public void onCancel(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toString());
                                            //ToastUtils.getToast(mContext, "取消分享");
                                        }
                                    }).withMedia(web).share();
                                }
                            }
                        }).open();
                break;
        }
    }


    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                PTApplication.myLongitude = aMapLocation.getLongitude();
                PTApplication.myLatitude = aMapLocation.getLatitude();
            }
        });
    }

    private void initPopupWindow(final View view, int propcount) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_useprops, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        //pop背景

        final AutoLinearLayout bg = contentView.findViewById(R.id.all_props_bg_pop);
        TextView props = contentView.findViewById(R.id.tv_count_fmt);
        Button useorbuy = contentView.findViewById(R.id.bt_props_buyoruse_pop);
        if (propcount == 0) {
            //没有补签卡
            bg.setBackgroundResource(R.drawable.buqian_notenght);
            props.setVisibility(View.GONE);
            useorbuy.setText("购买");
            useorbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PTApplication.getRequestService().buyProp(1, PTApplication.userToken, 3, PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    if (noDataBean.isSuccess()) {
                                        popupWindow.dismiss();
                                        ToastUtils.getToast("购买成功");
                                        initPopupWindow(view, 1);
                                    } else {
                                        popupWindow.dismiss();
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });
                }
            });
        } else {
            //有补签卡
            props.setText("剩余X" + propcount);
            bg.setBackgroundResource(R.drawable.props_buqian_bg);
            props.setVisibility(View.VISIBLE);
            useorbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initLogLat();
                    PTApplication.getRequestService().buqian(String.valueOf(PTApplication.myLatitude), String.valueOf(PTApplication.myLongitude), String.valueOf(roomId), PTApplication.userId, PTApplication.userToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    if (noDataBean.isSuccess()) {
                                        popupWindow.dismiss();
                                        ToastUtils.getToast("补签成功");
                                    } else {
                                        popupWindow.dismiss();
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });

                }
            });
        }
        Button cancel = contentView.findViewById(R.id.bt_props_cancel_pop);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 加载房间数据
     */
    @Override
    public void refreshGameChatRoomInfo(final GameChatRoomBean gameChatRoomBean) {
        //Logger.w("开始时间： " + gameChatRoomBean.getData().getPrepareTime());

        if (!TextUtils.isEmpty(gameChatRoomBean.getData().getPrepareTime())) {
            try {
                mPrepareTimeMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(gameChatRoomBean.getData().getPrepareTime()).getTime();
                isBegin = true;
            } catch (ParseException e) {
                Logger.e("prepareDate:  " + e.getMessage());
                mPrepareTimeMillis = 0;
                isBegin = false;
            }
        } else {
            mPrepareTimeMillis = 0;
            isBegin = false;
        }

        GameChatRoomBean.DataBean roomData = gameChatRoomBean.getData();
        List<GameChatRoomBean.DataBean.JoinMembersBean> joinMembersBeanList = roomData.getJoinMembers();
        startTimeValue = roomData.getBeginTime();
        endTimeValue = roomData.getEndTime();
        placeValue = roomData.getPlace();
        moneyValue = roomData.getMoney();
        discValue = roomData.getDescription();
        roomLat = roomData.getLatitude();
        roomLong = roomData.getLongitude();
        roomCity = roomData.getCity();
        womanCount = roomData.getWomanCount();
        manCount = roomData.getManCount();
        memberCount = roomData.getMemberCount();
        isOpen = roomData.isOpen();
        // 房间状态
        mRoomStatus = roomData.getState();

        for (GameChatRoomBean.DataBean.JoinMembersBean joinMembersBean : joinMembersBeanList) {
            if (joinMembersBean.getId() == PTApplication.myInfomation.getData().getId()) {
                // 自己的bean
                myJoinBean = joinMembersBean;
                break;
            }
        }

        switch (mRoomStatus) {
            case 0:
                if (mPrepareTimeMillis != 0 && countDownTimer == null) {
                    // CountDownTimer
                    long diff = mPrepareTimeMillis - System.currentTimeMillis();
                    Logger.i("diff:  " + diff);
                    if (diff >= 0) {
                        // 倒计时
                        tv_status_gamechatroom_fmt.setText("房间将在" + diff + "秒后就绪");
                        countDownTimer = new CountDownTimer(diff, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_status_gamechatroom_fmt.setText("房间将在" + millisUntilFinished / 1000 + "秒后就绪");
                            }

                            @Override
                            public void onFinish() {
                                // 如果执行完了，信号差，先改变状态，再刷新界面，以防万一
                                mRoomStatus = 1;
                                mPresenter.getGameChatRoomInfo(roomId);
                            }
                        }.start();
                    }
                } else {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = null;
                    }
                    tv_status_gamechatroom_fmt.setText("招募中");
                }

                // 判断房主
                if (roomData.getManager().getId() == PTApplication.myInfomation.getData().getId()) {
                    amIManager = true;
                    ib_ready_gamechatroom_fmt.setVisibility(View.GONE);
                    ib_begin_gamechatroom_fmt.setVisibility(View.VISIBLE);
                    if (mPrepareTimeMillis == 0) {
                        ib_begin_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_begin);
                    } else {
                        ib_begin_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
                    }
                } else {
                    if (amIReady = myJoinBean.isReady()) {
                        ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
                    } else {
                        ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_ready);
                    }
                }

                ll_status0_bottom_gamechatroom.setVisibility(View.VISIBLE);
                ll_status1_bottom_gamechatroom.setVisibility(View.GONE);
                break;
            case 1:
                ll_status0_bottom_gamechatroom.setVisibility(View.GONE);
                ll_status1_bottom_gamechatroom.setVisibility(View.VISIBLE);
                tv_status_gamechatroom_fmt.setText("已就绪");
                if (myJoinBean.isSigned()) {
                    ib_check_gamechatroom_fmt.setEnabled(false);
                } else {
                    ib_check_gamechatroom_fmt.setEnabled(true);
                }
                break;
            case 2:
                tv_status_gamechatroom_fmt.setText("活动中");
                ll_status1_bottom_gamechatroom.setVisibility(View.VISIBLE);
                ib_check_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_recheck);
                if (myJoinBean.isSigned()) {
                    ib_check_gamechatroom_fmt.setEnabled(false);
                } else {
                    ib_check_gamechatroom_fmt.setEnabled(true);
                }
                ll_status0_bottom_gamechatroom.setVisibility(View.GONE);
                break;
        }


        // 获取公告
        mNotice = new StringBuilder("开始时间：").append(roomData.getBeginTime()).append("\n结束时间：")
                .append(roomData.getEndTime()).append("\n活动地点：").append(roomData.getPlace())
                .append("\n保证金：").append(roomData.getMoney() / 100.0f).append("元\n活动介绍：").append(roomData.getDescription()).toString();
        invitedNotice = roomData.getBeginTime().substring(5) + " 到 " + roomData.getEndTime().substring(5) + " 在" + roomData.getPlace();
        // 设置房间名
        roomName = roomData.getName();
        gameId = roomData.getGame().getId();
        roomState = roomData.getState();
        tv_room_name_gamechatroom_fmg.setText(roomName);
        if (roomData.getMemberCount() == 0) {
            ll_havesex.setVisibility(View.GONE);
            ll_nosex.setVisibility(View.VISIBLE);
            tv_room_nosex_fmt.setText(roomData.getJoinMember() + "/无限");
        } else {
            if (roomData.getManCount() == 0 && roomData.getWomanCount() == 0) {
                ll_havesex.setVisibility(View.GONE);
                ll_nosex.setVisibility(View.VISIBLE);
                tv_room_nosex_fmt.setText(roomData.getJoinMember() + "/" + roomData.getMemberCount());
            } else {
                ll_havesex.setVisibility(View.VISIBLE);
                ll_nosex.setVisibility(View.GONE);
                tv_room_male_fmt.setText(roomData.getJoinManMember() + "/" + roomData.getManCount() + "    ");
                tv_room_female_fmt.setText(roomData.getJoinWomanMember() + "/" + roomData.getWomanCount());
            }
        }
        //是否可以切换房间
        if (isOpen) {
            ib_exchange_gamechatroom_fmt.setVisibility(View.GONE);
        } else {
            ib_exchange_gamechatroom_fmt.setVisibility(View.VISIBLE);
            ib_exchange_gamechatroom_fmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPopupExchange(v);
                }
            });
        }
        // adapter
        if (gameChatRoomMembersAdapter == null) {
            // 第一次进来检查房间状态

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    // 判断下有没有退出当前页面,以防空指针
                    if (!((Activity) mContext).isFinishing()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 插入一条公告
                                InformationNotificationMessage informationNotificationMessage = InformationNotificationMessage.obtain(mNotice);
                                Message message = Message.obtain(roomId, GameChatRoomFragment.this.mConversationType, informationNotificationMessage);
                                message.setObjectName("RC:InfoNtf");
                                mConversationList.add(message);
                                messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
                                // TODO: 2017/7/5 出现一次 rv_conversation_list_gamechatroom_fmt 空对象 ,现判断已改为((Activity) mContext).isFinishing()
                                rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
                            }
                        });
                    }

                }
            }).start();

            // 左边的成员列表
            gameChatRoomMembersAdapter = new GameChatRoomMembersAdapter(mContext, roomData, getActivity());
            rv_members_gamechatroom_fmt.setAdapter(gameChatRoomMembersAdapter);
            rv_members_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            // 设置房主ID
            gameChatRoomMembersAdapter.setmManagerId(roomData.getManager().getId());
            // 设置状态
            gameChatRoomMembersAdapter.setState(roomData.getState());

            // 刷新数据
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MemberDiffCallback(gameChatRoomMembersAdapter.getDate(), joinMembersBeanList), true);
            gameChatRoomMembersAdapter.setDate(joinMembersBeanList);
            diffResult.dispatchUpdatesTo(gameChatRoomMembersAdapter);
        }
        mMapBundle = new Bundle();
        mMapBundle.putDouble("roomLat", roomLat);
        mMapBundle.putDouble("roomLong", roomLong);
        mMapBundle.putString("roomCity", roomCity);
        mMapBundle.putString("roomPlace", placeValue);
        mMapBundle.putString("roomId", roomId);
    }

    /**
     * 弹出确认房间是否公开
     *
     * @param v
     */
    private void initPopupExchange(View v) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_isopen, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button changeisOpen = contentView.findViewById(R.id.bt_change_open_fmt);
        Button cancel = contentView.findViewById(R.id.bt_change_cancel_fmt);
        changeisOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initChange();
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    //确认房间公开
    private void initChange() {
        PTApplication.getRequestService().setOpen(true, roomId, PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.isSuccess()) {
                            ib_exchange_gamechatroom_fmt.setVisibility(View.GONE);
                        } else {
                            ToastUtils.getToast(noDataBean.getMsg());
                        }
                    }
                });
    }


    /**
     * 改变准备按钮状态
     */
    @Override
    public void changeReadyOrCancel() {
        // 先赋值后判断，如果为true，则说明点击了准备，改成取消按钮
        if (!amIManager) {
            if (amIReady = !amIReady) {
                ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
            } else {
                ib_ready_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_ready);
            }
        } else {
            if (isBegin = !isBegin) {
                ib_begin_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_cancel);
            } else {
                ib_begin_gamechatroom_fmt.setImageResource(R.drawable.selector_game_chat_room_begin);
            }
        }
    }

    /**
     * 签到后改变状态
     *
     * @param noDataBean 签到是否成功
     */
    @Override
    public void changeCheckButton(NoDataBean noDataBean) {
        if (noDataBean.isSuccess()) {
            ib_check_gamechatroom_fmt.setEnabled(false);
            ToastUtils.getToast("签到成功");
        } else {
            ib_check_gamechatroom_fmt.setEnabled(true);
            ToastUtils.getToast(noDataBean.getMsg());
        }
    }

    /**
     * 改变加载动画显隐
     *
     * @param isShown 是否显示
     */
    @Override
    public void changeLoadView(boolean isShown) {
        if (isShown) {
            if (rl_load_View.getVisibility() == View.GONE) {
                rl_load_View.setVisibility(View.VISIBLE);
            }
        } else {
            if (rl_load_View.getVisibility() == View.VISIBLE) {
                rl_load_View.setVisibility(View.GONE);
            }
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
                AudioRecordManager.getInstance().startRecord(v.getRootView(), this.mConversationType, this.roomId);
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
                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.roomId, "RC:VcMsg");
            }

        }
    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {
        Logger.e("onEmoticonToggleClick");
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        //rl_bottom_gamechatroom.setVisibility(View.VISIBLE);
        //rl_bottom_gamechatroom.setVisibility(View.GONE);
        //AndroidBug5497Workaround.assistActivity(mRootView);
        new Thread(new Runnable() {
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
        }).start();
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
        if (rl_bottom_gamechatroom.getVisibility() == View.VISIBLE) {
            rl_bottom_gamechatroom.setVisibility(View.GONE);
            // 解决键盘不上浮问题
            //AndroidBug5497Workaround.assistActivity(mRootView);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onExtensionCollapsed() {
        // 扩展框关闭
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        if (rl_bottom_gamechatroom.getVisibility() == View.GONE) {
            rl_bottom_gamechatroom.setVisibility(View.VISIBLE);
            // 解决键盘不上浮问题
            //AndroidBug5497Workaround.assistActivity(mRootView);
        }
    }

    @Override
    public void onExtensionExpanded(int i) {
        // 扩展框打开
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
        if (rl_bottom_gamechatroom.getVisibility() == View.VISIBLE) {
            rl_bottom_gamechatroom.setVisibility(View.GONE);
            // 解决键盘不上浮问题
            //AndroidBug5497Workaround.assistActivity(mRootView);
        }
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
     * 收到的文本消息
     */
    public class MsgComingItemDelegate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_msg_coming_gamechatroom;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return "RC:TxtMsg".equals(item.getObjectName()) && item.getMessageDirection().equals(Message.MessageDirection.RECEIVE);
        }

        @Override
        public void convert(ViewHolder holder, final Message message, int position) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)));
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
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_coming_gamechatroom)));
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
            layoutParams.width = (int) (rv_members_gamechatroom_fmt.getLayoutParams().width * (1.0 + voiceMessage.getDuration() / 60.0));
            textView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 发送的文本消息
     */
    public class MsgSendItemDelegate implements ItemViewDelegate<Message> {

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

            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .placeholder(R.drawable.person_default_icon)
                    .error(R.drawable.person_default_icon)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)));

            TextView textView = holder.getView(R.id.tv_msg_item_send_gamechatroom);
            textView.setText(voiceMessage.getDuration() + "\"");

            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.width = (int) (rv_members_gamechatroom_fmt.getLayoutParams().width * (1.0 + voiceMessage.getDuration() / 60.0));
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


    /**
     * 活动介绍
     *
     * @param v
     */
    private void initPopupWindos(View v) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_roominfo, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button cancel = contentView.findViewById(R.id.cancelforowner);
        Button modtify = contentView.findViewById(R.id.moditfyroominfo);
        final TextView startTime = contentView.findViewById(R.id.tv_roominfo_starttime_pop);
        TextView endTime = contentView.findViewById(R.id.tv_roominfo_endtime_pop);
        TextView place = contentView.findViewById(R.id.tv_roominfo_place_pop);
        TextView money = contentView.findViewById(R.id.tv_roominfo_money_pop);
        TextView disc = contentView.findViewById(R.id.tv_roominfo_disc_pop);
        ImageView location = contentView.findViewById(R.id.iv_roominfo_location_pop);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomLocationActivity.class);
                intent.putExtras(mMapBundle);
                startActivity(intent);
            }
        });
        //startTime.setText(mNotice);
        startTime.setText("开始时间:" + startTimeValue);
        endTime.setText("结束时间:" + endTimeValue);
        place.setText("活动地点:" + placeValue);
        money.setText("保证金:" + moneyValue / 100.0f);
        disc.setText("活动介绍:" + discValue);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        modtify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModitfyRoomInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("roomName", roomName);
                bundle.putString("disc", discValue);
                bundle.putString("startTime", startTimeValue);
                bundle.putString("endTime", endTimeValue);
                bundle.putString("roomId", roomId);
                bundle.putInt("womanCount", womanCount);
                bundle.putInt("manCount", manCount);
                bundle.putInt("membersCount", memberCount);
                intent.putExtras(bundle);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firstLoading) {
            firstLoading = false;
        } else {
            Logger.e("onStart: 上线");
            mPresenter.setOnline(true, roomId);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.e("onPause: 离线");
        mPresenter.setOnline(false, roomId);
    }
}
