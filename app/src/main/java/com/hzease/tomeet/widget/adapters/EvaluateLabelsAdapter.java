package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuq on 2017/8/25.
 */

public class EvaluateLabelsAdapter extends RecyclerView.Adapter<EvaluateLabelsAdapter.ViewHoder> {

    private List<String> mDatas;
    private Context context;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public EvaluateLabelsAdapter(List<String> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = View.inflate(context, R.layout.item_labels, null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHoder holder, final int position) {
        holder.tv_labels.setText(mDatas.get(position));
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView,position);
                    for (int i = 0; i < mDatas.size(); i++) {
                        Logger.e("123123");
                        holder.rl_bg.setBackgroundResource(R.drawable.normal_bg);
                        holder.tv_labels.setTextColor(Color.rgb(184,184,184));
                    }
                    holder.rl_bg.setBackgroundResource(R.drawable.checked_bg);
                    holder.tv_labels.setTextColor(Color.rgb(3,181,227));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_labels)
        TextView tv_labels;
        @BindView(R.id.rl_bg)
        RelativeLayout rl_bg;
        public ViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
