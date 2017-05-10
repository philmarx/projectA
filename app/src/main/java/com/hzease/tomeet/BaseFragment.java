package com.hzease.tomeet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.utils.AndroidBug5497Workaround;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Key on 2015/11/15.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    protected Context mContext;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);    //绑定布局
        AndroidBug5497Workaround.assistActivity(mRootView);
        mContext = getContext();    //getActivity() 和 getContext() 获得的是同一个对象，地址相同，只是返回的类型不同
        unbinder = ButterKnife.bind(this, mRootView);   //绑定黄油刀
        initView(savedInstanceState);   //初始化布局（界面）
        return mRootView;
    }


    /**
     * @return 布局文件ID
     */
    public abstract int getContentViewId();

    /**
     * TODO 初始化布局文件
     */
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();  //解绑黄油刀
    }
}
