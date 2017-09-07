package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ui.ActiveInterfaceWebview;
import com.hzease.tomeet.data.AccountBean;
import com.hzease.tomeet.data.ActivityBean;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/30.
 */

public class ActivityAdapter extends StaticPagerAdapter {
    private List<ImageView> mImageViews;
    private List<ActivityBean.DataBean> mDatas;
    private Context context;
    private Activity activity;

    private String tempUrl;
    private String tempName;
    private String temPMessage;
    private String tempPhotoUrl;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public ActivityAdapter(List<ActivityBean.DataBean> mDatas, Context context, Activity activity) {
        this.mDatas = mDatas;
        this.context = context;
        mImageViews = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context)
                    .load(mDatas.get(i).getPhotoUrl())
                    .into(imageView);
            mImageViews.add(imageView);
        }
        this.activity = activity;
    }

    @Override
    public View getView(ViewGroup container, final int position) {

        ViewGroup parent = (ViewGroup) mImageViews.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        mImageViews.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Logger.e("postion" + position);
                //onItemClickLitener.onItemClick(mImageViews.get(position),position);
                Logger.e("postion" + position);
                if (PTApplication.myInfomation == null) {
                    ToastUtils.getToast("请先登录");
                } else {
                    PTApplication.getRequestService().isBind3Part(PTApplication.userToken, PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<MapDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("onError" + e.getMessage());
                                }

                                @Override
                                public void onNext(MapDataBean mapDataBean) {
                                    if (mapDataBean.isSuccess()) {
                                        Map<String, Boolean> data = mapDataBean.getData();
                                        Logger.e("WECHAT:" + data.get("WECHAT"));
                                        if (!data.get("WECHAT")) {
                                            initOutManPop(view);
                                        } else {
                                            Intent intent = new Intent(activity, ActiveInterfaceWebview.class);
                                            tempUrl = mDatas.get(position).getUrl();
                                            tempName = mDatas.get(position).getName();
                                            temPMessage = mDatas.get(position).getMessage();
                                            tempPhotoUrl = mDatas.get(position).getShareUrl();
                                            intent.putExtra("url", tempUrl);
                                            intent.putExtra("name", tempName);
                                            intent.putExtra("desc", temPMessage);
                                            intent.putExtra("photoUrl", tempPhotoUrl);
                                            activity.startActivity(intent);
                                        }
                                    } else {
                                        ToastUtils.getToast(mapDataBean.getMsg());
                                    }
                                }
                            });
                }
            }
        });
        return mImageViews.get(position);
    }

    private void initOutManPop(View view) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_outreason, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        activity.getWindow().setAttributes(wlBackground);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button istrue = contentView.findViewById(R.id.bt_outreason_true_fmt);
        Button cancel = contentView.findViewById(R.id.bt_outreason_cancel_fmt);
        TextView tv_outreason_reason_fmt = contentView.findViewById(R.id.tv_outreason_reason_fmt);
        tv_outreason_reason_fmt.setText("参加该活动需要绑定微信，请先绑定微信");
        istrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart：" + share_media.name());
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Logger.i("onComplete:WX   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                        PTApplication.getRequestService().bind3Part(PTApplication.userToken, "WECHAT", map.get("unionid"), PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {
                                        popupWindow.dismiss();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError" + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        Logger.e("boolean:" + noDataBean.isSuccess());
                                        Bind3Part(noDataBean.isSuccess(), noDataBean.getMsg());
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
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //绑定成功后
    private void Bind3Part(boolean success, String msg) {
        if (success) {
            ToastUtils.getToast("绑定成功");
            Intent intent = new Intent(activity, ActiveInterfaceWebview.class);
            intent.putExtra("url", tempUrl);
            intent.putExtra("name", tempName);
            intent.putExtra("desc", temPMessage);
            intent.putExtra("photoUrl", tempPhotoUrl);
            activity.startActivity(intent);
        } else {
            ToastUtils.getToast(msg);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
