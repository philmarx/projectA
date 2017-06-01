package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.widget.CircleImageView;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/31.
 */

public class ChooseUserAdapter extends BaseAdapter {
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas;
    private Context context;
    public ChooseUserAdapter(List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    public void setData(List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_roommembers, null);
            viewHolder = new ViewHolder();
            viewHolder.member_icon = (CircleImageView) convertView.findViewById(R.id.civ_home_rooms_icon_item);
            viewHolder.member_name = (TextView) convertView.findViewById(R.id.tv__home_rooms_name_item);
            viewHolder.member_isComplaint = (CheckBox) convertView.findViewById(R.id.rb_home_rooms_choosemember_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //头像
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(viewHolder.member_icon);
        viewHolder.member_name.setText(mDatas.get(position).getNickname());
        if (mDatas.get(position).isChoose()){
            viewHolder.member_isComplaint.setChecked(true);
        }else{
            viewHolder.member_isComplaint.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder {
        private CircleImageView member_icon;
        private TextView member_name;
        private CheckBox member_isComplaint;
    }
}
