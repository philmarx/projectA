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
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PhoneContactBean;
import com.hzease.tomeet.utils.ChineseToEnglish;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/20.
 */

public class AddPhoneContactAdapter extends BaseAdapter {

    private Context context;

    private List<PhoneContactBean.DataBean> users;

    public AddPhoneContactAdapter(Context context, List<PhoneContactBean.DataBean> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addphonecontacts,null);
            viewHolder.mContactName = (TextView) convertView.findViewById(R.id.tv_phone_contact_name);
            viewHolder.mFriendStatus = (Button) convertView.findViewById(R.id.bt_phone_contact_status);
            viewHolder.mHeadIcon = (CircleImageView) convertView.findViewById(R.id.civ_phone_contact_icon);
            viewHolder.mNickName = (TextView) convertView.findViewById(R.id.tv_phone_contact_nickName);
            viewHolder.mNameTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mFriendStatusTv = (TextView) convertView.findViewById(R.id.tv_phone_contact_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mContactName.setText(users.get(position).getContactName());
        viewHolder.mNickName.setText("后会有期:"+users.get(position).getNickname());
        // 头像
        Glide.with(context)
                .load(AppConstants.YY_PT_OSS_USER_PATH + users.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(context))
                .signature(new StringSignature(users.get(position).getAvatarSignature()))
                .into(viewHolder.mHeadIcon);
        if (position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")){
            viewHolder.mNameTitle.setVisibility(View.VISIBLE);
            viewHolder.mNameTitle.setText(users.get(position).getLetter().toUpperCase());
        }else{
            viewHolder.mNameTitle.setVisibility(View.GONE);
        }
        //判断是否是好友
        if (users.get(position).getFriend() != 0){
            viewHolder.mFriendStatusTv.setText("已添加");
            viewHolder.mFriendStatusTv.setVisibility(View.VISIBLE);
            viewHolder.mFriendStatus.setVisibility(View.GONE);
        }else{
            viewHolder.mFriendStatusTv.setVisibility(View.GONE);
            viewHolder.mFriendStatus.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.mFriendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().sendInvitate(String.valueOf(users.get(position).getId()),"手机通讯录",PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                invitate(noDataBean.isSuccess(),noDataBean.getMsg(), finalViewHolder);
                            }
                        });
            }
        });

        return convertView;
    }

    private void invitate(boolean success, String msg, ViewHolder viewHolder) {
        if (success){
            viewHolder.mFriendStatusTv.setText("等待验证");
            viewHolder.mFriendStatusTv.setVisibility(View.VISIBLE);
            viewHolder.mFriendStatus.setVisibility(View.GONE);
            ToastUtils.getToast("发送请求成功");
        }else{
            ToastUtils.getToast("发送请求失败");
        }
    }


    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {

        String letter = users.get(position).getLetter();
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if(cnAscii == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     * @param letter
     * @return
     */
    public int getFirstLetterPosition(String letter){
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if(letter.charAt(0) == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }
    class ViewHolder{
        TextView mNameTitle;
        TextView mContactName;
        TextView mNickName;
        CircleImageView mHeadIcon;
        Button mFriendStatus;
        TextView mFriendStatusTv;
    }
}
