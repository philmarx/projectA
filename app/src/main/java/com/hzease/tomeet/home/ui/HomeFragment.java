package com.hzease.tomeet.home.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.ShowGameListBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class HomeFragment extends BaseFragment implements IHomeContract.View {
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private static final int REQUEST_CODE_PICK_GAME = 666;

    List<HomeRoomsBean.DataBean> list = new ArrayList<>();
    @BindView(R.id.tv_home_label_fmt)
    TextView tv_home_label_fmt;

    private List<ShowGameListBean.DataBean> mGameListDatas;
    public PopupWindow popupWindow;

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
    // 头像
    @BindView(R.id.iv_avatar_home_fmt)
    ImageView iv_avatar_home_fmt;
    // 昵称
    @BindView(R.id.tv_nickname_home_fmt)
    TextView tv_nickname_home_fmt;
    // 滚动条
    @BindView(R.id.pb_login_status_home_fmt)
    ProgressBar pb_login_status_home_fmt;

    public static double mLongitude = 100.0;
    public static double mLatitude = 100.0;
    /**
     * 创建事务管理器
     */
    FragmentTransaction transaction;
    HomeActivity meActivity;

    SharedPreferences sp = null;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;
    private int gameId;
    private String gameName;
    private int page = 0;
    private HomeRoomsAdapter adapter;
    private String location;
    private List<HomeRoomsBean.DataBean> footDatas;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void onEventMainThread(UserInfoBean userInfoBean) {
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
                    bundle.putLong("circleId",0);
                    bundle.putBoolean("isOpen",true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ToastUtils.getToast(mContext, "请先登录！");
                }
                break;

            // 点击头像和昵称的LL框
            case R.id.ll_avatar_nickname_home_fmt:
                if (PTApplication.myInfomation != null) {
                    startActivity(new Intent(mContext, MeActivity.class));
                    getActivity().finish();
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
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                location = city + "市";
                tv_home_cityname_fmt.setText(city);
                mPresenter.loadAllRooms(location, gameId, "",  mLatitude, mLongitude, 0, 20, "distance", 0,false);
            }
        }
        if (requestCode == REQUEST_CODE_PICK_GAME && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String gameName = data.getStringExtra(SelectGameTypeActivity.KEY_PICKED_CITY);
                gameId = data.getIntExtra(SelectGameTypeActivity.KEY_GAME_ID, 0);
                Logger.e(gameId + gameName + "onActivityResult");
                tv_home_label_fmt.setText(gameName);
                mPresenter.loadAllRooms(location, gameId, "",  mLatitude, mLongitude, 0, 20, "distance", 0,false);
            }
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        Logger.i("EventBus： " + EventBus.getDefault().isRegistered(this));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
        }

        setAvatarAndNickname();
        sp = getActivity().getSharedPreferences("game_name", Context.MODE_PRIVATE);
        gameName = sp.getString("gamename", "全部分类");
        gameId = sp.getInt("gameId", 0);
        tv_home_label_fmt.setText(gameName);

        /**
         * 获取当前activity
         */
        meActivity = (HomeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        rv_home_rooms_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home_rooms_fmt.addItemDecoration(new SpacesItemDecoration(20));
        location = tv_home_cityname_fmt.getText().toString().trim() + "市";
        mPresenter.loadAllRooms(location, gameId, "",  mLatitude, mLongitude, 0, 15, "distance", 0,false);

        home_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
                        mPresenter.loadAllRooms(location, gameId, "",  mLatitude, mLongitude, 0, 15, "distance", 0,false);
                    }
                }, 2000);
            }
        });

        rv_home_rooms_fmt.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(adapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footDatas = new ArrayList<HomeRoomsBean.DataBean>();
                            mPresenter.loadAllRooms(location, gameId, "",  mLatitude, mLongitude, ++page, 15, "distance", 0,true);
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
        // 在onResume()中的start中调用
        // setAvatarAndNickname();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 只需要相机权限,不需要SD卡读写权限
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.REQUEST_LOCATION_PERMISSION);
        } else {
            initLogLat();
        }
        //mPresenter.loadAllRooms("杭州市", gameId, "",  mLatitude, mLongitude, 0, 10, "distance", 0,false);
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
            // 请求相机权限
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
        Logger.i("Flags: " + getActivity().getIntent().getFlags() + "\nmyInfomation: " + PTApplication.myInfomation);
        if (AppConstants.YY_PT_NAVIGATION_SPLASH_REQUEST_CODE == getActivity().getIntent().getFlags() && PTApplication.myInfomation == null) {
            pb_login_status_home_fmt.setVisibility(View.VISIBLE);
        } else {
            pb_login_status_home_fmt.setVisibility(View.GONE);
            if (PTApplication.myInfomation != null) {
                Glide.with(mContext)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                        .into(iv_avatar_home_fmt);
                iv_avatar_home_fmt.setVisibility(View.VISIBLE);
            }
            String nickName = "登录";
            if (PTApplication.myInfomation != null) {
                nickName = PTApplication.myInfomation.getData().getNickname();
            }
            tv_nickname_home_fmt.setText(nickName);
            tv_nickname_home_fmt.setVisibility(View.VISIBLE);
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
    public void initGameList(List<ShowGameListBean.DataBean> data) {
        mGameListDatas = data;
        Logger.e(mGameListDatas.size() + "");
    }

    @Override
    public void initRoomsList(List<HomeRoomsBean.DataBean> date,boolean isLoadMore) {
        if (isLoadMore){
            footDatas.addAll(date);
            adapter.AddFooterItem(footDatas);
            //设置回到上拉加载更多
            if (date.size()==15){
                adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
            }else{
                adapter.changeMoreStatus(adapter.PULLUP_LOAD_MORE);
                adapter.changeMoreStatus(adapter.NO_LOAD_MORE);
            }
        }else{
            list = date;
            adapter = new HomeRoomsAdapter(list, getContext(), mLongitude, mLatitude);
            rv_home_rooms_fmt.setAdapter(adapter);
            home_swiperefreshlayout.setRefreshing(false);
        }
        adapter.setOnItemClickLitener(new HomeRoomsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (PTApplication.myInfomation != null) {
                    String roomId = String.valueOf(list.get(position).getId());
                    if (list.get(position).isLocked()) {
                        initPopupWindow(view,roomId);
                    } else {
                        mPresenter.canIJoinTheRoom(roomId, "");
                    }
                } else {
                    ToastUtils.getToast(mContext, "请先登录！");
                }

            }
        });

    }

    //加载当前位置
    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                Logger.w("mLongitude: " + mLongitude + "\nmLatitude: " + mLatitude);
                PTApplication.myLatitude = mLatitude;
                PTApplication.myLongitude = mLongitude;
                mPresenter.loadAllRooms("杭州市", gameId, "", mLatitude, mLongitude, 0, 10, "distance", 0,false);
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
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                meActivity.getWindow().setAttributes(wlBackground);
            }
        });
        final EditText pwdString = (EditText) contentView.findViewById(R.id.et_joinroom_pwd_pop);
        Button joinRoom = (Button) contentView.findViewById(R.id.bt_joinroom_join_fmt);
        Button cancel = (Button) contentView.findViewById(R.id.bt_joinroom_cancel_fmt);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = pwdString.getText().toString().trim();
                popupWindow.dismiss();
                mPresenter.canIJoinTheRoom(roomId,pwd);
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
