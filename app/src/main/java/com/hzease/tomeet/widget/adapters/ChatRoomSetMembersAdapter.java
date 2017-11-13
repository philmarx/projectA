package com.hzease.tomeet.widget.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.hzease.tomeet.chat.ui.AddFriendActivity;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.data.UserGameRankingBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

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

public class ChatRoomSetMembersAdapter extends RecyclerView.Adapter<ChatRoomSetMembersAdapter.GameChatRoomMembersViewHolder> {

    private Activity activity;
    private Context mContext;
    private long mManagerId;
    private long mRoomId;
    private int mGameId;
    private int state;

    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDate;
    private String gameType;
    private final Realm mRealm = Realm.getDefaultInstance();
    private View popupContent;
    private PopupWindow popup;

    // 踢人
    private ImageView tv_memberinfo_outman_pop;
    // 主页
    private ImageView tv_memberinfo_home_pop;
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
    //是否是vip
    private ImageView iv_memberinfo_vip_pop;
    //添加
    private ImageView tv_memberinfo_add_pop;
    //活动类型
    private TextView tv_game_type_pop;
    //活动类型的图标
    /*private int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.one_5, R.drawable.two_one1_1, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};*/

    public ChatRoomSetMembersAdapter(Context mContext, GameChatRoomBean.DataBean roomData, Activity activity) {
        this.mContext = mContext;
        this.mDate = roomData.getJoinMembers();
        this.mManagerId = roomData.getManager().getId();
        this.mRoomId = roomData.getId();
        this.mGameId = roomData.getGame().getId();
        this.state = roomData.getState();
        this.activity = activity;
        gameType = roomData.getGame().getName();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_member_chatroom_set_fmt, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
                wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        wlBackground.alpha = 1.0f;
                        activity.getWindow().setAttributes(wlBackground);
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    }
                });
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
                tv_memberinfo_add_pop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                        initPopAddFriend(v);
                    }
                });

                tv_memberinfo_outman_pop.setVisibility(View.GONE);

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
                                    String ranking = userGameRankingBean.getData().getRanking() == 0 ? "无排名" : userGameRankingBean.getData().getRanking() + "";
                                    Logger.w(ranking);
                                    tv_memberinfo_ranking_pop.setText(ranking);
                                    iv_tv_memberinfo_sex_pop.setImageResource(userGameRankingBean.getData().getGender() ? R.drawable.maleclick : R.drawable.femaleclick);
                                    tv_memberinfo_count_pop.setText(String.valueOf(userGameRankingBean.getData().getCount()));
                                    tv_memberinfo_point_pop.setText(String.valueOf(userGameRankingBean.getData().getPoint()));
                                    //popup.setContentView(popupContent);
                                } else {
                                    ToastUtils.getToast("获取用户信息失败");
                                }
                            }
                        });

                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + mDate.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .signature(new StringSignature(mDate.get(position).getAvatarSignature()))
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(civ_memberinfo_icon_pop);
                tv_game_type_pop.setText(gameType + "局数");
                //iv_memberinfo_type_pop.setImageResource(gameType[mGameId]);
                tv_memberinfo_name_pop.setText(mDate.get(position).getNickname());
                if (mDate.get(position).isVip()) {
                    iv_memberinfo_vip_pop.setVisibility(View.VISIBLE);
                } else {
                    iv_memberinfo_vip_pop.setVisibility(View.GONE);
                }
                //popup.showAsDropDown(v, v.getWidth() / 2, -v.getHeight() / 2);
                popup.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });
        return new GameChatRoomMembersViewHolder(view);
    }

    //添加弹窗
    private void initPopAddFriend(View v) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_addfriend, null);
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
        LinearLayout ll_addfriend_phone = (LinearLayout) contentView.findViewById(R.id.ll_addfriend_phone);
        LinearLayout ll_addfriend_wechat = (LinearLayout) contentView.findViewById(R.id.ll_addfriend_wechat);
        ll_addfriend_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddFriendActivity.class);
                activity.startActivity(intent);
            }
        });
        ll_addfriend_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到其他渠道分享
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    activity.requestPermissions(mPermissionList, 123);
                }
                new ShareAction(activity)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media != null) {
                                    UMWeb web = new UMWeb(AppConstants.TOMMET_SHARE_APP_SHARE + PTApplication.userId + "&origin=" + share_media.toString());
                                    web.setTitle("后会有期");
                                    web.setThumb(new UMImage(mContext, R.drawable.share_logo_200x200));
                                    web.setDescription("薛之谦的心愿是世界和平，我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）");
                                    new ShareAction(activity).setPlatform(share_media).setCallback(new UMShareListener() {
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
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onBindViewHolder(GameChatRoomMembersViewHolder holder, int position) {
        GameChatRoomBean.DataBean.JoinMembersBean joinedMember = mDate.get(position);

        holder.itemView.setTag(position);
        if (joinedMember.isVip()) {
            holder.iv_item_member_isvip.setVisibility(View.VISIBLE);
        } else {
            holder.iv_item_member_isvip.setVisibility(View.GONE);
        }
        holder.tv_nickname_item_member_gamechatroom_fmt.setText(joinedMember.getNickname());

        /*switch (state) {
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
        }*/

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

        civ_memberinfo_icon_pop = popupContent.findViewById(R.id.civ_memberinfo_icon_pop);
        iv_tv_memberinfo_sex_pop = popupContent.findViewById(R.id.iv_tv_memberinfo_sex_pop);
        tv_memberinfo_name_pop = popupContent.findViewById(R.id.tv_memberinfo_name_pop);
        tv_memberinfo_count_pop = popupContent.findViewById(R.id.tv_memberinfo_count_pop);
        tv_memberinfo_point_pop = popupContent.findViewById(R.id.tv_memberinfo_point_pop);
        tv_memberinfo_ranking_pop = popupContent.findViewById(R.id.tv_memberinfo_ranking_pop);
        tv_memberinfo_home_pop = popupContent.findViewById(R.id.tv_memberinfo_home_pop);
        tv_memberinfo_outman_pop = popupContent.findViewById(R.id.tv_memberinfo_outman_pop);
        iv_memberinfo_vip_pop = popupContent.findViewById(R.id.iv_memberinfo_vip_pop);
        tv_memberinfo_add_pop = popupContent.findViewById(R.id.tv_memberinfo_add_pop);
        tv_game_type_pop = popupContent.findViewById(R.id.tv_game_type_pop);
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
        //是否是vip
        @BindView(R.id.iv_item_member_isvip)
        ImageView iv_item_member_isvip;


        GameChatRoomMembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

