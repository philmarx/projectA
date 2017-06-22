package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;

/**
 * Created by xuq on 2017/6/22.
 */

public class FreezeBalanceFragment extends BaseFragment {
    public static FreezeBalanceFragment newInstance(){
        return new FreezeBalanceFragment();
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_freezebalance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
