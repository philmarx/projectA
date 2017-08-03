package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.LeafDetailedBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuq on 2017/8/3.
 */

public class LeafDetailedAdapter extends RecyclerView.Adapter<LeafDetailedAdapter.ViewHolder> {
    private List<LeafDetailedBean.DataBean> mDatas;
    private LayoutInflater mInflater;
    public LeafDetailedAdapter(List<LeafDetailedBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_leaf_detailed,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_me_leafdetails_desc_item.setText(mDatas.get(position).getDescription());
        holder.tv_me_leafdetails_time_item.setText(formatTime(mDatas.get(position).getCreateTime()));
        holder.tv_me_leafdetails_leaf_item.setText(mDatas.get(position).getVariation() > 0  ?  "+" + mDatas.get(position).getVariation() : String.valueOf(mDatas.get(position).getVariation()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_me_leafdetails_desc_item;
        TextView tv_me_leafdetails_time_item;
        TextView tv_me_leafdetails_leaf_item;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_me_leafdetails_desc_item = itemView.findViewById(R.id.tv_me_leafdetails_desc_item);
            tv_me_leafdetails_time_item = itemView.findViewById(R.id.tv_me_leafdetails_time_item);
            tv_me_leafdetails_leaf_item = itemView.findViewById(R.id.tv_me_leafdetails_leaf_item);
        }
    }
    private String formatTime(long time) {
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(d);
    }
}
