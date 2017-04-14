package pro.yueyuan.project_t;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.List;

import pro.yueyuan.project_t.data.AddressEntity;
import pro.yueyuan.project_t.utils.ToastUtils;
import pro.yueyuan.project_t.widget.adapters.AddressSearchAdapter;

public class ShareLocationActivity extends PermissionActivity implements View.OnClickListener,
        AMap.OnMapClickListener,
        PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener, Animation.AnimationListener,
        GeocodeSearch.OnGeocodeSearchListener {

    public static final String PLACE_NAME="10010";
    private static final int OPEN_SEARCH = 0X0001;
    private MapView mapview;
    private AMap mAMap;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;//
    private Marker locationMarker; // 选择的点
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据
    private String keyWord = "";
    private String city;
    private String cityName;
    private ImageView mIvCenter;
    private Animation animationMarker;
    private LatLng mFinalChoosePosition; //最终选择的点
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private RecyclerView recycleView;
    private AddressSearchAdapter recycleViewAdapter;
    private ArrayList<AddressEntity> mDatas = new ArrayList<>();
    private AddressEntity mAddressEntityFirst = null;
    private boolean isHandDrag = true;
    private boolean isFirstLoadList = true;
    private boolean isBackFromSearchChoose = false;
    private TextView tv_send;
    private LinearLayout search_bar_layout;
    private EditText mEtContent;
    private RelativeLayout rl_map_layout;
    private int mapHeight;
    private static final String TAG = "ShareLocationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_loaction);
        Intent intent = getIntent();
        double lon = intent.getDoubleExtra("lon", 0);
        double lat = intent.getDoubleExtra("lat", 0);
        city = intent.getStringExtra("cityCode");
        cityName = intent.getStringExtra("cityName");
        lp = new LatLonPoint(lat, lon);
        mapview = (MapView) findViewById(R.id.mapView);
        mIvCenter = (ImageView) findViewById(R.id.mIvCenter);
        rl_map_layout = (RelativeLayout) findViewById(R.id.rl_map_layout);
        tv_send = (TextView) findViewById(R.id.tv_send);
        mapview.onCreate(savedInstanceState);
        animationMarker = AnimationUtils.loadAnimation(this, R.anim.bounce_interpolator);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShareLocationActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleViewAdapter = new AddressSearchAdapter(ShareLocationActivity.this, mDatas);
        recycleView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setItemListener(new RecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                mFinalChoosePosition = convertToLatLng(mDatas.get(position).latLonPoint);
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).isChoose = false;
                }
                mDatas.get(position).isChoose = true;
                recycleViewAdapter.setDatas(mDatas);
                Logger.d("点击后的最终经纬度：  纬度" + mFinalChoosePosition.latitude + " 经度 " + mFinalChoosePosition.longitude);
                isHandDrag = false;
                // 点击之后，我利用代码指定的方式改变了地图中心位置，所以也会调用 onCameraChangeFinish
                // 只要地图发生改变，就会调用 onCameraChangeFinish ，不是说非要手动拖动屏幕才会调用该方法
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mFinalChoosePosition.latitude, mFinalChoosePosition.longitude), 20));

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        mapview.post(new Runnable() {
            @Override
            public void run() {
                mapHeight = mapview.getMeasuredHeight();
            }
        });
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                //只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    Log.i(TAG, " firstItemPosition = " + firstItemPosition);
                    int measuredHeight = rl_map_layout.getMeasuredHeight();
                    if (firstItemPosition == 0 && measuredHeight == mapHeight / 2) {
                        performAnimate(rl_map_layout, mapHeight / 2, mapHeight);
                    } else if (measuredHeight == mapHeight) {
                        performAnimate(rl_map_layout, mapHeight, mapHeight / 2);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
        init();
    }

    /**
     * 慢慢縮放的效果
     *
     * @param target
     * @param start
     * @param end
     */
    private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                Log.i(TAG, "current value = " + currentValue);

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //直接调用整型估值器通过比例计算出宽度
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });
        valueAnimator.end();
        valueAnimator.setDuration(500).start();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mapview.getMap();
            mAMap.setOnMapClickListener(this);
            mAMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器

            search_bar_layout = (LinearLayout) findViewById(R.id.search_bar_layout);
            mEtContent = (EditText) findViewById(R.id.et_search);
            mEtContent.setOnClickListener(this);
            search_bar_layout.setOnClickListener(this);
            animationMarker.setAnimationListener(this);

            locationMarker = mAMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.point_blue)))
                    .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
            mFinalChoosePosition = locationMarker.getPosition();
        }
        setup();
        // 只要地图发生改变，就会调用 onCameraChangeFinish ，不是说非要手动拖动屏幕才会调用该方法
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 20));
    }

    private void setup() {
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        mIvCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.getToast(ShareLocationActivity.this, "当前位置：" + mFinalChoosePosition.longitude + "  纬度：" + mFinalChoosePosition.latitude);
            }
        });
    }

    // 拖动地图
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Logger.e("拖动地图 onCameraChange ");
    }

    /**
     * 拖动地图 结束回调
     *
     * @param cameraPosition 当地图位置发生变化，就重新查询数据（手动拖动或者代码改变地图位置都会调用）
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mFinalChoosePosition = cameraPosition.target;
        Logger.d("拖动地图 Finish changeCenterMarker 经度" + mFinalChoosePosition.longitude + "   纬度：" + mFinalChoosePosition.latitude);
        mIvCenter.startAnimation(animationMarker);
        if (isHandDrag || isFirstLoadList) {//手动去拖动地图
            getAddress(cameraPosition.target);
            doSearchQuery();
        } else if (isBackFromSearchChoose) {
            doSearchQuery();
        } else {
            recycleViewAdapter.notifyDataSetChanged();
        }
        isHandDrag = true;
        isFirstLoadList = false;
    }

    // ========  poi搜索 周边  以下 =====================

    /**
     * 开始进行poi搜索   重点
     * 通过经纬度获取附近的poi信息
     * <p>
     * 1、keyword 传 ""
     * 2、poiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 5000, true)); 根据
     */
    protected void doSearchQuery() {

        currentPage = 0;
        query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        LatLonPoint lpTemp = convertToLatLonPoint(mFinalChoosePosition);

        if (lpTemp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);  // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 5000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    /**
     * poi 附近数据搜索
     *
     * @param result
     * @param rcode
     */
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    mDatas.clear();
                    //if(isFirstLoadList || isBackFromSearchChoose){
                    //mDatas.add(mAddressEntityFirst);// 第一个元素

                    AddressEntity addressEntity;
                    for (PoiItem poiItem : poiItems) {
                       Logger.d("得到的数据 poiItem " + poiItem.getSnippet());
                        addressEntity = new AddressEntity(false, poiItem.getLatLonPoint(), poiItem.getSnippet(), poiItem.getTitle());
                        mDatas.add(addressEntity);
                    }
                    if (isHandDrag) {
                        mDatas.get(0).isChoose = true;
                    }
                    recycleViewAdapter.notifyDataSetChanged();
                }
            }
        } else {
            ToastUtils.getToast(ShareLocationActivity.this, "对不起，没有搜索到相关数据！");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiitem, int rcode) {

    }

    /**
     * 按照关键字搜索附近的poi信息
     *
     * @param key
     */
    protected void doSearchQueryWithKeyWord(String key) {
        currentPage = 0;
        query = new PoiSearch.Query(key, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);   // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }
    // ========  poi搜索 周边  以上   =====================

    /**
     * 根据经纬度得到地址
     */
    public void getAddress(final LatLng latLonPoint) {
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(convertToLatLonPoint(latLonPoint), 1000, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getCity(); // 逆转地里编码不是每次都可以得到对应地图上的opi
                Logger.d("逆地理编码回调  得到的地址：" + addressName);
                mAddressEntityFirst = new AddressEntity(true, convertToLatLonPoint(mFinalChoosePosition), addressName, addressName);
            } else {
                ToastUtils.getToast(this, getString(R.string.no_result));
            }
        } else if (rCode == 27) {
            ToastUtils.getToast(this, getString(R.string.error_network));
        } else if (rCode == 32) {
            ToastUtils.getToast(this, getString(R.string.error_key));
        } else {
            ToastUtils.getToast(this,
                    getString(R.string.error_other) + rCode);
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
            case R.id.search_bar_layout:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("point", mFinalChoosePosition);
                intent.putExtra("city",cityName);
                ToastUtils.getToast(this,cityName);
                startActivityForResult(intent, OPEN_SEARCH);
                isBackFromSearchChoose = false;
                break;
            case R.id.tv_send:
                AddressEntity finalChooseEntity = null;
                for (AddressEntity searchTextEntity : mDatas) {
                    if (searchTextEntity.isChoose) {
                        finalChooseEntity = searchTextEntity;
                    }
                }
                if (finalChooseEntity != null) {
                    Logger.d("发送数据："
                            + "\n 经度" + finalChooseEntity.latLonPoint.getLongitude()
                            + "\n 纬度" + finalChooseEntity.latLonPoint.getLatitude()
                            + "\n 地址" + finalChooseEntity.snippet
                    );
                    ToastUtils.getToast(this, "发送数据："
                            + "\n 经度" + finalChooseEntity.latLonPoint.getLongitude()
                            + "\n 纬度" + finalChooseEntity.latLonPoint.getLatitude()
                            + "\n 地址" + finalChooseEntity.snippet);
                    back();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 将数据返回给前一个界面
     */
    private void back() {
        Intent data = new Intent();
        data.putExtra(PLACE_NAME, addressName);
        setResult(RESULT_OK, data);
        finish();
    }

    // 单击地图
    @Override
    public void onMapClick(LatLng latlng) {
//        ToastUtil.show(ShareLocationActivity.this, "点击结果：  经度：" + latlng.longitude + "   纬度： " + latlng.latitude);
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtils.getToast(this, infomation);
    }

    // 动画复写的三个方法
    @Override
    public void onAnimationStart(Animation animation) {
        mIvCenter.setImageResource(R.drawable.location_icon);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mIvCenter.setImageResource(R.drawable.location_icon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_SEARCH && resultCode == RESULT_OK) {
            AddressEntity backEntity = data.getParcelableExtra("backEntity");
            mAddressEntityFirst = backEntity; // 上一个页面传过来的 item对象
            mAddressEntityFirst.isChoose = true;
            isBackFromSearchChoose = true;
            isHandDrag = false;
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(backEntity.latLonPoint.getLatitude(), backEntity.latLonPoint.getLongitude()), 20));
        }
    }
}
