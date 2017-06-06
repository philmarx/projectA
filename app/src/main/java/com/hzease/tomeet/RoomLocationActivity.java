package com.hzease.tomeet;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import com.amap.api.location.AMapLocationClient;
        import com.amap.api.location.AMapLocationClientOption;
        import com.amap.api.maps.AMap;
        import com.amap.api.maps.CameraUpdateFactory;
        import com.amap.api.maps.LocationSource;
        import com.amap.api.maps.MapView;
        import com.amap.api.maps.model.LatLng;
        import com.amap.api.maps.model.Marker;
        import com.amap.api.maps.model.MarkerOptions;
        import com.amap.api.maps.model.MyLocationStyle;
        import com.orhanobut.logger.Logger;

public class RoomLocationActivity extends AppCompatActivity {
    MapView mMapView = null;
    private Marker locationMarker; // 选择的点
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private double roomLat;
    private double roomLong;
    private String roomCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_location);
        mMapView = (MapView) findViewById(R.id.mv_roomlocation_act);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        getBundle();
        //初始化地图控制器对象
        initAMap();
    }

    private void getBundle() {
        Intent intent = getIntent();
        //获取房间的经纬度
        roomLat = intent.getDoubleExtra("roomLat",0);
        roomLong = intent.getDoubleExtra("roomLong",0);
        roomCity = intent.getStringExtra("roomCity");
        Logger.e("roomLat" + roomLat + "roomLong" + roomLong + "roomcity" + roomCity);
    }

    private void initAMap() {
        AMap aMap = null;
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        LatLng latLng = new LatLng(roomLat,roomLong);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(roomCity).snippet("活动地点"));
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
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
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
}