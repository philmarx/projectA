package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public ViewHolder(View itemView) {
            super(itemView);
            count = (TextView) itemView.findViewById(R.id.tv_item_member_aty);
            point = (TextView) itemView.findViewById(R.id.tv_item_score_aty);
            order = (TextView) itemView.findViewById(R.id.tv_item_order_aty);
        }
    }
}
