package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HavaBZmoneyRoomBean;

import java.util.Calendar;
import java.util.List;

/**
 * Created by xuq on 2017/6/23.
 */

public class FreezeBalanceRoomAdapter extends RecyclerView.Adapter<FreezeBalanceRoomAdapter.ViewHolder> {

    List<HavaBZmoneyRoomBean.DataBean> list;
    private Context context;
    int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.one_5, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};

    public FreezeBalanceRoomAdapter(List<HavaBZmoneyRoomBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activitytype, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.gameType.setImageResource(gameType[list.get(position).getGame().getId()]);
        holder.roomName.setText(list.get(position).getName());
        holder.gamePlace.setText("保证金" + String.valueOf(list.get(position).getMoney() / 100.0f));
        holder.gamePlace.setTextColor(Color.rgb(255, 131, 115));
        int state = list.get(position).getState();
        switch (state) {
            case 0:
            case 1:
                holder.isReady.setText("未开始");
                    break;
            case 2:
                holder.isReady.setText("进行中");
                break;
            case 3:
                holder.isReady.setText("待评价");
                break;
            case 4:
                holder.isReady.setText("已结束");
                break;
        }
        holder.gameTime.setText(list.get(position).getBeginTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameType;
        TextView roomName;
        TextView gamePlace;
        TextView isReady;
        TextView gameTime;

        public ViewHolder(View itemView) {
            super(itemView);
            gameType = (ImageView) itemView.findViewById(R.id.iv_me_gametype_item);
            roomName = (TextView) itemView.findViewById(R.id.tv_me_roomname_item);
            gamePlace = (TextView) itemView.findViewById(R.id.tv_me_roomplace_item);
            isReady = (TextView) itemView.findViewById(R.id.tv_me_isready_item);
            gameTime = (TextView) itemView.findViewById(R.id.tv_me_time_item);
        }
    }

}
