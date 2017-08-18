package com.hzease.tomeet.home.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.utils.autoUpdate.AVersionService;
import com.hzease.tomeet.utils.autoUpdate.AutoUpdateService;
import com.hzease.tomeet.utils.autoUpdate.VersionParams;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
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

public class HomeFragment extends BaseFragment implements IHomeContract.View {
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private static final int REQUEST_CODE_PICK_GAME = 666;

    public BottomNavigationView bottomNavigationView;
    @BindView(R.id.tv_home_cityname_fmt)
    TextView tv_home_cityname_fmt;
    @BindView(R.id.ll_home_chosecity_fmt)
    LinearLayout ll_home_chosecity_fmt;
    @BindView(R.id.tv_home_select_fmt)
    TextView tv_home_select_fmt;
    @BindView(R.id.iv_home_addroom_fmt)
    ImageView iv_home_addroom_fmt;
    @BindView(R.id.lv_home_rooms_fmt)
    RecyclerView rv_home_rooms_fmt;
    @BindView(R.id.home_swiperefreshlayout)
    SwipeRefreshLayout home_swiperefreshlayout;
    @BindView(R.id.iv_isVip_home_fmt)
    ImageView iv_isVip_home_fmt;
    // 头像
    @BindView(R.id.iv_avatar_home_fmt)
    ImageView iv_avatar_home_fmt;
    // 昵称
    @BindView(R.id.tv_nickname_home_fmt)
    TextView tv_nickname_home_fmt;
    // 头像 载入 进度条
    @BindView(R.id.pb_login_status_home_fmt)
    ProgressBar pb_login_status_home_fmt;
    //活动分类选择标签
    @BindView(R.id.tfl_home_labels_fmt)
    TagFlowLayout tfl_home_labels_fmt;
    private List<GameTypeBean.ChildrenBean> mGameTypeLabels = new ArrayList<>();
    // 进度框
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;
    /**
     * 创建事务管理器
     */
    FragmentTransaction transaction;
    HomeActivity meActivity;
    List<HomeRoomsBean.DataBean> mDate;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;
    private int gameId = 0;
    private int page = 0;
    private HomeRoomsAdapter adapter;

    // 一次加载的条目数
    private final int LOAD_SIZE = 30;
    private List<HomeRoomsBean.DataBean> tempData = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * 第一次登录时刷新
     *
     * @param userInfoBean 空对象
     */
    public void onEventMainThread(UserInfoBean userInfoBean) {
        getActivity().getIntent().setFlags(0);
        setAvatarAndNickname();
    }

