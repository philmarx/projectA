package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.me.ui.fragment.DepositFragment;
import com.hzease.tomeet.me.ui.fragment.DepositMoneyFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuq on 2017/6/7.
 */

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {

    List<DepositBean.DataBean> mDatas;
    private LayoutInflater mInflater;
    FragmentTransaction transaction;
    public DepositAdapter(List<DepositBean.DataBean> mDatas, Context context,FragmentTransaction transaction) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
        this.transaction = transaction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_bzmoneylist,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.money.setText("充值+" + mDatas.get(position).getAmount());
        String time = formatTime(mDatas.get(position).getTime());
        holder.time.setText(time);
        float amount = Float.valueOf(mDatas.get(position).getAmount());
        float refundAmount = Float.valueOf(mDatas.get(position).getRefundAmount());
        if (amount > refundAmount){
            holder.refund.setVisibility(View.VISIBLE);
            holder.returned.setVisibility(View.GONE);
        }else{
            holder.refund.setVisibility(View.GONE);
            holder.returned.setVisibility(View.VISIBLE);
        }
        holder.refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, new DepositMoneyFragment(mDatas.get(position)));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
            }
        });
    }

    private String formatTime(long time) {
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(d);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView money;
        TextView time;
        Button refund;
        TextView returned;

        public ViewHolder(View itemView) {
            super(itemView);
            money = (TextView) itemView.findViewById(R.id.tv_deposit_rechargemoney_item);
            time = (TextView) itemView.findViewById(R.id.tv_deposit_rechargetime_item);
            refund = (Button) itemView.findViewById(R.id.bt_deposit_refund_item);
            returned = (TextView) itemView.findViewById(R.id.tv_deposit_returned_item);
        }
    }


}
