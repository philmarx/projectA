package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.PropsMumBean;

import java.util.Calendar;

/**
 * Created by xuq on 2017/10/18.
 */

public class MyPropsAdapterV2 extends BaseAdapter {

    int[] propsIcon = {R.drawable.small_paper_v2,R.drawable.label_miss_card_v2,R.drawable.change_name_card_v2,R.drawable.vip_v2,R.drawable.buqian_card_v2,R.drawable.into_room_card_v2,R.drawable.friend_card_v2,R.drawable.watch_movie_ticket_v2};
    String[] propName = {"小纸条","标签消除卡","改名卡","","补签卡","入场券","同伴卡","观影券"};
    private Context context;
    private PropsMumBean.DataBean dataBean = new PropsMumBean.DataBean();

    public MyPropsAdapterV2(Context context) {
        this.context = context;
    }

    public void setPropNum(PropsMumBean.DataBean dataBean){
        this.dataBean = dataBean;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        MyPropsViewHolder holder;
        if (view == null){
            holder = new MyPropsViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.props_item, null);
            holder.iv_Props_icon = view.findViewById(R.id.iv_icon);
            holder.tv_Props_name = view.findViewById(R.id.tv_prop_name);
            holder.tv_Props_num = view.findViewById(R.id.tv_prop_num);
            view.setTag(holder);
        }else{
            holder = (MyPropsViewHolder) view.getTag();
        }
        if (position == 3 ){
            holder.tv_Props_name.setText("VIP");
        }else{
            holder.tv_Props_name.setText(propName[position]);
        }
        holder.iv_Props_icon.setImageResource(propsIcon[position]);
        switch (position){
            //小纸条数量
            case 0:
                holder.tv_Props_num.setText("X" + dataBean.getNoteCount());
                break;
            //标签消除卡数量
            case 1:
                holder.tv_Props_num.setText("X" + dataBean.getLabelClearCount());
                break;
            //改名卡数量
            case 2:
                holder.tv_Props_num.setText("X" + dataBean.getChangeNicknameCount());
                break;
            //VIP数量
            case 3:
                holder.tv_Props_num.setText("");
                holder.tv_Props_name.setText("VIP" + calculateTime(dataBean.getVipExpireDate()));
                break;
            //补签卡数量
            case 4:
                holder.tv_Props_num.setText("X" + dataBean.getSignCount());
                break;
            //入场券数量
            case 5:
                holder.tv_Props_num.setText("X" + 10);
                break;
            //同伴卡数量
            case 6:
                holder.tv_Props_num.setText("X" + 10);
                break;
            //观影券数量
            case 7:
                holder.tv_Props_num.setText("X" + dataBean.getMovieTicket());
                break;
        }
        return view;
    }

    class MyPropsViewHolder{
        ImageView iv_Props_icon;
        TextView tv_Props_name;
        TextView tv_Props_num;
    }
    private String calculateTime(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long now = (System.currentTimeMillis() + offSet) / 60000;
        long create = (time + offSet) / 60000;
        long diff = create - now;
        if (diff / 60 < 24) {
            return "(剩余0天)";
        } else {
            return "(剩余" + diff / 60 / 24 + "天)";
        }
    }
}
