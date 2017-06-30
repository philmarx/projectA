package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.UserOrderBean;

import java.util.List;

/**
 * Created by xuq on 2017/6/30.
 */

public class SpaceCircleAdapter extends RecyclerView.Adapter<SpaceCircleAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<UserOrderBean.DataBean.CirclesBean> mData;
    Context context;
    public SpaceCircleAdapter(List<UserOrderBean.DataBean.CirclesBean> mData, Context context) {
        inflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_spacecircle,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCircleName.setText(mData.get(position).getName());
        int experience = mData.get(position).getExperience();
        if (experience >=100 && experience < 200){
            holder.mCircleLv.setImageResource(R.drawable.lv_1);
        }else if (experience >= 200 && experience <500){
            holder.mCircleLv.setImageResource(R.drawable.lv_2);
        }else if (experience >= 500 && experience < 1000){
            holder.mCircleLv.setImageResource(R.drawable.lv_3);
        }else if (experience >= 1000 && experience < 2000){
            holder.mCircleLv.setImageResource(R.drawable.lv_4);
        }else if (experience >= 2000){
            holder.mCircleLv.setImageResource(R.drawable.lv_5);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCircleLv;
        TextView mCircleName;

        public ViewHolder(View itemView) {
            super(itemView);
            mCircleLv = (ImageView) itemView.findViewById(R.id.iv_space_circle_lv_item);
            mCircleName = (TextView) itemView.findViewById(R.id.tv_space_circle_name_item);
        }
    }
}
