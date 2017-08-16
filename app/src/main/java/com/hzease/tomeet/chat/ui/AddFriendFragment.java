package com.hzease.tomeet.chat.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.InvitationsBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.InvitationsAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imageloader.utils.L;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class AddFriendFragment extends BaseFragment {

    @BindView(R.id.lv_phone_invitations_fmt)
    ListView lv_phone_invitations_fmt;

    public AddFriendFragment() {
    }

    public static AddFriendFragment newInstance() {
        return new AddFriendFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_add_friend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        PTApplication.getRequestService().getInviations(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InvitationsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InvitationsBean invitationsBean) {
                        initInvitations(invitationsBean.isSuccess(), invitationsBean.getData());
                    }
                });
    }

    private void initInvitations(boolean success, List<InvitationsBean.DataBean> data) {
        if (success) {
            lv_phone_invitations_fmt.setAdapter(new InvitationsAdapter(data, mContext));
        } else {
            ToastUtils.getToast(mContext, "网络链接错误");
        }
    }

    @OnClick({
            R.id.all_add_friend_fmt,
            R.id.all_add_friend_wechat_fmt
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_add_friend_fmt:
                Logger.e(PTApplication.myInfomation.getData().toString());
                if (TextUtils.isEmpty(PTApplication.myInfomation.getData().getPhone())) {
                    Logger.e("T");
                    startActivity(new Intent(getActivity(), ToBindPhoneActivity.class));
                } else {
                    Logger.e("T");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.REQUEST_CODE_CONTACT);
                    } else {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fl_content_add_friend_activity, AddPhoneContactsFragment.newInstance());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
            case R.id.all_add_friend_wechat_fmt:
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                    requestPermissions(mPermissionList, 123);
                }
                new ShareAction(getActivity())
                        .setDisplayList(SHARE_MEDIA.WEIXIN)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media != null) {
                                    UMWeb web = new UMWeb(AppConstants.TOMMET_SHARE_ADD_FRIEND + PTApplication.userId + "&origin=" + share_media.toString());
                                    web.setTitle("后会有期");
                                    web.setThumb(new UMImage(mContext, R.drawable.share_logo_200x200));
                                    web.setDescription("薛之谦的心愿是世界和平，我们的目标是拯救死宅！让我们成为好朋友吧！（通过此链接进入可直接成为蓝色好友）");
                                    new ShareAction(getActivity()).setPlatform(share_media).setCallback(new UMShareListener() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.REQUEST_CODE_CONTACT) {
            if (grantResults[0] == 0) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_content_add_friend_activity, AddPhoneContactsFragment.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                ToastUtils.getToast(mContext, "通讯录权限获取失败，无法读取联系人");
            }
        }
    }
}
