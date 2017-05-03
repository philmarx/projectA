package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class MeFragment extends BaseFragment implements IMeContract.View {
    @BindView(R.id.myrecycle)
    RecyclerView myrecycle;
    @BindView(R.id.mybalance)
    AutoLinearLayout mybalance;
    @BindView(R.id.iv_me_setting_fmt)
    ImageView iv_me_setting_fmt;
    @BindView(R.id.tv_me_nickname_fmt)
    TextView tvMeNickNameFmt;
    @BindView(R.id.tv_me_amount_fmt)
    TextView tvMeAmountFmt;
    @BindView(R.id.tv_me_freeze_fmt)
    TextView tv_me_freeze_fmt;
    @BindView(R.id.ll_me_seemyprops_fmt)
    AutoLinearLayout rl_me_seemyprops_fmt;
    /**
     * 头像
     */
    @BindView(R.id.iv_avatar_me_fmt)
    ImageView iv_avatar_me_fmt;


    public BottomNavigationView bottomNavigationView;

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private List<MyJoinRoomBean.DataBean> mDatas;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    public MeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }
    public static MeFragment newInstance() {
        return new MeFragment();
    }


    @OnClick({
            R.id.mybalance,
            R.id.iv_me_setting_fmt,
            R.id.tv_me_nickname_fmt,
            R.id.ll_me_seemyprops_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mybalance:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(1));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.iv_me_setting_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.tv_me_nickname_fmt:
                Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",Long.valueOf(PTApplication.userId));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_me_seemyprops_fmt:
                transaction.replace(R.id.fl_content_me_activity,meActivity.mFragmentList.get(8));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_mebak;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        if (PTApplication.myInfomation != null) {
            /**
             * 显示我的信息
             */
            mPresenter.loadMyInfo();
            /**
             * 显示我加入的活动
             */
            mPresenter.getMyJoinRooms(0,10,PTApplication.userToken,PTApplication.userId);
        }

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        /**
         * 获取当前activity
         */
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示我的信息
     */
    @Override
    public void showMyInfo() {
        tvMeNickNameFmt.setText(PTApplication.myInfomation.getData().getNickname());
        tvMeAmountFmt.setText(String.valueOf(PTApplication.myInfomation.getData().getAmount()));
        tv_me_freeze_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getLockAmount()));
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(iv_avatar_me_fmt);
    }

    /**
     * 显示我的加入过的房间
     * @param myJoinRoomBean
     */
    @Override
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {
        mDatas = myJoinRoomBean.getData();
        myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecycle.addItemDecoration(new SpacesItemDecoration(20));
        MyJoinRoomsAdapter adapter = new MyJoinRoomsAdapter(myJoinRoomBean.getData(),getContext());
        myrecycle.setAdapter(adapter);
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

    /**
     * 认证成功
     */
    @Override
    public void authorizedSuccess() {

    }
}
