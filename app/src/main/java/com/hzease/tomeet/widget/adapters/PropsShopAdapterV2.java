package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;

/**
 * Created by xuq on 2017/9/1.
 */

public class PropsShopAdapterV2 extends RecyclerView.Adapter<PropsShopAdapterV2.ViewHolder> {

    private LayoutInflater mInflater;
    private int[] propsBg = {R.drawable.small_paper_v2, R.drawable.labels_clear_v2, R.drawable.changename_v2, R.drawable.buqian_v2, R.drawable.vip_1_v2, R.drawable.vip_3_v2, R.drawable.vip_12_v2
            ,R.drawable.movie_ticket_v2};

    public PropsShopAdapterV2(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_propsshopv2, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bg.setImageResource(propsBg[position]);
        switch (position) {
            case 4:
                holder.iv_state.setVisibility(View.VISIBLE);
                holder.iv_state.setImageResource(R.drawable.hot);
                break;
            case 5:
            case 6:
            case 7:
                holder.iv_state.setVisibility(View.VISIBLE);
                holder.iv_state.setImageResource(R.drawable.limit);
                break;
            default:
                holder.iv_state.setVisibility(View.GONE);
                break;
        }
        if (mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return propsBg.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bg;
        ImageView iv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.iv_shop_prop_item);
            iv_state = itemView.findViewById(R.id.iv_state);
        }
    }
}
