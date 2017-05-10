package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Key on 2017/5/9 20:33
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomMembersAdapter extends RecyclerView.Adapter<GameChatRoomMembersAdapter.GameChatRoomMembersViewHolder> {

    private Context mContext;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;
    private final Realm mRealm = Realm.getDefaultInstance();

    public GameChatRoomMembersAdapter(Context mContext, List<GameChatRoomBean.DataBean.JoinMembersBean> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }

    @Override
    public GameChatRoomMembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_member_gamechatroom_fmt, parent, false);
        return new GameChatRoomMembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameChatRoomMembersViewHolder holder, int position) {
        holder.tv_nickname_item_member_gamechatroom_fmt.setText(mDate.get(position).getNickname());

        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDate.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .signature(new StringSignature(mDate.get(position).getAvatarSignature()))
                .into(holder.civ_avatar_item_member_gamechatroom_fmt);

        RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", mDate.get(position).getId()).findFirst();
        if (friendBean != null) {
            Logger.e("point:  ");
            int color = R.color.transparenttm;
            switch(friendBean.getPoint()) {
                case 1:
                case 2:
                    color = R.color.friend_level1;
                    break;
                case 3:
                case 4:
                    color = R.color.friend_level2;
                    break;
                case 5:
                case 6:
                    color = R.color.friend_level3;
                    break;
                case 7:
                case 8:
                    color = R.color.friend_level4;
                    break;
                case 9:
                case 10:
                    color = R.color.friend_level5;
                    break;
            }
            holder.civ_avatar_bg_item_member_gamechatroom_fmt.setImageResource(color);
        }

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class GameChatRoomMembersViewHolder extends RecyclerView.ViewHolder {
        //头像光环
        @BindView(R.id.civ_avatar_bg_item_member_gamechatroom_fmt)
        CircleImageView civ_avatar_bg_item_member_gamechatroom_fmt;
        // 头像
        @BindView(R.id.civ_avatar_item_member_gamechatroom_fmt)
        CircleImageView civ_avatar_item_member_gamechatroom_fmt;
        // 昵称
        @BindView(R.id.tv_nickname_item_member_gamechatroom_fmt)
        TextView tv_nickname_item_member_gamechatroom_fmt;

        public GameChatRoomMembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

