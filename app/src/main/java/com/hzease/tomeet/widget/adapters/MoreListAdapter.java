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
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private List<RankingBean.DataBean> list;
    private Context context;
    public MoreListAdapter(List<RankingBean.DataBean> list, Context context) {
        super();
        this.list = list;
        this.context = context;
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
        int type = getItemViewType(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            switch (type){
                case TYPE_1:
                    convertView = View.inflate(PTApplication.getInstance(), R.layout.item_rankingtop,null);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_rankingtop_order);
                    viewHolder.rankname = (TextView) convertView.findViewById(R.id.item_rankingtop_name);
                    viewHolder.rankpoint = (TextView) convertView.findViewById(R.id.item_rankingtop_point);
                    viewHolder.topIcon = (CircleImageView) convertView.findViewById(R.id.item_rankingtop_icon);
                    break;
                case TYPE_2:
                    convertView = View.inflate(PTApplication.getInstance(),R.layout.item_ranking,null);
                    viewHolder.order = (TextView) convertView.findViewById(R.id.item_ranking_order);
                    viewHolder.rankname = (TextView) convertView.findViewById(R.id.item_ranking_name);
                    viewHolder.rankpoint = (TextView) convertView.findViewById(R.id.item_ranking_point);
                    viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.item_ranking_icon);
                    break;
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (type){
            case TYPE_1:
                if (position==0){
                    viewHolder.imageView.setImageResource(R.drawable.no1);
                }else if (position == 1){
                    viewHolder.imageView.setImageResource(R.drawable.no2);
                }else if (position == 2){
                    viewHolder.imageView.setImageResource(R.drawable.no3);
                }
                viewHolder.rankname.setText(list.get(position).getNickname());
                viewHolder.rankpoint.setText(String.valueOf(list.get(position).getPoint()));
                Glide.with(context)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + list.get(position).getUserId()+ AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .signature(new StringSignature(list.get(position).getAvatarSignature()))
                        .into(viewHolder.topIcon);
                break;
            case TYPE_2:
                viewHolder.order.setText(String.valueOf(list.get(position).getRanking()));
                viewHolder.rankname.setText(list.get(position).getNickname());
                viewHolder.rankpoint.setText(String.valueOf(list.get(position).getPoint()));
                Glide.with(context)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + list.get(position).getUserId()+ AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .signature(new StringSignature(list.get(position).getAvatarSignature()))
                        .into(viewHolder.icon);
                break;
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0 || position == 2 || position ==1){
            return  TYPE_1;
        }else{
            return TYPE_2;
        }
    }

    public class ViewHolder{
        public CircleImageView topIcon;
        public CircleImageView icon;
        public ImageView imageView;
        public TextView order;
        public TextView rankname;
        public TextView rankpoint;
    }
}
