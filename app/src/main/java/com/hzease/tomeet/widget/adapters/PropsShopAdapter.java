package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.PropsShopBean;

import java.util.List;

/**
 * Created by xuq on 2017/5/18.
 */

public class PropsShopAdapter extends RecyclerView.Adapter<PropsShopAdapter.ViewHolder>{
    List<PropsShopBean> mDatas;
    private LayoutInflater mInflater;
    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private PropsShopAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(PropsShopAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public PropsShopAdapter(List<PropsShopBean> mDatas, Context context) {
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_propsshope,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.propsName.setText(mDatas.get(position).getPropsName());
        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
                holder.propsIcon.setImageResource(mDatas.get(position).getPropsIcon());
                holder.curreny.setVisibility(View.VISIBLE);
                break;
        }
        holder.propsDic.setText(mDatas.get(position).getPropsDic());
        holder.propMoney.setText(mDatas.get(position).getPropsMoney());
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
        TextView propsName;
        ImageView propsIcon;
        TextView propsDic;
        TextView propMoney;
        ImageView curreny;

        public ViewHolder(View itemView) {
            super(itemView);
            propsName = (TextView) itemView.findViewById(R.id.tv_props_name_item);
            propsIcon = (ImageView) itemView.findViewById(R.id.iv_props_icon_item);
            propsDic = (TextView) itemView.findViewById(R.id.tv_props_dic_item);
            propMoney = (TextView) itemView.findViewById(R.id.tv_props_money_item);
            curreny = (ImageView) itemView.findViewById(R.id.curreny);
        }
    }
}
