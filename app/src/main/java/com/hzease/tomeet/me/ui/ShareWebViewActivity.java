package com.hzease.tomeet.me.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import butterknife.OnClick;

/**
 * Created by xuq on 2017/9/1.
 */

public class ShareWebViewActivity extends NetActivity {
    @BindView(R.id.tv_title_webview_act)
    TextView tv_title_webview_act;
    @BindView(R.id.iv_webview_share)
    ImageView iv_webview_share;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setAppCacheEnabled(false);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);     // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("https://hzease.com/share/appInnerShare.html");
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

    @OnClick(R.id.iv_webview_share)
    public void onViewClicked() {
        new ShareAction(mySelf)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media != null) {
                            UMWeb web = new UMWeb(AppConstants.TOMMET_SHARE_APP_SHARE + PTApplication.userId + "&origin=" + share_media.toString());
                            web.setTitle("后会有期");
                            web.setThumb(new UMImage(mySelf, R.drawable.share_logo_200x200));
                            web.setDescription("我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）");
                            new ShareAction(mySelf).setPlatform(share_media).setCallback(new UMShareListener() {
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
    }
}
