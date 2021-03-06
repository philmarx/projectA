package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.chat.ChatPresenter;
import com.hzease.tomeet.chat.ChatPresenterModule;
import com.hzease.tomeet.chat.DaggerIChatComponent;
import com.hzease.tomeet.chat.IChatContract;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.ActivityUtils;
import com.hzease.tomeet.widget.AlertDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Key on 2017/3/24 16:56
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatVersion2Activity extends NavigationActivity {
    @Inject
    ChatPresenter mChatPresenter;

    @BindView(R.id.rb_friend_chat_act)
    RadioButton rb_friend_chat_act;
    @BindView(R.id.rb_circle_chat_act)
    RadioButton rb_circle_chat_act;

    @BindView(R.id.rg_circle_selector)
    RadioGroup rg_circle_selector;

    //全部已读
    @BindView(R.id.iv_all_message_read_fmt)
    ImageView iv_all_message_read_fmt;

    @BindView(R.id.iv_system_chat_act)
    TextView iv_system_chat_act;

    @BindView(R.id.fl_content_chat_version2_activity)
    FrameLayout fl_content_chat_version2_activity;

    @BindView(R.id.iv_addfriend_fmt)
    ImageView iv_addfriend_fmt;

    @BindView(R.id.iv_circle_unread_chat_act)
    ImageView iv_circle_unread_chat_act;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;
    public Badge systemUnreadBadge;
    private IUnReadMessageObserver mCircleUnReadMessageObserver;

    public void onEventMainThread(EventBean.LoginInvalid loginInvalid) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    // 系统消息红点
    public void onEventMainThread(EventBean.systemUnreadAddOne addOne) {
        systemUnreadBadge.setBadgeNumber(systemUnreadBadge.getBadgeNumber() + 1);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_version2;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
        }

        navigation_bottom.getMenu().findItem(R.id.navigation_chat).setChecked(true).setEnabled(false);

        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            ChatFragment chatFragment = ChatFragment.newInstance();
            mFragmentList.add(chatFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_chat_version2_activity);
        }

        // dagger2
        for (int i = 0; i < mFragmentList.size(); i++) {
            DaggerIChatComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    // .chatPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                    .chatPresenterModule(new ChatPresenterModule(((IChatContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

        // 设置好友聊天和圈子聊天的显隐
        fl_content_chat_version2_activity.setVisibility(View.VISIBLE);
        findViewById(R.id.subconversationlist).setVisibility(View.INVISIBLE);

        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_friend_chat_act:
                        fl_content_chat_version2_activity.setVisibility(View.VISIBLE);
                        findViewById(R.id.subconversationlist).setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_circle_chat_act:
                        fl_content_chat_version2_activity.setVisibility(View.INVISIBLE);
                        findViewById(R.id.subconversationlist).setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        iv_addfriend_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatVersion2Activity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });
        systemUnreadBadge = new QBadgeView(this)
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgePadding(1, true)
                .bindTarget(iv_system_chat_act)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                            // 清空未读
                            RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM, "888888", new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    Logger.e(errorCode.getMessage());
                                }
                            });
                        }

                    }
                });

        iv_system_chat_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversation(ChatVersion2Activity.this, Conversation.ConversationType.SYSTEM, AppConstants.TOMEET_ADMIN_ID, "系统消息");
            }
        });

        // 刷新群未读
        mCircleUnReadMessageObserver = new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i != 0) {
                    // 显隐红点
                    iv_circle_unread_chat_act.setVisibility(View.VISIBLE);
                } else {
                    iv_circle_unread_chat_act.setVisibility(View.GONE);
                }
            }
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(mCircleUnReadMessageObserver, Conversation.ConversationType.GROUP);
        iv_all_message_read_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog(ChatVersion2Activity.this).builder()
                        .setTitle("提示")
                        .setMsg("本操作将清除未读消息提示，确认此操作吗")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PTApplication.unReadNumber = 0;
                                Realm realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmResults<RealmFriendBean> all = realm.where(RealmFriendBean.class).findAll();
                                            for (final RealmFriendBean friendBean : all) {
                                                friendBean.setUnreadCount(0);
                                                RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, String.valueOf(friendBean.getId()), new RongIMClient.ResultCallback<Boolean>() {
                                                    @Override
                                                    public void onSuccess(Boolean aBoolean) {
                                                        //Logger.e("清除未读：" + aBoolean);

                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                                        Logger.e("清除未读Error：" + errorCode.getMessage());
                                                    }
                                                });
                                            }

                                        }
                                    });
                                    ((ChatFragment)mFragmentList.get(0)).conversationAdapter.notifyDataSetChanged();
                                    ((ChatFragment)mFragmentList.get(0)).goldBadge.setBadgeNumber(0);
                                    ((ChatFragment)mFragmentList.get(0)).blueBadge.setBadgeNumber(0);
                                    ((ChatFragment)mFragmentList.get(0)).greenBadge.setBadgeNumber(0);
                                    ((ChatFragment)mFragmentList.get(0)).grayBadge.setBadgeNumber(0);
                                    ((ChatFragment)mFragmentList.get(0)).redBadge.setBadgeNumber(0);
                                    PTApplication.badge.setBadgeNumber(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    realm.close();
                                }
                                // 清空系统未读
                                RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM, "888888", new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        systemUnreadBadge.setBadgeNumber(0);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        Logger.e(errorCode.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 刷新系统未读
        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.SYSTEM, "888888", new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                systemUnreadBadge.setBadgeNumber(integer);
                Logger.e(integer.toString());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e(errorCode.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(mCircleUnReadMessageObserver);
    }
}
