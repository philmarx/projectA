package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.Text;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateAdapter extends RecyclerView.Adapter<WaitEvaluateAdapter.ViewHolder> {

    List<WaitEvaluateBean.DataBean> list;
    Context context;
    public WaitEvaluateAdapter(List<WaitEvaluateBean.DataBean> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.item_evaluate,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.memberName.setText(list.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView icon;
        TextView memberName;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.memberIcon);
            memberName = (TextView) itemView.findViewById(R.id.memberName);
        }
    }
}
