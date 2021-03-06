package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.SmallPaperBean;
import com.hzease.tomeet.utils.TimeUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.adapters.PaperListAdapter;
import com.hzease.tomeet.widget.adapters.SendPaperListAdapter;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    @BindView(R.id.ptl_refresh)
    PullToRefreshLayout ptl_refresh;
    @BindView(R.id.ll_no_smallpaper)
    LinearLayout ll_no_smallpaper;
    @BindView(R.id.tv_no_smallpaper)
    TextView tv_no_smallpaper;
    //数据源
    List<SmallPaperBean.DataBean> mList = new ArrayList<>();
    private PaperListAdapter adapter;
    private int page = 0;
    private int pagebak = 0;
    private SendPaperListAdapter sendAdapter;
    private int noteCount;
    private boolean isRead;

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_smallpaper;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        initReceiveSmallPaper();
        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_paperlist_receiver:
                        initReceiveSmallPaper();
                        break;
                    case R.id.rb_send_paper_act:
                        initSendSmallPaper();
                        break;
                }
            }
        });
    }

    /**
     * 查看发送的小纸条
     */
    private void initSendSmallPaper() {
        PTApplication.getRequestService().mySendPaper(0, 50, PTApplication.userId, PTApplication.userToken)
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
                            initSendPaperList(smallPaperBean.getData(), false);
                        }
                    }
                });
        ptl_refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        PTApplication.getRequestService().mySendPaper(0, 50, PTApplication.userId, PTApplication.userToken)
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
                                            initSendPaperList(smallPaperBean.getData(), false);
                                        }
                                    }
                                });
                    }
                }, 1);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PTApplication.getRequestService().mySendPaper(++pagebak, 50, PTApplication.userId, PTApplication.userToken)
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
                                            initSendPaperList(smallPaperBean.getData(), true);
                                            ptl_refresh.finishLoadMore();
                                        }
                                    }
                                });
                    }
                }, 1);
            }
        });
    }

    /**
     * 加载发送的小纸条
     *
     * @param data
     */
    private void initSendPaperList(List<SmallPaperBean.DataBean> data, boolean isLoadMore) {
        if (isLoadMore) {
            sendAdapter.addData(data);
        } else {
            if (data.size() == 0) {
                ll_no_smallpaper.setVisibility(View.VISIBLE);
                tv_no_smallpaper.setText("您还有没有发送过小纸条~");
                lv_paperlist_receiver_fmt.setVisibility(View.GONE);
            } else {
                ll_no_smallpaper.setVisibility(View.GONE);
                lv_paperlist_receiver_fmt.setVisibility(View.VISIBLE);
                mList.clear();
                mList = data;
                sendAdapter = new SendPaperListAdapter(data, this);
                lv_paperlist_receiver_fmt.setAdapter(sendAdapter);
                ptl_refresh.finishRefresh();
            }

        }
        lv_paperlist_receiver_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initPopupWindow(view, position, true);
            }
        });


    }

    /**
     * 查看接收的小纸条
     */
    private void initReceiveSmallPaper() {
        PTApplication.getRequestService().getMyReceivePaper(0, 50, PTApplication.userId, PTApplication.userToken)
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
                            initPaperList(smallPaperBean.getData(), false);
                        }
                    }
                });
        ptl_refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        PTApplication.getRequestService().getMyReceivePaper(0, 50, PTApplication.userId, PTApplication.userToken)
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
                                            initPaperList(smallPaperBean.getData(), false);
                                            ptl_refresh.finishLoadMore();
                                        }
                                    }
                                });
                    }
                }, 1);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        PTApplication.getRequestService().getMyReceivePaper(++page, 50, PTApplication.userId, PTApplication.userToken)
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
                                            initPaperList(smallPaperBean.getData(), true);
                                            ptl_refresh.finishLoadMore();
                                        }
                                    }
                                });
                    }
                }, 1);
            }
        });
    }

    private void initPaperList(List<SmallPaperBean.DataBean> data, boolean isLoadMore) {
        if (isLoadMore) {
            mList.addAll(data);
            adapter.notifyDataSetChanged();
        } else {
            if (data.size() == 0) {
                ll_no_smallpaper.setVisibility(View.VISIBLE);
                tv_no_smallpaper.setText("您还有没接收到小纸条~");
                lv_paperlist_receiver_fmt.setVisibility(View.GONE);
            } else {
                ll_no_smallpaper.setVisibility(View.GONE);
                lv_paperlist_receiver_fmt.setVisibility(View.VISIBLE);
                mList.clear();
                mList = data;
                adapter = new PaperListAdapter(mList, this);
                lv_paperlist_receiver_fmt.setAdapter(adapter);
                ptl_refresh.finishRefresh();
            }
        }
        lv_paperlist_receiver_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                initPopupWindow(view, position, false);
            }
        });
    }

    /**
     * 弹框
     *
     * @param view     根布局
     * @param position 坐标
     * @param isSend   点击的是否是发送的小纸条
     */
    private void initPopupWindow(View view, final int position, boolean isSend) {
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
                if (isRead){
                    mList.get(position).setState(4);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        //两个按钮
        AutoLinearLayout two_state_pop =  contentView.findViewById(R.id.two_state_pop);
        //按钮
        AutoLinearLayout all_state_pop =  contentView.findViewById(R.id.all_state_pop);
        //发送者头像
        CircleImageView senderIcon =  contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        //时间
        TextView time = contentView.findViewById(R.id.tv_paper_time);
        time.setText(TimeUtils.calculateTime(mList.get(position).getCreateTime()));
        senderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySmallPaperActivity.this, PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",mList.get(position).getSenderId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (isSend){
            //头像
            Glide.with(this)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mList.get(position).getReceiverId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .signature(new StringSignature(mList.get(position).getAvatarSignature()))
                    .into(senderIcon);
        }else{
            //头像
            Glide.with(this)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mList.get(position).getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .signature(new StringSignature(mList.get(position).getAvatarSignature()))
                    .into(senderIcon);
        }

        //发送者的名字
        TextView senderName =  contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        senderName.setText(mList.get(position).getNickname());
        //纸条内容
        NoteEditor content =  contentView.findViewById(R.id.ne_smallpager_content_fmt);
        content.setText(mList.get(position).getContent());
        Button delete =  contentView.findViewById(R.id.bt_smallpager_delete_pop);
        Button delete_bak =  contentView.findViewById(R.id.bt_smallpager_delete_pop_bak);
        Button save_bak =  contentView.findViewById(R.id.bt_smallpager_save_pop_bak);
        Button send_newNote = contentView.findViewById(R.id.bt_smallpager_sendnewnote_fmt);

        send_newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                popupWindow.dismiss();
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
                                    initSmallPaperWindow(view, noteCount,mList.get(position));
                                } else {
                                    initBuySmallPaper(view,mList.get(position));
                                }
                            }
                        });
                Logger.e("6666");
            }
        });
        if (isSend) {
            two_state_pop.setVisibility(View.GONE);
            all_state_pop.setVisibility(View.GONE);
        } else {
            if (mList.get(position).getState() != 0) {
                all_state_pop.setVisibility(View.GONE);
            } else {
                all_state_pop.setVisibility(View.VISIBLE);
                two_state_pop.setVisibility(View.GONE);
            }
            if (mList.get(position).getState() == 4) {
                two_state_pop.setVisibility(View.VISIBLE);
            }
        }
        //另一个删除
        delete_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(mList.get(position).getId(), PTApplication.userToken, PTApplication.userId)
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
                                    mList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        //删除小纸条
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().deleteNote(mList.get(position).getId(), PTApplication.userToken, PTApplication.userId)
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
                                    mList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        //收起放好
        Button read =  contentView.findViewById(R.id.bt_smallpaper_read_pop);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().saveNote(mList.get(position).getId(), PTApplication.userToken, PTApplication.userId)
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
                                    mList.get(position).setState(1);
                                    adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                }
                            }
                        });
            }
        });
        Button reply =  contentView.findViewById(R.id.bt_smallpager_reply_fmt);
        if (mList.get(position).getState() == 2) {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.getToast("您已经回复过改小纸条了");
                }
            });
        } else {
            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    initPopupWindowReply(v, mList.get(position).getSenderId(), mList.get(position).getAvatarSignature(), mList.get(position).getNickname(), mList.get(position).getId());
                }
            });
        }
        //已读回复的小纸条
        save_bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().readReplyNote(String.valueOf(mList.get(position).getId()), PTApplication.userId, PTApplication.userToken)
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
                                    mList.get(position).setState(5);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    //购买小纸条
    private void initBuySmallPaper(View v, final SmallPaperBean.DataBean date) {
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
                                    initSmallPaperWindow(v, 1,date);
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


    private void initSmallPaperWindow(View view, int count, final SmallPaperBean.DataBean date) {
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
                .load(AppConstants.YY_PT_OSS_USER_PATH + date.getSenderId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(date.getAvatarSignature()))
                .into(head);
        TextView name = contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(date.getNickname());
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
                PTApplication.getRequestService().sendNote(content.getText().toString().trim(), String.valueOf(date.getSenderId()), PTApplication.userToken, PTApplication.userId)
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
                                                    date.setState(1);
                                                    adapter.notifyDataSetChanged();
                                                    popupWindow.dismiss();
                                                }
                                            }
                                        });



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
        final NoteEditor content =  contentView.findViewById(R.id.ne_smallpager_content_fmt);
        CircleImageView head =  contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(this))
                .signature(new StringSignature(avatarSignature))
                .into(head);
        TextView name =  contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(nickName);
        Button sendNote =  contentView.findViewById(R.id.bt_smallpager_send_fmt);
        Button dismiss =  contentView.findViewById(R.id.bt_smallpager_cancel_fmt);
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
                                ToastUtils.getToast(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.isSuccess() + "");
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast("回复纸条成功");
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

}
