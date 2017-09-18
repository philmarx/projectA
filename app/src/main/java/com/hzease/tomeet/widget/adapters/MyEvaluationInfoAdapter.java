package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EvaluationInfoBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/9/18.
 */

public class MyEvaluationInfoAdapter extends RecyclerView.Adapter {

    private List<EvaluationInfoBean.DataBean> mDatas = new ArrayList<>();
    private Context context;
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

    public MyEvaluationInfoAdapter(Context context) {
        this.context = context;
    }

    public List<EvaluationInfoBean.DataBean> getmData() {
        return mDatas;
    }
    public void changeMoreStatus(int status){
        mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setDatas(List<EvaluationInfoBean.DataBean> mDatas){
        this.mDatas = mDatas;
        if (mDatas.isEmpty()) {
            this.mLoadMoreStatus = NO_LOAD_MORE;
        } else {
            this.mLoadMoreStatus = PULLUP_LOAD_MORE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speach_info, parent, false);
            return new MyEvaluationHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyEvaluationHolder){
            MyEvaluationHolder evaluationHolder  = (MyEvaluationHolder) holder;
            //头像
            Glide.with(holder.itemView.getContext())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getSender().getId()+ AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                    .signature(new StringSignature(mDatas.get(position).getSender().getAvatarSignature()))
                    .into(evaluationHolder.sender_avatar);
            evaluationHolder.sender_nickname.setText(mDatas.get(position).getSender().getNickname());
            evaluationHolder.sender_content.setText(mDatas.get(position).getSender().getContent());
            evaluationHolder.evaluation_nickname.setText("@"+mDatas.get(position).getDeclaration().getNickname());
            evaluationHolder.evaluation_content.setText(mDatas.get(position).getDeclaration().getContent());
            evaluationHolder.sender_time.setText(calculateTime(mDatas.get(position).getCreateTime()));
        }else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    Logger.e("隐藏..." + position);
                    //footerViewHolder.mLoadLayout.setVisibility(View.GONE);
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
        return mDatas.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? TYPE_FOOTER : TYPE_ITEM;
    }

    class MyEvaluationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_sender_avatar)
        CircleImageView sender_avatar;
        @BindView(R.id.tv_sender_nickname)
        TextView sender_nickname;
        @BindView(R.id.tv_sender_time)
        TextView sender_time;
        @BindView(R.id.tv_sender_content)
        TextView sender_content;
        @BindView(R.id.tv_evaluation_nickname)
        TextView evaluation_nickname;
        @BindView(R.id.tv_evaluation_content)
        TextView evaluation_content;

        public MyEvaluationHolder(View itemView) {
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
            return diff / 60 / 24 + "天前";
        }
    }
}
