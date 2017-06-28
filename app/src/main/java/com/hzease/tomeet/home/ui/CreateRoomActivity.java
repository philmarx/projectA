package com.hzease.tomeet.home.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.data.CreateRoomBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.plugins.SelectData;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateRoomActivity extends NetActivity {
    public static final int RESULT_PLACE = 1314;
    @BindView(R.id.sv_createroom)
    ScrollView sv_createroom;

    @BindView(R.id.tv_createroom_gameName_aty)
    TextView tv_createroom_gameName_aty;
    @BindView(R.id.et_createroom_roomName_aty)
    EditText et_createroom_roomName_aty;
    @BindView(R.id.tv_createroom_modifitylocation_aty)
    EditText tv_createroom_modifitylocation_aty;
    @BindView(R.id.tv_createroom_starttime_fmt)
    TextView tv_createroom_starttime_fmt;
    @BindView(R.id.rl_createroom_starttime_fmt)
    AutoRelativeLayout rl_createroom_starttime_fmt;
    @BindView(R.id.tv_createroom_endtime_fmt)
    TextView tv_createroom_endtime_fmt;
    @BindView(R.id.tv_more_createroom)
    TextView tv_more_createroom;
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
    private int manAccount = 0;
    private int womanAccount = 0;
    private int memberAccount;
    private double myLongitude;
    private double myLatitude;
    private int money;
    private long circleId;
    private boolean isOpen;
    //地点是否可编辑
    private boolean isModifity = false;

    @OnClick({
            R.id.iv_createroom_chooseplace_act,
            R.id.rl_createroom_starttime_fmt,
            R.id.rl_createroom_endtime_fmt,
            R.id.bt_createroom_cancel_aty,
            R.id.bt_createaroom_success_aty,
            R.id.tv_more_createroom

    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_createroom:
                sv_createroom.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case R.id.iv_createroom_chooseplace_act:
                Intent openSend = new Intent(CreateRoomActivity.this, ShareLocationActivity.class);
                Logger.e(mLongitude + "");
                Logger.e(mLatitude + "");
                Logger.e(cityCode + "");
                Logger.e(cityName + "");
                openSend.putExtra("lon", mLongitude);
                openSend.putExtra("lat", mLatitude);
                openSend.putExtra("cityCode", cityCode);
                openSend.putExtra("cityName", cityName);
                startActivityForResult(openSend, RESULT_PLACE);
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
                if (roomName.isEmpty()) {
                    ToastUtils.getToast(this, "请输入房间名称");
                    break;
                }
                //活动地点
                String place = tv_createroom_modifitylocation_aty.getText().toString().trim();
                if (place.isEmpty()) {
                    ToastUtils.getToast(this, "请选择活动地点");
                    break;
                }
                //开始时间
                String starttime = tv_createroom_starttime_fmt.getText().toString().trim();
                if (starttime.isEmpty()) {
                    ToastUtils.getToast(this, "请选择开始时间");
                    break;
                }
                //结束时间
                String endtime = tv_createroom_endtime_fmt.getText().toString().trim();
                if (starttime.isEmpty()) {
                    ToastUtils.getToast(this, "请选择结束时间");
                    break;
                }

                String member = tv_createroom_memberaccout_fmt.getText().toString().trim();

                int i = compare_date(starttime, endtime);
                switch (i) {
                    case 0:
                    case 1:
                        ToastUtils.getToast(this, "结束时间必须大于开始时间");
                        return;
                }
                int k = calculateTime(starttime,endtime);
                if (k == 1){
                    ToastUtils.getToast(this, "活动时间必须超过一小时");
                    break;
                }
                if (cb_createroom_hasSex_aty.isChecked()) {
                    Logger.e(tv_createroom_manaccout_fmt.getText().toString().trim() + " -       - " + tv_createroom_femanaccout_fmt.getText().toString().trim());
                    manAccount = tv_createroom_manaccout_fmt.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(tv_createroom_manaccout_fmt.getText().toString().trim());
                    womanAccount = tv_createroom_femanaccout_fmt.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(tv_createroom_femanaccout_fmt.getText().toString().trim());
                    memberAccount = manAccount + womanAccount;
                } else {
                    if (member.isEmpty()) {
                        ToastUtils.getToast(this, "请输入活动人数");
                        break;
                    }
                    manAccount = 0;
                    womanAccount = 0;
                    memberAccount = Integer.parseInt(member);
                }
                if (memberAccount < 2) {
                    ToastUtils.getToast(this, "活动总人数不能少于两个！");
                    break;
                }
                String pwd = et_createaroom_pwd_aty.getText().toString().trim();
                if (et_createaroom_money_aty.getText().toString().trim().isEmpty()) {
                    money = 0;
                } else {
                    String temp = String.valueOf(Double.valueOf(et_createaroom_money_aty.getText().toString().trim()) * 100).split("\\.")[0];
                    money = Integer.valueOf(temp);
                }
                String description = et_createaroom_msg_aty.getText().toString().trim();

                PTApplication.getRequestService().createRoom(starttime, description, circleId, endtime, cityName, manAccount, myLatitude, myLongitude, memberAccount, money, roomName,
                        pwd, place, PTApplication.userToken, PTApplication.userId, womanAccount, ganmeId, isOpen, 0)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<CreateRoomBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError: " + e.getMessage());
                                ToastUtils.getToast(PTApplication.getInstance(), "创建活动失败，请重试");
                            }

                            @Override
                            public void onNext(CreateRoomBean createRoomBean) {
                                Logger.e("onNext");
                                if (createRoomBean.isSuccess()) {
                                    startActivity(new Intent(CreateRoomActivity.this, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, String.valueOf(createRoomBean.getData().getId())));
                                    finish();
                                } else {
                                    ToastUtils.getToast(PTApplication.getInstance(), createRoomBean.getMsg());
                                    Logger.e(createRoomBean.getMsg());
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

    public static int calculateTime(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            long diff = dt2.getTime() / 60000 - dt1.getTime() / 60000;
            if (diff < 60) {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        return 0;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        et_createaroom_money_aty.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {// ----->如果字符未改变则返回
                    return;
                }
                String str = s.toString();

                isChanged = true;
                String cuttedStr = str;
                /* 删除字符串中的dot */
                for (int i = str.length() - 1; i >= 0; i--) {
                    char c = str.charAt(i);
                    if ('.' == c) {
                        cuttedStr = str.substring(0, i)
                                + str.substring(i + 1);
                        break;
                    }
                }
                /* 删除前面多余的0 */
                int NUM = cuttedStr.length();
                int zeroIndex = -1;
                for (int i = 0; i < NUM - 2; i++) {
                    char c = cuttedStr.charAt(i);
                    if (c != '0') {
                        zeroIndex = i;
                        break;
                    } else if (i == NUM - 3) {
                        zeroIndex = i;
                        break;
                    }
                }
                if (zeroIndex != -1) {
                    cuttedStr = cuttedStr.substring(zeroIndex);
                }
                /* 不足3位补0 */
                if (cuttedStr.length() < 3) {
                    cuttedStr = "0" + cuttedStr;
                }
                /* 加上dot，以显示小数点后两位 */
                cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)
                        + "." + cuttedStr.substring(cuttedStr.length() - 2);

                et_createaroom_money_aty.setText(cuttedStr);

                et_createaroom_money_aty.setSelection(et_createaroom_money_aty.length());
                isChanged = false;
            }
        });
        tv_createroom_modifitylocation_aty.setEnabled(isModifity);
        Intent data = this.getIntent();
        String gameName = data.getStringExtra(CreateRoomBeforeActivity.KEY_PICKED_CITY);
        circleId = data.getLongExtra("circleId", 0);
        isOpen = data.getBooleanExtra("isOpen", true);

        ganmeId = data.getIntExtra(CreateRoomBeforeActivity.KEY_GAME_ID, 0);
        tv_createroom_gameName_aty.setText(gameName);
        et_createroom_roomName_aty.setText(gameName);
        initLogLat();
        cb_createroom_hasSex_aty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    all_sex_outnumber.setVisibility(View.VISIBLE);
                    all_nosex_outnumber.setVisibility(View.GONE);
                } else {
                    all_sex_outnumber.setVisibility(View.GONE);
                    all_nosex_outnumber.setVisibility(View.VISIBLE);
                }
            }
        });

/*        tv_createroom_manaccout_fmt.setText("0");
        tv_createroom_femanaccout_fmt.setText("0");*/
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
        if (requestCode == RESULT_PLACE && resultCode == this.RESULT_OK) {
            if (data != null) {
                String place = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                tv_createroom_modifitylocation_aty.setText(place);
                myLongitude = data.getDoubleExtra(ShareLocationActivity.LONGITUDE, 0);
                myLatitude = data.getDoubleExtra(ShareLocationActivity.LATITUDE, 0);
                isModifity = true;
                String uri = data.getStringExtra("thumb");
                double lat = data.getDoubleExtra("lat", 100.00);
                double lng = data.getDoubleExtra("lng", 100.00);
                String poi = data.getStringExtra("poi");
                Logger.e("uri" + uri + "\nlat" + lat + "\nlng" + lng + "\npoi" + poi);
                //ToastUtils.getToast(PTApplication.getInstance(),place);
                if (isModifity) {
                    tv_createroom_modifitylocation_aty.setEnabled(isModifity);
                }
            }
        }
    }
}
