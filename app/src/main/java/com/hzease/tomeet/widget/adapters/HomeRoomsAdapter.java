package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/4/10.
 *
 */

public class HomeRoomsAdapter extends RecyclerView.Adapter<HomeRoomsAdapter.ViewHolder> {
    private final Realm mRealm = Realm.getDefaultInstance();
    private LayoutInflater mInflater;
    private List<HomeRoomsBean.DataBean> list;
    private double mLongitude;
    private double mLatitude;
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


    public HomeRoomsAdapter(List<HomeRoomsBean.DataBean> list, Context context,double mLongitude,double mLatitude) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_home_rooms, null);

        //
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(view, (Integer) view.getTag());
                }
            });
        }

        //((TagFlowLayout) view.findViewById(R.id.tfl_avatar_list_item_home_rooms))

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 把position放到tag中
        holder.itemView.setTag(position);


        holder.tfl_avatar_list_item_home_rooms.setAdapter(new TagAdapter<HomeRoomsBean.DataBean.JoinMembersBean>(list.get(position).getJoinMembers()) {
            @Override
            public View getView(FlowLayout parent, int position, HomeRoomsBean.DataBean.JoinMembersBean joinMembersBean) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar_list_image_view_home_rooms, null);
                CircleImageView avatar_bg = (CircleImageView) view.findViewById(R.id.civ_avatar_bg_item_home_rooms);
                CircleImageView avatar = (CircleImageView) view.findViewById(R.id.civ_avatar_item_member_rooms);

                Glide.with(parent.getContext())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + joinMembersBean.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .placeholder(R.drawable.person_default_icon)
                        .error(R.drawable.person_default_icon)
                        .bitmapTransform(new CropCircleTransformation(parent.getContext()))
                        .signature(new StringSignature(joinMembersBean.getAvatarSignature()))
                        .into(avatar);

                RealmFriendBean friendBean = mRealm.where(RealmFriendBean.class).equalTo("id", joinMembersBean.getId()).findFirst();
                int color = R.color.transparenttm;
                if (friendBean != null) {
                    Logger.e("point:  " + friendBean.getPoint());
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
                avatar_bg.setImageResource(color);
                return view;
            }
        });



        holder.iv_rooms_gameicon_item.setImageResource(gameType[list.get(position).getGame().getId()]);
        holder.tv_homeroomsitem_name.setText(list.get(position).getName());

        String place = list.get(position).getPlace();
        if (place.length()>7){
            place = place.substring(0,6);
            holder.tv_homeroomsitem_place.setText(place+"... · ");
        }else{
            holder.tv_homeroomsitem_place.setText(list.get(position).getPlace()+" · ");
        }

        LatLng latLng1 = new LatLng(mLatitude,mLongitude);
        LatLng latLng2 = new LatLng(list.get(position).getLatitude(),list.get(position).getLongitude());
        float distance = AMapUtils.calculateLineDistance(latLng1,latLng2)/1000;
        // Logger.e(distance+"");
        String result = String.format("%.2f", distance);
        holder.tv_homeroomsitem_distance.setText(result+"km");
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
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // 头像列表
        private TagFlowLayout tfl_avatar_list_item_home_rooms;
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
            tfl_avatar_list_item_home_rooms = (TagFlowLayout) itemView.findViewById(R.id.tfl_avatar_list_item_home_rooms);
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
}
