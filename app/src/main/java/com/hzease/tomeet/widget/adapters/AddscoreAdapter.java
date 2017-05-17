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
import com.hzease.tomeet.circle.ui.MemberListActivity;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/8.
 */

public class AddscoreAdapter extends BaseAdapter {
    private List<GameFinishBean.DataBean.MembersBean> mDatas;
    private Context context;

    public AddscoreAdapter(List<GameFinishBean.DataBean.MembersBean> mDatas,Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView ==  null){
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_gamefinish,null);
            viewHolder = new ViewHolder();
            viewHolder.memberIcon = (CircleImageView) convertView.findViewById(R.id.civ_finfish_icon);
            viewHolder.memberName = (TextView) convertView.findViewById(R.id.tv_finish_name_fmt);
            viewHolder.memberPoint = (TextView) convertView.findViewById(R.id.tv_finish_point_fmt);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.memberName.setText(mDatas.get(position).getNickname());
        viewHolder.memberPoint.setText("+" + mDatas.get(position).getPoint()+"分");
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .placeholder(R.drawable.person_default_icon)
                .error(R.drawable.person_default_icon)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(viewHolder.memberIcon);
        return convertView;
    }
    public class ViewHolder{
        private CircleImageView memberIcon;
        private TextView memberName;
        private TextView memberPoint;
    }
}
