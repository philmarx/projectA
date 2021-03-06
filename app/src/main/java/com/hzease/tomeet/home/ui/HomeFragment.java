package com.hzease.tomeet.home.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hzease.tomeet.BaseActivity;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.ModifityPicActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.Bind3Part;
import com.hzease.tomeet.data.EventBean;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.data.HomeActivityBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.ShareWebViewActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.CountDownButtonHelper;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.utils.autoUpdate.AVersionService;
import com.hzease.tomeet.utils.autoUpdate.AutoUpdateService;
import com.hzease.tomeet.utils.autoUpdate.VersionParams;
import com.hzease.tomeet.widget.IdentifyingCodeView;
import com.hzease.tomeet.widget.SpacesItemTopBottomDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
    @BindView(R.id.ll_ishavadata)
    LinearLayout ll_ishavadata;
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
    private List<GameTypeBean.ChildrenBean> mGameTypeLabels;

    private CountDownButtonHelper helper;
    /**
     * 创建事务管理器
     */
    FragmentTransaction transaction;
    HomeActivity homeActivity;
    List<HomeRoomsBean.DataBean> mDate;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;
    private String gameId = "0";
    private int page = 0;
    private HomeRoomsAdapter adapter;

    // 一次加载的条目数
    private final int LOAD_SIZE = 30;
    private List<HomeRoomsBean.DataBean> tempData;
    private String phoneNum;
    private Boolean isBindQQ;
    private Boolean isBindWechat;
    private List<HomeActivityBean.DataBean> mActivityList;
    private PopupWindow popupWindow4activity;
    private PopupWindow popupWindow4pwd;
    private PopupWindow popupWindow4bindPhone;
    private PopupWindow initSmsCodepopupWindow;
    private PopupWindow popupWindow4bind3part;


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
                if (PTApplication.myInfomation != null){
                    Intent intent = new Intent(getActivity(),ChooseTypeActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtils.getToast("请先登录！");
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
                tv_home_cityname_fmt.setText(city + "市");
                if (gameId.length() == 12) {
                    loadCircleRoom(0, false);
                } else {
                    mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                }
            }
        }
        // 筛选回来
        if (requestCode == REQUEST_CODE_PICK_GAME && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                GameTypeBean.ChildrenBean gameType = (GameTypeBean.ChildrenBean) data.getSerializableExtra(SelectGameTypeActivity.SELECT_GAME_TYPE);
                gameId = gameType.getId();
                if (!gameType.getId().equals("0")) {
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
                if (gameId.length() == 12) {
                    loadCircleRoom(0, false);
                } else {
                    mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow4activity != null) {
            popupWindow4activity.dismiss();
        }
        if (popupWindow4pwd != null){
            popupWindow4pwd.dismiss();
        }
        if (popupWindow4bind3part != null){
            popupWindow4bind3part.dismiss();
        }
        if (popupWindow4bindPhone != null){
            popupWindow4bindPhone.dismiss();
        }
        if (initSmsCodepopupWindow != null){
            initSmsCodepopupWindow.dismiss();
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        transaction = homeActivity.getSupportFragmentManager().beginTransaction();
        if ("splash".equals(getActivity().getIntent().getStringExtra("from")) && !TextUtils.isEmpty(PTApplication.appVersion)) {
            // 版本检测升级
            VersionParams versionParams = new VersionParams().setRequestUrl("http://tomeet-app.hzease.com/application/findOne?platform=android");
            Intent intent = new Intent(mContext, AutoUpdateService.class);
            intent.putExtra(AVersionService.VERSION_PARAMS_KEY, versionParams);
            intent.putExtra(AVersionService.VERSION_PARAMS_TYPE, AVersionService.AUTOMATIC);
            mContext.startService(intent);
        }

        tempData = new ArrayList<>();
        mGameTypeLabels = new ArrayList<>();
        mDate = new ArrayList<>();

        // 读取本地保存的标签
        initLebelsDatas();
        // 填充
        initLabel();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus: " + getClass());
        }


        adapter = new HomeRoomsAdapter();
        adapter.setOnItemClickLitener(new HomeRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, HomeRoomsBean.DataBean roomBean) {
                if (PTApplication.myInfomation != null) {
                    final String roomId = String.valueOf(roomBean.getId());
                    if (PTApplication.myInfomation.getData().getAvatarSignature().isEmpty()) {
                        ToastUtils.getToast("请先上传头像");
                        Intent intent = new Intent(getActivity(), ModifityPicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("image1Signature", "");
                        bundle.putString("image2Signature", "");
                        bundle.putString("image3Signature", "");
                        bundle.putString("image4Signature", "");
                        bundle.putString("image5Signature", "");
                        bundle.putString("nickname", PTApplication.myInfomation.getData().getNickname());
                        bundle.putString("birthday", PTApplication.myInfomation.getData().getBirthday());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                    if (roomBean.getGame().getId() == 30){
                        //进入聊天室
                        PTApplication.getRequestService().joinRoom(PTApplication.userToken,PTApplication.userId,roomId,"")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        changeLoadView(true);
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        changeLoadView(false);
                                    }
                                })
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()){
                                            Bundle args = new Bundle();
                                            args.putString("chatroomId",roomId);
                                            homeActivity.mFragmentList.get(1).setArguments(args);
                                            transaction.replace(R.id.fl_content_home_activity, homeActivity.mFragmentList.get(1));
                                            transaction.addToBackStack(null);
                                            // 执行事务
                                            transaction.commit();
                                        }else{
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });

                    }else{
                        //进入房间
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
                    }
                } else {
                    ToastUtils.getToast("请先登录！");
                }
            }
        });

        rv_home_rooms_fmt.setAdapter(adapter);

        setAvatarAndNickname();


        bottomNavigationView = getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        rv_home_rooms_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home_rooms_fmt.addItemDecoration(new SpacesItemTopBottomDecoration(15));

        // 获取城市
        tv_home_cityname_fmt.setText(PTApplication.cityName);

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
                                if (gameId.length() == 12) {
                                    ++page;
                                    loadCircleRoom(page, true);
                                } else {
                                    mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, ++page, LOAD_SIZE, "distance", 0, true);
                                }
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long time = SpUtils.getLongValue(PTApplication.getInstance(), AppConstants.TOMEET_SP_AD_TIME);
                mActivityList = SpUtils.getList(PTApplication.getInstance(), "Activity_Pic");
                Logger.e("size   " + mActivityList);
                if (mActivityList != null && mActivityList.size() != 0 && (System.currentTimeMillis() - time) > 1000 * 60 * 60 * 24) {
                    initActivityPop(mRootView);
                }
            }
        }, 50);
        //PTApplication.showLoadingDialog();
    }

    /**
     * 加载圈子标签房间
     */
    public void loadCircleRoom(int page, final boolean isLoadMore) {
        PTApplication.getRequestService().findRoomsByCircle(Long.valueOf(gameId), page, 15, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeRoomsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("findRoomsByCircle Error: " + e);
                    }

                    @Override
                    public void onNext(HomeRoomsBean homeRoomsBean) {
                        if (homeRoomsBean.isSuccess()) {
                            initRoomsList(homeRoomsBean.isSuccess(), homeRoomsBean.getData(), isLoadMore);
                            Logger.e(homeRoomsBean.getData().toString());
                        } else {
                            initRoomsList(false, null, false);
                            ToastUtils.getToast("数据加载失败，请重试");
                        }
                    }
                });
    }

    //初始化标签数据
    private void initLebelsDatas() {
        String gameTypeList = SpUtils.getStringValue(mContext, AppConstants.TOMEET_SP_FILTRATE_GAME_TYPE_MEMORY);
        if (!TextUtils.isEmpty(gameTypeList)) {
            mGameTypeLabels = new Gson().fromJson(gameTypeList, new TypeToken<List<GameTypeBean.ChildrenBean>>() {
            }.getType());
        } else {
            GameTypeBean.ChildrenBean allGameType = new GameTypeBean.ChildrenBean();
            allGameType.setId("0");
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
                if (gameId.length() == 12) {
                    loadCircleRoom(0, false);
                } else {
                    mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
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
                    ToastUtils.getToast("定位权限被禁止,无法定位当前位置");
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
            ((BaseActivity) getActivity()).showLoadingDialog();
        } else {
            ((BaseActivity) getActivity()).hideLoadingDialog();
        }
    }

    @Override
    public void loadChatRoomInfo(GameChatRoomBean gameChatRoomBean) {

    }

    /**
     * 进入房间
     *
     * @param roomId
     * @param password
     */
    @Override
    public void joinTheRoom(NoDataBean noDataBean, String roomId, String password) {
        if (noDataBean.isSuccess()) {
            startActivity(new Intent(mContext, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
        } else {
            ToastUtils.getToast(noDataBean.getMsg());
        }
    }


    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void initRoomsList(boolean isSuccess, final List<HomeRoomsBean.DataBean> date, boolean isLoadMore) {
        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        if (home_swiperefreshlayout != null && !isLoadMore) {
            home_swiperefreshlayout.setRefreshing(false);
            mDate.clear();
        }
        if ("finish".equals(getActivity().getIntent().getStringExtra("from"))) {
            if (TextUtils.isEmpty(PTApplication.myInfomation.getData().getPhone())) {
                //绑定手机号
                initBinPhonePop(mRootView);
            }
            PTApplication.getRequestService().isBind3Part(PTApplication.userToken, PTApplication.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<MapDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(MapDataBean mapDataBean) {
                            if (mapDataBean.isSuccess()) {
                                Map<String, Boolean> data = mapDataBean.getData();
                                Logger.e(data.toString());
                                for (String key : data.keySet()) {
                                    switch (key) {
                                        case "QQ":
                                            isBindQQ = data.get(key);
                                            break;
                                        case "WEIBO":
                                            if (data.get(key)) {
                                            }
                                            break;
                                        case "WECHAT":
                                            isBindWechat = data.get(key);
                                            break;
                                    }
                                }
                            }
                        }
                    });
            if (!(isBindQQ || isBindWechat)) {
                //绑定第三方
                initBin3PartPop(mRootView);
            }
        }

        if (isSuccess) {
            if (isLoadMore) {
                adapter.getList().addAll(date);
                Logger.e("size           " + adapter.getList().size());
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
                if (mDate.size() == 0) {
                    ll_ishavadata.setVisibility(View.VISIBLE);
                    rv_home_rooms_fmt.setVisibility(View.GONE);
                } else {
                    ll_ishavadata.setVisibility(View.GONE);
                    rv_home_rooms_fmt.setVisibility(View.VISIBLE);
                    adapter.setList(mDate);
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            rv_home_rooms_fmt.setVisibility(View.GONE);
            ll_ishavadata.setVisibility(View.VISIBLE);
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
                if (gameId.length() == 12) {
                    loadCircleRoom(0, false);
                } else {
                    mPresenter.loadAllRooms(PTApplication.cityName, gameId, "", PTApplication.myLatitude, PTApplication.myLongitude, 0, LOAD_SIZE, "distance", 0, false);
                    // onResume 页面回到第0页
                    page = 0;
                }
            }
        });
    }

    //弹出输入密码pop
    private void initPopupWindow(View view, final String roomId) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_inputpwd, null);
        popupWindow4pwd = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow4pwd.setFocusable(true);
        popupWindow4pwd.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = homeActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        homeActivity.getWindow().setAttributes(wlBackground);
        homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow4pwd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                homeActivity.getWindow().setAttributes(wlBackground);
                homeActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        final EditText pwdString = contentView.findViewById(R.id.et_joinroom_pwd_pop);
        Button joinRoom = contentView.findViewById(R.id.bt_joinroom_join_fmt);
        ImageView cancel = contentView.findViewById(R.id.iv_cancel_pop_fmt);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwdString.getText().toString().trim();
                popupWindow4pwd.dismiss();
                mPresenter.canIJoinTheRoom(roomId, pwd);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow4pwd.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow4pwd.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow4pwd.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //弹出广告
    private void initActivityPop(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_activity_athome, null);
        popupWindow4activity = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow4activity.setFocusable(true);
        popupWindow4activity.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = homeActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        homeActivity.getWindow().setAttributes(wlBackground);
        homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow4activity.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                homeActivity.getWindow().setAttributes(wlBackground);
                homeActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                SpUtils.saveLong(PTApplication.getInstance(), AppConstants.TOMEET_SP_AD_TIME, System.currentTimeMillis());
            }
        });
        ViewPager viewPager = contentView.findViewById(R.id.iv_home_activity_bg);
        final List<ImageView> mImageList = new ArrayList<>();
        for (int i = 0; i < mActivityList.size(); i++) {
            File file = new File(PTApplication.imageLocalCachePath, "id" + mActivityList.get(i).getId() + ".png");
            Logger.e(file.toString());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView1 = new ImageView(mContext);
            imageView1.setImageBitmap(BitmapFactory.decodeStream(fis));
            mImageList.add(imageView1);
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                container.addView(mImageList.get(position));
                mImageList.get(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PTApplication.myInfomation != null) {
                            Intent intent = new Intent(mContext, ShareWebViewActivity.class);
                            intent.putExtra("url", mActivityList.get(position).getUrl());
                            startActivity(intent);
                            popupWindow4activity.dismiss();
                        } else {
                            ToastUtils.getToast("请先登录");
                        }
                    }
                });
                return mImageList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        ImageView cancel = contentView.findViewById(R.id.cancel_pop);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow4activity.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow4activity.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow4activity.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //弹出绑定手机
    private void initBinPhonePop(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_bindphone, null);
        popupWindow4bindPhone = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow4bindPhone.setFocusable(true);
        popupWindow4bindPhone.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = homeActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        homeActivity.getWindow().setAttributes(wlBackground);
        homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow4bindPhone.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                homeActivity.getWindow().setAttributes(wlBackground);
                homeActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        final EditText phone = contentView.findViewById(R.id.et_bindphone_pop);
        Button next = contentView.findViewById(R.id.bt_next_pop);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                phoneNum = phone.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNum)) {
                    popupWindow4bindPhone.dismiss();
                    initSmscodePop(view);
                    PTApplication.getRequestService().getSMSCode(phoneNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<StringDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(StringDataBean stringDataBean) {
                                    if (!stringDataBean.isSuccess()) {
                                        ToastUtils.getToast(stringDataBean.getMsg());
                                    }
                                }
                            });
                }

            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow4bindPhone.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow4bindPhone.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹出输入验证码
     *
     * @param view
     */
    private void initSmscodePop(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_smscode, null);
        initSmsCodepopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        initSmsCodepopupWindow.setFocusable(true);
        initSmsCodepopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = homeActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        homeActivity.getWindow().setAttributes(wlBackground);
        homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        initSmsCodepopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (helper != null) {
                    helper.stop();
                }
                wlBackground.alpha = 1.0f;
                homeActivity.getWindow().setAttributes(wlBackground);
                homeActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        ImageView back = contentView.findViewById(R.id.iv_back);
        final TextView time = contentView.findViewById(R.id.tv_smscode_cutdown_pop);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSmsCodepopupWindow.dismiss();
                initBinPhonePop(view);
            }
        });
        helper = new CountDownButtonHelper(time, "秒后可重新获取", 60, 1);
        time.setClickable(false);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                time.setText("重新发送");
                time.setClickable(true);
            }
        });
        helper.start();
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PTApplication.getRequestService().getSMSCode(phoneNum)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<StringDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(StringDataBean stringDataBean) {
                                if (stringDataBean.isSuccess()) {
                                    helper = new CountDownButtonHelper(time, "秒后可重新获取", 60, 1);
                                    time.setClickable(false);
                                    helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                                        @Override
                                        public void finish() {
                                            time.setText("重新发送");
                                            time.setClickable(true);
                                        }
                                    });
                                    helper.start();
                                } else {
                                    ToastUtils.getToast(stringDataBean.getMsg());
                                }
                            }
                        });
            }
        });
        TextView show_phone = contentView.findViewById(R.id.tv_smscode_phone_pop);
        show_phone.setText("验证码已发送至 +86 " + phoneNum);
        final IdentifyingCodeView smsCode = contentView.findViewById(R.id.icv_smscode_pop);
        smsCode.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String code = smsCode.getTextContent();
                if (code.length() == 6) {
                    PTApplication.getRequestService().bindPhone(phoneNum, code, PTApplication.userToken, PTApplication.userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    if (noDataBean.isSuccess()) {
                                        initSmsCodepopupWindow.dismiss();
                                        ToastUtils.getToast("绑定成功");
                                    } else {
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });
                }
            }

            @Override
            public void deleteContent() {

            }
        });
        //设置PopupWindow进入和退出动画
        initSmsCodepopupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        initSmsCodepopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //弹出绑定手第三方
    private void initBin3PartPop(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_bind3part, null);
        popupWindow4bind3part = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow4bind3part.setFocusable(true);
        popupWindow4bind3part.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = homeActivity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        homeActivity.getWindow().setAttributes(wlBackground);
        homeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow4bind3part.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                homeActivity.getWindow().setAttributes(wlBackground);
                homeActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        LinearLayout wechat = contentView.findViewById(R.id.ll_bind_wechat);
        LinearLayout qq = contentView.findViewById(R.id.ll_bind_qq);
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow4bind3part.dismiss();
                //绑定微信
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart：" + share_media.name());
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Logger.i("onComplete:WX   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                        String mAvatarUrl = map.get("iconurl");
                        String mNickName = map.get("name");
                        PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, "WECHAT", PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError" + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            Logger.e("保存三方信息成功");
                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                        PTApplication.getRequestService().bind3Part(PTApplication.userToken, "WECHAT", map.get("uid"), PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        showLoadingDialog();
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        hideLoadingDialog();
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Bind3Part>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Bind3Part noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            ToastUtils.getToast("绑定成功！");
                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Logger.e("onError: " + throwable.getMessage());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel: " + share_media.toString());
                        hideLoadingDialog();
                    }
                });
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow4bind3part.dismiss();
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart: " + share_media.toString());
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Logger.i("onComplete:QQ   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                        String mAvatarUrl = map.get("iconurl");
                        String mNickName = map.get("name");
                        PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, "QQ", PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e("onError" + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            Logger.e("保存三方信息成功");
                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                        PTApplication.getRequestService().bind3Part(PTApplication.userToken, "QQ", map.get("uid"), PTApplication.userId)
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        showLoadingDialog();
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        hideLoadingDialog();
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Bind3Part>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Bind3Part noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            ToastUtils.getToast("绑定成功");
                                        } else {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Logger.e("onError");
                        hideLoadingDialog();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel");
                        hideLoadingDialog();
                    }
                });
            }
        });

        //设置PopupWindow进入和退出动画
        popupWindow4bind3part.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow4bind3part.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void showLoadingDialog() {
        ((BaseActivity) getActivity()).showLoadingDialog();
    }

    public void hideLoadingDialog() {
        ((BaseActivity) getActivity()).hideLoadingDialog();
    }
}
