package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/7/12.
 */

public class SelectFriendAdapter extends BaseAdapter {

    private List<RealmFriendBean> friends;
    private final Realm mRealm = Realm.getDefaultInstance();
    private Context mContext;
    private List<RealmFriendBean> green;
    private List<RealmFriendBean> blue;
    private List<RealmFriendBean> gold;

    public SelectFriendAdapter(Context mContext) {
        friends = mRealm.where(RealmFriendBean.class).between("point", 5, 10).findAllSorted("point", Sort.DESCENDING);
        green = mRealm.where(RealmFriendBean.class).between("point", 5, 6).findAllSorted("point", Sort.DESCENDING);
        blue = mRealm.where(RealmFriendBean.class).between("point", 7, 8).findAllSorted("point", Sort.DESCENDING);
        gold = mRealm.where(RealmFriendBean.class).between("point", 9, 10).findAllSorted("point", Sort.DESCENDING);
        this.mContext = mContext;
    }

    public void setData(List<RealmFriendBean> friends) {
        this.friends = friends;
        green = mRealm.where(RealmFriendBean.class).between("point", 5, 6).findAllSorted("point", Sort.DESCENDING);
        blue = mRealm.where(RealmFriendBean.class).between("point", 7, 8).findAllSorted("point", Sort.DESCENDING);
        gold = mRealm.where(RealmFriendBean.class).between("point", 9, 10).findAllSorted("point", Sort.DESCENDING);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_select_friend, null);
            viewHolder.ll_select_friend_title = (LinearLayout) convertView.findViewById(R.id.ll_select_friend_title);
            viewHolder.tv_friend_point = (TextView) convertView.findViewById(R.id.tv_friend_point);
            viewHolder.civ_avatar_bg_select_friend = (CircleImageView) convertView.findViewById(R.id.civ_avatar_bg_select_friend);
            viewHolder.iv_avatar_select_friend = (ImageView) convertView.findViewById(R.id.iv_avatar_select_friend);
            viewHolder.tv_select_friend_name_item = (TextView) convertView.findViewById(R.id.tv_select_friend_name_item);
            viewHolder.rb_select_friend_item = (CheckBox) convertView.findViewById(R.id.rb_select_friend_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RealmFriendBean friendBean = friends.get(position);
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + friendBean.getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(friendBean.getAvatarSignature()))
                .into(viewHolder.iv_avatar_select_friend);
        viewHolder.tv_select_friend_name_item.setText(friendBean.getNickname());
        viewHolder.ll_select_friend_title.setVisibility(View.VISIBLE);
        int point = friendBean.getPoint();
        switch (point) {
            case 5:
            case 6:
                viewHolder.civ_avatar_bg_select_friend.setImageResource(R.color.friend_green);
                break;
            case 7:
            case 8:
                viewHolder.civ_avatar_bg_select_friend.setImageResource(R.color.friend_blue);
                break;
            case 9:
            case 10:
                viewHolder.civ_avatar_bg_select_friend.setImageResource(R.color.friend_gold);
                break;
        }
        if (friendBean.isChoose()) {
            viewHolder.rb_select_friend_item.setChecked(true);
        } else {
            viewHolder.rb_select_friend_item.setChecked(false);
        }
        Logger.e("index" + gold.get(0).toString());
        Logger.e("index" + friends.indexOf(blue.get(0)));
        Logger.e("index" + friends.indexOf(green.get(0)));
        if (friends.get(position).getId() == gold.get(0).getId()) {
            viewHolder.ll_select_friend_title.setVisibility(View.VISIBLE);
            viewHolder.tv_friend_point.setText("金色好友");
        } else if (friends.get(position).getId() == blue.get(0).getId()) {
            viewHolder.ll_select_friend_title.setVisibility(View.VISIBLE);
            viewHolder.tv_friend_point.setText("蓝色好友");
        } else if (friends.get(position).getId() == green.get(0).getId()) {
            viewHolder.ll_select_friend_title.setVisibility(View.VISIBLE);
            viewHolder.tv_friend_point.setText("绿色好友");
        } else {
            viewHolder.ll_select_friend_title.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_select_friend_title;
        TextView tv_friend_point;
        CircleImageView civ_avatar_bg_select_friend;
        ImageView iv_avatar_select_friend;
        TextView tv_select_friend_name_item;
        CheckBox rb_select_friend_item;
    }
}
