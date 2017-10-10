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
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/10/9.
 */

public class GameFinishAdapter extends BaseAdapter {
    /**
     * 三种类型item
     */
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;

    int[] gameType = {R.drawable.one_0,R.drawable.one_1,R.drawable.one_2,R.drawable.one_3,R.drawable.one_4,R.drawable.others_icon,R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};
    private List<GameFinishBean.DataBean.MembersBean> mDatas;
    private Context context;
    private GameFinishBean.DataBean.RoomBean roomData;
    private boolean isShowQRCode;


    public GameFinishAdapter(List<GameFinishBean.DataBean.MembersBean> mDatas, Context context,GameFinishBean.DataBean.RoomBean roomData) {
        this.mDatas = mDatas;
        this.context = context;
        this.roomData = roomData;
    }
    @Override
    public int getCount() {
        return mDatas.size() + 2;
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int postion, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        int type = getItemViewType(postion);
        if (view ==  null){
            viewHolder = new ViewHolder();
            switch (type){
                case TYPE_1:
                    view = View.inflate(context, R.layout.share_type_head, null);
                    viewHolder.iv_game_type = view.findViewById(R.id.iv_game_type);
                    viewHolder.tv_room_title = view.findViewById(R.id.tv_room_title);
                    viewHolder.tv_room_starttime = view.findViewById(R.id.tv_room_starttime);
                    break;
                case TYPE_2:
                    view = View.inflate(context,R.layout.share_type_info,null);
                    viewHolder.iv_avatar = view.findViewById(R.id.iv_avatar);
                    viewHolder.tv_name = view.findViewById(R.id.tv_name);
                    viewHolder.tv_currency_number = view.findViewById(R.id.tv_currency_number);
                    viewHolder.tv_getpoint = view.findViewById(R.id.tv_getpoint);
                    viewHolder.tv_number_fmt = view.findViewById(R.id.tv_number_fmt);
                    viewHolder.iv_isshow_gameinfo = view.findViewById(R.id.iv_isshow_gameinfo);
                    viewHolder.tv_room_number = view.findViewById(R.id.tv_room_number);
                    viewHolder.tv_all_number = view.findViewById(R.id.tv_all_number);
                    break;
                case TYPE_3:
                    view = View.inflate(context,R.layout.share_type_foot,null);
                    viewHolder.iv_QR_Code = view.findViewById(R.id.iv_qr_code);
                    break;
            }
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        switch (type){
            case TYPE_1:
                viewHolder.iv_game_type.setImageResource(gameType[roomData.getGame().getId()]);
                viewHolder.tv_room_title.setText(roomData.getName());
                break;
            case TYPE_2:
                //头像
                Glide.with(context)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(postion-1).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .signature(new StringSignature(mDatas.get(postion-1).getAvatarSignature()))
                        .into(viewHolder.iv_avatar);
                break;
            case TYPE_3:
                if (isShowQRCode){
                    viewHolder.iv_QR_Code.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.iv_QR_Code.setVisibility(View.GONE);
                }
                break;
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_1;
        }else if (position == mDatas.size()+1){
            return TYPE_3;
        }else{
            return TYPE_2;
        }
    }

    class ViewHolder{
        ImageView iv_QR_Code;
        TextView tv_room_title;
        TextView tv_room_starttime;
        ImageView iv_game_type;
        ImageView iv_avatar;
        TextView tv_name;
        TextView tv_currency_number;
        TextView tv_getpoint;
        TextView tv_number_fmt;
        ImageView iv_isshow_gameinfo;
        TextView tv_room_number;
        TextView tv_all_number;
    }
    public void changeMoreStatus(boolean status) {
        isShowQRCode = status;
    }
}
