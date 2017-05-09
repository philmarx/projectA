package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
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
    FragmentManager fragmentManager;

    @BindView(R.id.myrecycle)
    SuperRecyclerView myrecycle;
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
    private List<HomeRoomsBean.DataBean> mDatas = new ArrayList<>();
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    private MyJoinRoomsAdapter adapter;
    private int page=0;

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
            mPresenter.getMyJoinRooms(0,10,PTApplication.userToken,PTApplication.userId,false);
            myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            myrecycle.addItemDecoration(new SpacesItemDecoration(20));
            myrecycle.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.getMyJoinRooms(++page,10,PTApplication.userToken,PTApplication.userId,true);
                        }
                    },2000);
                }
            },1);

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
    public void showMyRooms(HomeRoomsBean myJoinRoomBean,boolean isLoadMore) {
        if (myJoinRoomBean.getData() == null){
            myrecycle.hideMoreProgress();
        }else{
            if (isLoadMore){
                if (myJoinRoomBean.getData().size() >= 10){
                    mDatas.addAll(myJoinRoomBean.getData());
                    adapter.notifyDataSetChanged();
                }else{
                    mDatas.addAll(myJoinRoomBean.getData());
                    adapter.notifyDataSetChanged();
                    myrecycle.hideMoreProgress();
                    myrecycle.removeMoreListener();
                }
            }else{
                mDatas = myJoinRoomBean.getData();
                adapter = new MyJoinRoomsAdapter(mDatas,PTApplication.getInstance());
                myrecycle.setAdapter(adapter);
            }
        }
        /*Logger.e("22222");
        if (isLoadMore){
            Logger.e("33333");
            if (myJoinRoomBean.getData().size()>8){
                Logger.e("666666");
                mDatas.addAll(myJoinRoomBean.getData());
                adapter.notifyDataSetChanged();
               myrecycle.hideMoreProgress();
                myrecycle.removeMoreListener();
            }
        }else{
            Lo   }else{
                Logger.e("44444");
                mDatas.addAll(myJoinRoomBean.getData());
                adapter.notifyDataSetChanged();
          gger.e("55555");
            mDatas = myJoinRoomBean.getData();
            adapter = new MyJoinRoomsAdapter(mDatas,getContext());
        }*/

        adapter.setOnItemClickLitener(new MyJoinRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                int state = mDatas.get(position).getState();
                switch (state){
                    case 0:
                    case 1:
                    case 2:
                        //TODO 直接打开聊天室
                        break;
                    case 3:
                        //TODO 打开待评价界面
                        break;
                    case 4:
                        //TODO 打开结束界面
                        //replaceFragment(meActivity.mFragmentList.get(9));
                        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
                        fragmentManager =meActivity.getSupportFragmentManager();
                        // 2.开启一个事务，通过调用beginTransaction()方法开启
                        transaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putLong("roomId",mDatas.get(position).getId());
                        meActivity.mFragmentList.get(9).setArguments(bundle);
                        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
                        transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(9));  //fr_container不能为fragment布局，可使用线性布局相对布局等。
                        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
                        transaction.addToBackStack(null);
                        // 5.提交事物,调用commit()方法来完成
                        transaction.commit();
                        break;
                }
            }
        });
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

    /**
     * 显示结束房间信息
     *
     * @param data
     */
    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    //Fragment启动方法：
    private void replaceFragment(Fragment fragment) {
        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager =meActivity.getSupportFragmentManager();
        // 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fl_content_me_activity, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        transaction.addToBackStack(null);
        // 5.提交事物,调用commit()方法来完成
        transaction.commit();
    }
}
