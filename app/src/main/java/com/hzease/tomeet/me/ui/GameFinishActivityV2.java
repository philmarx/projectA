package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hzease.tomeet.BaseActivity;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.utils.ScreentUtils;
import com.hzease.tomeet.widget.adapters.GameFinishAdapter;
import com.hzease.tomeet.widget.adapters.GameFinishAdapterRV;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/10/9.
 */

public class GameFinishActivityV2 extends NetActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.game_finish_share_fmt)
    ImageView game_finish_share_fmt;
    @BindView(R.id.lv_gamefinish_fmt)
    RecyclerView lv_gamefinish_fmt;
    private long roomId;
    private GameFinishAdapterRV adapter;

    @OnClick(R.id.game_finish_share_fmt)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.game_finish_share_fmt:
                adapter.changeIsShowQRCode(true);
                new ShareAction(mySelf)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                //final Bitmap bitmap = ScreentUtils.compressImage(ScreentUtils.getListViewBitmap(lv_gamefinish_fmt));
                                Bitmap bitmap = shotRecyclerView(lv_gamefinish_fmt);
                                if (share_media != null) {
                                    UMImage image = new UMImage(GameFinishActivityV2.this,bitmap);
                                    image.setThumb(new UMImage(mySelf,bitmap));
                                    image.compressStyle = UMImage.CompressStyle.QUALITY;
                                    new ShareAction(mySelf).setPlatform(share_media).setCallback(new UMShareListener() {
                                        @Override
                                        public void onStart(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                        }

                                        @Override
                                        public void onResult(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                            //ToastUtils.getToast(mContext, "分享成功");
                                            adapter.changeIsShowQRCode(false);
                                        }

                                        @Override
                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                            Logger.e(share_media.toString() + throwable.getMessage());
                                            //ToastUtils.getToast(mContext, "分享失败");
                                            adapter.changeIsShowQRCode(false);
                                        }

                                        @Override
                                        public void onCancel(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toString());
                                            //ToastUtils.getToast(mContext, "取消分享");
                                            adapter.changeIsShowQRCode(false);
                                        }
                                    }).withMedia(image).share();
                                }
                            }
                        }).open();
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_gamefinish_v2;
    }
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = intent.getLongExtra("roomId",0);
        lv_gamefinish_fmt.setLayoutManager(new LinearLayoutManager(this));
        PTApplication.getRequestService().gameFinishInfo(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        changeLoadView(true);
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        changeLoadView(false);
                    }
                })
                .subscribe(new Subscriber<GameFinishBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GameFinishBean gameFinishBean) {
                        adapter = new GameFinishAdapterRV(gameFinishBean.getData().getMembers(), GameFinishActivityV2.this, gameFinishBean.getData().getRoom());
                        lv_gamefinish_fmt.setAdapter(adapter);
                    }
                });
    }

    public void changeLoadView(boolean isShown) {
        if (isShown) {
            showLoadingDialog();
        } else {
            hideLoadingDialog();
        }
    }

}