    /**
     * 上线后被踢下线时刷新
     *
     * @param loginInvalid 空对象
     */
    public void onEventMainThread(EventBean.LoginInvalid loginInvalid) {
        getActivity().getIntent().setFlags(0);
        setAvatarAndNickname();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    /**
     * 点击时间的处理
     */
    @OnClick({
            R.id.ll_home_chosecity_fmt,
            R.id.tv_home_select_fmt,
            R.id.iv_home_addroom_fmt,
            R.id.ll_avatar_nickname_home_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home_chosecity_fmt:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
                break;
            case R.id.tv_home_select_fmt:
                //mPresenter.loadGameList("secret", "app.yueyuan.pro");
                startActivityForResult(new Intent(getActivity(), SelectGameTypeActivity.class),
                        REQUEST_CODE_PICK_GAME);
                break;
            case R.id.iv_home_addroom_fmt:
                if (PTApplication.myInfomation != null) {
                    Intent intent = new Intent(getActivity(), CreateRoomBeforeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("circleId", 0);
                    bundle.putBoolean("isOpen", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ToastUtils.getToast(mContext, "请先登录！");
                }

                break;

            // 点击头像和昵称的LL框
            case R.id.ll_avatar_nickname_home_fmt:
                if (PTApplication.myInfomation != null) {
                    Intent intent = new Intent(mContext, PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", PTApplication.myInfomation.getData().getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
        }
    }

    /**
     * 重写onActivityResult
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e(requestCode + "  " + resultCode + "   " + data);
        // 选择城市
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                PTApplication.cityName = city + "市";
                tv_home_cityname_fmt.setText(city);
                mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                tempData.clear();
                if (PTApplication.myInfomation != null){
                    PTApplication.getRequestService().findMyRunningRooms(PTApplication.userToken, PTApplication.userId, 0)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<HomeRoomsBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HomeRoomsBean homeRoomsBean) {
                                    if (homeRoomsBean.isSuccess()) {
                                        tempData.addAll(homeRoomsBean.getData());
                                    }
                                }
                            });
                }
            }
        }
        // 筛选回来
        if (requestCode == REQUEST_CODE_PICK_GAME && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                GameTypeBean.ChildrenBean gameType = (GameTypeBean.ChildrenBean) data.getSerializableExtra(SelectGameTypeActivity.SELECT_GAME_TYPE);
                gameId = gameType.getId();
                if (gameType.getId() != 0) {
                    int size = mGameTypeLabels.size();
                    // 查找相同的
                    for (int i = 0; i < size; i++) {
                        if (mGameTypeLabels.get(i).getId() == gameType.getId()) {
                            mGameTypeLabels.remove(i);
                            size -= 1;
                            break;
                        }
                    }
                    // 删除多出来的最后一个
                    if (size > 4) {
                        for (int i = size; i > 4; i--) {
                            mGameTypeLabels.remove(i - 1);
                        }
                    }
                    mGameTypeLabels.add(1, gameType);
                    SpUtils.saveString(mContext, AppConstants.TOMEET_SP_FILTRATE_GAME_TYPE_MEMORY, new Gson().toJson(mGameTypeLabels));
                    tfl_home_labels_fmt.getAdapter().notifyDataChanged();
                }
                mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                tempData.clear();
                if (PTApplication.myInfomation != null){
                    PTApplication.getRequestService().findMyRunningRooms(PTApplication.userToken, PTApplication.userId, 0)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<HomeRoomsBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HomeRoomsBean homeRoomsBean) {
                                    if (homeRoomsBean.isSuccess()) {
                                        tempData.addAll(homeRoomsBean.getData());
                                    }
                                }
                            });
                }

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 创建房间不属于遮挡。
    }

    @Override
    public void onResume() {
        super.onResume();
        // 脚标会错
        //mPresenter.loadAllRooms(PTApplication.cityName, gameId, "",  PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(188);
                initLogLat();
            }
        }).start();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        if ("splash".equals(getActivity().getIntent().getStringExtra("from"))) {
            // 版本检测升级
            VersionParams versionParams = new VersionParams().setRequestUrl("http://tomeet-app.hzease.com/application/findOne?platform=android");
            Intent intent = new Intent(mContext, AutoUpdateService.class);
            intent.putExtra(AVersionService.VERSION_PARAMS_KEY, versionParams);
            mContext.startService(intent);
        }


        mDate = new ArrayList<>();
        // 读取本地保存的标签
        initLebelsDatas();
        // 填充
        initLabel();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
        }

        adapter = new HomeRoomsAdapter();
        adapter.setOnItemClickLitener(new HomeRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, HomeRoomsBean.DataBean roomBean) {
                if (PTApplication.myInfomation != null) {
                    String roomId = String.valueOf(roomBean.getId());
                    if (roomBean.isLocked()) {
                        for (HomeRoomsBean.DataBean.JoinMembersBean joinMembersBean : roomBean.getJoinMembers()) {
                            if (joinMembersBean.getId() == PTApplication.myInfomation.getData().getId()) {
                                mPresenter.canIJoinTheRoom(roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD);
                                return;
                            }
                        }
                        initPopupWindow(view, roomId);
                    } else {
                        mPresenter.canIJoinTheRoom(roomId, "");
                    }
                } else {
                    ToastUtils.getToast(mContext, "请先登录！");
                }
            }
        });

        rv_home_rooms_fmt.setAdapter(adapter);

        setAvatarAndNickname();

        /**
         * 获取当前activity
         */
        meActivity = (HomeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView =  getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        rv_home_rooms_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home_rooms_fmt.addItemDecoration(new SpacesItemDecoration(20));

        // 获取城市
        tv_home_cityname_fmt.setText(PTApplication.cityName.replace("市", ""));

        home_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        initLogLat();
                    }
                }, 50);

            }
        });

