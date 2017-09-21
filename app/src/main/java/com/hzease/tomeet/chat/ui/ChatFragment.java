package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.chat.IChatContract;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.utils.AndroidBug5497Workaround;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.MyRongConversationListener;
import com.hzease.tomeet.widget.NoteEditor;
import com.hzease.tomeet.widget.adapters.ConversationAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.Event;
import io.rong.imlib.model.Message;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatFragment extends BaseFragment implements IChatContract.View {

    @BindView(R.id.rv_conversation_list_chat_fmt)
    RecyclerView rv_conversation_list_chat_fmt;
    @BindView(R.id.rg_friend_chat_fmt)
    RadioGroup rg_friend_chat_fmt;
    @BindView(R.id.rl_empty_conversation_list_chat_fmt)
    RelativeLayout rl_empty_conversation_list_chat_fmt;
    @BindView(R.id.tv_empty_conversation_list_chat_fmt)
    TextView tv_empty_conversation_list_chat_fmt;

    // 五个选择器
    @BindView(R.id.rb_gold_chat_fmt)
    RadioButton rb_gold_chat_fmt;

    @BindView(R.id.rb_blue_chat_fmt)
    RadioButton rb_blue_chat_fmt;

    @BindView(R.id.rb_green_chat_fmt)
    RadioButton rb_green_chat_fmt;

    @BindView(R.id.rb_gray_chat_fmt)
    RadioButton rb_gray_chat_fmt;

    @BindView(R.id.rb_red_chat_fmt)
    RadioButton rb_red_chat_fmt;

    private Realm mRealm;
    private IChatContract.Presenter mPresenter;
    public ConversationAdapter conversationAdapter;

    private String mChatingId = "";
    public Badge goldBadge;
    public Badge blueBadge;
    public Badge greenBadge;
    public Badge grayBadge;
    public Badge redBadge;
    private View tempCheckView;


    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void setPresenter(@NonNull IChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_chat;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        rg_friend_chat_fmt.setOrientation(LinearLayout.HORIZONTAL);
        // 注册event
        //EventBus.getDefault().register(this);
        //InternalModuleManager.getInstance().onLoaded();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
        }
        // 解决键盘不上浮问题
        AndroidBug5497Workaround.assistActivity(mRootView);

        if (PTApplication.myInfomation != null && (mRealm == null || !(PTApplication.userId + ".realm").equals(mRealm.getConfiguration().getRealmFileName()))) {
            //java.lang.IllegalStateException: Set default configuration by using `Realm.setDefaultConfiguration(RealmConfiguration)`.
            try {
                mRealm = Realm.getDefaultInstance();
            } catch (Exception e) {
                mRealm = Realm.getInstance(PTApplication.getRealmConfiguration());
                e.printStackTrace();
                Logger.e(e.getMessage());
                ToastUtils.getToast("配置文件加载失败");
            }
        }


        conversationAdapter = new ConversationAdapter(mContext);
        conversationAdapter.setOnItemClickListener(new ConversationAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v,String friendId, String nickName,String avatar) {
                Logger.e("buttonId: " + nickName + "  rb: " + rg_friend_chat_fmt.getCheckedRadioButtonId() + "   tempViewId: " + tempCheckView.getId());
                switch(tempCheckView.getId()) {
                    // 灰色红色不能点
                    case R.id.rb_red_chat_fmt:
                    case R.id.rb_gray_chat_fmt:
                        initNoFriendPop(friendId,avatar,nickName);
                        break;
                    case R.id.rb_gold_chat_fmt:
                    case R.id.rb_blue_chat_fmt:
                    case R.id.rb_green_chat_fmt:
                        // 点击后进入会话
                        RongIM.getInstance().startPrivateChat(mContext, friendId, nickName);
                        mChatingId = friendId;
                        break;
                    default:
                        Logger.e("default");
                        ToastUtils.getToast("T T对不起，请切换一下好友颜色试试");
                        break;
                }
            }
        });
        rv_conversation_list_chat_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_conversation_list_chat_fmt.setAdapter(conversationAdapter);
        switchFriends(conversationAdapter.getItemCount(), switchColor());

        // 金色
        goldBadge = new QBadgeView(mContext)
                .setGravityOffset(15, 4, true)
                .setBadgePadding(2, true)
                .bindTarget(rb_gold_chat_fmt);
        // 蓝
        blueBadge = new QBadgeView(mContext)
                .setGravityOffset(15, 4, true)
                .setBadgePadding(2, true)
                .bindTarget(rb_blue_chat_fmt);
        // 绿
        greenBadge = new QBadgeView(mContext)
                .setGravityOffset(15, 4, true)
                .setBadgePadding(2, true)
                .bindTarget(rb_green_chat_fmt);
        // 灰
        grayBadge = new QBadgeView(mContext)
                .setGravityOffset(15, 4, true)
                .setBadgePadding(2, true)
                .bindTarget(rb_gray_chat_fmt);
        // 红
        redBadge = new QBadgeView(mContext)
                .setGravityOffset(15, 4, true)
                .setBadgePadding(2, true)
                .bindTarget(rb_red_chat_fmt);
        colorUnread();
    }

    public void colorUnread() {
        goldBadge.setBadgeNumber(mRealm.where(RealmFriendBean.class).between("point", AppConstants.GOLD_POINT[0], AppConstants.GOLD_POINT[1]).sum("unreadCount").intValue());
        blueBadge.setBadgeNumber(mRealm.where(RealmFriendBean.class).between("point", AppConstants.BLUE_POINT[0], AppConstants.BLUE_POINT[1]).sum("unreadCount").intValue());
        greenBadge.setBadgeNumber(mRealm.where(RealmFriendBean.class).between("point", AppConstants.GREEN_POINT[0], AppConstants.GREEN_POINT[1]).sum("unreadCount").intValue());
        grayBadge.setBadgeNumber(mRealm.where(RealmFriendBean.class).between("point", AppConstants.GRAY_POINT[0], AppConstants.GRAY_POINT[1]).sum("unreadCount").intValue());
        redBadge.setBadgeNumber(mRealm.where(RealmFriendBean.class).between("point", AppConstants.RED_POINT[0], AppConstants.RED_POINT[1]).sum("unreadCount").intValue());
    }

    // 发出消息的event
    public void onEventMainThread(Message message) {
        Logger.w("发出消息的event: " + message.getSentStatus() + "  " + new String(message.getContent().encode()) + "  发送时间: " + message.getSentTime());
        // TODO: 2017/3/31 发送失败的效果还没处理
        switch(message.getSentStatus()) {
            case SENDING:
                mPresenter.sendMessage(message);
                break;
        }
    }

    // 接收到消息的event
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Logger.d("onEventMainThread   MessageContentEncode: " + new String(event.getMessage().getContent().encode())
                + "   getTargetId: " + event.getMessage().getTargetId() + "   Left: " +event.getLeft()
                + "   ObjectName: " + event.getMessage().getObjectName());

        // 更新好友数据(刷新列表)
        mPresenter.updateFriendsDate();
        // 刷新分页红点
        colorUnread();
    }

    // 清楚未读数后的红点改变
    public void onEventMainThread(EventBean.clearUnreadRedBadge redBadge) {
        // 刷新分页红点
        colorUnread();
    }

    @OnClick({R.id.rb_gold_chat_fmt,
            R.id.rb_blue_chat_fmt,
            R.id.rb_green_chat_fmt,
            R.id.rb_gray_chat_fmt,
            R.id.rb_red_chat_fmt})
    public void onClick(View view) {
        // 默认金色,注释掉金色,那没有符合的case就是金色

        if (tempCheckView != null) {
            ((RadioButton) tempCheckView).setChecked(false);
        }
        PTApplication.friendType = AppConstants.GOLD_POINT;
        switch (view.getId()) {
            /*case R.id.rb_gold_chat_fmt:
                Logger.i("切换到金色");
                type = AppConstants.GOLD_POINT;
                break;*/
            case R.id.rb_blue_chat_fmt:
                // Logger.i("切换到蓝色");
                PTApplication.friendType = AppConstants.BLUE_POINT;
                break;
            case R.id.rb_green_chat_fmt:
                // Logger.i("切换到绿色");
                PTApplication.friendType = AppConstants.GREEN_POINT;
                break;
            case R.id.rb_gray_chat_fmt:
                // Logger.i("切换到灰色");
                PTApplication.friendType = AppConstants.GRAY_POINT;
                break;
            case R.id.rb_red_chat_fmt:
                // Logger.i("切换到红色");
                PTApplication.friendType = AppConstants.RED_POINT;
                break;
        }
        rg_friend_chat_fmt.check(view.getId());
        switchFriends(conversationAdapter.switchFriends(PTApplication.friendType), switchColor());
    }

    private void switchFriends(int friendCount, String color) {
        if (friendCount == 0) {
            rl_empty_conversation_list_chat_fmt.setVisibility(View.VISIBLE);
            rv_conversation_list_chat_fmt.setVisibility(View.GONE);
            tv_empty_conversation_list_chat_fmt.setText(color);
        } else {
            rl_empty_conversation_list_chat_fmt.setVisibility(View.GONE);
            rv_conversation_list_chat_fmt.setVisibility(View.VISIBLE);
        }
    }

    private String switchColor() {
        String explain = "";
        switch(PTApplication.friendType[0]) {
            case 1:
                explain = "你还没有红色好友哦~\n（其实这也是个黑名单，不能聊天的）\n\n活动结束后只要你单方评价1或2分\n就会出现在这里，但是对面的看不到你哦";
                tempCheckView = rb_red_chat_fmt;
                rb_red_chat_fmt.setChecked(true);
                break;
            case 3:
                explain = "你还没有灰色好友哦~\n（其实这是个黑名单，不能聊天的）\n\n活动结束后只要你单方评价3或4分\n就会出现在这里，但是对面的看不到你哦";
                tempCheckView = rb_gray_chat_fmt;
                break;
            case 5:
                explain = "你还没有绿色好友哦~\n\n活动结束后双方好感评价最低分一方为5或6分\n就会出现在这里哦~（不会一辈子都在这里的）\n通过各种行为（遐想~）都能提升亲密度的呢";
                tempCheckView = rb_green_chat_fmt;
                break;
            case 7:
                explain = "你还没有蓝色好友哦~\n\n活动结束后双方好感评价最低分一方为7或8分\n就会出现在这里哦~（不会一辈子都在这里的）\n通过各种行为（遐想~）都能提升亲密度的呢";
                tempCheckView = rb_blue_chat_fmt;
                break;
            case 9:
                explain = "你还没有金色好友哦~\n\n活动结束后双方好感评价最低分一方为9或10分\n就会出现在这里哦~";
                tempCheckView = rb_gold_chat_fmt;
                break;
        }
        ((RadioButton) tempCheckView).setChecked(true);
        return explain;
    }

    /**
     * 更新好友列表
     */
    @Override
    public void updateFriendList() {
        conversationAdapter.updateFriends();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 如果是从打开的会话中回来.就清空未读
        if (!TextUtils.isEmpty(mChatingId)) {
            Logger.d(mChatingId);
            mPresenter.clearUnread(mChatingId);
            mChatingId = "";
            // 刷新分页红点
            colorUnread();
        }

        if (mPresenter != null) {
            mPresenter.start();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //不是好友
    private void initNoFriendPop(final String friendId, final String avatar, final String nickName) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_redorgray, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button sendSmallPaper =  contentView.findViewById(R.id.bt_send_smallpaper_fmt);
        Button intoPersonSpace =  contentView.findViewById(R.id.bt_into_personspace_fmt);
        sendSmallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                popupWindow.dismiss();
                PTApplication.getRequestService().findPropsMum(PTApplication.userToken,PTApplication.userId)
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
                                if (propsMumBean.getData().getNoteCount()>0){
                                    initSendSmallPaperPop(v,friendId,avatar,nickName);
                                }else{
                                    ToastUtils.getToast("小纸条数量不足，请购买");
                                }
                            }
                        });

            }
        });
        intoPersonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",Long.valueOf(friendId));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }


    private void initSendSmallPaperPop(View view, final String friend, String avatar, String nickName) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_smallpaper, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//解决在华为上背景不透明的bug
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        final NoteEditor content =  contentView.findViewById(R.id.ne_smallpager_content_fmt);
        final TextView notesize =  contentView.findViewById(R.id.tv_notesize_fmt);
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
        CircleImageView head =  contentView.findViewById(R.id.civ_sendsmallpaper_head_pop);
        Glide.with(this)
                .load(AppConstants.YY_PT_OSS_USER_PATH + friend + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(avatar))
                .into(head);
        TextView name =  contentView.findViewById(R.id.tv_sendsmallpaper_name_pop);
        name.setText(nickName);
        Button sendNote =  contentView.findViewById(R.id.bt_smallpager_send_fmt);
        Button dismiss =  contentView.findViewById(R.id.bt_smallpager_cancel_fmt);
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送纸条
                PTApplication.getRequestService().sendNote(content.getText().toString().trim(), friend, PTApplication.userToken, PTApplication.userId)
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
}
