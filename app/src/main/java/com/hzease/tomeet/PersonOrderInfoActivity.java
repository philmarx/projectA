package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.adapters.TurnsPicAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/4/6.
 */

public class PersonOrderInfoActivity extends NetActivity {


    int SEND_NOTE = 001;
    int EDIT_PIC = 002;
    int type = EDIT_PIC;
    private List<String> mLabels = new ArrayList<>();
    private int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    TagFlowLayout flowlayout_tabs;
    long userId;
    String mImage1 = "123";
    String mImage2 = "123";
    String mImage3 = "123";
    String mImage4 = "123";
    String mImage5 = "123";

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
                    Intent intent = new Intent(PersonOrderInfoActivity.this, ModifityPicActivity.class);
                    Logger.e("image1" + mImage1);
                    Logger.e("image2" + mImage2);
                    Logger.e("image3" + mImage3);
                    Logger.e("image4" + mImage4);
                    Logger.e("image5" + mImage5);
                    intent.putExtra("image1", mImage1);
                    intent.putExtra("image2", mImage2);
                    intent.putExtra("image3", mImage3);
                    intent.putExtra("image4", mImage4);
                    intent.putExtra("image5", mImage5);
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
        final NoteEditor content = (NoteEditor) contentView.findViewById(R.id.ne_smallpager_content_fmt);
        Button sendNote = (Button) contentView.findViewById(R.id.bt_smallpager_send_fmt);
        Button dismiss = (Button) contentView.findViewById(R.id.bt_smallpager_cancel_fmt);
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送纸条
                Logger.e(userId + "");
                PTApplication.getRequestService().sendNote(content.getText().toString().trim(), String.valueOf(userId), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                                Logger.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.getToast(PersonOrderInfoActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(PersonOrderInfoActivity.this, "传递纸条成功");
                                    popupWindow.dismiss();
                                } else {
                                    ToastUtils.getToast(PersonOrderInfoActivity.this, noDataBean.getMsg());
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personspace;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        flowlayout_tabs = (TagFlowLayout) findViewById(R.id.flowlayout_tabs);
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

    private void initPersonInfo() {
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
                        Logger.e("onNext" + userOrderBean.isSuccess());
                        if (userOrderBean.isSuccess()) {
                            List<String> avatarList = userOrderBean.getData().getAvatarList();
                            mImage1 = avatarList.get(1);
                            mImage2 = avatarList.get(2);
                            mImage3 = avatarList.get(3);
                            mImage4 = avatarList.get(4);
                            mImage5 = avatarList.get(5);
                            userOrderBean.getData().removeNullValue();
                            Map<String, String> imageSignatures = userOrderBean.getData().getImageSignatures();
                            mLabels = userOrderBean.getData().getLabels();
                            initLabelsAndName(mLabels, userOrderBean.getData().getNickname());
                            initViewList(imageSignatures, userOrderBean.getData().getId());
                            viewPager.setAdapter(new TurnsPicAdapter(mBitmaps, PersonOrderInfoActivity.this));
                        }
                    }
                });
    }

    /**
     * 加载图片集合
     */
    private void initViewList(Map<String, String> map, long userId) {
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String url = "/" + entry.getKey().replace("Signature", "");
            // Logger.e(url + "   " + userId);
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + userId + url)
                    .asBitmap()
                    .signature(new StringSignature(entry.getValue()))
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mBitmaps.add(resource);
                        }
                    });
        }
        Logger.e("mBitMaps" + mBitmaps.size());
    }

    /**
     * 加载标签
     *
     * @param mLabels
     */
    private void initLabelsAndName(final List<String> mLabels, String nickName) {
        tv_personspace_username_fmt.setText(nickName);
        tv_personspace_usernamebak_fmt.setText(nickName);
        flowlayout_tabs.setAdapter(new TagAdapter<String>(mLabels) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) View.inflate(PersonOrderInfoActivity.this, R.layout.labels_personspace, null);
                view.setText(mLabels.get(position));
                view.setBackgroundResource(mResources[position]);
                return view;
            }
        });
    }
}
