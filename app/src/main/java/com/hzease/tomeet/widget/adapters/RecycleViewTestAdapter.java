package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.hzease.tomeet.R;

/**
 * Created by xuq on 2017/4/21.
 */

public class RecycleViewTestAdapter extends RecyclerView.Adapter<RecycleViewTestAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;
    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RecycleViewTestAdapter(Context context, List<String> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    @Override
    public RecycleViewTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_listview, parent, false);
        RecycleViewTestAdapter.ViewHolder viewHolder = new RecycleViewTestAdapter.ViewHolder(view);
        viewHolder.mContent = (TextView) view.findViewById(R.id.list_item_content);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        TextView mContent;
    }

    @Override
    public void onBindViewHolder(final RecycleViewTestAdapter.ViewHolder holder, final int position) {
        holder.mContent.setText(mDatas.get(position));
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}

