package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.chat.ChatPresenter;
import com.hzease.tomeet.chat.ChatPresenterModule;
import com.hzease.tomeet.chat.DaggerIChatComponent;
import com.hzease.tomeet.chat.IChatContract;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.ActivityUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

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

    @BindView(R.id.iv_system_chat_act)
    ImageView iv_system_chat_act;

    @BindView(R.id.fl_content_chat_version2_activity)
    FrameLayout fl_content_chat_version2_activity;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;

    public void onEventMainThread(EventBean.LoginInvalid loginInvalid) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
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
                switch (checkedId){
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

        iv_system_chat_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversation(ChatVersion2Activity.this, Conversation.ConversationType.SYSTEM, AppConstants.TOMEET_ADMIN_ID, "系统消息");
            }
        });
    }
}
