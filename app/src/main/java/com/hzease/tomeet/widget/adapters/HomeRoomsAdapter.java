package com.hzease.tomeet.widget.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/4/10.
 */

public class HomeRoomsAdapter extends RecyclerView.Adapter {
    private Realm mRealm;

    private List<HomeRoomsBean.DataBean> list = new ArrayList<>();

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_OTHER = 2;
    // 隐藏
    public static final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public static final int LOADING_MORE = 1;
    // 没有更多
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = LOADING_MORE;

    public int getmLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    private int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.others_icon, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};

    /**
     * ItemClick的回调接口
     *
     * @author xq
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, HomeRoomsBean.DataBean position);
    }

    private HomeRoomsAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(HomeRoomsAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<HomeRoomsBean.DataBean> getList() {
        return list;
    }

    public void setList(List<HomeRoomsBean.DataBean> list) {
        if (list.isEmpty()) {
            this.mLoadMoreStatus = NO_LOAD_MORE;
        } else {
            this.mLoadMoreStatus = PULLUP_LOAD_MORE;
        }
        this.list = list;
    }

    public HomeRoomsAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rooms, parent, false);
            //如果设置了回调，则设置点击事件,把条目对象传出去更方便
            if (mOnItemClickLitener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(view, list.get((Integer) view.getTag()));
                    }
                });
            }
            return new ViewHolder(view);
        } else if (viewType == TYPE_OTHER) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activitytype, parent, false);
            if (mOnItemClickLitener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(view, list.get((Integer) view.getTag()));
                    }
                });
            }
            return new OthersStateHolder(view);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            // 把position放到tag中
            holder.itemView.setTag(position);
            int size = list.get(position).getJoinMembers().size();
            // 设置10个头像
            for (int i = 0; i < 9; i++) {
                int color = R.color.transparenttm;
                if (i < size) {
                    // 设置头像
                    Glide.with(holder.itemView.getContext())
                            .load(AppConstants.YY_PT_OSS_USER_PATH + list.get(position).getJoinMembers().get(i).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                            .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                            .signature(new StringSignature(list.get(position).getJoinMembers().get(i).getAvatarSignature()))
                            .into(holder.avatar_list.get(i));

                    // 设置背景
                    if (PTApplication.myInfomation != null) {
                        if (mRealm == null || !(PTApplication.userId + ".realm").equals(mRealm.getConfiguration().getRealmFileName())) {
                            mRealm = Realm.getDefaultInstance();
                        }
                        RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", list.get(position).getJoinMembers().get(i).getId()).findFirst();
                        if (friendBean != null) {
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
                    }
                } else {
                    // 删除SRC
                    holder.avatar_list.get(i).setImageResource(0);
                }
                holder.avatar_bg_list.get(i).setImageResource(color);
            }

            holder.iv_rooms_gameicon_item.setImageResource(gameType[list.get(position).getGame().getId()]);

            holder.tv_homeroomsitem_name.setText(list.get(position).getName());

            // 地名
            holder.tv_homeroomsitem_place.setText(list.get(position).getPlace());

            // 距离
            LatLng latLng1 = new LatLng(PTApplication.myLatitude, PTApplication.myLongitude);
            LatLng latLng2 = new LatLng(list.get(position).getLatitude(), list.get(position).getLongitude());

            float distance = AMapUtils.calculateLineDistance(latLng1, latLng2) / 1000;
            String result = String.format("%.2f", distance);
            holder.tv_homeroomsitem_distance.setText(result + " KM");
            //vip房间
            if (list.get(position).isVip()) {
                holder.arl_homeroom_isVip_item.setBackgroundResource(R.drawable.home_room_vip);
            } else {
                holder.arl_homeroom_isVip_item.setBackgroundResource(R.drawable.editsharp_trans);
            }
            if (list.get(position).getMemberCount() == 0) {
                holder.tv_nosex_outnumber_item.setVisibility(View.VISIBLE);
                holder.all_sex_outnumber_item.setVisibility(View.GONE);
                String member = list.get(position).getJoinMember() + "/无限";
                holder.tv_nosex_outnumber_item.setText(member);
            } else {
                if ((list.get(position).getWomanCount() == 0) && (list.get(position).getManCount() == 0)) {
                    //没有性别限制
                    holder.tv_nosex_outnumber_item.setVisibility(View.VISIBLE);
                    holder.all_sex_outnumber_item.setVisibility(View.GONE);
                    String member = list.get(position).getJoinMember() + "/" + list.get(position).getMemberCount();
                    holder.tv_nosex_outnumber_item.setText(member);
                } else {
                    //有性别限制
                    holder.tv_nosex_outnumber_item.setVisibility(View.GONE);
                    holder.all_sex_outnumber_item.setVisibility(View.VISIBLE);
                    String manMember = list.get(position).getJoinManMember() + "/" + list.get(position).getManCount();
                    String womanMember = list.get(position).getJoinWomanMember() + "/" + list.get(position).getWomanCount();
                    holder.tv_male_outnumber_item.setText(manMember);
                    holder.tv_female_outnumber_item.setText(womanMember);
                }
            }
            //判断房间是否有密码
            if (list.get(position).isLocked()) {
                holder.tv_rooms_roompwd_item.setVisibility(View.VISIBLE);
            } else {
                holder.tv_rooms_roompwd_item.setVisibility(View.GONE);
            }
            //判断房间是否有保证金
            if (list.get(position).getMoney() == 0) {
                holder.tv_rooms_roombond_item.setVisibility(View.GONE);
            } else {
                holder.tv_rooms_roombond_item.setVisibility(View.VISIBLE);
            }

            //活动开始时间
            holder.tv_rooms_starttime_item.setText(getDatas(list.get(position).getBeginTime()));
            //查看房间是否已经加入
            if (PTApplication.myInfomation != null) {
                for (HomeRoomsBean.DataBean.JoinMembersBean joinMembersBean : list.get(position).getJoinMembers()) {
                    if (joinMembersBean.getId() == PTApplication.myInfomation.getData().getId()) {
                        if (list.get(position).getState() == 0) {
                            holder.iv_is_joined.setImageResource(R.drawable.joined);
                            holder.iv_is_joined.setVisibility(View.VISIBLE);
                        } else {
                            holder.iv_is_joined.setImageResource(R.drawable.room_is_running);
                            holder.iv_is_joined.setVisibility(View.VISIBLE);
                        }
                        return;
                    } else {
                        holder.iv_is_joined.setVisibility(View.INVISIBLE);
                    }
                }
            }

        } else if (holder1 instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder1;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    Logger.e("隐藏..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    Logger.e("正在加载..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    Logger.e("已经到底了..." + position);
                    footerViewHolder.mTvLoadText.setText("已经到底了，不要再拉了！Σ( ° △ °|||)︴　");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //活动类型图标
        private ImageView iv_rooms_gameicon_item;
        //房间名称
        private TextView tv_homeroomsitem_name;
        //活动地点
        private TextView tv_homeroomsitem_place;
        //距离
        private TextView tv_homeroomsitem_distance;
        //没有性别限制
        private TextView tv_nosex_outnumber_item;
        //有性别限制
        private AutoLinearLayout all_sex_outnumber_item;
        //男生人数
        private TextView tv_male_outnumber_item;
        //女生人数
        private TextView tv_female_outnumber_item;
        //房间密码
        private TextView tv_rooms_roompwd_item;
        //房间保证金
        private TextView tv_rooms_roombond_item;
        //活动开始时间
        private TextView tv_rooms_starttime_item;
        //是否是会员
        private AutoRelativeLayout arl_homeroom_isVip_item;
        // 头像集合
        private List<ImageView> avatar_list = new ArrayList<>();
        // 头像背景集合
        private List<CircleImageView> avatar_bg_list = new ArrayList<>();
        //是否加入
        private ImageView iv_is_joined;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_homeroomsitem_name = itemView.findViewById(R.id.tv_homeroomsitem_name);
            tv_homeroomsitem_place = itemView.findViewById(R.id.tv_homeroomsitem_place);
            tv_homeroomsitem_distance = itemView.findViewById(R.id.tv_homeroomsitem_distance);
            tv_nosex_outnumber_item = itemView.findViewById(R.id.tv_nosex_outnumber_item);
            all_sex_outnumber_item = itemView.findViewById(R.id.all_sex_outnumber_item);
            tv_male_outnumber_item = itemView.findViewById(R.id.tv_male_outnumber_item);
            tv_female_outnumber_item = itemView.findViewById(R.id.tv_female_outnumber_item);
            tv_rooms_roompwd_item = itemView.findViewById(R.id.tv_rooms_roompwd_item);
            tv_rooms_roombond_item = itemView.findViewById(R.id.tv_rooms_roombond_item);
            tv_rooms_starttime_item = itemView.findViewById(R.id.tv_rooms_starttime_item);
            iv_rooms_gameicon_item = itemView.findViewById(R.id.iv_rooms_gameicon_item);
            arl_homeroom_isVip_item = itemView.findViewById(R.id.arl_homeroom_isVip_item);
            iv_is_joined = itemView.findViewById(R.id.iv_is_joined);
            // 添加头像到集合中
            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_1));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_1));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_2));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_2));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_3));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_3));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_4));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_4));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_5));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_5));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_6));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_6));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_7));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_7));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_8));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_8));

            avatar_list.add((ImageView) itemView.findViewById(R.id.iv_avatar_item_home_rooms_9));
            avatar_bg_list.add((CircleImageView) itemView.findViewById(R.id.civ_avatar_bg_item_home_rooms_9));

        }
    }

    private String getDatas(String datas) {
        try {
            Date dateCreate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datas);
            if (dateCreate.getYear() != new Date().getYear()) {
                return datas.substring(2);
            }
            long diff = dateCreate.getTime() / 86400000 - System.currentTimeMillis() / 86400000;
            switch ((int) diff) {
                case 0:
                    return "今天" + datas.substring(10);
                case 1:
                    return "明天" + datas.substring(10);
                case 2:
                    return "后天" + datas.substring(10);
                default:
                    return datas.substring(5);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return datas.substring(2);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class OthersStateHolder extends RecyclerView.ViewHolder {
        ImageView gameType;
        TextView roomName;
        TextView gamePlace;
        TextView isReady;
        TextView gameTime;

        public OthersStateHolder(View itemView) {
            super(itemView);
            gameType = itemView.findViewById(R.id.iv_me_gametype_item);
            roomName = itemView.findViewById(R.id.tv_me_roomname_item);
            gamePlace = itemView.findViewById(R.id.tv_me_roomplace_item);
            isReady = itemView.findViewById(R.id.tv_me_isready_item);
            gameTime = itemView.findViewById(R.id.tv_me_time_item);
        }

    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }
}
