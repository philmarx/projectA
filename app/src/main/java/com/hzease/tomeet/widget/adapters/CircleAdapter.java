package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {
    public static final int HEADVIEW_SIZE = 1;
    private List<CommentItemBean.DataBean> mDatas;
    private ICircleContract.Presenter mPresenter;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;
    public void setCirclePresenter(ICircleContract.Presenter presenter){
        this.mPresenter = presenter;
    }
    public CircleAdapter(List<CommentItemBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
            return new ViewHolder(view);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder){
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
        }else if (holder instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    //footerViewHolder.mTvLoadText.setText("上拉加载更多...");
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    footerViewHolder.mTvLoadText.setText("已经到底了，不要再拉了！Σ( ° △ °|||)︴　");
                    break;

            }
        }

    }
    @Override
    public int getItemCount() {
        //RecyclerView的count设置为数据总条数+ 1（footerView）
        return mDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()){
            return  TYPE_FOOTER;
        }else{
            return  TYPE_ITEM;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView headIcon;
        TextView headName;
        TextView headContent;
        TextView headTime;
        ImageView message;
        public ViewHolder(View itemView) {
            super(itemView);
            headIcon = (CircleImageView) itemView.findViewById(R.id.civ_circle_headicon_item);
            headName = (TextView) itemView.findViewById(R.id.civ_circle_headname_item);
            headContent = (TextView) itemView.findViewById(R.id.civ_circle_headcontent_item);
            headTime = (TextView) itemView.findViewById(R.id.civ_circle_headtime_item);
            message = (ImageView) itemView.findViewById(R.id.iv_circle_message_item);
        }

    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView     mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public void AddHeaderItem(List<CommentItemBean.DataBean> items) {
        mDatas.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<CommentItemBean.DataBean> items) {
        mDatas.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus=status;
        notifyDataSetChanged();
    }

}
