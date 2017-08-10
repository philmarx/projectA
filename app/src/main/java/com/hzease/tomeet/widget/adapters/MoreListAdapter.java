package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/4/5.
 */

public class MoreListAdapter extends BaseAdapter {
    private List<RankingBean.DataBean> list;
    private Context context;

    public MoreListAdapter(List<RankingBean.DataBean> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size()-3;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_ranking, null);
            viewHolder.order =  convertView.findViewById(R.id.item_ranking_order);
            viewHolder.rankname =  convertView.findViewById(R.id.item_ranking_name);
            viewHolder.rankpoint =  convertView.findViewById(R.id.item_ranking_point);
            viewHolder.icon =  convertView.findViewById(R.id.item_ranking_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        position = position+3;
        viewHolder.order.setText(String.valueOf(list.get(position).getRanking()));
        viewHolder.rankname.setText(list.get(position).getNickname());
        viewHolder.rankpoint.setText(String.valueOf(list.get(position).getPoint()));
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + list.get(position).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(list.get(position).getAvatarSignature()))
                .into(viewHolder.icon);
        return convertView;

}

public class ViewHolder {
    public CircleImageView icon;
    public TextView order;
    public TextView rankname;
    public TextView rankpoint;
}
}
