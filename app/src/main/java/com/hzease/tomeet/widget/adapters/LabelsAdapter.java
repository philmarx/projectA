package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.R;

import java.util.List;

/**
 * Created by xuq on 2017/6/29.
 */

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<String> mLabels;
    private int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};
    public LabelsAdapter(List<String> mLabels, Context context) {
        this.mLabels = mLabels;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.labels_personspace,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mLabels.get(position));
        holder.textView.setBackgroundResource(mResources[position % 5]);
    }

    @Override
    public int getItemCount() {
        return mLabels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView =  itemView.findViewById(R.id.tv_labels);
        }
    }
}
