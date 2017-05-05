package com.hzease.tomeet.home.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.plugins.SelectData;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateRoomActivity extends NetActivity {
    public static final int RESULT_PLACE = 1314;
    @BindView(R.id.tv_createroom_gameName_aty)
    TextView tv_createroom_gameName_aty;
    @BindView(R.id.et_createroom_roomName_aty)
    EditText et_createroom_roomName_aty;
    @BindView(R.id.tv_createroom_chosePlace_aty)
    TextView tv_createroom_chosePlace_aty;
    @BindView(R.id.tv_createroom_starttime_fmt)
    TextView tv_createroom_starttime_fmt;
    @BindView(R.id.rl_createroom_starttime_fmt)
    AutoRelativeLayout rl_createroom_starttime_fmt;
    @BindView(R.id.tv_createroom_endtime_fmt)
    TextView tv_createroom_endtime_fmt;
    @BindView(R.id.rl_createroom_endtime_fmt)
    AutoRelativeLayout rl_createroom_endtime_fmt;
    @BindView(R.id.tv_createroom_memberaccout_fmt)
    EditText tv_createroom_memberaccout_fmt;
    @BindView(R.id.all_nosex_outnumber)
    AutoLinearLayout all_nosex_outnumber;
    @BindView(R.id.tv_createroom_manaccout_fmt)
    EditText tv_createroom_manaccout_fmt;
    @BindView(R.id.tv_createroom_femanaccout_fmt)
    EditText tv_createroom_femanaccout_fmt;
    @BindView(R.id.all_sex_outnumber)
    AutoLinearLayout all_sex_outnumber;
    @BindView(R.id.cb_createroom_hasSex_aty)
    CheckBox cb_createroom_hasSex_aty;
    @BindView(R.id.rl_createroom_memberaccout_fmt)
    AutoRelativeLayout rl_createroom_memberaccout_fmt;
    @BindView(R.id.et_createaroom_pwd_aty)
    EditText et_createaroom_pwd_aty;
    @BindView(R.id.et_createaroom_money_aty)
    EditText et_createaroom_money_aty;
    @BindView(R.id.et_createaroom_msg_aty)
    EditText et_createaroom_msg_aty;
    @BindView(R.id.bt_createroom_cancel_aty)
    Button bt_createroom_cancel_aty;
    @BindView(R.id.bt_createaroom_success_aty)
    Button bt_createaroom_success_aty;
    private int ganmeId;
    private double mLongitude;
    private double mLatitude;
    private String cityCode;
    private String cityName;
    private SelectData selectData;
    private String startTime;
    private String endTime;
    private int manAccount;
    private int womanAccount;
    private int memberAccount;
    private double myLongitude;
    private double myLatitude;
    private int money;


    @OnClick({
            R.id.tv_createroom_chosePlace_aty,
            R.id.rl_createroom_starttime_fmt,
            R.id.rl_createroom_endtime_fmt,
            R.id.bt_createroom_cancel_aty,
            R.id.bt_createaroom_success_aty

    })
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_createroom_chosePlace_aty:
                Intent openSend = new Intent(CreateRoomActivity.this,ShareLocationActivity.class);
                Logger.e(mLongitude+"");
                Logger.e(mLatitude+"");
                Logger.e(cityCode+"");
                Logger.e(cityName+"");
                openSend.putExtra("lon",mLongitude);
                openSend.putExtra("lat",mLatitude);
                openSend.putExtra("cityCode",cityCode);
                openSend.putExtra("cityName",cityName);
                startActivityForResult(openSend,RESULT_PLACE);
                //startActivity(openSend);
                break;
            case R.id.rl_createroom_starttime_fmt:
                selectData = new SelectData(this, true);
                selectData.showAtLocation(tv_createroom_starttime_fmt, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        startTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        tv_createroom_starttime_fmt.setText(startTime);
                    }
                });
                break;
            case R.id.rl_createroom_endtime_fmt:
                selectData = new SelectData(this, true);
                selectData.showAtLocation(tv_createroom_starttime_fmt, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        tv_createroom_endtime_fmt.setText(endTime);
                    }
                });
                break;
            case R.id.bt_createroom_cancel_aty:
                finish();
                break;
            case R.id.bt_createaroom_success_aty:
                //房间名称
                String roomName = et_createroom_roomName_aty.getText().toString().trim();
                //活动地点
                String place = tv_createroom_chosePlace_aty.getText().toString().trim();
                //开始时间
                String starttime = tv_createroom_starttime_fmt.getText().toString().trim();
                //结束时间
                String endtime = tv_createroom_endtime_fmt.getText().toString().trim();
                int i = compare_date(starttime, endtime);
                switch (i){
                    case 0:
                    case 1:
                        ToastUtils.getToast(this,"结束时间必须大于开始时间");
                        return;
                }
                if (cb_createroom_hasSex_aty.isChecked()){
                    manAccount = Integer.parseInt(tv_createroom_manaccout_fmt.getText().toString().trim());
                    womanAccount = Integer.parseInt(tv_createroom_femanaccout_fmt.getText().toString().trim());
                    memberAccount = manAccount + womanAccount;
                }else{
                    manAccount = 0;
                    womanAccount = 0;
                    memberAccount = Integer.parseInt(tv_createroom_memberaccout_fmt.getText().toString().trim());
                }
                String pwd = et_createaroom_pwd_aty.getText().toString().trim();
                if (et_createaroom_money_aty.getText().toString().trim().isEmpty()){
                    money = 0;
                }else{
                    money = Integer.parseInt(et_createaroom_money_aty.getText().toString().trim())*100;
                }
                String description = et_createaroom_msg_aty.getText().toString().trim();
                PTApplication.getRequestService().createRoom(starttime,description,0,endtime,cityName,manAccount,myLatitude,myLongitude,memberAccount,money,roomName,
                        pwd,place,PTApplication.userToken,PTApplication.userId,womanAccount,ganmeId,true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                                Logger.e("onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError");
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e("onNext");
                                if (noDataBean.isSuccess()){
                                    ToastUtils.getToast(PTApplication.getInstance(),"创建房间成功");
                                    finish();
                                }else{
                                    ToastUtils.getToast(PTApplication.getInstance(),noDataBean.getMsg());
                                    Logger.e(noDataBean.getMsg());
                                }
                            }
                        });
                break;


        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_createroom;
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent data = this.getIntent();
        String gameName = data.getStringExtra(CreateRoomBeforeActivity.KEY_PICKED_CITY);
        ganmeId = data.getIntExtra(CreateRoomBeforeActivity.KEY_GAME_ID, 0);
        tv_createroom_gameName_aty.setText(gameName);
        initLogLat();
        cb_createroom_hasSex_aty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    all_sex_outnumber.setVisibility(View.VISIBLE);
                    all_nosex_outnumber.setVisibility(View.GONE);
                }else{
                    all_sex_outnumber.setVisibility(View.GONE);
                    all_nosex_outnumber.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                cityCode = aMapLocation.getCityCode();
                cityName = aMapLocation.getCity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PLACE && resultCode ==this.RESULT_OK){
            if (data != null){
                String place = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                tv_createroom_chosePlace_aty.setText(place);
                myLongitude = data.getDoubleExtra(ShareLocationActivity.LONGITUDE,0);
                myLatitude = data.getDoubleExtra(ShareLocationActivity.LATITUDE,0);
                //ToastUtils.getToast(PTApplication.getInstance(),place);
            }
        }
    }
}
