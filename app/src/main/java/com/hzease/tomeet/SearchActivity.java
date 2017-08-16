package com.hzease.tomeet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hzease.tomeet.circle.ui.SearchCircleActivity;
import com.hzease.tomeet.data.AddressEntity;
import com.hzease.tomeet.utils.KeyboardUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.AddressSearchAdapter;
import com.hzease.tomeet.widget.adapters.RecycleViewItemListener;
import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends PermissionActivity implements PoiSearch.OnPoiSearchListener {
    private static final int REQUEST_CODE_PICK_CITY = 233;
    private EditText mEtContent;
    private TextView tv_send;
    private String mSearchText;
    private LatLonPoint lp;//
    private PoiResult poiResult; // poi返回的结果
    private ArrayList<com.amap.api.services.core.PoiItem> poiItems;// poi数据
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private RecyclerView recycleView;
    private AddressSearchAdapter mAddressSearchAdapter;
    private ArrayList<AddressEntity> mDatas = new ArrayList<>();
    private String city;
    private TextView tv_select_city;
    private LinearLayout ll_toselectcity_fmt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        city = getIntent().getStringExtra("city");
        mEtContent = findViewById(R.id.et_search);
        tv_send = findViewById(R.id.tv_send);
        tv_send.setText("搜索");
        tv_select_city.setText(city);
        recycleView = findViewById(R.id.recycleView);
        tv_select_city = findViewById(R.id.tv_select_city);
        ll_toselectcity_fmt = findViewById(R.id.ll_toselectcity_fmt);
//        LatLng point = getIntent().getParcelableExtra("point");
        ll_toselectcity_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SearchActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });
//        lp = new LatLonPoint(116.46, 39.92);

        // Rv 列表
        recycleView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);

        mAddressSearchAdapter = new AddressSearchAdapter(SearchActivity.this, mDatas);
        recycleView.setAdapter(mAddressSearchAdapter);
        mAddressSearchAdapter.setItemListener(new RecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                AddressEntity addressEntity = mDatas.get(position);
                intent.putExtra("backEntity", addressEntity);
                setResult(RESULT_OK, intent);
                Logger.e("backEntity" + addressEntity.toString());
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchText = mEtContent.getText().toString().trim();
                if (TextUtils.isEmpty(mSearchText)) {
                    ToastUtils.getToast(SearchActivity.this, "请输入搜索关键字");
                    return;
                } else {
                    doSearchQueryWithKeyWord(mSearchText);
                    KeyboardUtils.hideKeyboard(mEtContent, (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                }
            }
        });
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchText = mEtContent.getText().toString().trim();
                doSearchQueryWithKeyWord(mSearchText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void doSearchQueryWithKeyWord(String key) {
        //ToastUtils.getToast(this,city);
        currentPage = 0;
        query = new PoiSearch.Query(key, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true); //限定城市

//        if (lp != null) {
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);   // 实现  onPoiSearched  和  onPoiItemSearched
//            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
//        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    mDatas.clear();
//                    mDatas.add(mAddressTextFirst);// 第一个元素
                    AddressEntity addressEntity;
                    for (int i = 0; i < poiItems.size(); i++) {
                        com.amap.api.services.core.PoiItem poiItem = poiItems.get(i);
                        if (i == 0) {
                            addressEntity = new AddressEntity(true, poiItem.getLatLonPoint(), poiItem.getSnippet(), poiItem.getTitle());
                        } else {
                            addressEntity = new AddressEntity(false, poiItem.getLatLonPoint(), poiItem.getSnippet(), poiItem.getTitle());
                        }
                        Logger.d("得到的数据 poiItem "
                                + "\npoiItem.getSnippet()" + poiItem.getSnippet()
                                + "\npoiItem.getAdCode()" + poiItem.getAdCode()
                                + "\npoiItem.getAdName()" + poiItem.getAdName()
                                + "\npoiItem.getDirection()" + poiItem.getDirection()
                                + "\npoiItem.getBusinessArea()" + poiItem.getBusinessArea()
                                + "\npoiItem.getCityCode()" + poiItem.getCityCode()
                                + "\npoiItem.getEmail()" + poiItem.getEmail()
                                + "\npoiItem.getParkingType()" + poiItem.getParkingType()
                                + "\npoiItem.getCityName()" + poiItem.getCityName()
                                + "\npoiItem.getProvinceName()" + poiItem.getProvinceName()
                                + "\npoiItem.getSnippet()" + poiItem.getSnippet()
                                + "\npoiItem.getTitle()" + poiItem.getTitle()
                                + "\npoiItem.getTypeDes()" + poiItem.getTypeDes()
                                + "\npoiItem.getDistance()" + poiItem.getDistance()
                                + "\npoiItem.getWebsite()" + poiItem.getWebsite()
                        );

                        mDatas.add(addressEntity);
                    }
                    mAddressSearchAdapter.notifyDataSetChanged();
                }
            } else {
                ToastUtils.getToast(SearchActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtils.getToast(SearchActivity.this, "没有数据！");
        }
    }

    @Override
    public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == Activity.RESULT_OK) {
            city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            tv_select_city.setText(city);
        }
    }

    /*@Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }*/
}
