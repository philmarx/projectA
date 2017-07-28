package com.hzease.tomeet.game.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/27.
 */

public class NoLateActivity extends NetActivity {
    @BindView(R.id.tv_home_rooms_prove_person_act)
    TextView tv_home_rooms_prove_person_act;
    @BindView(R.id.et_home_room_notlatecontent_fmt)
    EditText et_home_room_notlatecontent_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_fmt)
    ImageView iv_home_room_notlatephoto_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_two_fmt)
    ImageView iv_home_room_notlatephoto_two_fmt;
    @BindView(R.id.iv_home_room_notlatephoto_three_fmt)
    ImageView iv_home_room_notlatephoto_three_fmt;
    @BindView(R.id.bt_home_room_notlate_fmt)
    Button bt_home_room_notlate_fmt;
    private String roomId;
    private int witchPhoto;
    private PopupWindow popupWindow;
    private Uri uriForFileApiN;
    private List<String> mNotLatePhotos = new ArrayList<>();
    private long otherId = 0;


    @OnClick({
            R.id.tv_home_rooms_prove_person_act,
            R.id.bt_home_room_notlate_fmt,
            R.id.iv_home_room_notlatephoto_fmt,
            R.id.iv_home_room_notlatephoto_two_fmt,
            R.id.iv_home_room_notlatephoto_three_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_home_rooms_prove_person_act:
                Intent getIntent = getIntent();
                roomId = getIntent.getStringExtra("roomId");
                Intent startIntent = new Intent(this, ChooseUserActivity.class);
                startIntent.putExtra("roomId", roomId);
                Logger.e("roomId" + roomId);
                startActivity(startIntent);
                break;
            case R.id.bt_home_room_notlate_fmt:
                if (otherId == 0){
                    ToastUtils.getToast(this,"请选择为你证明未迟到的用户");
                    break;
                }
                if (et_home_room_notlatecontent_fmt.getText().toString().isEmpty()){
                    ToastUtils.getToast(this,"请输入未签到理由");
                    break;
                }
                Gson gson = new Gson();
                String s = gson.toJson(mNotLatePhotos);
                PTApplication.getRequestService().noLateWithReason(String.valueOf(otherId),s,et_home_room_notlatecontent_fmt.getText().toString().trim(),
                        roomId,PTApplication.userToken,PTApplication.userId)
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
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(NoLateActivity.this, "操作成功！");
                                    finish();
                                } else {
                                    ToastUtils.getToast(NoLateActivity.this,noDataBean.getMsg());
                                }
                            }
                        });
                break;
            case R.id.iv_home_room_notlatephoto_fmt:
                witchPhoto = 0;
                initPopupWindow();
                break;
            case R.id.iv_home_room_notlatephoto_two_fmt:
                witchPhoto = 1;
                initPopupWindow();
                break;
            case R.id.iv_home_room_notlatephoto_three_fmt:
                witchPhoto = 2;
                initPopupWindow();
                break;

        }
    }
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_nolate;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(GameChatRoomBean.DataBean.JoinMembersBean membersBean) {
        otherId = membersBean.getId();
        String nickName = membersBean.getNickname();
        tv_home_rooms_prove_person_act.setText(nickName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    /**
     * 底部弹出popwind
     */
    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    protected void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_login, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
        backgroundAlpha(0.3f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        TextView gallery = (TextView) popupWindowView.findViewById(R.id.local);
        TextView camera = (TextView) popupWindowView.findViewById(R.id.tokenphoto);
        TextView close = (TextView) popupWindowView.findViewById(R.id.close);
        // 相册选择头像
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    //intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
                popupWindow.dismiss();
            }
        });
        // 拍照选择头像
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("权限：" + ContextCompat.checkSelfPermission(NoLateActivity.this, Manifest.permission.CAMERA));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(NoLateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 只需要相机权限,不需要SD卡读写权限
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAKE_PHOTO_PERMISSION);
                } else {
                    takePhotoForAvatar();
                }
                popupWindow.dismiss();
            }
        });
        // 关闭
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void takePhotoForAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uriForFileApiN = FileProvider.getUriForFile(NoLateActivity.this, "com.hzease.tomeet.FileProvider", new File(PTApplication.imageLocalCachePath, "/imageTemp"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFileApiN);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
        }
        if (ImageCropUtils.checkFileExists()) {
            startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.e("onRequestPermissionsResult:\n" + requestCode + "\n" + Arrays.toString(permissions) + "\n" + Arrays.toString(grantResults));

        switch (requestCode) {
            // 请求相机权限
            case AppConstants.REQUEST_TAKE_PHOTO_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("相机权限申请成功");
                    takePhotoForAvatar();
                } else {
                    ToastUtils.getToast(NoLateActivity.this, "相机权限被禁止,无法打开照相机");
                }
                break;
            // 请求SD卡写入权限,一般不可能会弹出来,以防万一
            case AppConstants.REQUEST_SD_WRITE_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("SD权限申请成功");
                } else {
                    ToastUtils.getToast(NoLateActivity.this, "没有读写SD卡的权限");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(NoLateActivity.this, "取消上传头像");
            return;
        }
        Intent resultIntent = null;
        switch (requestCode) {
            //如果是来自相册,直接裁剪图片
            case AppConstants.REQUEST_CODE_GALLERY:
                resultIntent = ImageCropUtils.cropImage(intent.getData());
                break;
            case AppConstants.REQUEST_CODE_CAMERA:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    resultIntent = ImageCropUtils.cropImage(uriForFileApiN);
                } else {
                    resultIntent = ImageCropUtils.cropImage(PTApplication.imageLocalCache);
                }
                break;
            case AppConstants.REQUEST_CODE_CROP:
                long currentTime = System.currentTimeMillis();
                //设置图片框并上传
                switch (witchPhoto) {
                    case 0:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_COMPLAINT + currentTime, iv_home_room_notlatephoto_fmt);
                        break;
                    case 1:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_COMPLAINT + currentTime, iv_home_room_notlatephoto_two_fmt);
                        break;
                    case 2:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_COMPLAINT + currentTime, iv_home_room_notlatephoto_three_fmt);
                        break;
                }
                mNotLatePhotos.add(AppConstants.YY_PT_OSS_FEEDBACK.substring(1) +currentTime);
                if (iv_home_room_notlatephoto_two_fmt.getVisibility() != 0) {
                    iv_home_room_notlatephoto_two_fmt.setVisibility(0);
                } else {
                    if (iv_home_room_notlatephoto_three_fmt.getVisibility() != 0) {
                        iv_home_room_notlatephoto_three_fmt.setVisibility(0);
                    }
                }
                break;
        }
        if (requestCode == AppConstants.REQUEST_CODE_GALLERY || requestCode == AppConstants.REQUEST_CODE_CAMERA) {
            if (resultIntent != null) {
                // 只有Intent正确回来的时候才会进来,所有的判断都在创建Intent的时候做
                Logger.d(resultIntent);
                startActivityForResult(resultIntent, AppConstants.REQUEST_CODE_CROP);
            } else {
                // 创建不了,大部分可能是因为没权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 只需要相机权限,不需要SD卡读写权限
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.REQUEST_SD_WRITE_PERMISSION);
                }
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
