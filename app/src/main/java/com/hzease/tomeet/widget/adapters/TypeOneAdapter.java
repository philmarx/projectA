package com.hzease.tomeet.widget.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameTypeBean;

import java.util.List;

/**
 * Created by xuq on 2017/5/2.
 */

public class TypeOneAdapter extends BaseAdapter {

    private List<GameTypeBean.ChildrenBean> list;
    private int[] drawables = {R.drawable.one_1,R.drawable.one_2,R.drawable.one_3,R.drawable.one_4,R.drawable.one_5, R.drawable.create_chat_room};
    public TypeOneAdapter(List<GameTypeBean.ChildrenBean> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_gamelist,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.game_icon);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.game_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(drawables[position]);
        viewHolder.textView.setText(list.get(position).getName());
        return convertView;
    }

    public class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
