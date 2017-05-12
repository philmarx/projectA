package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuq on 2017/5/12.
 */

public class TurnsPicAdapter extends PagerAdapter {
    List<Bitmap> mBitmaps;
    List<ImageView> mImageViews;
    Context context;
    public TurnsPicAdapter(List<Bitmap> mBitmaps,Context context) {
        this.mBitmaps = mBitmaps;
        this.context = context;
        mImageViews = new ArrayList<>();
        Logger.e("TurnsPicAdapter");
        for (int i = 0; i < mBitmaps.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(mBitmaps.get(i));
            mImageViews.add(imageView);
            Logger.e(mImageViews.size()+"");
        }
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
