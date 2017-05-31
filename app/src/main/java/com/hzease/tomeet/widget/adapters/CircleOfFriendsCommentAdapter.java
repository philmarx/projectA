package com.hzease.tomeet.widget.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Key on 2017/5/23 11:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class CircleOfFriendsCommentAdapter extends RecyclerView.Adapter<CircleOfFriendsCommentAdapter.CommentViewHolder> {

    private List<CommentItemBean.DataBean.EvaluationsBean> mData = new ArrayList<>();

    public List<CommentItemBean.DataBean.EvaluationsBean> getmData() {
        return mData;
    }

    public void setmData(List<CommentItemBean.DataBean.EvaluationsBean> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public CircleOfFriendsCommentAdapter() {
    }

    public CircleOfFriendsCommentAdapter(List<CommentItemBean.DataBean.EvaluationsBean> mData) {
        this.mData = mData;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_comment, parent, false);
        if (mOnItemClickLitener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick((CommentItemBean.DataBean.EvaluationsBean.SenderBean) view.getTag());
                }
            });
        }
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.itemView.setTag(mData.get(position).getSender());
        // 发送人
        holder.tv_sender_circle_comment_item.setText(mData.get(position).getSender().getNickname());
        // 发送的内容
        holder.tv_content_circle_comment_item.setText(mData.get(position).getContent());

        if (mData.get(position).getReceiver() == null) {
            // 回复者
            holder.tv_receiver_circle_comment_item.setVisibility(View.GONE);
            // 回复间隔
            holder.tv_to_circle_comment_item.setVisibility(View.GONE);
        } else {
            holder.tv_to_circle_comment_item.setVisibility(View.VISIBLE);
            holder.tv_receiver_circle_comment_item.setVisibility(View.VISIBLE);
            holder.tv_receiver_circle_comment_item.setText(mData.get(position).getReceiver().getNickname());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        // 发送人
        @BindView(R.id.tv_sender_circle_comment_item)
        TextView tv_sender_circle_comment_item;
        // “回复”
        @BindView(R.id.tv_to_circle_comment_item)
        TextView tv_to_circle_comment_item;
        // 回复谁
        @BindView(R.id.tv_receiver_circle_comment_item)
        TextView tv_receiver_circle_comment_item;
        // 内容
        @BindView(R.id.tv_content_circle_comment_item)
        TextView tv_content_circle_comment_item;

        CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private OnItemClickLitener mOnItemClickLitener;
}
