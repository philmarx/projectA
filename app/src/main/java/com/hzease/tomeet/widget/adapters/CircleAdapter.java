package com.hzease.tomeet.widget.adapters;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {
    public static final int HEADVIEW_SIZE = 1;
    private List<CommentItemBean.DataBean> mDatas;
    private ICircleContract.Presenter mPresenter;

    public void setCirclePresenter(ICircleContract.Presenter presenter){
        this.mPresenter = presenter;
    }
    public CircleAdapter(List<CommentItemBean.DataBean> mDatas) {
        this.mDatas = mDatas;
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
