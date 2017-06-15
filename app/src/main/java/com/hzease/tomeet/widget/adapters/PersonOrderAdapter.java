package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.UserOrderBean;

import java.util.List;

/**
 * Created by xuq on 2017/5/15.
 */

public class PersonOrderAdapter extends RecyclerView.Adapter<PersonOrderAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<UserOrderBean.DataBean.OrdersBean> list;
    private Context context;
    int[] gameType = {R.drawable.one_0,R.drawable.one_1,R.drawable.one_2,R.drawable.one_3,R.drawable.one_4,R.drawable.one_5,R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_3,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
            R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2};
    public PersonOrderAdapter(List<UserOrderBean.DataBean.OrdersBean> list,Context context) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_personinfo,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageResource(gameType[list.get(position).getGameId()]);
        holder.order.setText(list.get(position).getRanking()+"");
        holder.count.setText(list.get(position).getCount()+"");
        holder.point.setText(list.get(position).getPoint()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView count;
        TextView point;
        TextView order;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_item_icon_act);
            count = (TextView) itemView.findViewById(R.id.tv_item_member_aty);
            point = (TextView) itemView.findViewById(R.id.tv_item_score_aty);
            order = (TextView) itemView.findViewById(R.id.tv_item_order_aty);
        }
    }
}
