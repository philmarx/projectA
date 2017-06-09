package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.widget.CircleImageView;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/4/21.
 */

public class RecommandCircleAdapter extends RecyclerView.Adapter<RecommandCircleAdapter.ViewHolder> {

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

    public RecommandCircleAdapter(Context context, List<CircleInfoBean.DataBean> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    @Override
    public RecommandCircleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.recommandCircleName.setText(mDatas.get(position).getName());
        if (mDatas.get(position).getNotice() == ""){
            holder.recommandCircleNote.setText("这个圈子什么也没有，快来开启你智慧的小脑袋");
        }else{
            holder.recommandCircleNote.setText(mDatas.get(position).getNotice());
        }
        //holder.recommandCircleNote.setText(mDatas.get(position).getNotice());
        String personCount = String.valueOf(mDatas.get(position).getMemberCount());
        String roomCount = String.valueOf(mDatas.get(position).getRoomCount());
        String othersInfo = "人数" + personCount + "·" + "活动" + roomCount;
        holder.recommandCircleOthers.setText(othersInfo);
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
        Glide.with(holder.recommandCircleIcon.getContext())
                .load(AppConstants.YY_PT_OSS_PATH+AppConstants.YY_PT_OSS_CIRCLE + mDatas.get(position).getId() + AppConstants.YY_PT_OSS_CIRCLE_AVATAR + AppConstants.YY_PT_OSS_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(holder.recommandCircleIcon.getContext()))
                .error(R.drawable.circle_defalut_icon)
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(holder.recommandCircleIcon);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recommandCircleName;
        TextView recommandCircleNote;
        TextView recommandCircleOthers;
        CircleImageView recommandCircleIcon;
        public ViewHolder(View view) {
            super(view);
            recommandCircleName = (TextView) view.findViewById(R.id.tv_recommandcircle_name_fmt);
            recommandCircleNote = (TextView) view.findViewById(R.id.tv_recommandcircle_note_fmt);
            recommandCircleOthers = (TextView) view.findViewById(R.id.tv_recommandcircle_otherinfo_fmt);
            recommandCircleIcon = (CircleImageView) view.findViewById(R.id.civ_recommandcircle_headicon_fmt);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

