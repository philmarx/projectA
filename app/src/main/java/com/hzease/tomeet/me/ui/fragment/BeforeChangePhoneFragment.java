package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by xuq on 2017/7/4.
 */

public class BeforeChangePhoneFragment extends BaseFragment {
    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    @BindView(R.id.tv_setting_changphone_oldphone_fmt)
    TextView tv_setting_changphone_oldphone_fmt;
    @BindView(R.id.et_setting_changphone_oldphone_fmt)
    EditText et_setting_changphone_oldphone_fmt;
    @BindView(R.id.bt_me_next_fmt)
    Button bt_me_next_fmt;

    @OnClick({
            R.id.bt_me_next_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_me_next_fmt:
                String phone = et_setting_changphone_oldphone_fmt.getText().toString().trim();
                if (phone.isEmpty()) {
                    ToastUtils.getToast(mContext, "请输入手机号码");
                    break;
                }
                if (phone.equals(PTApplication.myInfomation.getData().getPhone())){
                    transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(5));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    ToastUtils.getToast(mContext, "请输入正确的手机号码");
                }
                break;
        }
    }

    public static BeforeChangePhoneFragment newInstance() {
        return new BeforeChangePhoneFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_beforechangephone;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        tv_setting_changphone_oldphone_fmt.setText("当前手机号码为" + PTApplication.myInfomation.getData().getPhone().substring(0,4)+"****" + PTApplication.myInfomation.getData().getPhone().substring(7,11));
    }
}
