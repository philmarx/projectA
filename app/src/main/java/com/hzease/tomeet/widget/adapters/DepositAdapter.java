package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.me.ui.fragment.DepositMoneyFragment;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositAdapter extends RecyclerView.Adapter {

    List<DepositBean.DataBean> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    FragmentTransaction transaction;
    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    // 隐藏
    public static final int PULLUP_LOAD_MORE = 0;
    // 正在加载中
    public static final int LOADING_MORE     = 1;
    // 没有更多
    public static final int NO_LOAD_MORE     = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = LOADING_MORE;
    public DepositAdapter(Context context,FragmentTransaction transaction) {
        mInflater = LayoutInflater.from(context);
        this.transaction = transaction;
    }
    public int getmLoadMoreStatus() {
        return mLoadMoreStatus;
    }
    public List<DepositBean.DataBean> getList() {
        return mDatas;
    }
    public void setList(List<DepositBean.DataBean> mDatas) {
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
            View view = mInflater.inflate(R.layout.item_bzmoneylist,null);
            return new ViewHolder(view);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1,final int position) {
        if (holder1 instanceof DepositAdapter.ViewHolder){
            ViewHolder holder = (DepositAdapter.ViewHolder) holder1;
            holder.money.setText(mDatas.get(position).getType() + "充值 +" + String.format("%.2f", Double.valueOf(mDatas.get(position).getAmount()) / 100).toString() + "元");
            String time = formatTime(mDatas.get(position).getTime());
            holder.time.setText(time);
            float amount = Float.valueOf(mDatas.get(position).getAmount());
            float refundAmount = Float.valueOf(mDatas.get(position).getRefundAmount());
            if (amount > refundAmount){
                holder.refund.setVisibility(View.VISIBLE);
                holder.refund.setText("可退 +" + String.format("%.2f", (amount - refundAmount) / 100).toString() + "元");
                holder.returned.setVisibility(View.GONE);
            }else{
                holder.refund.setVisibility(View.GONE);
                holder.returned.setVisibility(View.VISIBLE);
            }
            holder.refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 将 fragment_container View 中的内容替换为此 Fragment ，
                    transaction.replace(R.id.fl_content_me_activity, DepositMoneyFragment.newInstance(mDatas.get(position)));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.addToBackStack(null);
                    // 执行事务
                    transaction.commit();
                }
            });}else if(holder1 instanceof DepositAdapter.FooterViewHolder){
            DepositAdapter.FooterViewHolder footerViewHolder = (DepositAdapter.FooterViewHolder) holder1;
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
    private String formatTime(long time) {
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(d);
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? TYPE_FOOTER : TYPE_ITEM;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView money;
        TextView time;
        Button refund;
        TextView returned;

        public ViewHolder(View itemView) {
            super(itemView);
            money =  itemView.findViewById(R.id.tv_deposit_rechargemoney_item);
            time =  itemView.findViewById(R.id.tv_deposit_rechargetime_item);
            refund =  itemView.findViewById(R.id.bt_deposit_refund_item);
            returned =  itemView.findViewById(R.id.tv_deposit_returned_item);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }

}
