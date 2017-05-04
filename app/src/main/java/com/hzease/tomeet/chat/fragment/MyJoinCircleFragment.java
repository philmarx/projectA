package com.hzease.tomeet.chat.fragment;

import android.os.Bundle;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.chat.IChatContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/5/4.
 */

public class MyJoinCircleFragment extends BaseFragment implements IChatContract.View {

    private IChatContract.Presenter mPresenter;

    public static MyJoinCircleFragment newInstance() {
        return new MyJoinCircleFragment();
    }

    @Override
    public void setPresenter(IChatContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 更新好友列表
     */
    @Override
    public void updateFriendList() {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return 0;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
