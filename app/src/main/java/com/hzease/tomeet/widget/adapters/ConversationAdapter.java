package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RealmFriendBean;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Key on 2017/3/25 00:12
 * email: MrKey.K@gmail.com
 * description:
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private onRecyclerViewItemClickListener mItemClickListener = null;
    private List<RealmFriendBean> friends;
    private Context mContext;
    private final Realm mRealm = Realm.getDefaultInstance();
    private String avatar;

    public ConversationAdapter(Context mContext) {
        friends = mRealm.where(RealmFriendBean.class).between("point", 9, 10).findAllSorted("lastTime", Sort.DESCENDING);
        this.mContext = mContext;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation_chat_fmt, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    // 昵称 R.id.width  --  ID  R.id.height
                    mItemClickListener.onItemClick(v, String.valueOf(v.getTag(R.id.height)), (String) v.getTag(R.id.width),avatar);
                }
            }
        });

        /*Badge badge = new QBadgeView(mContext)
                //.setBadgeNumber(friendBean.getUnreadCount())
                .setGravityOffset(0, 0, true)
                .bindTarget(view.findViewById(R.id.tv_unread_item_conversation_chat_fmt))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(mContext, "清空聊天记录", Toast.LENGTH_SHORT).show();
                    }
                });*/

        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConversationViewHolder holder, int position) {
        // 拿到好友对象
        final RealmFriendBean friendBean = friends.get(position);

        // 昵称 R.id.width
        holder.itemView.setTag(R.id.width, friendBean.getNickname());
        // ID  R.id.height
        holder.itemView.setTag(R.id.height, friendBean.getId());

        // 最后一条消息
        holder.tv_message_item_conversation_chat_fmt.setText(friendBean.getLastMessage());
        // 最后受到消息的时间
        long lastTime = friendBean.getLastTime();
        holder.tv_time_item_conversation_chat_fmt.setText(lastTime == 0 ? "" : DateFormat.format("HH:mm", lastTime));
        // 昵称
        holder.tv_name_item_conversation_chat_fmt.setText(friendBean.getNickname());

        avatar = friendBean.getAvatarSignature();
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + String.valueOf(friendBean.getId()) + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(friendBean.getAvatarSignature()))
                .into(holder.iv_avatar_item_conversation_chat_fmt);

        // 未读
        // holder.tv_unread_item_conversation_chat_fmt.setText(String.valueOf(friendBean.getUnreadCount()));
        holder.badge.setBadgeNumber(friendBean.getUnreadCount());

    }

    @Override
    public long getItemId(int position) {
        return friends.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void updateFriends() {

        notifyDataSetChanged();
    }
    public void switchFriends(int[] pointArray) {
        friends = mRealm.where(RealmFriendBean.class).between("point", pointArray[0], pointArray[1]).findAllSorted("lastTime", Sort.DESCENDING);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_avatar_item_conversation_chat_fmt;
        private final TextView tv_name_item_conversation_chat_fmt;
        private final TextView tv_time_item_conversation_chat_fmt;
        private final TextView tv_message_item_conversation_chat_fmt;
        // private final TextView tv_unread_item_conversation_chat_fmt;
        private final Badge badge;

        public ConversationViewHolder(final View itemView) {
            super(itemView);
            // tv_unread_item_conversation_chat_fmt = (TextView) itemView.findViewById(R.id.tv_unread_item_conversation_chat_fmt);
            tv_name_item_conversation_chat_fmt = (TextView) itemView.findViewById(R.id.tv_name_item_conversation_chat_fmt);
            iv_avatar_item_conversation_chat_fmt = (ImageView) itemView.findViewById(R.id.iv_avatar_item_conversation_chat_fmt);
            tv_time_item_conversation_chat_fmt = (TextView) itemView.findViewById(R.id.tv_time_item_conversation_chat_fmt);
            tv_message_item_conversation_chat_fmt = (TextView) itemView.findViewById(R.id.tv_message_item_conversation_chat_fmt);
            this.badge = new QBadgeView(mContext)
                    //.setBadgeNumber(friendBean.getUnreadCount())
                    .setGravityOffset(0, 0, true)
                    .bindTarget(itemView.findViewById(R.id.tv_unread_item_conversation_chat_fmt))
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                                RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, String.valueOf(itemView.getTag(R.id.height)), new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        Logger.e("清除未读：" + aBoolean);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        Logger.e("清除未读Error：" + errorCode.getMessage());
                                    }
                                });


                                Realm realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmFriendBean first = realm.where(RealmFriendBean.class).equalTo("id", (long)itemView.getTag(R.id.height)).findFirst();
                                            if (first != null) {
                                                PTApplication.unReadNumber -= first.getUnreadCount();
                                                PTApplication.badge.setBadgeNumber(PTApplication.unReadNumber);
                                                first.setUnreadCount(0);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    realm.close();
                                }
                            }

                        }
                    });
        }
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View v, String friendId, String nickName,String avatar);
    }
}