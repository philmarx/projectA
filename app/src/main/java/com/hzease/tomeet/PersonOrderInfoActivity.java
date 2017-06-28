package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.adapters.PersonOrderAdapter;
import com.hzease.tomeet.widget.adapters.TurnsPicAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
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
    private List<String> mImages = new ArrayList<>();
    private int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    @BindView(R.id.lv_personspace_order_fmt)
    RecyclerView lv_personspace_order_fmt;
    TagFlowLayout flowlayout_tabs;
    String nickName;
    private long userId;
    private String avatarSignature;
    private Intent modifityIntent;


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
                    if (PTApplication.myInfomation != null) {
                        initPopupWindow(v);
                    }else{
                        ToastUtils.getToast(this,"请先登录");
                    }
                } else {
                    modifityIntent.putExtra("nickname", nickName);
                    startActivity(modifityIntent);
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//解决在华为上背景不透明的bug
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        final NoteEditor content = (NoteEditor) contentView.findViewById(R.id.ne_smallpager_content_fmt);
        CircleImageView head = (CircleImageView) contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(avatarSignature))
                .into(head);
        TextView name = (TextView) contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(nickName);
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
        modifityIntent = new Intent(this, ModifityPicActivity.class);
        /*mImages.add(mImage0);
        mImages.add(mImage1);
        mImages.add(mImage2);
        mImages.add(mImage3);
        mImages.add(mImage4);
        mImages.add(mImage5);*/
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(final UserOrderBean userOrderBean) {
                        if (userOrderBean.isSuccess()) {
                            List<String> avatarList = userOrderBean.getData().getAvatarList();
                            for (int i = 0; i < avatarList.size(); i++) {
                                mImages.add(avatarList.get(i));
                                Logger.e(i + ": " + avatarList.get(i));
                            }
                            for (int i = 1; i < 6; i++) {
                                String mapKey = "image" + i + "Signature";
                                modifityIntent.putExtra(mapKey, userOrderBean.getData().getImageSignatures().get(mapKey));
                            }
                            avatarSignature = userOrderBean.getData().getAvatarSignature();
                            userOrderBean.getData().removeNullValue();
                            Map<String, String> imageSignatures = userOrderBean.getData().getImageSignatures();
                            mLabels = userOrderBean.getData().getLabels();
                            Logger.e("mLabels:" + mLabels.toString());
                            initLabelsAndName(mLabels, userOrderBean.getData().getNickname());
                            initOrder(userOrderBean);
                            viewPager.setAdapter(new TurnsPicAdapter(imageSignatures, PersonOrderInfoActivity.this, userOrderBean.getData().getId()));
                        }
                    }
                });
    }

    /**
     * 加载排名
     *
     * @param userOrderBean
     */
    private void initOrder(UserOrderBean userOrderBean) {
        lv_personspace_order_fmt.setLayoutManager(new LinearLayoutManager(this));
        lv_personspace_order_fmt.setAdapter(new PersonOrderAdapter(userOrderBean.getData().getOrders(), PersonOrderInfoActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("onResume");
        initPersonInfo();
    }

    /**
     * 加载标签
     *
     * @param mLabels
     */
    private void initLabelsAndName(final List<String> mLabels, String nickName) {
        this.nickName = nickName;
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
