package com.hzease.tomeet.me.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseActivity;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.Bind3Part;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.rong.imkit.RongIM;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.http.POST;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.hzease.tomeet.utils.ChString.To;
import static com.hzease.tomeet.utils.ChString.type;

/**
 * Created by xuq on 2017/6/23.
 */

public class BindOthersFragment extends BaseFragment {

    @BindView(R.id.tv_setting_bindingwechat_fmt)
    TextView tv_setting_bindingwechat_fmt;
    @BindView(R.id.rl_bind_wechat_fmt)
    AutoRelativeLayout rl_bind_wechat_fmt;
    @BindView(R.id.tv_setting_bindingqq_fmt)
    TextView tv_setting_bindingqq_fmt;
    @BindView(R.id.rl_bind_qq_fmt)
    AutoRelativeLayout rl_bind_qq_fmt;
    @BindView(R.id.tv_binding_weibo_fmt)
    TextView tv_binding_weibo_fmt;
    @BindView(R.id.rl_bind_weibo_fmt)
    AutoRelativeLayout rl_bind_weibo_fmt;

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private String mAvatarUrl;
    private String mNickName;
    private boolean mGender;
    private boolean isFrom3Part;

    @OnClick({
            R.id.rl_bind_wechat_fmt,
            R.id.rl_bind_qq_fmt,
            R.id.rl_bind_weibo_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bind_wechat_fmt:
                if (!tv_setting_bindingwechat_fmt.getText().toString().equals("已绑定")) {
                    //去绑定
                    // 转圈
                    //绑定微信
                    UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            Logger.e("onStart：" + share_media.name());
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            Logger.i("onComplete:WX   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                            mAvatarUrl = map.get("iconurl");
                            mNickName = map.get("name");
                            mGender = "男".equals(map.get("gender"));
                            PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, "WECHAT", PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Action0() {
                                        @Override
                                        public void call() {
                                            showLoadingDialog();
                                        }
                                    })
                                    .doAfterTerminate(new Action0() {
                                        @Override
                                        public void call() {
                                            hideLoadingDialog();
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("onError" + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            if (noDataBean.isSuccess()) {
                                                Logger.e("保存三方信息成功");
                                            } else {
                                                ToastUtils.getToast(noDataBean.getMsg());
                                            }
                                        }
                                    });
                            PTApplication.getRequestService().bind3Part(PTApplication.userToken, "WECHAT", map.get("unionid"), PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Action0() {
                                        @Override
                                        public void call() {
                                            showLoadingDialog();
                                        }
                                    })
                                    .doAfterTerminate(new Action0() {
                                        @Override
                                        public void call() {
                                            hideLoadingDialog();
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Bind3Part>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Bind3Part noDataBean) {
                                            Bind3Part(noDataBean,"WECHAT");
                                        }
                                    });

                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            Logger.e("onError: " + throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Logger.e("onCancel: " + share_media.toString());
                        }
                    });
                } else {
                    //去解绑微信
                    Logger.e("123123123");
                    UnboundFragment unboundFragment = UnboundFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("boundtype", "微信号");
                    bundle.putString("type", "WECHAT");
                    unboundFragment.setArguments(bundle);
                    transaction.replace(R.id.fl_content_me_activity, unboundFragment);
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.rl_bind_qq_fmt:
                if (!tv_setting_bindingqq_fmt.getText().toString().equals("已绑定")) {
                    //绑定QQ
                    UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            Logger.e("onStart: " + share_media.toString());
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            Logger.i("onComplete:QQ   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                            mAvatarUrl = map.get("iconurl");
                            mNickName = map.get("name");
                            mGender = "男".equals(map.get("gender"));
                            PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, "QQ", PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Action0() {
                                        @Override
                                        public void call() {
                                            showLoadingDialog();
                                        }
                                    })
                                    .doAfterTerminate(new Action0() {
                                        @Override
                                        public void call() {
                                            hideLoadingDialog();
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<NoDataBean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Logger.e("onError" + e.getMessage());
                                        }

                                        @Override
                                        public void onNext(NoDataBean noDataBean) {
                                            if (noDataBean.isSuccess()) {
                                                Logger.e("保存三方信息成功");
                                            } else {
                                                ToastUtils.getToast(noDataBean.getMsg());
                                            }
                                        }
                                    });
                            PTApplication.getRequestService().bind3Part(PTApplication.userToken, "QQ", map.get("openid"), PTApplication.userId)
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe(new Action0() {
                                        @Override
                                        public void call() {
                                            showLoadingDialog();
                                        }
                                    })
                                    .doAfterTerminate(new Action0() {
                                        @Override
                                        public void call() {
                                            hideLoadingDialog();
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Bind3Part>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Bind3Part noDataBean) {
                                            Bind3Part(noDataBean,"QQ");
                                        }
                                    });
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            Logger.e("onError");
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Logger.e("onCancel");
                        }
                    });
                } else {
                    Logger.e("123123123");
                    UnboundFragment unboundFragment = UnboundFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("boundtype", "QQ号");
                    bundle.putString("type", "QQ");
                    unboundFragment.setArguments(bundle);
                    transaction.replace(R.id.fl_content_me_activity, unboundFragment);
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
            case R.id.rl_bind_weibo_fmt:
                ToastUtils.getToast("暂未开通");
                break;
        }
    }

    //绑定成功后
    private void Bind3Part(Bind3Part mapDataBean,String type) {
        if (mapDataBean.isSuccess()) {
            ToastUtils.getToast("绑定成功");
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            if (mapDataBean.getMsg().startsWith("可合并")) {
                //可合并
                initMergePopupWindos(mRootView, mapDataBean);
            } else {
                //可强制解除
                initForcePopupWindos(mRootView, mapDataBean,type);
            }
        }
    }

    //可合并弹窗
    private void initMergePopupWindos(final View v, final Bind3Part mapDataBean) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_mergeaccout, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        CircleImageView head1 = contentView.findViewById(R.id.civ_head_1);
        CircleImageView head2 = contentView.findViewById(R.id.civ_head_2);
        final TextView nickName1 = contentView.findViewById(R.id.tv_nickname_1);
        TextView nickName2 = contentView.findViewById(R.id.tv_nickname_2);
        final CheckBox cb1 = contentView.findViewById(R.id.cb_1);
        final CheckBox cb2 = contentView.findViewById(R.id.cb_2);
        nickName1.setText(PTApplication.myInfomation.getData().getNickname());
        nickName2.setText(mapDataBean.getData().getNickname2());
        Button toMarge = contentView.findViewById(R.id.bt_marge);
        Button cancel = contentView.findViewById(R.id.bt_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                .into(head2);
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(head1);
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb2.setChecked(false);
                    isFrom3Part = false;
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cb1.setChecked(false);
                    isFrom3Part = true;
                }
            }
        });
        toMarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                initPwdPopupWindos(v, mapDataBean, true,"");
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void initForcePwdPopupWindos(View v, Bind3Part mapDataBean,boolean isMager) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_force_pwd, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        TextView nickName = contentView.findViewById(R.id.tv_forceunbound_nickname_pop);
        CircleImageView avatar = contentView.findViewById(R.id.civ_forceunbound_avatar_pop);
        Button sure = contentView.findViewById(R.id.btn_pos);
        Button cancel = contentView.findViewById(R.id.btn_neg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        if (isMager){
            if (isFrom3Part) {
                nickName.setText(PTApplication.myInfomation.getData().getNickname());
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(avatar);
            } else {
                //昵称
                nickName.setText(String.valueOf(mapDataBean.getData().getNickname2()));
                //头像
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                        .into(avatar);
            }
        }else{
            //昵称
            nickName.setText(String.valueOf(mapDataBean.getData().getNickname2()));
            //头像
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                    .into(avatar);
        }

        //设置PopupWindow进入和退出动画
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    //输入密码弹窗
    private void initPwdPopupWindos(final View v, final Bind3Part mapDataBean, final boolean isMarge, final String type) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_merge_enterpwd, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        CircleImageView avatar = contentView.findViewById(R.id.civ_accountavatar_merge_pop);
        TextView nickName = contentView.findViewById(R.id.tv_nickname_merge_pop);
        final EditText pwd = contentView.findViewById(R.id.et_password_merge_pop);
        Button toforceBind = contentView.findViewById(R.id.bt_merge_sure);
        ImageView cancel = contentView.findViewById(R.id.iv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        if (isMarge) {
            if (isFrom3Part) {
                nickName.setText(PTApplication.myInfomation.getData().getNickname());
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(avatar);
            } else {
                //昵称
                nickName.setText(String.valueOf(mapDataBean.getData().getNickname2()));
                //头像
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                        .into(avatar);
            }
        } else {
            //昵称
            nickName.setText(String.valueOf(mapDataBean.getData().getNickname2()));
            //头像
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                    .into(avatar);
        }

        toforceBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pwd.getText().toString().trim();
                String fromId = "";
                String toId = "";
                if (isMarge){
                    //合并
                    if (isFrom3Part){
                        fromId = PTApplication.userId;
                        toId = String.valueOf(mapDataBean.getData().getUserId2());
                    }else{
                        fromId = String.valueOf(mapDataBean.getData().getUserId2());
                        toId = PTApplication.userId;
                    }
                    PTApplication.getRequestService().newMarge(fromId,toId,password,PTApplication.userToken,PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("onError" + e.getMessage());
                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    if (noDataBean.isSuccess()){
                                        popupWindow.dismiss();
                                        ToastUtils.getToast("绑定成功，请重新登录");
                                        logoutUser();
                                        startActivity(new Intent(getActivity(),LoginActivity.class));
                                        getActivity().finish();
                                    }else{
                                        popupWindow.dismiss();
                                        initForcePwdPopupWindos(v,mapDataBean,true);
                                    }
                                }
                            });
                }else{
                    //强制绑定
                    PTApplication.getRequestService().forceUnbind(String.valueOf(mapDataBean.getData().getUserId2()), PTApplication.userId, password, PTApplication.userToken,type, PTApplication.userId)
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
                                    if (noDataBean.isSuccess()) {
                                        ToastUtils.getToast("绑定成功");
                                        popupWindow.dismiss();
                                    } else {
                                        Logger.e(noDataBean.getMsg());
                                        popupWindow.dismiss();
                                        initForcePwdPopupWindos(v, mapDataBean,false);
                                    }
                                }
                            });
                }

            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    public void logoutUser() {
        // 注销个人信息
        PTApplication.myInfomation = null;
        PTApplication.userId = "";
        PTApplication.userToken = "";
        Realm.removeDefaultConfiguration();
        // 清空本地保存
        saveUserIdAndToken();
        // 注销融云
        if (PTApplication.isRongCloudInit) {
            RongIM.getInstance().logout();
            PTApplication.isRongCloudInit = false;
        }
        // 注销阿里云OSS
        PTApplication.aliyunOss = null;
        PTApplication.aliyunOssExpiration = 0;
        // 停止发送友盟用户信息
        MobclickAgent.onProfileSignOff();

        // 移除未读监听
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(PTApplication.unReadMessageObserver);
        PTApplication.badge.setBadgeNumber(0);
    }

    public void saveUserIdAndToken() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(AppConstants.TOMMET_SHARED_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("userId", String.valueOf(PTApplication.userId));
        editor.putString("userToken", PTApplication.userToken);
        editor.apply();
        Logger.d("保存成功");
    }
    //强制解除
    private void initForcePopupWindos(final View v, final Bind3Part mapDataBean, final String type) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_forceunbound, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        TextView nickName = contentView.findViewById(R.id.tv_forceunbound_nickname_pop);
        CircleImageView avatar = contentView.findViewById(R.id.civ_forceunbound_avatar_pop);
        Button sure = contentView.findViewById(R.id.btn_pos);
        Button cancel = contentView.findViewById(R.id.btn_neg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                initPwdPopupWindos(v, mapDataBean, false,type);
            }
        });
        //昵称
        nickName.setText(String.valueOf(mapDataBean.getData().getNickname2()));
        //头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mapDataBean.getData().getUserId2() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(String.valueOf(mapDataBean.getData().getAvatarSignature2())))
                .into(avatar);
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }


    public static BindOthersFragment newInstance() {
        return new BindOthersFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_bindothers;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        PTApplication.getRequestService().isBind3Part(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoadingDialog();
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        hideLoadingDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MapDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MapDataBean mapDataBean) {
                        if (mapDataBean.isSuccess()) {
                            Map<String, Boolean> data = mapDataBean.getData();
                            Logger.e(data.toString());
                            for (String key : data.keySet()) {
                                switch (key) {
                                    case "QQ":
                                        if (data.get(key)) {
                                            tv_setting_bindingqq_fmt.setText("已绑定");
                                        }
                                        break;
                                    case "WEIBO":
                                        if (data.get(key)) {
                                            tv_binding_weibo_fmt.setText("已绑定");
                                        }
                                        break;
                                    case "WECHAT":
                                        if (data.get(key)) {
                                            tv_setting_bindingwechat_fmt.setText("已绑定");
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    public void showLoadingDialog() {
        ((BaseActivity) getActivity()).showLoadingDialog();
    }

    public void hideLoadingDialog() {
        ((BaseActivity) getActivity()).hideLoadingDialog();
    }
}
