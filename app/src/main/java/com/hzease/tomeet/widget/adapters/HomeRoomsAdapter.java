package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by xuq on 2017/4/10.
 */

public class HomeRoomsAdapter extends RecyclerView.Adapter<HomeRoomsAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<HomeRoomsBean.DataBean> list;
    private double mLongitude;
    private double mLatitude;
    int[] gameType = {R.drawable.two_one1_1,R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
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

    private RecycleViewTestAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(RecycleViewTestAdapter.OnItemClickLitener mOnItemClickLitener) {
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
        View view = mInflater.inflate(R.layout.item_home_rooms, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.iv_rooms_gameicon_item.setImageResource(gameType[list.get(position).getGame().getId()-6]);
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
        Logger.e(distance+"");
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
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
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
        }
    }
}
