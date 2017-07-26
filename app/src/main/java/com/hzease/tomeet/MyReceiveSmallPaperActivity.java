package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.SmallPaperBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.NoteEditor;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/7.
 */

public class MyReceiveSmallPaperActivity extends NetActivity {
    @BindView(R.id.arl_smallpaper_bg)
    AutoRelativeLayout arl_smallpaper_bg;
    private SmallPaperBean.DataBean date;
    private boolean flag;


    @OnClick({
            R.id.iv_receive_open_pop,
            R.id.iv_receive_cancel_pop
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_receive_open_pop:
                arl_smallpaper_bg.setVisibility(View.GONE);
                initPopupWindow(date, v);
                break;
            case R.id.iv_receive_cancel_pop:
                finish();
                break;
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reveivesmallpaper;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String str = intent.getStringExtra("json");
        Gson gson = new Gson();
        date = gson.fromJson(str, SmallPaperBean.DataBean.class);

    }

    @OnClick(R.id.arl_reciverpaper_view)
    public void onViewClicked() {
        finish();
    }

    private void initPopupWindow(final SmallPaperBean.DataBean date, View view) {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_smallpaper_recevie, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!flag) {
                    finish();
                }
            }
        });
        //两个按钮
        AutoLinearLayout two_state_pop = (AutoLinearLayout) contentView.findViewById(R.id.two_state_pop);
        //按钮
        AutoLinearLayout all_state_pop = (AutoLinearLayout) contentView.findViewById(R.id.all_state_pop);
        //发送者头像
        CircleImageView senderIcon = (CircleImageView) contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        //头像
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + date.getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(date.getAvatarSignature()))
                .into(senderIcon);
        //发送者的名字
        TextView senderName = (TextView) contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        senderName.setText(date.getNickname());
        //纸条内容
        NoteEditor content = (NoteEditor) contentView.findViewById(R.id.ne_smallpager_content_fmt);
        content.setText(date.getContent());
        Button delete = (Button) contentView.findViewById(R.id.bt_smallpager_delete_pop);
        Button delete_bak = (Button) contentView.findViewById(R.id.bt_smallpager_delete_pop_bak);
        Button save_bak = (Button) contentView.findViewById(R.id.bt_smallpager_save_pop_bak);
        //另一个删除
        delete_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(date.getId(), PTApplication.userToken, PTApplication.userId)
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
                                }
                            }
                        });
            }
        });
        //已读回复的小纸条
        save_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().readReplyNote(String.valueOf(date.getId()), PTApplication.userId, PTApplication.userToken)
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
                                Logger.e("onNext" + noDataBean.isSuccess());
                                if (noDataBean.isSuccess()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        if (date.getState() != 0) {
            all_state_pop.setVisibility(View.GONE);
        } else {
            all_state_pop.setVisibility(View.VISIBLE);
        }
        if (date.getState() == 4) {
            two_state_pop.setVisibility(View.VISIBLE);
        }

        //删除小纸条
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(date.getId(), PTApplication.userToken, PTApplication.userId)
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
                                    finish();
                                }
                            }
                        });
            }
        });
        //收起放好
        Button read = (Button) contentView.findViewById(R.id.bt_smallpaper_read_pop);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().saveNote(date.getId(), PTApplication.userToken, PTApplication.userId)
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
                                    finish();
                                }
                            }
                        });
            }
        });
        Button reply = (Button) contentView.findViewById(R.id.bt_smallpager_reply_fmt);
        if (date.getState() == 2) {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.getToast(MyReceiveSmallPaperActivity.this, "您已经回复过改小纸条了");
                }
            });
        } else {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = true;
                    popupWindow.dismiss();
                    initPopupWindowReply(v, date.getSenderId(), date.getAvatarSignature(), date.getNickname(), date.getId());
                }
            });
        }
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //回复弹窗
    private void initPopupWindowReply(View view, final long userId, String avatarSignature, String nickName, final long noteId) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_smallpaper, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
      /*  // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
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
        });*/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
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
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送纸条
                Logger.e(userId + "");
                PTApplication.getRequestService().replyNote(content.getText().toString().trim(), String.valueOf(noteId), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                                Logger.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.getToast(MyReceiveSmallPaperActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(MyReceiveSmallPaperActivity.this, "回复纸条成功");
                                    popupWindow.dismiss();
                                    finish();
                                } else {
                                    ToastUtils.getToast(MyReceiveSmallPaperActivity.this, noDataBean.getMsg());
                                    popupWindow.dismiss();
                                    finish();
                                }
                            }
                        });
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
