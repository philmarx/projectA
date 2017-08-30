package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hzease.tomeet.data.AccountBean;
import com.hzease.tomeet.data.ActivityBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuq on 2017/8/30.
 */

public class ActivityAdapter extends StaticPagerAdapter {
    private List<ImageView> mImageViews;
    private List<ActivityBean.DataBean> mDatas;
    private Context context;


    public interface OnItemClickLitener {
        void onItemClick(View view,int position);
    }

    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener){
        this.onItemClickLitener = onItemClickLitener;
    }

    public ActivityAdapter(List<ActivityBean.DataBean> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
        mImageViews = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context)
                    .load(mDatas.get(i).getPhotoUrl())
                    .into(imageView);
            mImageViews.add(imageView);
        }
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        ViewGroup parent = (ViewGroup) mImageViews.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        if (onItemClickLitener!=null){
            mImageViews.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLitener.onItemClick(mImageViews.get(position),position);
                }
            });
        }
        return mImageViews.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
