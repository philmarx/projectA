package pro.yueyuan.project_t.widget.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;

/**
 * Created by xuq on 2017/4/5.
 */

public class MainListAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return 24;
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
        holder.imageView.setImageResource(R.drawable.basketball);
        return convertView;
    }
    public class ViewHolder{
        public ImageView imageView;
    }
}

