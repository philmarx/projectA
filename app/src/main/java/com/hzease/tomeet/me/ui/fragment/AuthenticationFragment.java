package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.MatchUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class AuthenticationFragment extends BaseFragment implements IMeContract.View {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.et_me_realname_fmt)
    EditText et_me_realname_fmt;
    @BindView(R.id.et_me_IDCard_fmt)
    EditText et_me_IDCard_fmt;
    @BindView(R.id.bt_me_submit_fmt)
    Button bt_me_submit_fmt;


    @OnClick(R.id.bt_me_submit_fmt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_me_submit_fmt:
                String realName = et_me_realname_fmt.getText().toString().trim();
                String IDCard = et_me_IDCard_fmt.getText().toString().trim();
                MatchUtils.isRealName(realName);
                if (MatchUtils.isIDCard(IDCard)){
                    Logger.e("身份证号码输入正确");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }
    @Override
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {

    }


    /**
     * 更新密码成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 提交反馈成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_authentication;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}

