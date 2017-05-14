package com.hzease.tomeet.chat.ui;

import android.os.Bundle;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;

/**
 * 隐式意图启动的会话列表，融云自带的界面
 */
public class ChatConversationActivity extends NetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_conversation;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }
}
