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
import com.hzease.tomeet.data.HomeRoomsBean;

import java.util.List;


/**
 * Created by xuq on 2017/5/3.
 */

public class MyJoinRoomsAdapter extends RecyclerView.Adapter<MyJoinRoomsAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<HomeRoomsBean.DataBean> list;
    int[] gameType = {R.drawable.two_one1_1,R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
                      R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
                      R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
                      R.drawable.two_one4_1,R.drawable.two_one4_2,R.drawable.two_one4_3,R.drawable.two_one4_4,R.drawable.two_one4_5};
    public MyJoinRoomsAdapter(List<HomeRoomsBean.DataBean> list, Context context) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_activitytype,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.gameType.setImageResource(gameType[list.get(position).getGame().getId()-6]);
        holder.roomName.setText(list.get(position).getName());
        holder.gamePlace.setText(list.get(position).getPlace());
        int state = list.get(position).getState();
        switch (state){
            case 0 :
            case 1 :
                holder.isReady.setTextColor(Color.rgb(3,181,19));
                holder.isReady.setText("未开始");
                break;
            case 2 :
                holder.isReady.setTextColor(Color.rgb(255,131,115));
                holder.isReady.setText("待评价");
                break;
            case 3 :
                holder.isReady.setTextColor(Color.rgb(184,184,184));
                holder.isReady.setText("已结束");
                break;
        }
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
