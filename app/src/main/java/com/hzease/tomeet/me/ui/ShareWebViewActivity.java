package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

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
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.webView)
    WebView webView;

    String shareUrl;
    String title;
    String photoUrl;
    String desc;

    private String activityUrl;

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
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        webView.loadUrl(activityUrl + "?userId=" + PTApplication.userId + "&inapp=true&appuser=" + PTApplication.userId + "&apptoken=" + PTApplication.userToken);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.e("url" + url);
                webView.loadUrl(url);
                return true;
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pb_progress != null) {
                    pb_progress.setProgress(newProgress);
                    if (newProgress == 100) {
                        iv_webview_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // url cunzai
                                if (TextUtils.isEmpty(activityUrl)) {
                                    ToastUtils.getToast("请刷新页面再试");
                                    return;
                                }
                                new ShareAction(mySelf)
                                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                        .setShareboardclickCallback(new ShareBoardlistener() {
                                            @Override
                                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                if (share_media != null) {
                                                    UMWeb web = new UMWeb(shareUrl + "?userId=" + PTApplication.userId + "&origin=" + share_media.toString());
                                                    web.setTitle(title);
                                                    web.setThumb(new UMImage(mySelf, photoUrl));
                                                    web.setDescription(desc);
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
                            }
                        });
                        pb_progress.setVisibility(View.GONE);
                        // TODO: 2017/9/7 API判断
                        if (Build.VERSION.SDK_INT >= 19){
                            webView.evaluateJavascript("returnJson()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    Logger.e("s" + s);
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        photoUrl = jsonObject.getString("image");
                                        title = jsonObject.getString("title");
                                        shareUrl = jsonObject.getString("url");
                                        desc = jsonObject.getString("message");
                                        tv_title_webview_act.setText(title);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ToastUtils.getToast("请刷新页面再试");
                                    }
                                }
                            });
                        }else{
                            photoUrl = "https://hzease.com/images/logo.png";
                            title = "后会有期";
                            shareUrl = "https://hzease.com/share/share.html";
                            desc = "我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）";
                            tv_title_webview_act.setText(title);
                            ToastUtils.getToast("手机版本过低,暂只能分享APP，请升级系统后重试");
                        }
                    } else {
                        iv_webview_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtils.getToast("请等待页面加载完成");
                            }
                        });
                    }
                    super.onProgressChanged(view, newProgress);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
