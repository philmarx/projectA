package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.InvitationsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/20.
 */

public class InvitationsAdapter extends BaseAdapter {
    private List<InvitationsBean.DataBean> mDatas;
    private Context context;

    public InvitationsAdapter(List<InvitationsBean.DataBean> mDatas, Context context) {
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addphonecontacts, null);
            viewHolder.mContactName = (TextView) convertView.findViewById(R.id.tv_phone_contact_name);
            viewHolder.mFriendStatus = (Button) convertView.findViewById(R.id.bt_phone_contact_status);
            viewHolder.mHeadIcon = (CircleImageView) convertView.findViewById(R.id.civ_phone_contact_icon);
            viewHolder.mNickName = (TextView) convertView.findViewById(R.id.tv_phone_contact_nickName);
            viewHolder.mNameTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mFriendStatusTv = (TextView) convertView.findViewById(R.id.tv_phone_contact_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mNameTitle.setVisibility(View.GONE);
        if (mDatas.get(position).getOrigin().equals("手机联系人")) {
            viewHolder.mNickName.setText("来自手机联系人");
        }
        // 头像
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(viewHolder.mHeadIcon);
        viewHolder.mContactName.setText(mDatas.get(position).getNickname());
        //判断是否添加
        if (mDatas.get(position).getState() == 0){
            viewHolder.mFriendStatusTv.setVisibility(View.GONE);
            viewHolder.mFriendStatus.setVisibility(View.VISIBLE);
            viewHolder.mFriendStatus.setText("接受");
        }else{
            viewHolder.mFriendStatusTv.setText("已添加");
            viewHolder.mFriendStatusTv.setVisibility(View.VISIBLE);
            viewHolder.mFriendStatus.setVisibility(View.GONE);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mFriendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().handleInviate(String.valueOf(mDatas.get(position).getId()),2,PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError" + e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()){
                                    finalViewHolder.mFriendStatusTv.setText("已添加");
                                    finalViewHolder.mFriendStatusTv.setVisibility(View.VISIBLE);
                                    finalViewHolder.mFriendStatus.setVisibility(View.GONE);
                                    ToastUtils.getToast(context,"添加好友成功");
                                }else{
                                    ToastUtils.getToast(context,noDataBean.getMsg());
                                }
                            }
                        });
            }
        });
        return convertView;
    }
    class ViewHolder {
        TextView mNameTitle;
        TextView mContactName;
        TextView mNickName;
        CircleImageView mHeadIcon;
        Button mFriendStatus;
        TextView mFriendStatusTv;
    }
}
