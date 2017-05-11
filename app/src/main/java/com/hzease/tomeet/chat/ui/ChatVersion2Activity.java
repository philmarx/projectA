package com.hzease.tomeet.chat.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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
import com.hzease.tomeet.chat.fragment.MyJoinCircleFragment;
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

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
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

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;
    //设置第一个按钮是否选中
    private boolean isChecked=true;
    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
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
            MyJoinCircleFragment myJoinCircleFragment = MyJoinCircleFragment.newInstance();
            mFragmentList.add(chatFragment);
            mFragmentList.add(myJoinCircleFragment);
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
        rb_friend_chat_act.setChecked(isChecked);
        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_friend_chat_act:
                        break;
                    case R.id.rb_circle_chat_act:
                        RongIM.getInstance().startSubConversationList(ChatVersion2Activity.this, Conversation.ConversationType.GROUP);
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
    //Fragment启动方法：
    private void replaceFragment(Fragment fragment) {
        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager =getSupportFragmentManager();
        // 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fl_content_chat_version2_activity, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        //transaction.addToBackStack(null);
        // 5.提交事物,调用commit()方法来完成
        transaction.commit();
    }

}
