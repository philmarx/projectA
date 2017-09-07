package com.hzease.tomeet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.data.FriendLocationBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.radarView.RadarViewGroup;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RoomLocationActivity extends AppCompatActivity {
    MapView mMapView = null;
    private Marker locationMarker; // 选择的点
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private double roomLat;
    private double roomLong;
    private String roomCity;
    private RouteSearch mRouteSearch;
    private AMap aMap;
    private DriveRouteResult mDriveRouteResult;
    private String roomPlace;
    private String roomId;
    private LatLng latLng;
    private Button bt_read_ohterlocation;
    private List<FriendLocationBean.DataBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_location);
        mMapView = (MapView) findViewById(R.id.mv_roomlocation_act);
        bt_read_ohterlocation = (Button) findViewById(R.id.bt_read_ohterlocation);
        getBundle();
        //初始化地图控制器对象
        initAMap();
        getRoute();
        Logger.e("roomId" + roomId);
        Logger.e("roomCity" + roomCity);
        bt_read_ohterlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                findOtherLocation(view);
                /*PTApplication.getRequestService().findLocation(roomId,PTApplication.userToken,PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<FriendLocationBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError" + e.getMessage());
                            }

                            @Override
                            public void onNext(FriendLocationBean friendLocationBean) {
                                Logger.e("isSuccess" + friendLocationBean.isSuccess());
                                if (friendLocationBean.isSuccess()){
                                    initPopupWindow(view,friendLocationBean.getData());
                                    mDatas = friendLocationBean.getData();
                                }else{
                                    ToastUtils.getToast(friendLocationBean.getMsg());
                                }
                            }
                        });*/
            }
        });
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    public void findOtherLocation(final View view) {
        // 填充布局
        final RelativeLayout contentView = (RelativeLayout) RelativeLayout.inflate(this, R.layout.radar_map, null);

        // 初始化POP
        PopupWindow popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        PTApplication.getRequestService().findLocation(roomId, PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FriendLocationBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getToast("网络异常，请重试");
                        Logger.e("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(FriendLocationBean friendLocationBean) {
                        Logger.e("FriendLocationBean: " + friendLocationBean.toString());
                        if (friendLocationBean.isSuccess()) {
                            // 雷达圈
                            ImageView iv_circle = (ImageView) contentView.findViewById(R.id.iv_circle);
                            // 中心点
                            ImageView iv_center = (ImageView) contentView.findViewById(R.id.iv_center);
                            double one = 1500.0 / (iv_circle.getWidth() / 10.0);
                            int left = iv_circle.getLeft();
                            int right = iv_circle.getRight() - (int)one;
                            int top = iv_circle.getTop();
                            int bottom = iv_circle.getBottom() - (int)one;
                            int[] wh = new int[2];
                            iv_center.getLocationInWindow(wh);
                            Logger.e("wh: " + Arrays.toString(wh) + "  left: " + left + "  right: " + right + "  top: " + top + "  bottom: " + bottom + "   one: " + one);



                            List<FriendLocationBean.DataBean> friendLocationBeanData = friendLocationBean.getData();
                            for (FriendLocationBean.DataBean people : friendLocationBeanData) {
                                // 填充头像
                                View inflate = View.inflate(view.getContext(), R.layout.radar_people, null);
                                contentView.addView(inflate);
                                TextView km_tv = (TextView) inflate.findViewById(R.id.tv_name_radar_people);
                                float distance = AMapUtils.calculateLineDistance(new LatLng(roomLat, roomLong), new LatLng(people.getLatitude(), people.getLongitude()));
                                if (distance < 1000) {
                                    km_tv.setVisibility(View.GONE);
                                } else {
                                    km_tv.setText((((int) (distance / 1000.0)) + "km"));
                                }
                                Glide.with(view.getContext())
                                        .load(AppConstants.YY_PT_OSS_USER_PATH + people.getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                                        .bitmapTransform(new CropCircleTransformation(view.getContext()))
                                        .signature(new StringSignature(people.getAvatarSignature()))
                                        .into((ImageView) inflate.findViewById(R.id.iv_avatar_radar_people));

                                double x1 = wh[0] - (roomLong - people.getLongitude()) * 111319.55 / one;
                                x1 = (x1 = x1 > right ? right : x1) < left ? left : x1;
                                double y1 = wh[1] - (roomLat - people.getLatitude()) * 111319.55 / one;
                                y1 = (y1 = y1 > bottom ? bottom : y1) < top ? top : y1;

                                TranslateAnimation translateAnimation = new TranslateAnimation(0, (float) x1, 0, (float) y1);
                                translateAnimation.setDuration(1000);
                                translateAnimation.setFillAfter(true);
                                inflate.startAnimation(translateAnimation);
                            }
                        } else {
                            ToastUtils.getToast(friendLocationBean.getMsg());
                            Logger.e("FriendLocationBean: " + friendLocationBean.getMsg());
                        }
                    }
                });
    }

    private void initPopupWindow(View view, List<FriendLocationBean.DataBean> dataBeen) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_showlocation, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        RadarViewGroup radar = contentView.findViewById(R.id.radar);
        radar.setiRadarClickListener(new RadarViewGroup.IRadarClickListener() {
            @Override
            public void onRadarItemClick(int position) {
                popupWindow.dismiss();
                Intent intent = new Intent(RoomLocationActivity.this, PersonOrderInfoActivity.class);
                intent.putExtra("userId", mDatas.get(position).getUserId());
                startActivity(intent);
            }
        });
        radar.setDatas(dataBeen, latLng);
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void getRoute() {
        LatLonPoint mMyLocation = new LatLonPoint(PTApplication.myLatitude, PTApplication.myLongitude);
        final LatLonPoint roomLocation = new LatLonPoint(roomLat, roomLong);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
                aMap.clear();// 清理地图上的所有覆盖物
                if (errorCode == 1000) {
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                        if (driveRouteResult.getPaths().size() > 0) {
                            mDriveRouteResult = driveRouteResult;
                            final DrivePath drivePath = mDriveRouteResult.getPaths()
                                    .get(0);
                            DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                    RoomLocationActivity.this, aMap, drivePath,
                                    mDriveRouteResult.getStartPos(),
                                    mDriveRouteResult.getTargetPos(), null, roomLat, roomLong, roomPlace, RoomLocationActivity.this);
                            drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                            drivingRouteOverlay.removeFromMap();
                            drivingRouteOverlay.addToMap();
                            drivingRouteOverlay.zoomToSpan();
                        } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                            ToastUtils.getToast("没有搜索到信息");
                        }
                    } else {
                        ToastUtils.getToast("没有搜索到信息");
                    }
                } else {
                    ToastUtils.getToast(errorCode + "");
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        if (mMyLocation == null) {
            ToastUtils.getToast("获取位置失败");
            return;
        }
        if (roomLocation == null) {
            ToastUtils.getToast("获取房间位置失败");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mMyLocation, roomLocation);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    private void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //获取房间的经纬度
        roomLat = bundle.getDouble("roomLat", 0);
        roomLong = bundle.getDouble("roomLong", 0);
        roomCity = bundle.getString("roomCity");
        roomId = bundle.getString("roomId");
        roomPlace = bundle.getString("roomPlace");


    }

    private void initAMap() {
        aMap = null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        latLng = new LatLng(roomLat, roomLong);
        //aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        /*LatLng latLng = new LatLng(roomLat, roomLong);
        LatLng myLocation = new LatLng(PTApplication.myLatitude, PTApplication.myLongitude);
        LatLngBounds latLngBounds = new LatLngBounds(latLng, myLocation);
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 15));*/
        /*final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(roomCity).snippet("活动地点"));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。*/
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }
}
