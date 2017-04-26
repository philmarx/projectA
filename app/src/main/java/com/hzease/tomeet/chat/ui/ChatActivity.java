package com.hzease.tomeet.chat.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.RadioButton;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imlib.model.Conversation;
import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.utils.ActivityUtils;
import com.hzease.tomeet.widget.KeyRadioGroupV1;

public class ChatActivity extends NavigationActivity{
    @BindView(R.id.rb_prefrect)
    RadioButton mRbPrefrect;
    @BindView(R.id.rb_great)
    RadioButton mRbGreat;
    @BindView(R.id.rb_good)
    RadioButton mRbGood;
    @BindView(R.id.rb_bad)
    RadioButton mRbBad;
    @BindView(R.id.rb_miss)
    RadioButton mRbMiss;
    @BindView(R.id.rg_classification)
    KeyRadioGroupV1 mRgClassification;
    private Fragment mConversationList;
    private List<Fragment> mFragment;

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
        return R.layout.activity_chat;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        mConversationList = initConversationList();//获取融云会话的列表对象

        navigation_bottom.getMenu().findItem(R.id.navigation_chat).setChecked(true).setEnabled(false);
        //初始化fragment
        if (mFragment == null || mFragment.size() != 1) {
            mFragment = new ArrayList<>();
            mFragment.add(mConversationList);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment.get(0), R.id.fl_content_chatlist);
        }
        mRbPrefrect.setChecked(isChecked);
        Log.e("TTT","1");
        mRgClassification.setOnCheckedChangeListener(new KeyRadioGroupV1.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(KeyRadioGroupV1 group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_prefrect:
                        Log.e("TTT","1");
                        break;
                    case R.id.rb_great:
                        Log.e("TTT","2");
                        isChecked = false;
                        break;
                    case R.id.rb_good:
                        Log.e("TTT","3");
                        isChecked = false;
                        break;
                    case R.id.rb_bad:
                        Log.e("TTT","4");
                        isChecked = false;
                        break;
                    case R.id.rb_miss:
                        Log.e("TTT","5");
                        isChecked = false;
                        break;
                }
            }
        });




        // 模拟
        /*for (int i = 0; i < 10; i++) {
            // 不置顶,让会话出现
            RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, Long.valueOf("20000800015") + i + "", false, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    Logger.d(aBoolean);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.d(errorCode);
                }
            });
        }*/

        /*RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null) {
                    Logger.e(conversations.size() + "", "为什么这行字打不出来");
                    conversations.add(Conversation.obtain(Conversation.ConversationType.PRIVATE,"11888888","我日你妈"));
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });*/
    }

    private Fragment initConversationList() {
        ConversationListFragment listFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")//设置私聊会话界面是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置私聊会话界面是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        listFragment.setUri(uri);
        listFragment.getConversationList(new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE}, new IHistoryDataResultCallback<List<Conversation>>() {
            @Override
            public void onResult(List<Conversation> conversations) {
                if (conversations != null) {
                    // 1490358931492    1490359845273
                    conversations.get(0).setReceivedTime(Long.valueOf("1490358931492"));
                    Logger.e("getReceivedTime:  " + conversations.get(0).getReceivedTime());
                    conversations.get(0).setDraft("");
                    Conversation 我日你妈 = Conversation.obtain(Conversation.ConversationType.PRIVATE, "11888888", "我日你妈");
                    我日你妈.setDraft("");
                    conversations.add(我日你妈);
                    Logger.e(conversations.size() + "", "为什么这行字打不出来");
                }
            }

            @Override
            public void onError() {

            }
        });

        return listFragment;
    }
}
