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
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuq on 2017/5/12.
 */

public class TurnsPicAdapter extends PagerAdapter {
    Map<String, String> maps;
    List<ImageView> mImageViews;
    Context context;
    long userId;
    public TurnsPicAdapter(Map<String, String> maps, Context context,long userId) {
        this.maps = maps;
        this.context = context;
        this.userId = userId;
        mImageViews = new ArrayList<>();
        Logger.e("TurnsPicAdapter");
        Set<Map.Entry<String, String>> entries = maps.entrySet();
        Logger.e(entries.size()+"");
        for (Map.Entry<String, String> entry : entries) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = "/" + entry.getKey().replace("Signature", "");
            // Logger.e(url + "   " + userId);
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + userId + url)
                    .signature(new StringSignature(entry.getValue()))
                    .into(imageView);
            mImageViews.add(imageView);
        }
        Logger.e(mImageViews.size()+"");
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViews.get(position));
        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }
}
