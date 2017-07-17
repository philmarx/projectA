package com.hzease.tomeet.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.NoDataBean;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AMapLocUtils implements AMapLocationListener {
    private AMapLocationClient locationClient = null;  // 定位
    private AMapLocationClientOption locationOption = null;  // 定位设置
    private LonLatListener mLonLatListener;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mLonLatListener.getLonLat(aMapLocation);
        locationClient.stopLocation();
        locationClient.onDestroy();
        locationClient = null;
        locationOption = null;
    }

    public void  getLonLat(Context context, LonLatListener lonLatListener){
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);// 设置定位模式为高精度模式
        locationClient.setLocationListener(this);// 设置定位监听
        locationOption.setOnceLocation(false); // 单次定位
        locationOption.setNeedAddress(true);//逆地理编码
        mLonLatListener = lonLatListener;//接口
        locationClient.setLocationOption(locationOption);// 设置定位参数
        locationClient.startLocation(); // 启动定位
    }
    public interface  LonLatListener{
        void getLonLat(AMapLocation aMapLocation);
    }

    public void getLonLatAndSendLocation(final String roomId) {
        getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                PTApplication.myLongitude = aMapLocation.getLongitude();
                PTApplication.myLatitude = aMapLocation.getLatitude();
                PTApplication.getRequestService().sendLocation(PTApplication.myLatitude, PTApplication.myLongitude, Long.valueOf(roomId), PTApplication.userToken, PTApplication.myInfomation.getData().getId(), "Android: " + PTApplication.appVersion + " add: " + aMapLocation.getAddress() + "(" + aMapLocation.getStreet() + aMapLocation.getStreetNum() + ")")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e("updateLocation: " + noDataBean.toString());
                            }
                        });
            }
        });
    }
}
