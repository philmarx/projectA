package com.hzease.tomeet.circle.ui;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xuq on 2017/8/3.
 */

public class ActiveInterfaceWebview extends NetActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;

    @OnClick(R.id.iv_webview_share)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_webview_share:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    requestPermissions(mPermissionList, 123);
                }
                new ShareAction(this)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media != null) {
                                    UMWeb web = new UMWeb(AppConstants.TOMMET_SHARE_APP_SHARE + PTApplication.userId + "&origin=" + share_media.toString());
                                    web.setTitle("后会有期");
                                    web.setThumb(new UMImage(ActiveInterfaceWebview.this, R.drawable.share_logo_200x200));
                                    web.setDescription("薛之谦的心愿是世界和平，我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）");
                                    new ShareAction(ActiveInterfaceWebview.this).setPlatform(share_media).setCallback(new UMShareListener() {
                                        @Override
                                        public void onStart(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                        }

                                        @Override
                                        public void onResult(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toSnsPlatform().mShowWord);
                                            //ToastUtils.getToast(mContext, "分享成功");
                                        }

                                        @Override
                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                            Logger.e(share_media.toString()+throwable.getMessage());
                                            //ToastUtils.getToast(mContext, "分享失败");
                                        }

                                        @Override
                                        public void onCancel(SHARE_MEDIA share_media) {
                                            Logger.e(share_media.toString());
                                            //ToastUtils.getToast(mContext, "取消分享");
                                        }
                                    }).withMedia(web).share();
                                }
                            }
                        }).open();
                break;
        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.baidu.com");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb_progress.setProgress(newProgress);
                if (newProgress == 100){
                    pb_progress.setVisibility(View.GONE);
                }
                Logger.e("progress" + newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
