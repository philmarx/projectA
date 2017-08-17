package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.me.ui.fragment.FreezeBalanceFragment;
import com.hzease.tomeet.me.ui.fragment.ShareFragment;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.iv_me_isVip_fmt)
    ImageView iv_me_isVip_fmt;
    @BindView(R.id.tv_tosetID_fmt)
    TextView tv_tosetID_fmt;
    /**
     * 头像
     */
    @BindView(R.id.iv_avatar_me_fmt)
    ImageView iv_avatar_me_fmt;

    /**
     * 分享
     */
    @BindView(R.id.iv_share_me_fmt)
    ImageView iv_share_me_fmt;
    @BindView(R.id.ll_no_myjoinroom)
    LinearLayout ll_no_myjoinroom;

    // 一次加载的条目数
    private final int LOAD_SIZE = 30;

    public BottomNavigationView bottomNavigationView;

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
            R.id.myfreezebalance,
            R.id.iv_me_setting_fmt,
            R.id.tv_me_nickname_fmt,
            R.id.ll_me_seemyprops_fmt,
            R.id.all_me_smallpaper_fmt,
            // 分享按钮
            R.id.iv_share_me_fmt,
            R.id.iv_avatar_me_fmt,
            R.id.tv_tosetID_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            // 我的钱包
            case R.id.mybalance:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(1));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.myfreezebalance:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, FreezeBalanceFragment.newInstance());
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
            case R.id.iv_avatar_me_fmt:
                Intent intent2 = new Intent(getActivity(), PersonOrderInfoActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putLong("userId",Long.valueOf(PTApplication.userId));
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.ll_me_seemyprops_fmt:
                transaction.replace(R.id.fl_content_me_activity,meActivity.mFragmentList.get(8));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.all_me_smallpaper_fmt:
                Intent startSmallPaper = new Intent(getActivity(),MySmallPaperActivity.class);
                startActivity(startSmallPaper);
                break;
            // 分享
            case R.id.iv_share_me_fmt:
                transaction.replace(R.id.fl_content_me_activity, ShareFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            //设置ID
            case R.id.tv_tosetID_fmt:
                initPopupWindow(v);
                break;
        }
    }

    private void initPopupWindow(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_setid, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        final EditText account =  contentView.findViewById(R.id.et_user_id_fmt);
        Button setAccount =  contentView.findViewById(R.id.bt_setid_true_fmt);
        ImageView cancel =  contentView.findViewById(R.id.iv_cancel_fmt);
        setAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myAccount = account.getText().toString().trim();
                if (myAccount.length() >=5 && myAccount.length() <= 10){
                    mPresenter.setAccount(myAccount,PTApplication.userToken,PTApplication.userId);
                    popupWindow.dismiss();
                }else{
                    ToastUtils.getToast(mContext,"请输入正确的账号哦~");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
        // 查看第三方绑定状态
        PTApplication.getRequestService().getThirdPartyBindingState(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MapDataBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(MapDataBean mapDataBean) {
                        if (mapDataBean.isSuccess()) {
                            Logger.e(mapDataBean.getData().toString() + "\n" + mapDataBean.getData().get(AppConstants.AUTHORIZED_LOGIN_WX));
                        }
                    }
                });
        // 底部导航栏
        bottomNavigationView =  getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        if (PTApplication.myInfomation != null) {
            // 显示我加入的活动
            mPresenter.getMyJoinRooms(0,LOAD_SIZE,PTApplication.userToken,PTApplication.userId,false);
            myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            myrecycle.addItemDecoration(new SpacesItemDecoration(20));
        }

        adapter = new MyJoinRoomsAdapter(PTApplication.getInstance());
        adapter.setOnItemClickLitener(new MyJoinRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, MyJoinRoomsBean.DataBean userBean) {
                int state = userBean.getState();
                switch (state){
                    case 0:
                    case 1:
                    case 2:
                        //直接打开聊天室
                        mPresenter.setOnline(true, String.valueOf(userBean.getId()));
                        startActivity(new Intent(mContext, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID,String.valueOf(userBean.getId())));
                        break;
                    case 3:
                        if (!userBean.isEvaluated()){
                            //打开待评价界面
                            //replaceFragment(meActivity.mFragmentList.get(9));
                            // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
                            fragmentManager =meActivity.getSupportFragmentManager();
                            // 2.开启一个事务，通过调用beginTransaction()方法开启
                            transaction = fragmentManager.beginTransaction();
                            Bundle bundle1 = new Bundle();
                            bundle1.putLong("roomId", userBean.getId());
                            meActivity.mFragmentList.get(9).setArguments(bundle1);
                            Logger.e(userBean.getId()+"");
                            // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
                            transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(9));  //fr_container不能为fragment布局，可使用线性布局相对布局等。
                            // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
                            transaction.addToBackStack(null);
                            // 5.提交事物,调用commit()方法来完成
                            transaction.commit();
                        }else{
                            ToastUtils.getToast(getContext(),"请等待其他玩家评价！");
                        }
                        break;
                    case 4:
                        Intent intent = new Intent(mContext,GameFinishActivity.class);
                        intent.putExtra("roomId",userBean.getId());
                        startActivity(intent);
                        break;
                }
            }
        });
        myrecycle.setAdapter(adapter);

        // 下啦刷新
        me_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadMyInfo();
                        page = 0;
                        mPresenter.getMyJoinRooms(0, LOAD_SIZE, PTApplication.userToken, PTApplication.userId, false);
                    }
                }, 10);
            }
        });

        // 上拉加载
        myrecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == adapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && adapter.getmLoadMoreStatus() != adapter.NO_LOAD_MORE
                            ) {
                        adapter.changeMoreStatus(adapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getMyJoinRooms(++page, LOAD_SIZE, PTApplication.userToken, PTApplication.userId, true);
                            }
                        }, 200);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastCompletelyVisibleItem=layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
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
        if (TextUtils.isEmpty(PTApplication.myInfomation.getData().getAccount())){
            tv_tosetID_fmt.setText("点击设置ID");
            tv_tosetID_fmt.setEnabled(true);
        }else{
            tv_tosetID_fmt.setText("ID:" + PTApplication.myInfomation.getData().getAccount());
            tv_tosetID_fmt.setEnabled(false);
        }
        // 头像
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(iv_avatar_me_fmt);
        Logger.e("isVip" + PTApplication.myInfomation.getData().isIsVip());
        if (PTApplication.myInfomation.getData().isIsVip()){
            iv_me_isVip_fmt.setVisibility(View.VISIBLE);
        }else {
            iv_me_isVip_fmt.setVisibility(View.GONE);
        }
    }

    /**
     * 显示我的加入过的房间
     * @param myJoinRoomBean
     */
    @Override
    public void showMyRooms(final MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {
        if (isLoadMore){
            adapter.getList().addAll(myJoinRoomBean.getData());
            if (myJoinRoomBean.getData().size() == LOAD_SIZE){
                adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
            }else{
                adapter.changeMoreStatus(adapter.NO_LOAD_MORE);
            }
        }else{
            if (myJoinRoomBean.getData().size()>0) {

            /*Collections.sort(myJoinRoomBean.getData(), new Comparator<MyJoinRoomsBean.DataBean>() {
                @Override
                public int compare(MyJoinRoomsBean.DataBean o1, MyJoinRoomsBean.DataBean o2) {
                    if (o1.getState()>o2.getState()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });*/
                adapter.setList(myJoinRoomBean.getData());
                me_swiperefreshlayout.setRefreshing(false);
            }else{
                ll_no_myjoinroom.setVisibility(View.VISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
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

    /**
     * 显示道具数量
     *
     * @param data
     */
    @Override
    public void showPropsMum(PropsMumBean.DataBean data) {

    }

    /**
     * 修改昵称成功
     */
    @Override
    public void showChangeNameSuccess() {

    }

    /**
     * 显示购买道具结果
     *
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index,boolean success, String msg) {

    }

    /**
     * 查看设置账号结果
     * @param noDataBean
     */
    @Override
    public void initResult(NoDataBean noDataBean) {
        if (noDataBean.isSuccess()){
            ToastUtils.getToast(mContext,"设置账号成功");
            mPresenter.loadMyInfo();
        }else{
            ToastUtils.getToast(mContext,noDataBean.getMsg());
        }
    }
}
