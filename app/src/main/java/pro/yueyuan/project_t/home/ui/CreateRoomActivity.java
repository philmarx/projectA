package pro.yueyuan.project_t.home.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.utils.ToastUtils;
import pro.yueyuan.project_t.widget.CashierInputFilter;
import pro.yueyuan.project_t.widget.DateChooseWheelViewDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateRoomActivity extends NetActivity implements View.OnClickListener{

    @BindView(R.id.room_name)
    EditText mRoomName;
    @BindView(R.id.room_pwd)
    EditText mRoomPwd;
    @BindView(R.id.room_activityName)
    EditText mRoomActivityName;
    @BindView(R.id.room_starttime)
    TextView mRoomStarttime;
    @BindView(R.id.room_endtime)
    TextView mRoomEndtime;
    @BindView(R.id.room_activityPlace)
    EditText mRoomActivityPlace;
    @BindView(R.id.room_margin)
    EditText mRoomMargin;
    @BindView(R.id.room_personNum)
    EditText mRoomPersonNum;
    @BindView(R.id.room_man)
    EditText mRoomMan;
    @BindView(R.id.room_woman)
    EditText mRoomWoman;
    @BindView(R.id.room_description)
    EditText mRoomDescription;
    @BindView(R.id.room_createRoom)
    Button mRoomCreateRoom;

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_createroom;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        /*SharedPreferences sharedPreferences = getSharedPreferences("wonengzhemerongyirangnirenchulai",MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userId","");
        final String userToken = sharedPreferences.getString("userToken","");*/
        mRoomCreateRoom.setOnClickListener(this);
        mRoomStarttime.setOnClickListener(this);
        mRoomEndtime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.room_createRoom:
               createRoom();
                break;
            case R.id.room_starttime:
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(CreateRoomActivity.this,
                        new DateChooseWheelViewDialog.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                mRoomStarttime.setText(time);
                            }
                        });
                startDateChooseDialog.setDateDialogTitle("开始时间");
                startDateChooseDialog.showDateChooseDialog();
                break;
            case R.id.room_endtime:
                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(CreateRoomActivity.this,
                        new DateChooseWheelViewDialog.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                mRoomEndtime.setText(time);
                            }
                        });
                //endDateChooseDialog.setTimePickerGone(true);
                endDateChooseDialog.setDateDialogTitle("结束时间");
                endDateChooseDialog.showDateChooseDialog();
                break;
        }
    }

    /**
     * 创建房间
     */
    private void createRoom() {
        //Logger.e("id", PTApplication.userId);
        //Logger.e("token",PTApplication.userToken);
        InputFilter[] filters = {new CashierInputFilter()};
        mRoomMargin.setFilters(filters);
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
        mRoomMan.setRawInputType(inputType);
        //获取房间名称
        String roomName = mRoomName.getText().toString().trim();
        //获取活动地点
        String activityPlace = mRoomActivityPlace.getText().toString().trim();
        //获取男生人数
        String manCount = mRoomMan.getText().toString().trim();
        //获取女生人数
        String womanCount = mRoomWoman.getText().toString().trim();
        //获取总人数
        String personCount = mRoomPersonNum.getText().toString().trim();
        //获取保证金
        String roomMargin = mRoomMargin.getText().toString().trim();
        if ((Integer.valueOf(manCount) + Integer.valueOf(womanCount) == Integer.valueOf(personCount)) && (!TextUtils.isEmpty(roomName))
                && (!TextUtils.isEmpty(activityPlace))){
            if (TextUtils.isEmpty(roomMargin)){
                roomMargin = "0";
            }
            mRequestService.createRoom(mRoomStarttime.getText().toString().trim()+":00",//添加到秒
                    mRoomDescription.getText().toString().trim(),
                    mRoomEndtime.getText().toString().trim()+":00",//添加到秒
                    mRoomMan.getText().toString().trim(),
                    mRoomPersonNum.getText().toString().trim(),
                    roomMargin,
                    roomName,
                    mRoomPwd.getText().toString().trim(),
                    activityPlace,
                    PTApplication.userToken,
                    PTApplication.userId,
                    mRoomWoman.getText().toString().trim(),
                    mRoomActivityName.getText().toString().trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserInfoBean>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                        }
                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            Log.e("房间是否创建成功",String.valueOf(userInfoBean.isSuccess()));
                        }
                    });
        }else{
            ToastUtils.getToast(CreateRoomActivity.this,"请完善房间信息");
        }
    }
}
