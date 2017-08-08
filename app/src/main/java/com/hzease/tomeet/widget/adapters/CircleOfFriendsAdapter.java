package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ui.ActiveInterfaceWebview;
import com.hzease.tomeet.data.ActivityBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/5/22 21:01
 * email: MrKey.K@gmail.com
 * description:
 */

public class CircleOfFriendsAdapter extends RecyclerView.Adapter {


    private  Context context;
    private Activity activity;
    private List<CommentItemBean.DataBean> mData = new ArrayList<>();

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;

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
        notifyItemChanged(getItemCount() - 1);
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

    public CircleOfFriendsAdapter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
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
                public void onItemClick(CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean) {
                    mOnItemClickLitener.onItemClick((int) inflateView.getTag(), mData.get((int) inflateView.getTag()), senderBean);
                }
            });
            recyclerView.setAdapter(commentAdapter);
            return new CircleOfFriendsViewHolder(inflateView);
        } else if(viewType == TYPE_HEADER){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
            return new HeadViewHolder(itemView);
        }else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CircleOfFriendsViewHolder) {
            position = position-1;
            holder.itemView.setTag(position);
            CircleOfFriendsViewHolder circleOfFriendsViewHolder = (CircleOfFriendsViewHolder) holder;
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

            final int finalPosition = position;
            circleOfFriendsViewHolder.iv_avatar_circle_of_friends_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",mData.get(finalPosition).getDeclareId());
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
        }else if (holder instanceof HeadViewHolder){
            final HeadViewHolder headerViewHolder = (HeadViewHolder) holder;
            headerViewHolder.iv_avatar_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.tv_name_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.tv_content_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.tv_time_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.tv_reply_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.rv_comment_circle_of_friends_item.setVisibility(View.GONE);
            headerViewHolder.ll_isHeadView.setVisibility(View.VISIBLE);
            //headerViewHolder.iv_bg_circle_of_friends_item.setImageResource(R.drawable.headview_bg);
            PTApplication.getRequestService().findAllActivity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ActivityBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(final ActivityBean activityBean) {
                            URL url = null;
                            try {
                                url = new URL(activityBean.getData().get(0).getPhotoUrl());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            Glide.with(context)
                                    .load(url)
                                    .into(headerViewHolder.iv_bg_circle_of_friends_item);
                            headerViewHolder.iv_bg_circle_of_friends_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PTApplication.getRequestService().isBind3Part(PTApplication.userToken,PTApplication.userId)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Subscriber<MapDataBean>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onNext(MapDataBean mapDataBean) {
                                                    if (mapDataBean.isSuccess()){
                                                        Map<String, Boolean> data = mapDataBean.getData();
                                                        Logger.e("WECHAT:" + data.get("WECHAT"));
                                                        if (!data.get("WECHAT")){
                                                            ToastUtils.getToast(context,"参加该活动需要绑定微信，请先绑定微信");
                                                        }else{
                                                            Intent intent = new Intent(activity, ActiveInterfaceWebview.class);
                                                            intent.putExtra("url",activityBean.getData().get(0).getUrl());
                                                            intent.putExtra("name",activityBean.getData().get(0).getName());
                                                            intent.putExtra("desc",activityBean.getData().get(0).getMessage());
                                                            intent.putExtra("photoUrl",activityBean.getData().get(0).getPhotoUrl());
                                                            activity.startActivity(intent);
                                                        }
                                                    }
                                                }
                                            });


                                }
                            });
                        }
                    });

        }
    }
    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else if(position == 0){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
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

    class HeadViewHolder extends RecyclerView.ViewHolder{
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
        @BindView(R.id.ll_isHeadView)
        LinearLayout ll_isHeadView;
        public HeadViewHolder(View itemView) {
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
