package com.hzease.tomeet.widget.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;
import com.orhanobut.logger.Logger;

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
                    Logger.e("v" + v.getId());
                    mOnItemClickLitener.onItemClick((CommentItemBean.DataBean.EvaluationsBean.SenderBean) view.getTag());
                }
            });
        }
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.itemView.setTag(mData.get(position).getSender());
        String ReplyContent="";
        SpannableString msp = null;
        if (mData.get(position).getReceiver() == null) {
            ReplyContent = mData.get(position).getSender().getNickname() +"：" + mData.get(position).getContent();
            msp = new SpannableString(ReplyContent);
            msp.setSpan(new ForegroundColorSpan(Color.rgb(3,181,227)),0,mData.get(position).getSender().getNickname().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            String reply = "回复";
            String receiver = mData.get(position).getReceiver().getNickname();
            ReplyContent = mData.get(position).getSender().getNickname() + reply + receiver+"：" + mData.get(position).getContent();
            msp = new SpannableString(ReplyContent);
            msp.setSpan(new ForegroundColorSpan(Color.rgb(3,181,227)),0,mData.get(position).getSender().getNickname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.rgb(3,187,227)),mData.get(position).getSender().getNickname().length()+2,mData.get(position).getSender().getNickname().length()+2+mData.get(position).getReceiver().getNickname().length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.tv_sender_circle_comment_item.setText(msp);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        // 评论内容
        @BindView(R.id.tv_sender_circle_comment_item)
        TextView tv_sender_circle_comment_item;
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
