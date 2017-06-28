package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.widget.adapters.ClearLabelsAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Created by xuq on 2017/6/28.
 */

public class ClearLabelsFragment extends BaseFragment {
    @BindView(R.id.tv_clearlabels_member_fmt)
    TextView tv_clearlabels_member_fmt;
    @BindView(R.id.lv_clearlabels_fmt)
    ListView lv_clearlabels_fmt;
    private int member;

    public static ClearLabelsFragment newInstance(int member){
        ClearLabelsFragment clearLabelsFragment = new ClearLabelsFragment();
        clearLabelsFragment.member = member;
        return clearLabelsFragment;
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_clearlabels;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_clearlabels_member_fmt.setText("X" + member);
        lv_clearlabels_fmt.setAdapter(new ClearLabelsAdapter(mContext,getActivity()));
    }

}
