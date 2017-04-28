package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.data.UserOrderBean;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.hzease.tomeet.widget.XCFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * Created by xuq on 2017/4/6.
 */

public class PersonOrderInfoActivity extends NetActivity {


    int SEND_NOTE = 001;
    int EDIT_PIC = 002;
    int type = EDIT_PIC;
    private String mNames[] = {
            "welcome", "android", "TextView",
            "apple", "jamy"};
    private int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    XCFlowLayout flowlayout_tabs;
    private int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
    long userId;
    String mImage1;
    String mImage2;
    String mImage3;
    String mImage4;
    String mImage5;

    private List<Bitmap> mBitmaps = new ArrayList<>();
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.tv_personspace_sendoredit_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_personspace_sendoredit_fmt:
                if (type == SEND_NOTE) {
                    initPopupWindow(v);
                } else {
                    Logger.e("编辑");
                    Intent intent = new Intent(PersonOrderInfoActivity.this,ModifityPicActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("image1",mImage1);
                    intent.putExtra("image2",mImage2);
                    intent.putExtra("image3",mImage3);
                    intent.putExtra("image4",mImage4);
                    intent.putExtra("image5",mImage5);
                    startActivity(intent);
                }
                break;
        }
    }

    private void initPopupWindow(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_smallpaper, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personspace;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) throws ExecutionException, InterruptedException {
        flowlayout_tabs = (XCFlowLayout) findViewById(R.id.flowlayout_tabs);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                width, height);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0;i< mNames.length;i++){
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            view.setTextSize(12);
            view.setGravity(Gravity.CENTER);
            view.setBackgroundDrawable(getResources().getDrawable(mResources[i]));
            flowlayout_tabs.addView(view,lp);
        }
        Bundle bundle = this.getIntent().getExtras();
        userId = bundle.getLong("userId");
        Logger.e(userId + "");
        if (String.valueOf(userId).equals(PTApplication.userId)) {
            type = EDIT_PIC;
            tv_personspace_sendoredit_fmt.setText("编辑");
        } else {
            type = SEND_NOTE;
            tv_personspace_sendoredit_fmt.setText("传纸条");
        }
        //加载个人信息
        initPersonInfo();

    }

    private void initPersonInfo() throws ExecutionException, InterruptedException {
        PTApplication.getRequestService().getOrderById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserOrderBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(final UserOrderBean userOrderBean) {
                        Logger.e("onNext");
                        if (userOrderBean.isSuccess()){
                            tv_personspace_username_fmt.setText(userOrderBean.getData().getNickname());
                            tv_personspace_usernamebak_fmt.setText(userOrderBean.getData().getNickname());
                            mImage1 = userOrderBean.getData().getImageSignatures().get("image1Signature");
                            mImage2 = userOrderBean.getData().getImageSignatures().get("image2Signature");
                            mImage3 = userOrderBean.getData().getImageSignatures().get("image3Signature");
                            mImage4 = userOrderBean.getData().getImageSignatures().get("image4Signature");
                            mImage5 = userOrderBean.getData().getImageSignatures().get("image5Signature");
                            viewPager.setAdapter(new PagerAdapter() {
                                @Override
                                public int getCount() {
                                  /*  new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadBitmaps(userOrderBean);
                                        }
                                    }).start();
                                    userOrderBean.getData().removeNullValue();
                                    return userOrderBean.getData().getImageSignatures().size() + 1;*/
                                    return 1;
                                }

                                @Override
                                public boolean isViewFromObject(View view, Object object) {
                                    return view==object;
                                }

                                @Override
                                public Object instantiateItem(ViewGroup container, int position) {
                                    ImageView imageView = new ImageView(PersonOrderInfoActivity.this);
                                    imageView.setImageBitmap(mBitmaps.get(position));
                                    if (position == 1) {
                                        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.xiangsiyin));
                                    }
                                    return imageView;
                                }

                                @Override
                                public void destroyItem(ViewGroup container, int position, Object object) {
                                    container.removeView((View) object);
                                }
                            });


                        }
                    }
                });
    }

    private void loadBitmaps(UserOrderBean userOrderBean) {
        if (!userOrderBean.getData().getAvatarSignature().isEmpty()) {
            try {
                mBitmaps.add(Glide.with(this)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR)
                        .asBitmap()
                        .centerCrop()
                        .signature(new StringSignature(userOrderBean.getData().getAvatarSignature()))
                        .into(1080, 1080)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (!userOrderBean.getData().getImageSignatures().isEmpty()) {
            for (Map.Entry<String, String> entry : userOrderBean.getData().getImageSignatures().entrySet()) {
                try {

                    mBitmaps.add(Glide.with(PTApplication.getInstance())
                            .load(AppConstants.YY_PT_OSS_USER_PATH + userId + entry.getKey().replaceFirst("Signature", ""))
                            .asBitmap()
                            .centerCrop()
                            .signature(new StringSignature(entry.getValue()))
                            .into(750,750)
                            .get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }


        /*//加载第一张照片
        if (!userOrderBean.getData().getAvatarSignature().isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //加载第二张照片
        if (!userOrderBean.getData().getImageSignatures().get("image1Signature").isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_IMAGE1)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //加载第三张照片
        if (!userOrderBean.getData().getImageSignatures().get("image2Signature").isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_IMAGE2)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //加载第四张照片
        if (!userOrderBean.getData().getImageSignatures().get("image3Signature").isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_IMAGE3)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //加载第五种照片
        if (!userOrderBean.getData().getImageSignatures().getImage1Signature().isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_IMAGE4)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //加载第六张照片
        if (!userOrderBean.getData().getImageSignatures().getImage1Signature().isEmpty()){
            try {
                mBitmaps.add(Glide.with(PTApplication.getInstance())
                        .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_IMAGE5)
                        .asBitmap()
                        .centerCrop()
                        .into(750,750)
                        .get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }*/
    }
}
