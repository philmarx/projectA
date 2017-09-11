package com.hzease.tomeet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.GlideRoundTransform;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/4/26.
 */

public class ModifityPicActivity extends TakePhotoActivity {
    @BindView(R.id.iv_pic_head_aty)
    ImageView iv_pic_head_aty;
    @BindView(R.id.iv_pic_two_aty)
    ImageView iv_pic_two_aty;
    @BindView(R.id.iv_pic_three_aty)
    ImageView iv_pic_three_aty;
    @BindView(R.id.iv_pic_four_aty)
    ImageView iv_pic_four_aty;
    @BindView(R.id.iv_pic_five_aty)
    ImageView iv_pic_five_aty;
    @BindView(R.id.iv_pic_six_aty)
    ImageView iv_pic_six_aty;
    @BindView(R.id.tv_modifitypic_name_aty)
    TextView tv_modifitypic_name_aty;
    @BindView(R.id.tv_modifity_nickName_fmt)
    TextView tv_modifity_nickName_fmt;
    @BindView(R.id.tv_modifity_age_fmt)
    TextView tv_modifity_age_fmt;

    String mImage1;
    String mImage2;
    String mImage3;
    String mImage4;
    String mImage5;

    private String nickName;
    private String birthday;

    @OnClick({
            R.id.iv_pic_head_aty,
            R.id.iv_pic_two_aty,
            R.id.iv_pic_three_aty,
            R.id.iv_pic_four_aty,
            R.id.iv_pic_five_aty,
            R.id.iv_pic_six_aty,
            R.id.rl_moditity_setAge_fmt,
            R.id.rl_moditity_setNickName_fmt,
            R.id.iv_back
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic_head_aty:
            case R.id.iv_pic_two_aty:
            case R.id.iv_pic_three_aty:
            case R.id.iv_pic_four_aty:
            case R.id.iv_pic_five_aty:
            case R.id.iv_pic_six_aty:
                takePhotoPopupWindow(v.getId());
                break;
            case R.id.rl_moditity_setAge_fmt:
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long age = 0;
                        try {
                            long birthday = sdf.parse(dateDesc).getTime();
                            long now = System.currentTimeMillis();
                            age = (now - birthday) / 365 / 24 / 60 / 60 / 1000;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tv_modifity_age_fmt.setText(String.valueOf(age));
                        PTApplication.getRequestService().updateBirthday(dateDesc, PTApplication.userId, PTApplication.userToken)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (!noDataBean.isSuccess()) {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(22) // pick view text size
                        .colorCancel(Color.parseColor("#b8b8b8")) //color of cancel button
                        .colorConfirm(Color.parseColor("#03b5e3"))//color of confirm button
                        .minYear(1900) //min year in loop
                        .maxYear(2010) // max year in loop
                        //.showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                        .dateChose(birthday) // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(this);
                break;
            case R.id.rl_moditity_setNickName_fmt:
                initAskChangePop(v);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @OnLongClick({
            R.id.iv_pic_head_aty,
            R.id.iv_pic_two_aty,
            R.id.iv_pic_three_aty,
            R.id.iv_pic_four_aty,
            R.id.iv_pic_five_aty,
            R.id.iv_pic_six_aty
    })
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pic_head_aty:
                ToastUtils.getToast("头像不能删除哦~");
                break;
            case R.id.iv_pic_two_aty:
                if (!mImage1.isEmpty()) {
                    initOutManPop(view, view.getId(), 1);
                }
                break;
            case R.id.iv_pic_three_aty:
                if (!mImage2.isEmpty()) {
                    initOutManPop(view, view.getId(), 2);
                }
                break;
            case R.id.iv_pic_four_aty:
                if (!mImage3.isEmpty()) {
                    initOutManPop(view, view.getId(), 3);
                }
                break;
            case R.id.iv_pic_five_aty:
                if (!mImage4.isEmpty()) {
                    initOutManPop(view, view.getId(), 4);
                }
                break;
            case R.id.iv_pic_six_aty:
                if (!mImage5.isEmpty()) {
                    initOutManPop(view, view.getId(), 5);
                }
                break;
        }
        return true;
    }

    //弹出踢人理由
    private void initOutManPop(View v, final int ImageViewID, final int witch) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_outreason, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button istrue = contentView.findViewById(R.id.bt_outreason_true_fmt);
        Button cancel = contentView.findViewById(R.id.bt_outreason_cancel_fmt);
        TextView tv_outreason_reason_fmt = contentView.findViewById(R.id.tv_outreason_reason_fmt);
        tv_outreason_reason_fmt.setText("是否删除该相片");
        istrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                PTApplication.getRequestService().updateImageSignature(PTApplication.userId, PTApplication.userToken, "image" + witch, "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    ((ImageView) (findViewById(ImageViewID))).setImageResource(R.drawable.upload_photo_small);
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
                                }
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
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void initAskChangePop(View v) {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_askchangename, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        final Button dismiss = contentView.findViewById(R.id.bt_changename_cancel_pop);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Button success = contentView.findViewById(R.id.bt_changename_sure_pop);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                popupWindow.dismiss();
                PTApplication.getRequestService().findPropsMum(PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭转圈
                                hideLoadingDialog();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                // 转圈
                                showLoadingDialog();
                            }
                        })
                        .subscribe(new Subscriber<PropsMumBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(PropsMumBean propsMumBean) {
                                if (propsMumBean.isSuccess()) {
                                    if (propsMumBean.getData().getChangeNicknameCount() > 0) {
                                        initChangePop(v);
                                    } else {
                                        initBuyChangeName(v);
                                    }
                                } else {
                                    ToastUtils.getToast(propsMumBean.getMsg());
                                }
                            }
                        });
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    //购买改名卡
    private void initBuyChangeName(View v) {
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
        bg.setBackgroundResource(R.drawable.changname_notenough);
        props.setVisibility(View.GONE);
        useorbuy.setText("购买");
        useorbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PTApplication.getRequestService().buyProp(1, PTApplication.userToken, 2, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭转圈
                                hideLoadingDialog();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                // 转圈
                                showLoadingDialog();
                            }
                        })
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                    initChangePop(v);
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
        Button cancel = contentView.findViewById(R.id.bt_props_cancel_pop);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_moditifypic;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();

        mImage1 = bundle.getString("image1Signature", "");
        mImage2 = bundle.getString("image2Signature", "");
        mImage3 = bundle.getString("image3Signature", "");
        mImage4 = bundle.getString("image4Signature", "");
        mImage5 = bundle.getString("image5Signature", "");
        Logger.e("mImage1" + mImage1);
        Logger.e("mImage2" + mImage2);
        Logger.e("mImage3" + mImage3);
        Logger.e("mImage4" + mImage4);
        Logger.e("mImage5" + mImage5);
        nickName = bundle.getString("nickname","");
        birthday = bundle.getString("birthday","");
        setAge(birthday);
        Logger.e("bundle: " + bundle.toString());
        tv_modifitypic_name_aty.setText(nickName);
        tv_modifity_nickName_fmt.setText(nickName);
        Glide.with(PTApplication.getInstance())
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR)
                .thumbnail(0.1f)
                .transform(new GlideRoundTransform(this, 10))
                .error(R.drawable.upload_photo_big)
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(iv_pic_head_aty);
        if (!mImage1.equals("")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE1)
                    .signature(new StringSignature(mImage1))
                    .transform(new GlideRoundTransform(this, 10))
                    .into(iv_pic_two_aty);
        }
        if (!mImage2.equals("")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE2)
                    .signature(new StringSignature(mImage2))
                    .transform(new GlideRoundTransform(this, 10))
                    .into(iv_pic_three_aty);
        }
        if (!mImage3.equals("")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE3)
                    .signature(new StringSignature(mImage3))
                    .transform(new GlideRoundTransform(this, 10))
                    .into(iv_pic_four_aty);
        }
        if (!mImage4.equals("")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE4)
                    .signature(new StringSignature(mImage4))
                    .transform(new GlideRoundTransform(this, 10))
                    .into(iv_pic_five_aty);
        }
        if (!mImage5.equals("")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE5)
                    .signature(new StringSignature(mImage5))
                    .transform(new GlideRoundTransform(this, 10))
                    .into(iv_pic_six_aty);
        }
    }

    private void setAge(String birthday) {
        if (!TextUtils.isEmpty(birthday)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                long birthdaytime = sdf.parse(birthday).getTime();
                long now = System.currentTimeMillis();
                int age = (int) ((now - birthdaytime) / 365 / 24 / 60 / 60 / 1000);
                tv_modifity_age_fmt.setText(String.valueOf(age));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            tv_modifity_age_fmt.setText("0");
        }
    }

    private void initChangePop(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_changename, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        final EditText nickName =  contentView.findViewById(R.id.et_changename_pop);
        final Button dismiss =  contentView.findViewById(R.id.bt_changename_cancel_pop);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Button success =  contentView.findViewById(R.id.bt_changename_success_pop);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("v" + v.getId());
                final String newName = nickName.getText().toString().trim();
                PTApplication.getRequestService().changeName(newName, PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭转圈
                                hideLoadingDialog();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                // 转圈
                                showLoadingDialog();
                            }
                        })
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                // TODO: 2017/8/21 修改昵称成功没更新界面
                                Logger.e("noData" + noDataBean.toString());
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast("昵称修改成功");
                                    tv_modifity_nickName_fmt.setText(newName);
                                    tv_modifitypic_name_aty.setText(newName);
                                }else{
                                    ToastUtils.getToast(noDataBean.getMsg());
                                }
                            }
                        });
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}
