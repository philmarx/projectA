package com.hzease.tomeet.me.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.SmallPaperBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.adapters.PaperListAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/13.
 */

public class MySmallPaperActivity extends NetActivity {
    @BindView(R.id.rb_paperlist_receiver)
    RadioButton rb_paperlist_receiver;
    @BindView(R.id.rb_send_paper_act)
    RadioButton rb_send_paper_act;
    @BindView(R.id.rg_circle_selector)
    RadioGroup rg_circle_selector;
    @BindView(R.id.lv_paperlist_receiver_fmt)
    ListView lv_paperlist_receiver_fmt;
    //数据源
    List<SmallPaperBean.DataBean> mList;
    private PaperListAdapter adapter;

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_smallpaper;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        PTApplication.getRequestService().getMyReceivePaper(0, 15, PTApplication.userId, PTApplication.userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmallPaperBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SmallPaperBean smallPaperBean) {
                        if (smallPaperBean.isSuccess()) {
                            initPaperList(smallPaperBean.getData());
                        }
                    }
                });
        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_paperlist_receiver:
                        //TODO 转换到收到小纸条界面，现在只有这一个界面
                        break;
                    case R.id.rb_send_paper_act:
                        //跳回去
                        ToastUtils.getToast(MySmallPaperActivity.this,"暂未开通查看已发送纸条");
                        rb_paperlist_receiver.setChecked(true);
                        break;
                }
            }
        });
    }

    private void initPaperList(List<SmallPaperBean.DataBean> data) {
        mList = new ArrayList<>();
        mList = data;
        adapter = new PaperListAdapter(mList, this);
        lv_paperlist_receiver_fmt.setAdapter(adapter);
        lv_paperlist_receiver_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                /*mList.get(position).setState(1);
                adapter.notifyDataSetChanged();*/
                PTApplication.getRequestService().saveNote(mList.get(position).getId(),PTApplication.userToken,PTApplication.userId)
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
                                initPopupWindow(view, position);
                                adapter.notifyDataSetChanged();
                            }
                        });

            }
        });
    }

    private void initPopupWindow(View view, final int position) {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_smallpaper_recevie, null);
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
        CircleImageView senderIcon = (CircleImageView) contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        //头像
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + mList.get(position).getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(mList.get(position).getAvatarSignature()))
                .into(senderIcon);
        //发送者的名字
        TextView senderName = (TextView) contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        senderName.setText(mList.get(position).getNickname());
        //纸条内容
        NoteEditor content = (NoteEditor) contentView.findViewById(R.id.ne_smallpager_content_fmt);
        content.setText(mList.get(position).getContent());
        Button delete = (Button) contentView.findViewById(R.id.bt_smallpager_delete_pop);
        //删除小纸条
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(mList.get(position).getId(),PTApplication.userToken,PTApplication.userId)
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
                                if (noDataBean.isSuccess()){
                                    mList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
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
                PTApplication.getRequestService().saveNote(mList.get(position).getId(),PTApplication.userToken,PTApplication.userId)
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
                                if (noDataBean.isSuccess()){
                                    mList.get(position).setState(1);
                                    adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        Button reply = (Button) contentView.findViewById(R.id.bt_smallpager_reply_fmt);
        if (mList.get(position).getState() == 2){
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.getToast(MySmallPaperActivity.this,"您已经回复过改小纸条了");
                }
            });
        }else{
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    initPopupWindowReply(v,mList.get(position).getSenderId(),mList.get(position).getAvatarSignature(),mList.get(position).getNickname(),mList.get(position).getId());
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
                                ToastUtils.getToast(MySmallPaperActivity.this, e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(MySmallPaperActivity.this, "回复纸条成功");
                                    popupWindow.dismiss();
                                } else {
                                    ToastUtils.getToast(MySmallPaperActivity.this, noDataBean.getMsg());
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

}
