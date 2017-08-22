package com.hzease.tomeet.chat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.AccountBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/22.
 */

public class AddAccountFriendActivity extends NetActivity {
    @BindView(R.id.et_friend_accout_act)
    EditText et_friend_accout_act;
    @BindView(R.id.tv_search_accout_fmt)
    TextView tv_search_accout_fmt;
    @BindView(R.id.arl_account_info)
    AutoRelativeLayout arl_account_info;
    @BindView(R.id.civ_account_contact_icon)
    CircleImageView civ_account_contact_icon;
    @BindView(R.id.tv_account_contact_name)
    TextView tv_account_contact_name;
    private long userId;

    @OnClick({
            R.id.tv_search_accout_fmt,
            R.id.bt_account_contact_status
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_account_contact_status:
                PTApplication.getRequestService().sendInvitate(String.valueOf(userId),"账号搜索",PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                invitate(noDataBean.isSuccess(),noDataBean.getMsg());
                            }
                        });
                break;
            case R.id.tv_search_accout_fmt:
                final String account = et_friend_accout_act.getText().toString().trim();
                Logger.e("account" + account);
                if (account.length() < 5 || account.length() > 10) {
                    ToastUtils.getToast("请输入正确的用户账号");
                } else {
                    PTApplication.getRequestService().findByAccount(account)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<AccountBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("onError" + e.getMessage());
                                }

                                @Override
                                public void onNext(AccountBean accountBean) {
                                    if (accountBean.getData() == null) {
                                        ToastUtils.getToast("用户不存在");
                                    } else {
                                        //lv_accoutfriend_act.setAdapter(new AddAccountFriendAdapter(accountBean.getData(),AddAccountFriendActivity.this));
                                        userId = accountBean.getData().getId();
                                        arl_account_info.setVisibility(View.VISIBLE);
                                        //头像
                                        Glide.with(AddAccountFriendActivity.this)
                                                .load(AppConstants.YY_PT_OSS_USER_PATH + accountBean.getData().getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                                                .bitmapTransform(new CropCircleTransformation(AddAccountFriendActivity.this))
                                                .signature(new StringSignature(accountBean.getData().getAvatarSignature()))
                                                .into(civ_account_contact_icon);
                                        tv_account_contact_name.setText(accountBean.getData().getNickname());
                                    }
                                }
                            });
                }
                break;
        }
    }
    private void invitate(boolean success, String msg) {
        if (success){
            ToastUtils.getToast("发送请求成功");
            finish();
        }else{
            ToastUtils.getToast("发送请求失败");
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_addaccountfriend;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }
}
