package com.hzease.tomeet.home.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateRoomActivity extends NetActivity {
    private static final int RESULT_PLACE = 5201314;
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
                //startActivityForResult(openSend,RESULT_PLACE);
                startActivity(openSend);
                break;
            case R.id.rl_createroom_starttime_fmt:

                break;
            case R.id.rl_createroom_endtime_fmt:

                break;
            case R.id.bt_createroom_cancel_aty:

                break;
            case R.id.bt_createaroom_success_aty:

                break;
            case R.id.cb_createroom_hasSex_aty:
                if (cb_createroom_hasSex_aty.isChecked()){
                    all_sex_outnumber.setVisibility(View.VISIBLE);
                    all_nosex_outnumber.setVisibility(View.GONE);
                }else{
                    all_sex_outnumber.setVisibility(View.GONE);
                    all_nosex_outnumber.setVisibility(View.VISIBLE);
                }
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

    @Override
    protected void initLayout(Bundle savedInstanceState) throws ExecutionException, InterruptedException {
        Intent data = this.getIntent();
        String gameName = data.getStringExtra(CreateRoomBeforeActivity.KEY_PICKED_CITY);
        ganmeId = data.getIntExtra(CreateRoomBeforeActivity.KEY_GAME_ID, 0);
        tv_createroom_gameName_aty.setText(gameName);
        initLogLat();
        if (cb_createroom_hasSex_aty.isChecked()){
            all_sex_outnumber.setVisibility(View.VISIBLE);
            all_nosex_outnumber.setVisibility(View.GONE);
        }else{
            all_sex_outnumber.setVisibility(View.GONE);
            all_nosex_outnumber.setVisibility(View.VISIBLE);
        }
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PLACE && resultCode ==this.RESULT_OK){
            if (data != null){
                String place = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                ToastUtils.getToast(PTApplication.getInstance(),place);
            }
        }
    }
}
