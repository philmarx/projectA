package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CircleInfoBean;

import java.util.List;

/**
 * Created by xuq on 2017/5/8.
 */

public class NearByCircleAdapter extends RecyclerView.Adapter<NearByCircleAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<CircleInfoBean.DataBean> mDatas;

    public NearByCircleAdapter(List<CircleInfoBean.DataBean> mDatas, Context context) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_recycle,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mDatas.get(position).getName());
        if (mDatas.get(position).getNotice() == ""){
            holder.content.setText("这个圈子什么也没有，快来开起...");
        }else{
            holder.content.setText(mDatas.get(position).getNotice());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_nearbycircle_img);
            title = (TextView) itemView.findViewById(R.id.tv_nearbycircle_title);
            content = (TextView) itemView.findViewById(R.id.tv_nearbycircle_content);
        }
    }
}