        rv_home_rooms_fmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastCompletelyVisibleItem;
            int firstCompletelyVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastCompletelyVisibleItem + 1 == adapter.getItemCount()
                            && firstCompletelyVisibleItem != 0
                            && adapter.getmLoadMoreStatus() != HomeRoomsAdapter.NO_LOAD_MORE
                            ) {
                        adapter.changeMoreStatus(HomeRoomsAdapter.LOADING_MORE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, ++page, LOAD_SIZE, "distance", 0, true);
                            }
                        }, 300);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                // 第一个完全可见
                firstCompletelyVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            }
        });


        // 在onResume()中的start中调用
        // setAvatarAndNickname();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.REQUEST_LOCATION_PERMISSION);
        }

        //PTApplication.showLoadingDialog();
    }

    //初始化标签数据
    private void initLebelsDatas() {
        String gameTypeList = SpUtils.getStringValue(mContext, AppConstants.TOMEET_SP_FILTRATE_GAME_TYPE_MEMORY);
        if (!TextUtils.isEmpty(gameTypeList)) {
            mGameTypeLabels = new Gson().fromJson(gameTypeList, new TypeToken<List<GameTypeBean.ChildrenBean>>() {
            }.getType());
        } else {
            GameTypeBean.ChildrenBean allGameType = new GameTypeBean.ChildrenBean();
            allGameType.setId(0);
            allGameType.setName("全部分类");
            mGameTypeLabels.add(0, allGameType);
        }
    }

    //填充标签
    private void initLabel() {
        tfl_home_labels_fmt.setAdapter(new TagAdapter<GameTypeBean.ChildrenBean>(mGameTypeLabels) {
            @Override
            public View getView(FlowLayout parent, int position, GameTypeBean.ChildrenBean game) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.tv_home_label, null);
                textView.setText(game.getName());
                return textView;
            }
        });

        // 点击事件
        tfl_home_labels_fmt.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                gameId = mGameTypeLabels.get(position).getId();
                tempData.clear();
                mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                if (PTApplication.myInfomation != null){
                    PTApplication.getRequestService().findMyRunningRooms(PTApplication.userToken, PTApplication.userId, gameId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<HomeRoomsBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HomeRoomsBean homeRoomsBean) {
                                    if (homeRoomsBean.isSuccess()) {
                                        tempData.addAll(homeRoomsBean.getData());
                                    }
                                }
                            });
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstants.REQUEST_LOCATION_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("定位权限申请成功");
                    initLogLat();
                } else {
                    ToastUtils.getToast(mContext, "定位权限被禁止,无法定位当前位置");
                }
                break;
        }
    }

    @Override
    public void setAvatarAndNickname() {
        Logger.w("Flags: " + getActivity().getIntent().getFlags() + "  from: " + getActivity().getIntent().getStringExtra("from") + "\nmyInfomation: " + PTApplication.myInfomation);
        if (PTApplication.myLoadingStatus != AppConstants.YY_PT_LOGIN_LOADING) {
            pb_login_status_home_fmt.setVisibility(View.GONE);
            String nickName = "登录";
            if (PTApplication.myInfomation != null) {
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(iv_avatar_home_fmt);
                iv_avatar_home_fmt.setVisibility(View.VISIBLE);
                nickName = PTApplication.myInfomation.getData().getNickname();
                if (PTApplication.myInfomation.getData().isIsVip()) {
                    iv_isVip_home_fmt.setVisibility(View.VISIBLE);
                } else {
                    iv_isVip_home_fmt.setVisibility(View.GONE);
                }
            } else {
                iv_avatar_home_fmt.setVisibility(View.GONE);
            }
            tv_nickname_home_fmt.setText(nickName);
            tv_nickname_home_fmt.setVisibility(View.VISIBLE);
        } else {
            pb_login_status_home_fmt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 改变加载动画显隐
     *
     * @param isShown 是否显示
     */
    @Override
    public void changeLoadView(boolean isShown) {
        if (isShown) {
            if (load_View.getVisibility() == View.GONE) {
                load_View.setVisibility(View.VISIBLE);
            }
        } else {
            if (load_View.getVisibility() == View.VISIBLE) {
                load_View.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 进入房间
     *
     * @param roomId
     * @param password
     */
    @Override
    public void joinTheRoom(String roomId, String password) {
        startActivity(new Intent(mContext, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
    }


    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void initRoomsList(boolean isSuccess, final List<HomeRoomsBean.DataBean> date, boolean isLoadMore) {
        if (home_swiperefreshlayout != null && !isLoadMore) {
            home_swiperefreshlayout.setRefreshing(false);
            mDate.clear();
        }
        if (isSuccess) {
            if (isLoadMore) {
                adapter.getList().addAll(date);
                //设置回到上拉加载更多
                if (date.size() == LOAD_SIZE) {
                    Logger.i("date.size():  " + date.size());
                    adapter.changeMoreStatus(HomeRoomsAdapter.PULLUP_LOAD_MORE);
                } else {
                    adapter.changeMoreStatus(HomeRoomsAdapter.NO_LOAD_MORE);
                }
            } else {
                mDate.addAll(tempData);
                mDate.addAll(date);
                adapter.setList(mDate);
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.getToast(mContext, "数据加载失败，请重试");
        }
    }
    //加载当前位置
    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                PTApplication.myLongitude = aMapLocation.getLongitude();
                PTApplication.myLatitude = aMapLocation.getLatitude();
                Logger.w("Home界面：\n经度: " + PTApplication.myLongitude + "\n维度: " + PTApplication.myLatitude + "\n地址： " + aMapLocation.getAddress());
                mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                tempData.clear();
                if (PTApplication.myInfomation != null){
                    PTApplication.getRequestService().findMyRunningRooms(PTApplication.userToken, PTApplication.userId, gameId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<HomeRoomsBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HomeRoomsBean homeRoomsBean) {
                                    if (homeRoomsBean.isSuccess()) {
                                        tempData = homeRoomsBean.getData();
                                    }
                                }
                            });
                }

            }
        });
    }

    //弹出输入密码pop
    private void initPopupWindow(View view, final String roomId) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_inputpwd, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = meActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        meActivity.getWindow().setAttributes(wlBackground);
        meActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                meActivity.getWindow().setAttributes(wlBackground);
                meActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        final EditText pwdString =  contentView.findViewById(R.id.et_joinroom_pwd_pop);
        Button joinRoom =  contentView.findViewById(R.id.bt_joinroom_join_fmt);
        ImageView cancel =  contentView.findViewById(R.id.iv_cancel_pop_fmt);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwdString.getText().toString().trim();
                popupWindow.dismiss();
                mPresenter.canIJoinTheRoom(roomId, pwd);
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
}
