package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/9/15.
 */

public class MySpeachAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<CommentItemBean.DataBean> mData = new ArrayList<>();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    // 隐藏
    public final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public final int LOADING_MORE = 1;
    // 没有更多
    public final int NO_LOAD_MORE = 2;
    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = LOADING_MORE;
    public int getmLoadMoreStatus() {
        return mLoadMoreStatus;
    }
    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus = status;
        notifyItemChanged(getItemCount()-1);
    }
    public List<CommentItemBean.DataBean> getmData() {
        return mData;
    }
    public void setmData(List<CommentItemBean.DataBean> mData) {
        this.mData = mData;
        if (mData.isEmpty()) {
            this.mLoadMoreStatus = NO_LOAD_MORE;
        } else {
            this.mLoadMoreStatus = PULLUP_LOAD_MORE;
        }
    }
    public MySpeachAdapter(Activity activity) {
        this.activity = activity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            final View inflateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
            if (mOnItemClickLitener != null) {
                inflateView.findViewById(R.id.tv_reply_circle_of_friends_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick((int) inflateView.getTag(), mData.get((int) inflateView.getTag()), null);
                    }
                });
            }
            RecyclerView recyclerView =  inflateView.findViewById(R.id.rv_comment_circle_of_friends_item);
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            CircleOfFriendsCommentAdapter commentAdapter = new CircleOfFriendsCommentAdapter();
            commentAdapter.setOnItemClickLitener(new CircleOfFriendsCommentAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean,int postion) {
                    mOnItemClickLitener.onItemClick((int) inflateView.getTag(), mData.get((int) inflateView.getTag()), senderBean);
                }
            });
            recyclerView.setAdapter(commentAdapter);
            return new CircleOfFriendsViewHolder(inflateView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CircleOfFriendsViewHolder) {
            holder.itemView.setTag(position);
            CircleOfFriendsViewHolder circleOfFriendsViewHolder = (CircleOfFriendsViewHolder) holder;
            Logger.e(mData.get(position).getAvatarSignature());
            // 头像
            Glide.with(holder.itemView.getContext())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mData.get(position).getDeclareId()+ AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                    .signature(new StringSignature(mData.get(position).getAvatarSignature()))
                    .into(circleOfFriendsViewHolder.iv_avatar_circle_of_friends_item);
            // 背景
            Glide.with(holder.itemView.getContext())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mData.get(position).getDeclareId() + AppConstants.YY_PT_OSS_AVATAR)
                    .signature(new StringSignature(mData.get(position).getAvatarSignature()))
                    .centerCrop()
                    .into(circleOfFriendsViewHolder.iv_bg_circle_of_friends_item);
            // 名字
            circleOfFriendsViewHolder.tv_name_circle_of_friends_item.setText(mData.get(position).getDeclareNickname());
            // 时间
            circleOfFriendsViewHolder.tv_time_circle_of_friends_item.setText(calculateTime(mData.get(position).getCreateTime()));
            // 喊话内容
            circleOfFriendsViewHolder.tv_content_circle_of_friends_item.setText(mData.get(position).getContent());
            // 回复列表
            ((CircleOfFriendsCommentAdapter) circleOfFriendsViewHolder.rv_comment_circle_of_friends_item.getAdapter()).setmData(mData.get(position).getEvaluations());
            circleOfFriendsViewHolder.iv_avatar_circle_of_friends_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",mData.get(position).getDeclareId());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    Logger.e("隐藏..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    Logger.e("正在加载..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    Logger.e("已经到底了..." + position);
                    footerViewHolder.mTvLoadText.setText("已经到底了，不要再拉了！Σ( ° △ °|||)︴　");
                    break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }
    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? TYPE_FOOTER : TYPE_ITEM;
    }
    class CircleOfFriendsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_bg_circle_of_friends_item)
        ImageView iv_bg_circle_of_friends_item;
        @BindView(R.id.iv_avatar_circle_of_friends_item)
        ImageView iv_avatar_circle_of_friends_item;
        @BindView(R.id.tv_name_circle_of_friends_item)
        TextView tv_name_circle_of_friends_item;
        @BindView(R.id.tv_content_circle_of_friends_item)
        TextView tv_content_circle_of_friends_item;
        @BindView(R.id.tv_time_circle_of_friends_item)
        TextView tv_time_circle_of_friends_item;
        @BindView(R.id.tv_reply_circle_of_friends_item)
        ImageView tv_reply_circle_of_friends_item;
        @BindView(R.id.rv_comment_circle_of_friends_item)
        RecyclerView rv_comment_circle_of_friends_item;
        CircleOfFriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickLitener {
        void onItemClick(int position, CommentItemBean.DataBean dataBean, CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean);
    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    private OnItemClickLitener mOnItemClickLitener;
    private String calculateTime(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long now = (System.currentTimeMillis() + offSet) / 60000;
        long create = (time + offSet) / 60000;
        long diff = now - create;
        if (diff < 60) {
            return diff + "分钟前";
        } else if (diff < 1440) {
            return diff / 60 + "小时前";
        } else {
            return diff / 60 /24 + "天前";
        }
    }

}
