package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/31.
 */

public class ChooseUserAdapter extends BaseAdapter {
    private List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas;
    private Context context;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    public String key;

    public ChooseUserAdapter(List<GameChatRoomBean.DataBean.JoinMembersBean> mDatas, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_roommembers, null);
            viewHolder = new ViewHolder();
            viewHolder.member_icon = (CircleImageView) convertView.findViewById(R.id.civ_home_rooms_icon_item);
            viewHolder.member_name = (TextView) convertView.findViewById(R.id.tv__home_rooms_name_item);
            //viewHolder.member_isComplaint = (RadioButton) convertView.findViewById(R.id.rb_home_rooms_choosemember_item);
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
        final RadioButton radio = (RadioButton) convertView.findViewById(R.id.rb_home_rooms_choosemember_item);
        viewHolder.member_isComplaint = radio;
        viewHolder.member_isComplaint.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // 重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), radio.isChecked());
                ChooseUserAdapter.this.notifyDataSetChanged();
            }
        });

        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else {
            res = true;
        }
        viewHolder.member_isComplaint.setChecked(res);
        Iterator iter = states.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry next = (Map.Entry) iter.next();
            boolean val = (boolean) next.getValue();
            if (val){
                Logger.e(next.getKey()+"");
                key = (String) next.getKey();
            }
        }
        return convertView;
    }

    class ViewHolder {
        private CircleImageView member_icon;
        private TextView member_name;
        private RadioButton member_isComplaint;
    }
}
