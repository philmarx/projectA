package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/11/2.
 */

public class ChatRoomMemberListAdapter extends RecyclerView.Adapter<ChatRoomMemberListAdapter.ViewHolder> {
    private Context mContext;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;



    public ChatRoomMemberListAdapter(Context mContext,List<GameChatRoomBean.DataBean.JoinMembersBean> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_memberlist_avatar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDate.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .signature(new StringSignature(mDate.get(position).getAvatarSignature()))
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.civ_chatroom_memberlist_avatar_fmt);
        }
    }
}
