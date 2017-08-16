package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuq on 2017/5/12.
 */

public class TurnsPicAdapter extends StaticPagerAdapter {
    private List<ImageView> mImageViews;
    Context context;
    public TurnsPicAdapter(Map<String, String> maps, Context context,long userId) {
        this.context = context;
        mImageViews = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Logger.e("size" + entries.size());
        if (entries.size() == 0){
            ImageView defaultImage = new ImageView(context);
            defaultImage.setImageResource(R.drawable.person_default_icon);
            defaultImage.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews.add(defaultImage);
        }else{
            for (Map.Entry<String, String> entry : entries) {

                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.person_default_icon);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                String url = "/" + entry.getKey().replace("Signature", "");

                Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + url)
                        .signature(new StringSignature(entry.getValue()))
                        .into(imageView);
                if (url.equals("/avatar")) {
                    mImageViews.add(0, imageView);
                } else {
                    mImageViews.add(imageView);
                }
            }
        }

    }

    @Override
    public View getView(ViewGroup container, int position) {
        ViewGroup parent = (ViewGroup) mImageViews.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        return mImageViews.get(position);
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }
   /* private List<ImageView> mImageViews;
    Context context;
    public TurnsPicAdapter(Map<String, String> maps, Context context,long userId) {
        this.context = context;

        mImageViews = new ArrayList<>();

        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Logger.e("size" + entries.size());
        if (entries.size() == 0){
            ImageView defaultImage = new ImageView(context);
            defaultImage.setImageResource(R.drawable.person_default_icon);
            defaultImage.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews.add(defaultImage);
        }else{
            for (Map.Entry<String, String> entry : entries) {

                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.person_default_icon);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                String url = "/" + entry.getKey().replace("Signature", "");

                Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + url)
                        .signature(new StringSignature(entry.getValue()))
                        .into(imageView);
                if (url.equals("/avatar")) {
                    mImageViews.add(0, imageView);
                } else {
                    mImageViews.add(imageView);
                }
            }
        }

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Logger.e("mImageViews" +mImageViews.size());
            ViewGroup parent = (ViewGroup) mImageViews.get(position % mImageViews.size()).getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(mImageViews.get(position % mImageViews.size()),0);
            return mImageViews.get(position % mImageViews.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }*/
}
