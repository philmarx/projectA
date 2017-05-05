package com.hzease.tomeet.game.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.game.IGameChatRoomContract;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/5/4 14:19
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomFragment extends BaseFragment implements IGameChatRoomContract.View{

    @BindView(R.id.rv_conversation_list_gamechatroom_fmt)
    RecyclerView rv_conversation_list_gamechatroom_fmt;

    private IGameChatRoomContract.Presenter mPresenter;
    private String roomId;

    private List<Message> mConversationList = new ArrayList<>();

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
        for (int i = 0; i < 10; i++) {
            String s = "test";
            Message message = new Message();
            message.setContent(new TextMessage(s + i));
            mConversationList.add(message);
        }

        roomId = getActivity().getIntent().getStringExtra(AppConstants.TOMEET_ROOM_ID);
        Logger.i(roomId);
        RongIMClient.getInstance().joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                // 注册event
                //EventBus.getDefault().register(this);
                //InternalModuleManager.getInstance().onLoaded();

                // 插入一条房间信息

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

        // 会话列表adapter
        MultiItemTypeAdapter messageMultiItemTypeAdapter = new MultiItemTypeAdapter<>(mContext, mConversationList);
        messageMultiItemTypeAdapter.addItemViewDelegate(new MsgComingItemDelagate());
        //messageMultiItemTypeAdapter.addItemViewDelegate(new MsgSendItemDelagate());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RongIMClient.getInstance().quitChatRoom(roomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
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
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + message.getSenderUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .asBitmap()
                        .centerCrop()
                        .into(200, 200).get();
                holder.setImageBitmap(R.id.tv_msg_item_coming_gamechatroom, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            holder.setText(R.id.iv_avatar_item_coming_gamechatroom, new String(message.getContent().encode()));
        }
    }

    /**
     * 发送的消息
     */
    public class MsgSendItemDelagate implements ItemViewDelegate<Message> {

        @Override
        public int getItemViewLayoutId() {
            return 0;
        }

        @Override
        public boolean isForViewType(Message item, int position) {
            return item.getSenderUserId().equals(PTApplication.userId);
        }

        @Override
        public void convert(ViewHolder holder, Message message, int position) {

        }
    }
}
