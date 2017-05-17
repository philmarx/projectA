package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.data.UserGameRankingBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Key on 2017/5/9 20:33
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomMembersAdapter extends RecyclerView.Adapter<GameChatRoomMembersAdapter.GameChatRoomMembersViewHolder> {

    private Context mContext;
    private long mManagerId;
    private long mRoomId;
    private int mGameId;

    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;
    private final Realm mRealm = Realm.getDefaultInstance();
    private View popupContent;
    private PopupWindow popup;

    // 踢人
    private TextView tv_memberinfo_outman_pop;
    // 主页
    private TextView tv_memberinfo_home_pop;
    // 昵称
    private TextView tv_memberinfo_name_pop;
    // 头像
    private ImageView civ_memberinfo_icon_pop;
    // 性别
    private ImageView iv_tv_memberinfo_sex_pop;
    // 参加的次数
    private TextView tv_memberinfo_count_pop;
    // 该项目分数
    private TextView tv_memberinfo_point_pop;
    // 该项目排名，0是没参加过没有排名
    private TextView tv_memberinfo_ranking_pop;

    public GameChatRoomMembersAdapter(Context mContext, List<GameChatRoomBean.DataBean.JoinMembersBean> mDate, long mManagerId, long mRoomId, int mGameId) {
        this.mContext = mContext;
        this.mDate = mDate;
        this.mManagerId = mManagerId;
        this.mRoomId = mRoomId;
        this.mGameId = mGameId;

        popupContent = View.inflate(mContext, R.layout.pop_memberinfo, null);
        initPop();
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
            @Override
            public void onClick(View v) {
                final int position = (int) v.getTag();
                tv_memberinfo_home_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/5/17 跳到个人主页
                    }
                });
                tv_memberinfo_outman_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                        // 踢人
                        PTApplication.getRequestService().outMan(mDate.get(position).getId(), mRoomId, PTApplication.userToken, PTApplication.myInfomation.getData().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError:  " + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {

                                        } else {
                                            ToastUtils.getToast(mContext, noDataBean.getMsg());
                                        }
                                    }
                                });
                    }
                });

                Logger.w(mDate.get(position).getId() +"            " + mGameId);
                PTApplication.getRequestService().findGameRankingByUserId(mDate.get(position).getId(), mGameId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<UserGameRankingBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("findGameRankingByUserId:  " + e.getMessage());
                            }

                            @Override
                            public void onNext(UserGameRankingBean userGameRankingBean) {
                                if (userGameRankingBean.isSuccess()) {
                                    Logger.e("success:  " + userGameRankingBean.getData().toString());
                                    String ranking = userGameRankingBean.getData().getRanking() == 0 ? "无排名" : "第" + userGameRankingBean.getData().getRanking() + "名";
                                    Logger.w(ranking);
                                    tv_memberinfo_ranking_pop.setText(ranking);
                                    iv_tv_memberinfo_sex_pop.setImageResource(userGameRankingBean.getData().getGender() ? R.drawable.maleclick : R.drawable.femaleclick);
                                    tv_memberinfo_count_pop.setText(String.valueOf(userGameRankingBean.getData().getCount()));
                                    tv_memberinfo_point_pop.setText(String.valueOf(userGameRankingBean.getData().getPoint()));
                                    //popup.setContentView(popupContent);
                                } else {
                                    ToastUtils.getToast(mContext, "获取用户信息失败");
                                }
                            }
                        });

                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + mDate.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .signature(new StringSignature(mDate.get(position).getAvatarSignature()))
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(civ_memberinfo_icon_pop);

                tv_memberinfo_name_pop.setText(mDate.get(position).getNickname());

                popup.showAsDropDown(v, v.getWidth()/2, -v.getHeight()/2);

            }
        });
        return new GameChatRoomMembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameChatRoomMembersViewHolder holder, int position) {
        GameChatRoomBean.DataBean.JoinMembersBean joinedMember = mDate.get(position);

        holder.itemView.setTag(position);

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


    private void initPop() {
        popup = new PopupWindow(popupContent, -2, -2, true);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new ColorDrawable(0));

        civ_memberinfo_icon_pop = (ImageView) popupContent.findViewById(R.id.civ_memberinfo_icon_pop);
        iv_tv_memberinfo_sex_pop = (ImageView) popupContent.findViewById(R.id.iv_tv_memberinfo_sex_pop);
        tv_memberinfo_name_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_name_pop);
        tv_memberinfo_count_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_count_pop);
        tv_memberinfo_point_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_point_pop);
        tv_memberinfo_ranking_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_ranking_pop);
        tv_memberinfo_home_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_home_pop);
        tv_memberinfo_outman_pop = (TextView) popupContent.findViewById(R.id.tv_memberinfo_outman_pop);
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

