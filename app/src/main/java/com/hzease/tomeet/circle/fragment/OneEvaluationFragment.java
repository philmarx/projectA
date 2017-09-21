package com.hzease.tomeet.circle.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.CircleOfFriendsCommentAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.IExtensionClickListener;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.plugin.IPluginModule;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/9/18.
 */

public class OneEvaluationFragment extends BaseFragment implements IExtensionClickListener {
    @BindView(R.id.iv_bg_circle_of_friends_item)
    ImageView iv_bg_circle_of_friends_item;
    @BindView(R.id.iv_avatar_circle_of_friends_item)
    ImageView iv_avatar_circle_of_friends_item;
    @BindView(R.id.tv_name_circle_of_friends_item)
    TextView tv_name_circle_of_friends_item;
    @BindView(R.id.tv_content_circle_of_friends_item)
    TextView tv_content_circle_of_friends_item;
    @BindView(R.id.tv_time_circle_of_friends_item)
    TextView tv_time_circle_of_friends_item;
    @BindView(R.id.tv_reply_circle_of_friends_item)
    ImageView tv_reply_circle_of_friends_item;
    @BindView(R.id.rv_comment_circle_of_friends_item)
    RecyclerView rv_comment_circle_of_friends_item;
    @BindView(R.id.rc_extension_circle_of_friends_fmt)
    RongExtension rc_extension_circle_of_friends_fmt;

    // 回复给谁的ID，0为不指定
    private long mToUserId;
    // 喊话ID
    private long mDeclaration;
    private InputMethodManager imm;
    private String who;
    private long id;

    public OneEvaluationFragment() {
    }

    public static OneEvaluationFragment newInstance() {
        return new OneEvaluationFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_oneevaluation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id = bundle.getLong("id");
        who = bundle.getString("nickname");
        rc_extension_circle_of_friends_fmt.setExtensionClickListener(this);
        rc_extension_circle_of_friends_fmt.setFragment(this);
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {
                EditText inputEditText = rc_extension_circle_of_friends_fmt.getInputEditText();
                inputEditText.getText().insert(inputEditText.getSelectionStart(), emoji);
            }

            @Override
            public void onDeleteClick() {
                rc_extension_circle_of_friends_fmt.getInputEditText().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        rc_extension_circle_of_friends_fmt.addEmoticonTab(emojiTab, "1");
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        PTApplication.getRequestService().getOneDeclaration(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentItemBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommentItemBean commentItemBean) {
                        if (commentItemBean.isSuccess()) {
                            initEvaluation(commentItemBean.getData());
                        }
                    }
                });
    }

    private void initEvaluation(List<CommentItemBean.DataBean> data) {
        rv_comment_circle_of_friends_item.setLayoutManager(new LinearLayoutManager(mContext));
        CircleOfFriendsCommentAdapter commentAdapter = new CircleOfFriendsCommentAdapter();

        rv_comment_circle_of_friends_item.setAdapter(commentAdapter);
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(0).getDeclareId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(data.get(0).getAvatarSignature()))
                .into(iv_avatar_circle_of_friends_item);
        // 背景
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(0).getDeclareId() + AppConstants.YY_PT_OSS_AVATAR)
                .signature(new StringSignature(data.get(0).getAvatarSignature()))
                .centerCrop()
                .into(iv_bg_circle_of_friends_item);
        // 名字
        tv_name_circle_of_friends_item.setText(data.get(0).getDeclareNickname());
        // 时间
        tv_time_circle_of_friends_item.setText(calculateTime(data.get(0).getCreateTime()));
        // 喊话内容
        tv_content_circle_of_friends_item.setText(data.get(0).getContent());
        ((CircleOfFriendsCommentAdapter) rv_comment_circle_of_friends_item.getAdapter()).setmData(data.get(0).getEvaluations());
        commentAdapter.setOnItemClickLitener(new CircleOfFriendsCommentAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(CommentItemBean.DataBean.EvaluationsBean.SenderBean senderBean,int postion) {
// 输入框显示
                if (rc_extension_circle_of_friends_fmt.isExtensionExpanded()) {
                    rc_extension_circle_of_friends_fmt.collapseExtension();
                }
                if (rc_extension_circle_of_friends_fmt.getVisibility() == View.GONE) {
                    rc_extension_circle_of_friends_fmt.setVisibility(View.VISIBLE);
                }
                EditText inputEditText = rc_extension_circle_of_friends_fmt.getInputEditText();


                if (senderBean == null) {
                    mToUserId = 0;
                } else {
                    mToUserId = senderBean.getId();
                }
                // 喊话ID
                mDeclaration = id;
                // "消息Id:  " + dataBean.getId() + "   消息发布者Id: " + dataBean.getDeclareId()
                inputEditText.setHint("回复  " + who);

                inputEditText.requestFocusFromTouch();


                Logger.e("active: " + imm.isActive());
                if (imm.isActive()) {
                    //imm.showSoftInput(inputEditText, InputMethodManager.SHOW_FORCED);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                    imm.toggleSoftInputFromWindow(inputEditText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    private String calculateTime(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long now = (System.currentTimeMillis() + offSet) / 60000;
        long create = (time + offSet) / 60000;
        long diff = now - create;
        if (diff < 60) {
            return diff + "分钟前";
        } else if (diff < 1440) {
            return diff / 60 + "小时前";
        } else {
            return diff / 60 / 24 + "天前";
        }
    }

    //-------------融云扩展插件接口--------------------------------------------------------------------
    @Override
    public void onSendToggleClick(View view, String s) {
        Logger.e("6666666");
        if (TextUtils.isEmpty(s.trim())) {
            ToastUtils.getToast("不可以回复空消息");
        } else {
            PTApplication.getRequestService().commentCircleOfFriend(s.trim().replace("\n", "，"), mDeclaration, mToUserId,PTApplication.userToken,Long.valueOf(PTApplication.userId))
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
                                PTApplication.getRequestService().getOneDeclaration(id)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<CommentItemBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(CommentItemBean commentItemBean) {
                                                if (commentItemBean.isSuccess()) {
                                                    initEvaluation(commentItemBean.getData());
                                                }else{
                                                    ToastUtils.getToast(commentItemBean.getMsg());
                                                }
                                            }
                                        });
                            }else{
                                ToastUtils.getToast(noDataBean.getMsg());
                            }
                        }
                    });
            rc_extension_circle_of_friends_fmt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageResult(List<Uri> list, boolean b) {

    }

    @Override
    public void onLocationResult(double v, double v1, String s, Uri uri) {

    }

    @Override
    public void onSwitchToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onVoiceInputToggleTouch(View view, MotionEvent motionEvent) {

    }

    @Override
    public void onEmoticonToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onPluginToggleClick(View view, ViewGroup viewGroup) {

    }

    @Override
    public void onMenuClick(int i, int i1) {

    }

    @Override
    public void onEditTextClick(EditText editText) {

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onExtensionCollapsed() {

    }

    @Override
    public void onExtensionExpanded(int i) {

    }

    @Override
    public void onPluginClicked(IPluginModule iPluginModule, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
