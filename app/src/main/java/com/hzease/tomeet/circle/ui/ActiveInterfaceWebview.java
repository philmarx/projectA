package com.hzease.tomeet.circle.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import butterknife.OnClick;

/**
 * Created by xuq on 2017/8/3.
 */

public class ActiveInterfaceWebview extends NetActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    @BindView(R.id.tv_title_webview_act)
    TextView tv_title_webview_act;

    private String activityUrl;
    private String name;
    private String desc;
    private String photoUrl;

    @OnClick(R.id.iv_webview_share)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_webview_share:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    requestPermissions(mPermissionList, 123);
                }
                new ShareAction(this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media != null) {
                                    UMWeb web = new UMWeb(activityUrl + "?userId=" + PTApplication.userId);
                                    web.setTitle(name);
                                    web.setThumb(new UMImage(ActiveInterfaceWebview.this, photoUrl));
                                    web.setDescription(desc);
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
                                            Logger.e(share_media.toString() + throwable.getMessage());
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
        Intent intent = getIntent();
        activityUrl = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        photoUrl = intent.getStringExtra("photoUrl");
        tv_title_webview_act.setText(name);
        //webView.getSettings().setJavaScriptEnabled(true);
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        //s.setAppCacheEnabled(true);
        //s.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);     // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        //s.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; 1503-M02 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.2 TBS/036558 Safari/537.36 MicroMessenger/6.3.25.861 NetType/WIFI Language/zh_CN");
        //Logger.e(s.getUserAgentString());
        webView.requestFocus();
        webView.setScrollBarStyle(0);
        webView.loadUrl(activityUrl + "?userId=" + PTApplication.userId + "&inapp=true&appuser=" + PTApplication.userId + "&apptoken=" + PTApplication.userToken);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb_progress.setProgress(newProgress);
                if (newProgress == 100) {
                    pb_progress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
}
