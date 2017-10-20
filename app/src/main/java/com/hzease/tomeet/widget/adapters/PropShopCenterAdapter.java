package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.PropsShopCenter;

import java.util.List;

/**
 * Created by xuq on 2017/10/19.
 */

public class PropShopCenterAdapter extends BaseAdapter {
    private Context context;
    private List<PropsShopCenter.DataBean> mData;

    public PropShopCenterAdapter(Context context, List<PropsShopCenter.DataBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        PropShopCenterHolder holder;
        if (view == null) {
            holder = new PropShopCenterHolder();
            view = LayoutInflater.from(context).inflate(R.layout.prop_shop_item, null);
            holder.iv_prop_shop_icon = view.findViewById(R.id.iv_prop_shop_icon);
            holder.iv_prop_shop_state = view.findViewById(R.id.iv_prop_shop_state);
            holder.tv_prop_shop_name = view.findViewById(R.id.tv_prop_shop_name);
            holder.iv_prop_shop_ismoney = view.findViewById(R.id.iv_prop_shop_ismoney);
            holder.tv_prop_shop_money = view.findViewById(R.id.tv_prop_shop_money);
            holder.tv_prop_shop_issale = view.findViewById(R.id.tv_prop_shop_issale);
            view.setTag(holder);
        } else {
            holder = (PropShopCenterHolder) view.getTag();
        }

        //设置删除线
        holder.tv_prop_shop_issale.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //加载道具商城图片
        Glide.with(context)
                .load(mData.get(position).getPhotoUrl())
                .into(holder.iv_prop_shop_icon);
        //是否显示热门或者限时
        switch (mData.get(position).getDiscountType()){
            case "无":
                holder.iv_prop_shop_state.setVisibility(View.GONE);
                break;
            case "hot":
                holder.iv_prop_shop_state.setImageResource(R.drawable.shop_hot);
                break;
            case "time":
                holder.iv_prop_shop_state.setImageResource(R.drawable.shop_sale_time);
                break;
        }
        //判断是否显示价格或者叶子
        if (mData.get(position).getMoney() == -1){
            if (mData.get(position).getBadge() == -1){
                holder.iv_prop_shop_ismoney.setVisibility(View.GONE);
                holder.tv_prop_shop_money.setText("查看获取方式");
            }else{
                holder.tv_prop_shop_money.setPadding(20,0,0,0);
                holder.tv_prop_shop_money.setText(String.valueOf(mData.get(position).getBadge()));
            }
        }else{
            holder.iv_prop_shop_ismoney.setVisibility(View.GONE);
            holder.tv_prop_shop_money.setText("¥" + mData.get(position).getMoney() / 100);
        }
        //是否是特价
        if (mData.get(position).getOriginalMoney() == -1){
            holder.tv_prop_shop_issale.setVisibility(View.GONE);
        }else{
            holder.tv_prop_shop_issale.setText(String.valueOf(mData.get(position).getOriginalMoney()/100));
        }
        //道具名称
        holder.tv_prop_shop_name.setText(mData.get(position).getTitle());
        return view;
    }

    class PropShopCenterHolder {
        ImageView iv_prop_shop_icon;
        ImageView iv_prop_shop_state;
        TextView tv_prop_shop_name;
        ImageView iv_prop_shop_ismoney;
        TextView tv_prop_shop_money;
        TextView tv_prop_shop_issale;
    }
}
