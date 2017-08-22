package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.hzease.tomeet.widget.adapters.PersonOrderAdapter;
import com.hzease.tomeet.widget.adapters.SpaceCircleAdapter;
import com.hzease.tomeet.widget.adapters.TurnsPicAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @BindView(R.id.viewPager)
    RollPagerView viewPager;
    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    @BindView(R.id.lv_personspace_order_fmt)
    RecyclerView lv_personspace_order_fmt;
    @BindView(R.id.tfl_personspace_labels_fmt)
    TagFlowLayout tfl_personspace_labels_fmt;
    @BindView(R.id.iv_me_space_sex)
    ImageView iv_me_space_sex;
    @BindView(R.id.tfl_spcae_circle_fmt)
    TagFlowLayout tfl_spcae_circle_fmt;
    @BindView(R.id.iv_me_isVip)
    ImageView iv_me_isVip;
    @BindView(R.id.tv_me_space_age)
    TextView tv_me_space_age;
    @BindView(R.id.ll_birthday_bg)
    LinearLayout ll_birthday_bg;
    @BindView(R.id.tv_constellation)
    TextView tv_constellation;
    private int SEND_NOTE = 0xaa;
    private int EDIT_PIC = 0xbb;
    private int type = EDIT_PIC;
    private String nickName;
    private long userId;
    private String avatarSignature;
    private int noteCount;
    private Bundle extras;
    private int vpWidth;


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
                        ToastUtils.getToast("请先登录");
                    }
                } else {
                    Intent modifityIntent = new Intent(this, ModifityPicActivity.class);
                    extras.putString("nickname", nickName);
                    modifityIntent.putExtras(extras);
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
        final AutoLinearLayout bg =  contentView.findViewById(R.id.all_props_bg_pop);
        TextView props =  contentView.findViewById(R.id.tv_count_fmt);
        Button useorbuy =  contentView.findViewById(R.id.bt_props_buyoruse_pop);
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
                                    ToastUtils.getToast("购买成功！");
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
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
        final NoteEditor content = contentView.findViewById(R.id.ne_smallpager_content_fmt);
        final TextView notesize = contentView.findViewById(R.id.tv_notesize_fmt);
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
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        CircleImageView head = contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(avatarSignature))
                .into(head);
        TextView name = contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(nickName);
        Button sendNote = contentView.findViewById(R.id.bt_smallpager_send_fmt);
        Button dismiss = contentView.findViewById(R.id.bt_smallpager_cancel_fmt);
        AutoLinearLayout all_isSendPapaer = contentView.findViewById(R.id.all_isSendPapaer);
        TextView paperMember = contentView.findViewById(R.id.tv_smallpaper_others);
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
                                ToastUtils.getToast(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast("传递纸条成功");
                                    popupWindow.dismiss();
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
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
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
      /*  rcv_spcae_circle_fmt.setLayoutManager(linearLayoutManager1);
        rcv_spcae_circle_fmt.addItemDecoration(new SpacesItemDecoration(10));*/
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
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vpWidth = viewPager.getMeasuredWidth();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
                params.height = vpWidth;
                viewPager.setLayoutParams(params);
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
                            Logger.e(userOrderBean.getData().getId()+"");
                            Logger.e(userOrderBean.toString());
                            // 在map未被改变前放入
                            extras = new Bundle();
                            for (int i = 1; i < 6; i++) {
                                String mapKey = "image" + i + "Signature";
                                extras.putString(mapKey, userOrderBean.getData().getImageSignatures().get(mapKey));
                            }

                            if (userOrderBean.getData().isGender()) {
                                iv_me_space_sex.setImageResource(R.drawable.newmale_icon);
                            } else {
                                iv_me_space_sex.setImageResource(R.drawable.newfemale_icon);
                            }

                            avatarSignature = userOrderBean.getData().getAvatarSignature();

                            nickName = userOrderBean.getData().getNickname();
                            initLabelsAndName(userOrderBean.getData().getLabels());

                            /*SpaceCircleAdapter circleAdapter = new SpaceCircleAdapter(userOrderBean.getData().getCircles(), PersonOrderInfoActivity.this);
                            rcv_spcae_circle_fmt.setAdapter(circleAdapter);
                            //点击圈子进入圈子详情
                            circleAdapter.setOnItemClickLitener(new SpaceCircleAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int postion) {
                                    Intent intent = new Intent(PersonOrderInfoActivity.this, CircleInfoActivity.class);
                                    intent.putExtra("circleId", userOrderBean.getData().getCircles().get(postion).getId());
                                    startActivity(intent);
                                }
                            });*/
                            tfl_spcae_circle_fmt.setAdapter(new TagAdapter<UserOrderBean.DataBean.CirclesBean>(userOrderBean.getData().getCircles()) {
                                @Override
                                public View getView(FlowLayout parent, int position, UserOrderBean.DataBean.CirclesBean s) {
                                    AutoLinearLayout view = (AutoLinearLayout) View.inflate(PersonOrderInfoActivity.this, R.layout.item_spacecircle, null);
                                    TextView circleName = view.findViewById(R.id.tv_space_circle_name_item);
                                    ImageView circleLevel = view.findViewById(R.id.iv_space_circle_lv_item);
                                    circleName.setText(userOrderBean.getData().getCircles().get(position).getName());
                                    int experience = userOrderBean.getData().getCircles().get(position).getExperience();
                                    if (experience >=100 && experience < 200){
                                        circleLevel.setImageResource(R.drawable.lv_1);
                                    }else if (experience >= 200 && experience <500){
                                        circleLevel.setImageResource(R.drawable.lv_2);
                                    }else if (experience >= 500 && experience < 1000){
                                        circleLevel.setImageResource(R.drawable.lv_3);
                                    }else if (experience >= 1000 && experience < 2000){
                                        circleLevel.setImageResource(R.drawable.lv_4);
                                    }else if (experience >= 2000){
                                        circleLevel.setImageResource(R.drawable.lv_5);
                                    }
                                    return view;
                                }
                            });
                            tfl_spcae_circle_fmt.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    Intent intent = new Intent(PersonOrderInfoActivity.this, CircleInfoActivity.class);
                                    intent.putExtra("circleId", userOrderBean.getData().getCircles().get(position).getId());
                                    startActivity(intent);
                                    return true;
                                }
                            });
                            initOrder(userOrderBean);
                            // 轮播图
                            viewPager.setAdapter(new TurnsPicAdapter(userOrderBean.getData().removeNullValue(), PersonOrderInfoActivity.this, userOrderBean.getData().getId()));
                            /*if (userOrderBean.getData().removeNullValue().size() == 1 || userOrderBean.getData().removeNullValue().size() == 0) {
                                *//*viewPager.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        return true;
                                    }
                                });
                                viewPager.setClickable(false);*//*
                                //viewPager.pause();
                                //.setEnabled(false);
                            }*/
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
                            String birthday = userOrderBean.getData().getBirthday();
                            extras.putString("birthday", birthday);
                            setAge(birthday);
                        } else {
                            ToastUtils.getToast(userOrderBean.getMsg());
                        }
                    }
                });
    }

    private void setAge(String birthday) {
        if (!birthday.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long birthdaytime = 0;
            try {
                birthdaytime = sdf.parse(birthday).getTime();
                int month = sdf.parse(birthday).getMonth() + 1;
                int day = sdf.parse(birthday).getDate();
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
     */
    private void initLabelsAndName(final List<String> mLabels) {
        tv_personspace_username_fmt.setText(nickName);
        tv_personspace_usernamebak_fmt.setText(nickName);
        //rcv_personspace_labels_fmt.setAdapter(new LabelsAdapter(mLabels, this));
        final int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};
        tfl_personspace_labels_fmt.setAdapter(new TagAdapter<String>(mLabels) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) View.inflate(PersonOrderInfoActivity.this, R.layout.labels_personspace, null);
                view.setText(mLabels.get(position));
                view.setBackgroundResource(mResources[position % 5]);
                return view;
            }
        });
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
