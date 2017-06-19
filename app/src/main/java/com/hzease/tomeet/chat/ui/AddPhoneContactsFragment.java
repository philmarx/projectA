package com.hzease.tomeet.chat.ui;

import android.os.Bundle;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;

import butterknife.OnClick;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class AddPhoneContactsFragment extends BaseFragment {

    public AddPhoneContactsFragment() {
    }

    public static AddPhoneContactsFragment newInstance() {
        return new AddPhoneContactsFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_add_phone_contacts;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.all_add_friend_fmt)
    public void onViewClicked() {
        // read_contacts
    }
}
