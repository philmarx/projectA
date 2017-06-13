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
import com.hzease.tomeet.data.SmallPaperBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/6/13.
 */

public class PaperListAdapter extends BaseAdapter {

    List<SmallPaperBean.DataBean> mDatas;
    Context context;
    public PaperListAdapter(List<SmallPaperBean.DataBean> mDatas, Context context) {
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
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_paperlist,null);
            viewHolder.senderIcon = (CircleImageView) convertView.findViewById(R.id.civ_pagerlist_sendericon_item);
            viewHolder.senderInfo = (TextView) convertView.findViewById(R.id.tv_pagerlist_senderinfo_item);
            viewHolder.paperState = (ImageView) convertView.findViewById(R.id.iv_pagerlist_pagerstate_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 头像
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(viewHolder.senderIcon);
        viewHolder.senderInfo.setText(mDatas.get(position).getNickname()+"给你传个小纸条");
        int state = mDatas.get(position).getState();
        //判断小纸条是否已读
        if (state == 0){
            viewHolder.paperState.setVisibility(View.VISIBLE);
        }else{
            viewHolder.paperState.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        public CircleImageView senderIcon;
        public TextView senderInfo;
        public ImageView paperState;
    }
}
