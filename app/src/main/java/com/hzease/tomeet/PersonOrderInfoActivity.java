package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.circle.ui.CircleInfoActivity;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.LabelsAdapter;
import com.hzease.tomeet.widget.adapters.PersonOrderAdapter;
import com.hzease.tomeet.widget.adapters.SpaceCircleAdapter;
import com.hzease.tomeet.widget.adapters.TurnsPicAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.rcv_personspace_labels_fmt)
    RecyclerView rcv_personspace_labels_fmt;
    @BindView(R.id.iv_me_space_sex)
    ImageView iv_me_space_sex;
    @BindView(R.id.rcv_spcae_circle_fmt)
    RecyclerView rcv_spcae_circle_fmt;
    @BindView(R.id.iv_me_isVip)
    ImageView iv_me_isVip;
    @BindView(R.id.tv_me_space_age)
    TextView tv_me_space_age;
    @BindView(R.id.ll_birthday_bg)
    LinearLayout ll_birthday_bg;
    @BindView(R.id.tv_constellation)
    TextView tv_constellation;
    String nickName;
    private long userId;
    private String avatarSignature;
    private Intent modifityIntent;
    private String birthday;
    private int month;
    private int day;
    private int noteCount;


    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @OnClick({
            R.id.tv_personspace_sendoredit_fmt,
    })
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_personspace_sendoredit_fmt:
                if (type == SEND_NOTE) {
                    if (PTApplication.myInfomation != null) {
                        PTApplication.getRequestService().findPropsMum(PTApplication.userToken, PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<PropsMumBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(PropsMumBean propsMumBean) {
                                        Logger.e("noteCount " + propsMumBean.getData().getNoteCount());
                                        if (propsMumBean.getData().getNoteCount() > 0) {
                                            noteCount = propsMumBean.getData().getNoteCount();
                                            initPopupWindow(v, noteCount);
                                        } else {
                                            initBuySmallPaper(v);
                                        }
                                    }
                                });
                    } else {
                        ToastUtils.getToast(this, "请先登录");
                    }
                } else {
                    modifityIntent.putExtra("nickname", nickName);
                    modifityIntent.putExtra("birthday", birthday);
                    startActivity(modifityIntent);
                }
                break;
        }
    }

    //购买小纸条
    private void initBuySmallPaper(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_useprops, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        final AutoLinearLayout bg = (AutoLinearLayout) contentView.findViewById(R.id.all_props_bg_pop);
        TextView props = (TextView) contentView.findViewById(R.id.tv_count_fmt);
        Button useorbuy = (Button) contentView.findViewById(R.id.bt_props_buyoruse_pop);
        bg.setBackgroundResource(R.drawable.smallpaper_notenough);
        props.setVisibility(View.GONE);
        useorbuy.setText("购买");
        useorbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PTApplication.getRequestService().buyProp(1, PTApplication.userToken, 0, PTApplication.userId)
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
                                    popupWindow.dismiss();
                                    initPopupWindow(v, 1);
                                    ToastUtils.getToast(PersonOrderInfoActivity.this,"购买成功！");
                                }else{
                                    ToastUtils.getToast(PersonOrderInfoActivity.this,noDataBean.getMsg());
                                }
                            }
                        });
            }
        });
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        Button cancel = (Button) contentView.findViewById(R.id.bt_props_cancel_pop);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void initPopupWindow(View view, int count) {
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
        final TextView notesize = (TextView) contentView.findViewById(R.id.tv_notesize_fmt);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                notesize.setText(content.length() + "/68");
            }
        });
        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
            }
        });
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
        AutoLinearLayout all_isSendPapaer = (AutoLinearLayout) contentView.findViewById(R.id.all_isSendPapaer);
        TextView paperMember = (TextView) contentView.findViewById(R.id.tv_smallpaper_others);
        paperMember.setText("剩余X" + count);
        all_isSendPapaer.setVisibility(View.VISIBLE);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_personspace_labels_fmt.setLayoutManager(linearLayoutManager);
        rcv_personspace_labels_fmt.addItemDecoration(new SpacesItemDecoration(10));
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_spcae_circle_fmt.setLayoutManager(linearLayoutManager1);
        rcv_spcae_circle_fmt.addItemDecoration(new SpacesItemDecoration(10));
        Bundle bundle = this.getIntent().getExtras();
        userId = bundle.getLong("userId");
        //Logger.e(userId + "");
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
                            Logger.e(userOrderBean.toString());
                            if (userOrderBean.getData().isGender()) {
                                iv_me_space_sex.setImageResource(R.drawable.newmale_icon);
                            } else {
                                iv_me_space_sex.setImageResource(R.drawable.newfemale_icon);
                            }
                            List<String> avatarList = userOrderBean.getData().getAvatarList();
                            for (int i = 0; i < avatarList.size(); i++) {
                                mImages.add(avatarList.get(i));
                                Logger.e("avatarList遍历：" + i + "  : " + avatarList.get(i));
                            }
                            for (int i = 1; i < 6; i++) {
                                String mapKey = "image" + i + "Signature";
                                modifityIntent.putExtra(mapKey, userOrderBean.getData().getImageSignatures().get(mapKey));
                            }
                            avatarSignature = userOrderBean.getData().getAvatarSignature();

                            mLabels = userOrderBean.getData().getLabels();
                            initLabelsAndName(mLabels, userOrderBean.getData().getNickname());
                            SpaceCircleAdapter circleAdapter = new SpaceCircleAdapter(userOrderBean.getData().getCircles(), PersonOrderInfoActivity.this);
                            rcv_spcae_circle_fmt.setAdapter(circleAdapter);
                            //点击圈子进入圈子详情
                            circleAdapter.setOnItemClickLitener(new SpaceCircleAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int postion) {
                                    Intent intent = new Intent(PersonOrderInfoActivity.this, CircleInfoActivity.class);
                                    intent.putExtra("circleId",userOrderBean.getData().getCircles().get(postion).getId());
                                    startActivity(intent);
                                }
                            });
                            initOrder(userOrderBean);

                            // 轮播图
                            viewPager.setAdapter(new TurnsPicAdapter(userOrderBean.getData().removeNullValue(), PersonOrderInfoActivity.this, userOrderBean.getData().getId()));

                            if (userOrderBean.getData().isVip()) {
                                iv_me_isVip.setVisibility(View.VISIBLE);
                            } else {
                                iv_me_isVip.setVisibility(View.GONE);
                            }
                            if (userOrderBean.getData().isGender()) {
                                ll_birthday_bg.setBackgroundResource(R.drawable.shape_space_birth_male);
                            } else {
                                ll_birthday_bg.setBackgroundResource(R.drawable.shape_space_birth_female);
                            }
                        }
                        birthday = userOrderBean.getData().getBirthday();
                        setAge(birthday);
                    }
                });
    }

    private void setAge(String birthday) {
        if (!birthday.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long birthdaytime = 0;
            try {
                birthdaytime = sdf.parse(birthday).getTime();
                month = sdf.parse(birthday).getMonth() + 1;
                day = sdf.parse(birthday).getDate();
                getAstro(month, day);
                long now = System.currentTimeMillis();
                int age = (int) ((now - birthdaytime) / 365 / 24 / 60 / 60 / 1000);
                tv_me_space_age.setText(String.valueOf(age));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            tv_me_space_age.setText("0");
        }
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
        //Logger.e("onResume");
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
        rcv_personspace_labels_fmt.setAdapter(new LabelsAdapter(mLabels, this));
    }

    private void getAstro(int month, int day) {
        //Logger.e("month" + month);
        //Logger.e("month" + day);
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        tv_constellation.setText(astro[index]);
        //Logger.e("星座" + astro[index]);
        // 返回索引指向的星座string
    }
}
