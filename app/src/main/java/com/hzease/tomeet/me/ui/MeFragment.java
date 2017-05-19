package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    @BindView(R.id.me_swiperefreshlayout)
    SwipeRefreshLayout me_swiperefreshlayout;
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
    private List<MyJoinRoomsBean.DataBean> mDatas = new ArrayList<>();
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    private MyJoinRoomsAdapter adapter;
    private int page=0;
    private ArrayList<MyJoinRoomsBean.DataBean> footDatas;

    public MeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Logger.e("onResume");
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
             * 显示我加入的活动
             */
            mPresenter.getMyJoinRooms(0,15,PTApplication.userToken,PTApplication.userId,false);
            myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            myrecycle.addItemDecoration(new SpacesItemDecoration(20));
        }

        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        me_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadMyInfo();
                        mPresenter.getMyJoinRooms(0,15,PTApplication.userToken,PTApplication.userId,false);
                    }
                }, 2000);
            }
        });
        myrecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(adapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footDatas = new ArrayList<MyJoinRoomsBean.DataBean>();
                            mPresenter.getMyJoinRooms(++page,15,PTApplication.userToken,PTApplication.userId,true);
                        }
                    }, 2000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
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
        tvMeAmountFmt.setText(String.format("%.2f", PTApplication.myInfomation.getData().getAmount()/100.0));
        tv_me_freeze_fmt.setText(String.format("%.2f", PTApplication.myInfomation.getData().getLockAmount()/100.0));
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(iv_avatar_me_fmt);
    }

    /**
     * 显示我的加入过的房间
     * @param myJoinRoomBean
     */
    @Override
    public void showMyRooms(final MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {
        if (isLoadMore){
            footDatas.addAll(myJoinRoomBean.getData());
            adapter.AddFooterItem(footDatas);
            if (myJoinRoomBean.getData().size()==15){
                adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
            }else{
                adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
                adapter.changeMoreStatus(adapter.NO_LOAD_MORE);
            }
        }else{
            mDatas = myJoinRoomBean.getData();
            Collections.sort(mDatas, new Comparator<MyJoinRoomsBean.DataBean>() {
                @Override
                public int compare(MyJoinRoomsBean.DataBean o1, MyJoinRoomsBean.DataBean o2) {
                    if (o1.getState()>o2.getState()){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });
            adapter = new MyJoinRoomsAdapter(mDatas,PTApplication.getInstance());
            myrecycle.setAdapter(adapter);
            me_swiperefreshlayout.setRefreshing(false);
        }

        adapter.setOnItemClickLitener(new MyJoinRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                int state = mDatas.get(position).getState();
                switch (state){
                    case 0:
                    case 1:
                    case 2:
                        //直接打开聊天室
                        startActivity(new Intent(mContext, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID,String.valueOf(mDatas.get(position).getId())));
                        break;
                    case 3:
                        if (!mDatas.get(position).isEvaluated()){
                            //打开待评价界面
                            //replaceFragment(meActivity.mFragmentList.get(9));
                            // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
                            fragmentManager =meActivity.getSupportFragmentManager();
                            // 2.开启一个事务，通过调用beginTransaction()方法开启
                            transaction = fragmentManager.beginTransaction();
                            Bundle bundle1 = new Bundle();
                            bundle1.putLong("roomId",mDatas.get(position).getId());
                            meActivity.mFragmentList.get(10).setArguments(bundle1);
                            Logger.e(mDatas.get(position).getId()+"");
                            // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
                            transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(10));  //fr_container不能为fragment布局，可使用线性布局相对布局等。
                            // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
                            transaction.addToBackStack(null);
                            // 5.提交事物,调用commit()方法来完成
                            transaction.commit();
                        }else{
                            ToastUtils.getToast(getContext(),"请等待其他玩家评价！");
                        }
                        break;
                    case 4:
                        //打开结束界面
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

    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {
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
