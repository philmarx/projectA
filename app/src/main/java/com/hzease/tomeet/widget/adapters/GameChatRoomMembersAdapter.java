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
    private long mManagerId;
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;
    private final Realm mRealm = Realm.getDefaultInstance();

    public GameChatRoomMembersAdapter(Context mContext, List<GameChatRoomBean.DataBean.JoinMembersBean> mDate, long mManagerId) {
        this.mContext = mContext;
        this.mDate = mDate;
        this.mManagerId = mManagerId;
    }
    public List<GameChatRoomBean.DataBean.JoinMembersBean> getDate() {
        return mDate;
    }

    public void setDate(List<GameChatRoomBean.DataBean.JoinMembersBean> mDate) {
        this.mDate = mDate;
    }

    public long getmManagerId() {
        return mManagerId;
    }

    public void setmManagerId(long mManagerId) {
        this.mManagerId = mManagerId;
    }

    @Override
    public GameChatRoomMembersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_member_gamechatroom_fmt, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            {
                Logger.e("onCreateViewHolder");
            }
            @Override
            public void onClick(View v) {
                long joinedMemberId = (long) v.getTag();
                // // TODO: 2017/5/11 弹窗
            }
        });
        return new GameChatRoomMembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameChatRoomMembersViewHolder holder, int position) {
        GameChatRoomBean.DataBean.JoinMembersBean joinedMember = mDate.get(position);

        holder.itemView.setTag(joinedMember.getId());

        holder.tv_nickname_item_member_gamechatroom_fmt.setText(joinedMember.getNickname());

        if (joinedMember.isReady()) {
            holder.tv_status_item_member_gamechatroom_fmt.setVisibility(View.VISIBLE);
            if (joinedMember.getId() == mManagerId) {
                holder.tv_status_item_member_gamechatroom_fmt.setText("房主");
                holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.red);
            } else {
                holder.tv_status_item_member_gamechatroom_fmt.setText("准备");
                holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.topcolor);
            }
        } else {
            holder.tv_status_item_member_gamechatroom_fmt.setVisibility(View.INVISIBLE);
        }


        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + joinedMember.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .placeholder(R.drawable.person_default_icon)
                .error(R.drawable.person_default_icon)
                .signature(new StringSignature(joinedMember.getAvatarSignature()))
                .into(holder.civ_avatar_item_member_gamechatroom_fmt);

        RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", joinedMember.getId()).findFirst();
        int color = R.color.transparenttm;
        if (friendBean != null) {
            Logger.e("point:  " + friendBean.getPoint());
            switch(friendBean.getPoint()) {
                case 1:
                case 2:
                    color = R.color.friend_red;
                    break;
                case 3:
                case 4:
                    color = R.color.friend_gray;
                    break;
                case 5:
                case 6:
                    color = R.color.friend_green;
                    break;
                case 7:
                case 8:
                    color = R.color.friend_blue;
                    break;
                case 9:
                case 10:
                    color = R.color.friend_gold;
                    break;
            }
        }
        holder.civ_avatar_bg_item_member_gamechatroom_fmt.setImageResource(color);

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    class GameChatRoomMembersViewHolder extends RecyclerView.ViewHolder {
        //头像光环
        @BindView(R.id.civ_avatar_bg_item_member_gamechatroom_fmt)
        CircleImageView civ_avatar_bg_item_member_gamechatroom_fmt;
        // 头像
        @BindView(R.id.civ_avatar_item_member_gamechatroom_fmt)
        CircleImageView civ_avatar_item_member_gamechatroom_fmt;
        // 昵称
        @BindView(R.id.tv_nickname_item_member_gamechatroom_fmt)
        TextView tv_nickname_item_member_gamechatroom_fmt;
        //
        @BindView(R.id.tv_status_item_member_gamechatroom_fmt)
        TextView tv_status_item_member_gamechatroom_fmt;

        GameChatRoomMembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

