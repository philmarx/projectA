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
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.utils.FormatCurrentData;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {
    public static final int HEADVIEW_SIZE = 1;
    private List<CommentItemBean.DataBean> mDatas;
    private ICircleContract.Presenter mPresenter;
    private Context context;
    public void setCirclePresenter(ICircleContract.Presenter presenter){
        this.mPresenter = presenter;
    }
    public CircleAdapter(List<CommentItemBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
        viewHolder = new ViewHolder(view);
        viewHolder.headIcon = (CircleImageView) view.findViewById(R.id.civ_circle_headicon_item);
        viewHolder.headName = (TextView) view.findViewById(R.id.civ_circle_headname_item);
        viewHolder.headContent = (TextView) view.findViewById(R.id.civ_circle_headcontent_item);
        viewHolder.headTime = (TextView) view.findViewById(R.id.civ_circle_headtime_item);
        viewHolder.message = (ImageView) view.findViewById(R.id.iv_circle_message_item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.headName.setText(mDatas.get(position).getDeclareNickname());
        viewHolder.headContent.setText(mDatas.get(position).getContent());
        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("onClick");
                if(mPresenter != null){
                    CommentConfig config = new CommentConfig();
                    config.circlePosition = position;
                    config.commentType = CommentConfig.Type.PUBLIC;
                    mPresenter.showEditTextBody(config);
                }
            }
        });
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getId()+ AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .placeholder(R.drawable.person_default_icon)
                .error(R.drawable.person_default_icon)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(viewHolder.headIcon);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(new Date(mDatas.get(position).getCreateTime()));
        viewHolder.headTime.setText(FormatCurrentData.getTimeRange(createTime));
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        CircleImageView headIcon;
        TextView headName;
        TextView headContent;
        TextView headTime;
        ImageView message;
    }

}
