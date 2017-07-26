package com.hzease.tomeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.plugins.SelectData;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/4/11.
 */

public class ModitfyRoomInfoActivity extends NetActivity {
    //房间名称
    @BindView(R.id.et_moditity_roomName)
    EditText et_moditity_roomName;
    //房间详情
    @BindView(R.id.et_moditity_roomDisc)
    EditText et_moditity_roomDisc;
    //开始时间
    @BindView(R.id.tv_moditity_roomBeginTime)
    TextView tv_moditity_roomBeginTime;
    //选择开始时间
    @BindView(R.id.rl_moditity_starttime_fmt)
    AutoRelativeLayout rl_moditity_starttime_fmt;
    //结束时间
    @BindView(R.id.tv_moditity_endtime_fmt)
    TextView tv_moditity_endtime_fmt;
    //选择结束时间
    @BindView(R.id.rl_moditity_endtime_fmt)
    AutoRelativeLayout rl_moditity_endtime_fmt;
    //所有人数
    @BindView(R.id.tv_moditity_memberaccout_fmt)
    EditText tv_moditity_memberaccout_fmt;
    //显示所有人数
    @BindView(R.id.all_nosex_outnumber)
    AutoLinearLayout all_nosex_outnumber;
    //男生人数
    @BindView(R.id.tv_moditity_manaccout_fmt)
    EditText tv_moditity_manaccout_fmt;
    //女生人数
    @BindView(R.id.tv_moditity_femanaccout_fmt)
    EditText tv_moditity_femanaccout_fmt;
    @BindView(R.id.all_sex_outnumber)
    AutoLinearLayout all_sex_outnumber;
    @BindView(R.id.cb_modifity_hasSex_aty)
    CheckBox cb_modifity_hasSex_aty;
    //确定按钮
    @BindView(R.id.bt_moditity_sure_act)
    Button bt_moditity_sure_act;
    private String startTime;
    private String endTime;
    private SelectData selectData;
    private String roomId;
    private int manAccount;
    private int womanAccount;
    private int memberAccount;

    @OnClick({
            R.id.rl_moditity_starttime_fmt,
            R.id.rl_moditity_endtime_fmt,
            R.id.bt_moditity_sure_act
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_moditity_starttime_fmt:
                selectData = new SelectData(this, true,-1);
                selectData.showAtLocation(tv_moditity_roomBeginTime, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        startTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        tv_moditity_roomBeginTime.setText(startTime);
                    }
                });
                break;
            case R.id.rl_moditity_endtime_fmt:
                selectData = new SelectData(this, true,-1);
                selectData.showAtLocation(tv_moditity_endtime_fmt, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                        tv_moditity_endtime_fmt.setText(endTime);
                    }
                });
                break;
            case R.id.bt_moditity_sure_act:
                String roomName = et_moditity_roomName.getText().toString().trim();
                String startTime = tv_moditity_roomBeginTime.getText().toString().trim();
                String endTime = tv_moditity_endtime_fmt.getText().toString().trim();
                String desc = et_moditity_roomDisc.getText().toString().trim();
                String member = tv_moditity_memberaccout_fmt.getText().toString().trim();
                int k = calculateTime(startTime,endTime);
                if (k == 1){
                    ToastUtils.getToast(this, "活动时间必须超过一小时");
                    break;
                }
                if (cb_modifity_hasSex_aty.isChecked()){
                    Logger.e(tv_moditity_manaccout_fmt.getText().toString().trim() + " -       - " + tv_moditity_femanaccout_fmt.getText().toString().trim());
                    manAccount = tv_moditity_manaccout_fmt.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(tv_moditity_manaccout_fmt.getText().toString().trim());
                    womanAccount = tv_moditity_femanaccout_fmt.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(tv_moditity_femanaccout_fmt.getText().toString().trim());
                    memberAccount = manAccount + womanAccount;
                }else{
                    if (member.isEmpty()){
                        ToastUtils.getToast(this,"请输入活动人数");
                        break;
                    }
                    manAccount = 0;
                    womanAccount = 0;
                    memberAccount = Integer.parseInt(member);
                }
                PTApplication.getRequestService().updateRoomInfo(startTime,desc,endTime,manAccount,memberAccount,roomName,roomId,PTApplication.userToken,PTApplication.userId,womanAccount)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                showUpdateResult(noDataBean.isSuccess(),noDataBean.getMsg());
                            }
                        });
                break;
        }
    }

    private void showUpdateResult(boolean success, String msg) {
        if (success){
            ToastUtils.getToast(this,"更新房间信息成功");
            finish();
        }else{
            ToastUtils.getToast(this,msg);
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_moditfyroominfo;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String disc = intent.getStringExtra("disc");
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        int womanCount = intent.getIntExtra("womanCount",0);
        int manCount = intent.getIntExtra("manCount",0);
        int memberCount = intent.getIntExtra("membersCount",0);
        roomId = intent.getStringExtra("roomId");
        et_moditity_roomName.setText(roomName);
        et_moditity_roomDisc.setText(disc);
        tv_moditity_roomBeginTime.setText(startTime);
        tv_moditity_endtime_fmt.setText(endTime);
        if (womanCount == 0 && manCount == 0){
            cb_modifity_hasSex_aty.setChecked(false);
            tv_moditity_memberaccout_fmt.setText(memberCount+"");
        }else{
            cb_modifity_hasSex_aty.setChecked(true);
            tv_moditity_manaccout_fmt.setText(manCount+"");
            tv_moditity_femanaccout_fmt.setText(womanCount+"");
        }
        boolean checked = cb_modifity_hasSex_aty.isChecked();
        if (checked){
            all_sex_outnumber.setVisibility(View.VISIBLE);
            all_nosex_outnumber.setVisibility(View.GONE);
        }else{
            all_sex_outnumber.setVisibility(View.GONE);
            all_nosex_outnumber.setVisibility(View.VISIBLE);
        }
        cb_modifity_hasSex_aty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    public static int calculateTime(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

}
