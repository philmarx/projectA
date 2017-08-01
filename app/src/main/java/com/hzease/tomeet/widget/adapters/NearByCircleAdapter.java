package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CircleInfoBean;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/8.
 */

public class NearByCircleAdapter extends RecyclerView.Adapter<NearByCircleAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<CircleInfoBean.DataBean> mDatas;



    /**
     * ItemClick的回调接口
     * @author zhy
     **/

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(mDatas.get(position).getName());
        if (mDatas.get(position).getNotice() == ""){
            holder.content.setText("这个圈子什么也没有，快来开起...");
        }else{
            holder.content.setText(mDatas.get(position).getNotice());
        }
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

        }
        Glide.with(holder.icon.getContext())
                .load(AppConstants.YY_PT_OSS_PATH+AppConstants.YY_PT_OSS_CIRCLE + mDatas.get(position).getId() + AppConstants.YY_PT_OSS_CIRCLE_BG)
                .error(R.drawable.bg_neaybycircle)
                .centerCrop()
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature() == null ? "" : mDatas.get(position).getAvatarSignature()))
                .into(holder.icon);
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
            icon = itemView.findViewById(R.id.iv_nearbycircle_img);
            title = itemView.findViewById(R.id.tv_nearbycircle_title);
            content = itemView.findViewById(R.id.tv_nearbycircle_content);
        }
    }
}
