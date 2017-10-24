package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuq on 2017/8/18.
 */

public class AuthenSuccess extends BaseFragment {

    public static AuthenSuccess newInstance(){
        return new AuthenSuccess();
    }
    @BindView(R.id.tv_realname_fmt)
    TextView tv_realname_fmt;
    @BindView(R.id.tv_realIDCard_fmt)
    TextView tv_realIDCard_fmt;
    @OnClick({R.id.iv_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_authensuccess;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String idCard = SpUtils.getStringValue(mContext, "IDCard");
        String realName = SpUtils.getStringValue(mContext,"realName");
        tv_realname_fmt.setText(realName);
        tv_realIDCard_fmt.setText(idCard.replaceAll("(\\d{3})\\d{11}(\\w{4})","$1*****$2"));
    }
}
