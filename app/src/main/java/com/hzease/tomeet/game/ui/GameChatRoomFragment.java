package com.hzease.tomeet.game.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.game.IGameChatRoomContract;
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
import io.rong.eventbus.EventBus;
import io.rong.imkit.model.Event;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/5/4 14:19
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomFragment extends BaseFragment implements IGameChatRoomContract.View {

    // 会话内容
    @BindView(R.id.rv_conversation_list_gamechatroom_fmt)
    RecyclerView rv_conversation_list_gamechatroom_fmt;
    // 成员列表
    @BindView(R.id.rv_members_gamechatroom_fmt)
    RecyclerView rv_members_gamechatroom_fmt;
    // 房间名字
    @BindView(R.id.tv_room_name_gamechatroom_fmg)
    TextView tv_room_name_gamechatroom_fmg;

    private IGameChatRoomContract.Presenter mPresenter;
    private String roomId;

    private List<Message> mConversationList = new ArrayList<>();
    private MultiItemTypeAdapter messageMultiItemTypeAdapter;
    private GameChatRoomMembersAdapter gameChatRoomMembersAdapter;

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
        EventBus.getDefault().register(this);

        RongIMClient.getInstance().joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
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
        rv_conversation_list_gamechatroom_fmt.setAdapter(messageMultiItemTypeAdapter);
        rv_conversation_list_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));




        // 初始化完的最后一步加载数据
        mPresenter.getGameChatRoomInfo(roomId);
    }

    // 接收到消息的event
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Logger.d("聊天室onEventMainThread   MessageContentEncode: " + new String(event.getMessage().getContent().encode())
                + "   getTargetId: " + event.getMessage().getTargetId() + "   Left: " + event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName());
        // 插入数据源
        mConversationList.add(event.getMessage());
        // 更新界面
        messageMultiItemTypeAdapter.notifyItemInserted(mConversationList.size());
        rv_conversation_list_gamechatroom_fmt.smoothScrollToPosition(mConversationList.size());
    }

    // 发出消息的event
    public void onEventMainThread(Message message) {
        Logger.w("发出消息的event: " + message.getSentStatus() + "  " + new String(message.getContent().encode()) + "  发送时间: " + message.getSentTime());
        // TODO: 2017/3/31 发送失败的效果还没处理
        //
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 离开房间
        mPresenter.leaveRoom(roomId);

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

    @OnClick({R.id.ib_return_gamechatroom_fmg, R.id.ib_detail_gamechatroom_fmg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 关闭聊天室
            case R.id.ib_return_gamechatroom_fmg:
                getActivity().finish();
                break;
            // 房间详情
            case R.id.ib_detail_gamechatroom_fmg:
                ToastUtils.getToast(mContext, "房间详情页");
                break;
        }
    }

    /**
     * 加载房间数据
     */
    @Override
    public void refreshGameChatRoomInfo(final GameChatRoomBean gameChatRoomBean) {
        // adapter
        if (gameChatRoomMembersAdapter == null) {
            // 左边的成员列表
            gameChatRoomMembersAdapter = new GameChatRoomMembersAdapter(mContext, gameChatRoomBean.getData().getJoinMembers());
            Logger.i("adapter:  " + gameChatRoomMembersAdapter);
            rv_members_gamechatroom_fmt.setAdapter(gameChatRoomMembersAdapter);
            rv_members_gamechatroom_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        tv_room_name_gamechatroom_fmg.setText(gameChatRoomBean.getData().getName());
        Logger.w(gameChatRoomBean.getData().getName() +"   " + gameChatRoomBean.getData().getJoinMembers().toString());
        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MemberDiffCallback(gameChatRoomMembersAdapter.getDatas(), gameChatRoomBean.getData().getJoinMembers()), true);
        //diffResult.dispatchUpdatesTo(gameChatRoomMembersAdapter);

        // TODO: 2017/5/8 数据刷新
        //gameChatRoomMembersAdapter.notifyDataSetChanged();
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
            return (!item.getSenderUserId().equals(PTApplication.userId));
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
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
            return item.getSenderUserId().equals(PTApplication.userId);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                    .into(((ImageView) holder.getView(R.id.iv_avatar_item_send_gamechatroom)));
            holder.setText(R.id.tv_msg_item_send_gamechatroom, new TextMessage(message.getContent().encode()).getContent());
        }
    }
}
