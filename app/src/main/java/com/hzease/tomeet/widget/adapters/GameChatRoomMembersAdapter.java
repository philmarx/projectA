package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
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

    private Activity activity;
    private Context mContext;
    private long mManagerId;
    private long mRoomId;
    private int mGameId;
    private int state;

    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;
    private int gameId;
    private final Realm mRealm = Realm.getDefaultInstance();
    private View popupContent;
    private PopupWindow popup;

    //活动图标
    private ImageView iv_memberinfo_type_pop;
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
    //活动类型的图标
    private int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.one_5, R.drawable.two_one1_1, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};

    public GameChatRoomMembersAdapter(Context mContext, GameChatRoomBean.DataBean roomData, Activity activity) {
        this.mContext = mContext;
        this.mDate = roomData.getJoinMembers();
        this.mManagerId = roomData.getManager().getId();
        this.mRoomId = roomData.getId();
        this.mGameId = roomData.getGame().getId();
        this.state = roomData.getState();
        this.activity = activity;
        gameId = roomData.getGame().getId();
        popupContent = View.inflate(mContext, R.layout.pop_memberinfo, null);
        initPop();
    }

    public List<GameChatRoomBean.DataBean.JoinMembersBean> getDate() {
        return mDate;
    }

    public void setDate(List<GameChatRoomBean.DataBean.JoinMembersBean> mDate) {
        this.mDate = mDate;
    }

    public void setState(int state) {
        this.state = state;
        // 改变状态时，彻底刷新一次
        notifyDataSetChanged();
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
                        Intent intent = new Intent(mContext, PersonOrderInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("userId", mDate.get(position).getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });

                // 判断是否是房主，可以踢人
                if (mManagerId == PTApplication.myInfomation.getData().getId()) {
                    tv_memberinfo_outman_pop.setVisibility(View.VISIBLE);
                    tv_memberinfo_outman_pop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 踢人vip 要有提示
                            if (mDate.get(position).isVip()) {
                                popup.dismiss();
                                initPopOutMan(v, mDate.get(position).getId(), mRoomId);
                            } else {
                                outMan(mDate.get(position).getId(), mRoomId, "");
                                popup.dismiss();
                            }
                        }
                    });
                } else {
                    tv_memberinfo_outman_pop.setVisibility(View.GONE);
                }

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
                iv_memberinfo_type_pop.setImageResource(gameType[mGameId]);
                tv_memberinfo_name_pop.setText(mDate.get(position).getNickname());

                popup.showAsDropDown(v, v.getWidth() / 2, -v.getHeight() / 2);
            }
        });
        return new GameChatRoomMembersViewHolder(view);
    }

    private void initPopOutMan(View v, final long id, final long mRoomId) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_outvipman, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        activity.getWindow().setAttributes(wlBackground);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        final EditText outreason = (EditText) contentView.findViewById(R.id.et_outman_reason_pop);
        Button outman = (Button) contentView.findViewById(R.id.bt_outman_outman_fmt);
        Button cancel = (Button) contentView.findViewById(R.id.bt_outman_cancel_fmt);
        outman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outreason.getText().toString().trim().length() < 5) {
                    ToastUtils.getToast(mContext, "尊重点vip，人家花钱的，理由写长点");
                } else {
                    outMan(id, mRoomId, outreason.getText().toString().trim());
                    popupWindow.dismiss();
                }
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

    //踢人
    private void outMan(long id, long mRoomId, String reason) {
        PTApplication.getRequestService().outMan(id, mRoomId, PTApplication.userToken, PTApplication.myInfomation.getData().getId(), reason)
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

    @Override
    public void onBindViewHolder(GameChatRoomMembersViewHolder holder, int position) {
        GameChatRoomBean.DataBean.JoinMembersBean joinedMember = mDate.get(position);

        holder.itemView.setTag(position);

        holder.tv_nickname_item_member_gamechatroom_fmt.setText(joinedMember.getNickname());

        switch (state) {
            case 0:
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
                break;
            case 1:
                holder.tv_status_item_member_gamechatroom_fmt.setVisibility(View.VISIBLE);
                if (joinedMember.isSigned()) {
                    holder.tv_status_item_member_gamechatroom_fmt.setText("已签到");
                    holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.topcolor);
                } else if (joinedMember.isAttend()) {
                    holder.tv_status_item_member_gamechatroom_fmt.setText("已出发");
                    holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.game_chat_room_go);
                } else {
                    holder.tv_status_item_member_gamechatroom_fmt.setText("未出发");
                    holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.gray);
                }
                break;
            case 2:
                holder.tv_status_item_member_gamechatroom_fmt.setVisibility(View.VISIBLE);
                if (joinedMember.isSigned()) {
                    holder.tv_status_item_member_gamechatroom_fmt.setText("已签到");
                    holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.topcolor);
                } else {
                    holder.tv_status_item_member_gamechatroom_fmt.setText("已迟到");
                    holder.tv_status_item_member_gamechatroom_fmt.setBackgroundResource(R.color.friend_red);
                }
                break;
        }

        holder.civ_online_item_member_gamechatroom_fmt.setImageResource(joinedMember.isOnline() || joinedMember.getId() == PTApplication.myInfomation.getData().getId() ? R.color.online_green : R.color.online_gray);

        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + joinedMember.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .signature(new StringSignature(joinedMember.getAvatarSignature()))
                .into(holder.civ_avatar_item_member_gamechatroom_fmt);

        RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", joinedMember.getId()).findFirst();
        int color = R.color.transparenttm;
        if (friendBean != null) {
            //Logger.e("point:  " + friendBean.getPoint());
            switch (friendBean.getPoint()) {
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

        iv_memberinfo_type_pop = (ImageView) popupContent.findViewById(R.id.iv_memberinfo_type_pop);
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
        //
        @BindView(R.id.civ_online_item_member_gamechatroom_fmt)
        CircleImageView civ_online_item_member_gamechatroom_fmt;



        GameChatRoomMembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

