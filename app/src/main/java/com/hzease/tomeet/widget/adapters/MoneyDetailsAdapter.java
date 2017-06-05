package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MoneyDetailsBean;

import java.util.List;

/**
 * Created by xuq on 2017/6/5.
 */

public class MoneyDetailsAdapter extends RecyclerView.Adapter<MoneyDetailsAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<MoneyDetailsBean.DataBean> mDatas;
    private Context context;
    public MoneyDetailsAdapter(List<MoneyDetailsBean.DataBean> mDatas,Context context) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_touchbalance,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.desc.setText(mDatas.get(position).getDescription());
        holder.time.setText(mDatas.get(position).getCreateTime());
        int money = mDatas.get(position).getMoney()/100;
        String result = String.format("%.2f", Float.valueOf(money));
        holder.money.setText(result);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        TextView time;
        TextView money;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.tv_me_moenydetails_desc_item);
            time = (TextView) itemView.findViewById(R.id.tv_me_moenydetails_time_item);
            money = (TextView) itemView.findViewById(R.id.tv_me_moenydetails_money_item);
        }
    }
}
