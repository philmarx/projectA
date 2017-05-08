package com.hzease.tomeet.widget.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.ActivityTypeBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by xuq on 2017/4/5.
 */

public class MainListAdapter extends BaseAdapter {

    List<ActivityTypeBean.DataBean> list;

    int[] gameType = {R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_3,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
            R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1,R.drawable.two_one4_2,R.drawable.two_one4_3,R.drawable.two_one4_4,R.drawable.two_one4_5};

    public MainListAdapter(List<ActivityTypeBean.DataBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return gameType.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(PTApplication.getInstance(),R.layout.item_ranking_left,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.mainlist_src);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(gameType[position]);
        Logger.e(position + "");
        return convertView;
    }
    public class ViewHolder{
        public ImageView imageView;
    }
}

