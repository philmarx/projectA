package com.hzease.tomeet.widget.adapters;

import android.content.Context;
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
import com.zhy.autolayout.AutoLinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/4/10.
 *
 */

public class HomeRoomsAdapter extends RecyclerView.Adapter {
    private Realm mRealm;
    private LayoutInflater mInflater;

    private List<HomeRoomsBean.DataBean> list;

    private double mLongitude;
    private double mLatitude;
    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE     = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE     = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;
    int[] gameType = {R.drawable.one_0,R.drawable.one_1,R.drawable.one_2,R.drawable.one_3,R.drawable.one_4,R.drawable.one_5,R.drawable.two_one1_1,R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
            R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1,R.drawable.two_one4_2,R.drawable.two_one4_3,R.drawable.two_one4_4,R.drawable.two_one4_5};

    /**
     * ItemClick的回调接口
     *
     * @author xq
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private HomeRoomsAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(HomeRoomsAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<HomeRoomsBean.DataBean> getList() {
        return list;
    }

    public void setList(List<HomeRoomsBean.DataBean> list) {
        this.list = list;
        if (list.isEmpty()) {
            this.mLoadMoreStatus = NO_LOAD_MORE;
        } else {
            this.mLoadMoreStatus = PULLUP_LOAD_MORE;
        }
    }

    public HomeRoomsAdapter(Context context, double mLongitude, double mLatitude) {
        mInflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
        this.mLoadMoreStatus = LOADING_MORE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            final View view = mInflater.inflate(R.layout.item_home_rooms, null);
            //如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(view, (Integer) view.getTag());
                    }
                });
            }
            return new ViewHolder(view);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof ViewHolder){
            ViewHolder holder = (ViewHolder) holder1;
            // 把position放到tag中
            holder.itemView.setTag(position);
            int size = list.get(position).getJoinMembers().size();
            for (int i = 0; i < size && i < 6; i++) {
                // 设置头像
                Glide.with(holder.itemView.getContext())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + list.get(position).getJoinMembers().get(i).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                        .signature(new StringSignature(list.get(position).getJoinMembers().get(i).getAvatarSignature()))
                        .into(holder.avatar_list.get(i));



                // 设置背景
                if (PTApplication.myInfomation != null) {
                    if (mRealm == null || (PTApplication.userId + ".realm").equals(mRealm.getConfiguration().getRealmFileName())) {
                        mRealm = Realm.getDefaultInstance();
                    }
                    RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", list.get(position).getJoinMembers().get(i).getId()).findFirst();
                    int color = R.color.transparenttm;
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
                    holder.avatar_bg_list.get(i).setImageResource(color);
                }
            }

            holder.iv_rooms_gameicon_item.setImageResource(gameType[list.get(position).getGame().getId()]);

            holder.tv_homeroomsitem_name.setText(list.get(position).getName());

            // 地名
            holder.tv_homeroomsitem_place.setText(list.get(position).getPlace());

            // 距离
            LatLng latLng1 = new LatLng(mLatitude,mLongitude);
            LatLng latLng2 = new LatLng(list.get(position).getLatitude(),list.get(position).getLongitude());
            float distance = AMapUtils.calculateLineDistance(latLng1,latLng2)/1000;
            String result = String.format("%.2f", distance);
            holder.tv_homeroomsitem_distance.setText(result+" KM");


            if ((list.get(position).getWomanCount() == 0) && (list.get(position).getManCount() == 0)){
                //没有性别限制
                holder.tv_nosex_outnumber_item.setVisibility(View.VISIBLE);
                holder.all_sex_outnumber_item.setVisibility(View.GONE);
                String member = list.get(position).getJoinMember() + "/" + list.get(position).getMemberCount();
                holder.tv_nosex_outnumber_item.setText(member);
            }else{
                //有性别限制
                holder.tv_nosex_outnumber_item.setVisibility(View.GONE);
                holder.all_sex_outnumber_item.setVisibility(View.VISIBLE);
                String manMember = list.get(position).getJoinManMember() + "/" + list.get(position).getManCount();
                String womanMember = list.get(position).getJoinWomanMember() + "/" + list.get(position).getWomanCount();
                holder.tv_male_outnumber_item.setText(manMember);
                holder.tv_female_outnumber_item.setText(womanMember);
            }
            //判断房间是否有密码
            if (list.get(position).isLocked()){
                holder.tv_rooms_roompwd_item.setVisibility(View.VISIBLE);
            }else{
                holder.tv_rooms_roompwd_item.setVisibility(View.GONE);
            }
            //判断房间是否有保证金
            if (list.get(position).getMoney() == 0){
                holder.tv_rooms_roombond_item.setVisibility(View.GONE);
            }else{
                holder.tv_rooms_roombond_item.setVisibility(View.VISIBLE);
            }

            //活动开始时间
            String createTime = list.get(position).getBeginTime();
            try {
                String state = getDatas(createTime+":00");
                switch (state){
                    case "0":
                        String today = "今天" + createTime.substring(11);

                        holder.tv_rooms_starttime_item.setText(today);
                        break;
                    case "1":
                        String tomorrow = "明天"+createTime.substring(11);
                        holder.tv_rooms_starttime_item.setText(tomorrow);
                        break;
                    case "2":
                        String afterTomorrow = "后天" + createTime.substring(11);
                        holder.tv_rooms_starttime_item.setText(afterTomorrow);
                        break;
                    case "out":
                        String datas = createTime.substring(5);
                        datas.replace("-",".");
                        holder.tv_rooms_starttime_item.setText(datas);
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(holder1 instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder1;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    //footerViewHolder.mTvLoadText.setText("上拉加载更多...");
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.mTvLoadText.setText("已经到底了，不要再拉了！Σ( ° △ °|||)︴　");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()){
            return  TYPE_FOOTER;
        }else{
            return  TYPE_ITEM;
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

        // 头像集合
        private List<ImageView> avatar_list = new ArrayList<>();
        // 头像背景集合
        private List<CircleImageView> avatar_bg_list = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);
            tv_homeroomsitem_name = (TextView) itemView.findViewById(R.id.tv_homeroomsitem_name);
            tv_homeroomsitem_place = (TextView) itemView.findViewById(R.id.tv_homeroomsitem_place);
            tv_homeroomsitem_distance = (TextView) itemView.findViewById(R.id.tv_homeroomsitem_distance);
            tv_nosex_outnumber_item = (TextView) itemView.findViewById(R.id.tv_nosex_outnumber_item);
            all_sex_outnumber_item = (AutoLinearLayout) itemView.findViewById(R.id.all_sex_outnumber_item);
            tv_male_outnumber_item = (TextView) itemView.findViewById(R.id.tv_male_outnumber_item);
            tv_female_outnumber_item = (TextView) itemView.findViewById(R.id.tv_female_outnumber_item);
            tv_rooms_roompwd_item = (TextView) itemView.findViewById(R.id.tv_rooms_roompwd_item);
            tv_rooms_roombond_item = (TextView) itemView.findViewById(R.id.tv_rooms_roombond_item);
            tv_rooms_starttime_item = (TextView) itemView.findViewById(R.id.tv_rooms_starttime_item);
            iv_rooms_gameicon_item = (ImageView) itemView.findViewById(R.id.iv_rooms_gameicon_item);

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

        }
    }

    private String getDatas(String datas) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datas);
        Calendar today = Calendar.getInstance();
        Calendar createTime = Calendar.getInstance();
        createTime.setTime(date1);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        createTime.set(Calendar.HOUR, 0);
        createTime.set(Calendar.MINUTE, 0);
        createTime.set(Calendar.SECOND, 0);
        long intervalMilli = createTime.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        // 0：今天 1：明天 2：后天， out：显示日期
        if (xcts >= 0 && xcts <= 2) {
            return String.valueOf(xcts);
        } else {
            return "out";
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView     mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public void AddHeaderItem(List<HomeRoomsBean.DataBean> items) {
        list.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<HomeRoomsBean.DataBean> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus=status;
        notifyDataSetChanged();
    }
}
