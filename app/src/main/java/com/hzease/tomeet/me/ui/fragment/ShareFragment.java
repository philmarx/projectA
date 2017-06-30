package com.hzease.tomeet.me.ui.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.Button;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;

/**
 * Created by xuq on 2017/6/30.
 */

public class ShareFragment extends BaseFragment {
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.bt_share_fmt)
    Button bt_share_fmt;
    public static ShareFragment newInstance(){
        return new ShareFragment();
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_share;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        bt_share_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    requestPermissions(mPermissionList, 123);
                }
                UMWeb web = new UMWeb("https://a.mlinks.cc/AcMN?userId=" + PTApplication.userId);
                web.setTitle("后会有期");
                web.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));
                web.setDescription("薛之谦的心愿是世界和平，我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）");
                new ShareAction(getActivity()).withMedia(web)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(new UMShareListener() {
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
                                Logger.e(share_media.toString());
                                //ToastUtils.getToast(mContext, "分享失败");
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                Logger.e(share_media.toString());
                                //ToastUtils.getToast(mContext, "取消分享");
                            }
                        }).open();
            }
        });
    }
}
