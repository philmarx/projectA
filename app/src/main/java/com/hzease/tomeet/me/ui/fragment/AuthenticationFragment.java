package com.hzease.tomeet.me.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.MatchUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class AuthenticationFragment extends BaseFragment implements IMeContract.View {

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
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
    private String realName;


    @OnClick(R.id.bt_me_submit_fmt)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_me_submit_fmt:
                realName = et_me_realname_fmt.getText().toString().trim();
                String IDCard = et_me_IDCard_fmt.getText().toString().trim();
                MatchUtils.isRealName(realName);
                if (MatchUtils.isIDCard(IDCard)){
                    Logger.e("身份证号码输入正确");
                    mPresenter.authorized(IDCard,realName,PTApplication.userToken,PTApplication.userId);
                    transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(2));
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    transaction.commit();
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
    public void showMyRooms(HomeRoomsBean myJoinRoomBean) {

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
    public void authorizedSuccess() {
        ToastUtils.getToast(PTApplication.getInstance(),"认证成功!!!");
        SharedPreferences sp = meActivity.getSharedPreferences("game_name", Context.MODE_PRIVATE);
        sp.edit().putBoolean("isAuthorizedSuccess",true).putString("authorizedName",realName).commit();
        meActivity.getSupportFragmentManager().popBackStack();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_authentication;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
    }
}

