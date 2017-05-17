package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

/**
 * Created by xuq on 2017/5/15.
 */

public class MyCirclePage1Adapter extends RecyclerView.Adapter<MyCirclePage1Adapter.ViewHolder>{
    private LayoutInflater mInflater;
    List<CircleInfoBean.DataBean> mDatas;



    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private TypeTwoAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(TypeTwoAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MyCirclePage1Adapter(List<CircleInfoBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyCirclePage1Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.item_circle_grildivew,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.circleName.setText(mDatas.get(position).getName());
        holder.circleRooms.setText("活动" + mDatas.get(position).getRoomCount());
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
    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleIcon;
        TextView circleName;
        TextView circleRooms;

        public ViewHolder(View itemView) {
            super(itemView);
            circleIcon = (CircleImageView) itemView.findViewById(R.id.civ_mycircle_circleIcon_fmt);
            circleName = (TextView) itemView.findViewById(R.id.tv_mycircle_circleName_fmt);
            circleRooms = (TextView) itemView.findViewById(R.id.tv_mycircle_circleRooms_fmt);
        }
    }
}
