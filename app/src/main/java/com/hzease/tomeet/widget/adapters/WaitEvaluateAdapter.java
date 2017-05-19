package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EvaluteBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.xw.repo.BubbleSeekBar;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateAdapter extends RecyclerView.Adapter<WaitEvaluateAdapter.ViewHolder> {

    EvaluteBean evaluteBean;
    List<WaitEvaluateBean.DataBean> list;
    Context context;

    public WaitEvaluateAdapter(List<WaitEvaluateBean.DataBean> list,Context context,long roomId) {
        this.list = list;
        this.context = context;
        evaluteBean = new EvaluteBean();
        evaluteBean.setToken(PTApplication.userToken);
        evaluteBean.setUserId(PTApplication.userId);
        evaluteBean.setRoomId(String.valueOf(roomId));
        for (int i = 0; i < list.size(); i++) {
            evaluteBean.getEvaluations().add(new EvaluteBean.EvaluationsBean());
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_evaluate,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EvaluteBean.EvaluationsBean evaluationsBean = evaluteBean.getEvaluations().get(position);

        evaluationsBean.setFriendId(String.valueOf(list.get(position).getId()));

        holder.memberName.setText(list.get(position).getNickname());
        holder.likeValue.correctOffsetWhenContainerOnScrolling();
        holder.showValue.correctOffsetWhenContainerOnScrolling();
        Logger.i("Point:   " + list.get(position).getPoint());
        if (list.get(position).getPoint() == 0){
            holder.all_friendlikevalue.setVisibility(View.VISIBLE);
            holder.likeValue.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {
                    String likeValueToServer = String.valueOf(holder.likeValue.getProgress());
                    Logger.e("likeValue:" + likeValueToServer);
                    evaluationsBean.setFriendPoint(likeValueToServer);
                }
                @Override
                public void getProgressOnActionUp(int progress, float progressFloat) {

                }
                @Override
                public void getProgressOnFinally(int progress, float progressFloat) {

                }
            });
        }else{
            holder.all_friendlikevalue.setVisibility(View.GONE);
            evaluationsBean.setFriendPoint("5");
        }

        holder.showValue.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                String showValueToServer = String.valueOf(holder.showValue.getProgress());
                Logger.e("showValue" + showValueToServer);
                evaluationsBean.setRoomEvaluationPoint(showValueToServer);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        final List<String> labels = list.get(position).getLabels();
        holder.evaluate.setAdapter(new TagAdapter<String>(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) View.inflate(context,R.layout.tv,null);
                view.setText(labels.get(position));
                return view;
            }
        });
        holder.evaluate.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                holder.toServerEvaluate.setText(labels.get(position));
                List<String> memberLabels = new ArrayList<>();
                memberLabels.add(labels.get(position));
                Logger.e(memberLabels.toString());
                evaluationsBean.setLabels(memberLabels);
                return true;
            }
        });
    }
    public EvaluteBean getEvaluteBean(){
        return evaluteBean;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView icon;
        TextView memberName;
        BubbleSeekBar likeValue;
        BubbleSeekBar showValue;
        TagFlowLayout evaluate;
        EditText toServerEvaluate;
        AutoLinearLayout all_friendlikevalue;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.memberIcon);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
            likeValue = (BubbleSeekBar) itemView.findViewById(R.id.bsb_likevalue_evaluate_item);
            showValue = (BubbleSeekBar) itemView.findViewById(R.id.bsb_show_evaluate_item);
            evaluate = (TagFlowLayout) itemView.findViewById(R.id.flowlayout_tabs_evaluate);
            toServerEvaluate = (EditText) itemView.findViewById(R.id.et_evaluate_item);
            all_friendlikevalue = (AutoLinearLayout) itemView.findViewById(R.id.all_friendlikevalue);
        }
    }
}
