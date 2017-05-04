package com.hzease.tomeet.home.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.ShowGameListBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.HomeRoomsAdapter;
import com.hzease.tomeet.widget.adapters.RecycleViewTestAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;

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

public class HomeFragment extends BaseFragment implements IHomeContract.View {
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private static final int REQUEST_CODE_PICK_GAME = 666;

    List<HomeRoomsBean.DataBean> list = new ArrayList<>();
    @BindView(R.id.tv_home_label_fmt)
    TextView tv_home_label_fmt;

    private List<ShowGameListBean.DataBean> mGameListDatas;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
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
    SuperRecyclerView lv_home_rooms_fmt;
    // 头像
    @BindView(R.id.iv_avatar_home_fmt)
    ImageView iv_avatar_home_fmt;
    // 昵称
    @BindView(R.id.tv_nickname_home_fmt)
    TextView tv_nickname_home_fmt;
    private double mLongitude = 100.0;
    private double mLatitude = 100.0;
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
    private int page;
    private HomeRoomsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_homebak;
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
                startActivity(new Intent(getActivity(), CreateRoomBeforeActivity.class));
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
                tv_home_cityname_fmt.setText(city);
            }
        }
        if (requestCode == REQUEST_CODE_PICK_GAME && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String gameName = data.getStringExtra(SelectGameTypeActivity.KEY_PICKED_CITY);
                gameId = data.getIntExtra(SelectGameTypeActivity.KEY_GAME_ID, 0);
                Logger.e(gameId + gameName + "onActivityResult");
                tv_home_label_fmt.setText(gameName);
                mPresenter.loadAllRooms("杭州市", gameId, "",  mLatitude, mLongitude, 0, 20, "distance", 0,false);
            }
        }
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
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
        initLogLat();
        lv_home_rooms_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_home_rooms_fmt.addItemDecoration(new SpacesItemDecoration(20));

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadAllRooms("杭州市", gameId, "",  mLatitude, mLongitude, 0, 10, "distance", 0,false);
                    }
                }, 2000);
            }
        };
        lv_home_rooms_fmt.setRefreshListener(refreshListener);
        // 在onResume()中的start中调用
        // setAvatarAndNickname();
        lv_home_rooms_fmt.setupMoreListener(new OnMoreListener() {

            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadAllRooms("杭州市", gameId, "",  mLatitude, mLongitude, page++, 10, "distance", 0,true);
                    }
                },2000);

            }

        },1);

        //mPresenter.loadAllRooms("杭州市", gameId, "",  mLatitude, mLongitude, 0, 10, "distance", 0,false);

    }

    @Override
    public void setAvatarAndNickname() {
        if (PTApplication.myInfomation != null) {
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                    .into(iv_avatar_home_fmt);
        }
        String nickName = "登录";
        if (PTApplication.myInfomation != null) {
            nickName = PTApplication.myInfomation.getData().getNickname();
        }
        tv_nickname_home_fmt.setText(nickName);
    }


    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void initGameList(List<ShowGameListBean.DataBean> data) {
        mGameListDatas = data;
        //initPopupWindow();
        Logger.e(mGameListDatas.size() + "");
    }

    @Override
    public void initRoomsList(List<HomeRoomsBean.DataBean> date,boolean isLoadMore) {
        if (date == null){
            lv_home_rooms_fmt.hideMoreProgress();
        }else{
            if (isLoadMore){
                list.addAll(date);
                lv_home_rooms_fmt.hideMoreProgress();
                adapter.notifyDataSetChanged();
            }else{
                list.clear();
                list = date;
                adapter = new HomeRoomsAdapter(list, getContext(), mLongitude, mLatitude);
                lv_home_rooms_fmt.setAdapter(adapter);
            }
        }
        adapter.setOnItemClickLitener(new RecycleViewTestAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (PTApplication.myInfomation != null) {
                    String roomId = String.valueOf(list.get(position).getId());
                    // RongIM.getInstance().startChatRoomChat(mContext, chatRoomId, true);
                    startActivity(new Intent(mContext, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
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
                mPresenter.loadAllRooms("杭州市", gameId, "", mLatitude, mLongitude, 0, 10, "distance", 0,false);
            }
        });
    }


    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    /*class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    protected void initPopupWindow() {
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.firstchose, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        RecyclerView mRecycle = (RecyclerView) popupWindowView.findViewById(R.id.rcv_pop_item);
        mRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        if (mGameListDatas != null && mGameListDatas.size() > 0) {
            Logger.e(mGameListDatas.size() + "");
            mRecycle.setAdapter(new CommonAdapter<ShowGameListBean.DataBean>(getContext(), R.layout.item_firsetchose, mGameListDatas) {
                @Override
                protected void convert(ViewHolder holder, ShowGameListBean.DataBean dataBean, int position) {
                    holder.itemView.setTag(dataBean.getName());
                    holder.setText(R.id.tv_item_gamename, dataBean.getName());
                }

                @Override
                public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
                    super.setOnItemClickListener(onItemClickListener);
                    Logger.d("setOnItemClickListener: " + getTag());
                }
            });
        }


        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
        //popupWindow.setHeight(LayoutParams.FILL_PARENT);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.activity_home, null), Gravity.RIGHT, 0, 1000);
        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return true;
            }
        });

        Button open = (Button) popupWindowView.findViewById(R.id.cancel);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Open", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
    }

    *//**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *//*
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }*/
}
