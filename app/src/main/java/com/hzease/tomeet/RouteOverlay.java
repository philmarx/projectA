package com.hzease.tomeet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.hzease.tomeet.circle.ui.CircleInfoActivity;
import com.hzease.tomeet.data.NoDataBean;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RouteOverlay {
    protected List<Marker> stationMarkers = new ArrayList<Marker>();
    protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
    protected Marker startMarker;
    protected Marker endMarker;
    protected LatLng startPoint;
    protected LatLng endPoint;
    protected AMap mAMap;
    private Context mContext;
    private Bitmap startBit, endBit, busBit, walkBit, driveBit;
    protected boolean nodeIconVisible = true;
    public double lat;
    public double lng;
    public String roomPlace;
    public Activity activity;
    private PopupWindow popupWindow;

    public RouteOverlay(Context context, double lat, double lng, String roomPlace, Activity activity) {
        mContext = context;
        this.lat = lat;
        this.lng = lng;
        this.roomPlace = roomPlace;
        this.activity = activity;
    }

    /**
     * 去掉BusRouteOverlay上所有的Marker。
     *
     * @since V2.1.0
     */
    public void removeFromMap() {
        if (startMarker != null) {
            startMarker.remove();

        }
        if (endMarker != null) {
            endMarker.remove();
        }
        for (Marker marker : stationMarkers) {
            marker.remove();
        }
        for (Polyline line : allPolyLines) {
            line.remove();
        }
        destroyBit();
    }

    private void destroyBit() {
        if (startBit != null) {
            startBit.recycle();
            startBit = null;
        }
        if (endBit != null) {
            endBit.recycle();
            endBit = null;
        }
        if (busBit != null) {
            busBit.recycle();
            busBit = null;
        }
        if (walkBit != null) {
            walkBit.recycle();
            walkBit = null;
        }
        if (driveBit != null) {
            driveBit.recycle();
            driveBit = null;
        }
    }

    /**
     * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_start);
    }

    /**
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_end);
    }

    /**
     * 给公交Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getBusBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_bus);
    }

    /**
     * 给步行Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getWalkBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_man);
    }

    protected BitmapDescriptor getDriveBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_car);
    }

    protected void addStartAndEndMarker() {

        startMarker = mAMap.addMarker((new MarkerOptions())
                .position(startPoint).icon(getStartBitmapDescriptor())
                );
        // startMarker.showInfoWindow();

        endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation_icon)));
        // mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,
        // getShowRouteZoom()));
        endMarker.setClickable(true);
        endMarker.setTitle("活动地点");
        endMarker.setSnippet(roomPlace);
        endMarker.showInfoWindow();
        mAMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.equals(endMarker)){
                    /*try {
                        Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + PTApplication.myLatitude + "," + PTApplication.myLongitude + "|name:我的位置&destination=latlng:" + lat + "," + lng + "|name:"+roomPlace+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end").addFlags(FLAG_ACTIVITY_NEW_TASK);
                        PTApplication.getInstance().startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }*/
                   /* Dialog mCameraDialog = new Dialog(activity,R.style.my_dialog);
                    View root = LayoutInflater.from(PTApplication.getInstance()).inflate(
                            R.layout.pop_select_dialog, null);
                    mCameraDialog.setContentView(root);
                    Window dialogWindow = mCameraDialog.getWindow();
                    dialogWindow.setGravity(Gravity.BOTTOM);
                    dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                    lp.x = 0; // 新位置X坐标
                    lp.y = -20; // 新位置Y坐标
                    lp.width = PTApplication.getInstance().getResources().getDisplayMetrics().widthPixels; // 宽度
                    root.measure(0, 0);
                    lp.height = root.getMeasuredHeight();
                    lp.alpha = 9f; // 透明度
                    dialogWindow.setAttributes(lp);
                    mCameraDialog.show();*/
                    initPopupWindow();
                }
            }
        });
    }

    private void initPopupWindow() {
        View popupWindowView = activity.getLayoutInflater().inflate(R.layout.pop_select_dialog, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.activity_login, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
       /* final WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的*/
        TextView tv_AMap_item = (TextView) popupWindowView.findViewById(R.id.tv_AMap_item);
        TextView tv_BaiduMap_item = (TextView) popupWindowView.findViewById(R.id.tv_BaiduMap_item);
        if (isInstallByread("com.baidu.BaiduMap")){
            tv_BaiduMap_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + PTApplication.myLatitude + "," + PTApplication.myLongitude + "|name:我的位置&destination=latlng:" + lat + "," + lng + "|name:"+roomPlace+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PTApplication.getInstance().startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            tv_BaiduMap_item.setVisibility(View.GONE);
        }
        if (isInstallByread("com.autonavi.minimap")){
            tv_AMap_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+PTApplication.myLatitude+"&slon="+PTApplication.myLongitude+"&sname="+"我的位置"+"&dlat="+lat+"&dlon="+lng+"&dname="+roomPlace+"&dev=0&m=0&t=0").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PTApplication.getInstance().startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            tv_AMap_item.setVisibility(View.GONE);
        }
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
          /*      lp.alpha = 1.0f;
                activity.getWindow().setAttributes(lp);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
            }
        });
    }
    /**
     * 移动镜头到当前的视角。
     *
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (startPoint != null) {
            if (mAMap == null)
                return;
            try {
                LatLngBounds bounds = getLatLngBounds();
                mAMap.animateCamera(CameraUpdateFactory
                        .newLatLngBounds(bounds, 50));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    protected LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        b.include(new LatLng(startPoint.latitude, startPoint.longitude));
        b.include(new LatLng(endPoint.latitude, endPoint.longitude));
        for (Polyline polyline : allPolyLines) {
            for (LatLng point : polyline.getPoints()) {
                b.include(point);
            }
        }
        return b.build();
    }

    /**
     * 路段节点图标控制显示接口。
     *
     * @param visible true为显示节点图标，false为不显示。
     * @since V2.3.1
     */
    public void setNodeIconVisibility(boolean visible) {
        try {
            nodeIconVisible = visible;
            if (this.stationMarkers != null && this.stationMarkers.size() > 0) {
                for (int i = 0; i < this.stationMarkers.size(); i++) {
                    this.stationMarkers.get(i).setVisible(visible);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void addStationMarker(MarkerOptions options) {
        if (options == null) {
            return;
        }
        Marker marker = mAMap.addMarker(options);
        if (marker != null) {
            stationMarkers.add(marker);
        }

    }

    protected void addPolyLine(PolylineOptions options) {
        if (options == null) {
            return;
        }
        Polyline polyline = mAMap.addPolyline(options);
        if (polyline != null) {
            allPolyLines.add(polyline);
        }
    }

    protected float getRouteWidth() {
        return 18f;
    }

    protected int getWalkColor() {
        return Color.parseColor("#6db74d");
    }

    /**
     * 自定义路线颜色。
     * return 自定义路线颜色。
     *
     * @since V2.2.1
     */
    protected int getBusColor() {
        return Color.parseColor("#537edc");
    }

    protected int getDriveColor() {
        return Color.parseColor("#537edc");
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
