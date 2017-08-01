package com.hzease.tomeet.circle.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.circle.ui.CircleInfoActivity;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/8/1.
 */

public class CreateCircleActivityFinishInfo extends NetActivity {

    @BindView(R.id.iv_perfectcircleInfo_bg)
    ImageView iv_perfectcircleInfo_bg;
    @BindView(R.id.civ_perfectcircleInfo_avatar)
    CircleImageView civ_perfectcircleInfo_avatar;
    @BindView(R.id.et_perfectcircleInfo_notices)
    EditText et_perfectcircleInfo_notices;
    @BindView(R.id.bt_perfectcircleInfo_complted)
    Button bt_perfectcircleInfo_complted;
    private long circleId;
    private PopupWindow popupforCircle;
    private int type;
    private String avatarTime = "";
    private String bgTime = "";
    private String notices = "";

    @OnClick({
            R.id.iv_perfectcircleInfo_bg,
            R.id.civ_perfectcircleInfo_avatar,
            R.id.bt_perfectcircleInfo_complted
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_perfectcircleInfo_bg:
                initPopupforCircle(2);
                break;
            case R.id.civ_perfectcircleInfo_avatar:
                initPopupforCircle(1);
                break;
            case R.id.bt_perfectcircleInfo_complted:
                notices = et_perfectcircleInfo_notices.getText().toString().trim();
                PTApplication.getRequestService().perfectCircleInfo(avatarTime,bgTime,circleId,notices,PTApplication.userToken,PTApplication.userId)
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
                                perfectInfoResult(noDataBean);
                            }
                        });
                break;
        }
    }

    //完善信息结果
    private void perfectInfoResult(NoDataBean noDataBean) {
        if (noDataBean.isSuccess()){
            Intent intent = new Intent(this, CircleInfoActivity.class);
            intent.putExtra("circleId", circleId);
            startActivity(intent);
            finish();
        }else{
            ToastUtils.getToast(this,noDataBean.getMsg());
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_createcircle_finishinfo;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        circleId = intent.getLongExtra("circleId", 0);
    }


    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    protected void initPopupforCircle(int type) {
        this.type = type;
        View popupWindowView = getLayoutInflater().inflate(R.layout.pop, null);
        //内容，高度，宽度
        popupforCircle = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupforCircle.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
    /*    ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindowforImage.setBackgroundDrawable(dw);*/
        //显示位置
        popupforCircle.showAtLocation(getLayoutInflater().inflate(R.layout.activity_login, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        //设置背景半透明
        //关闭事件
        popupforCircle.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
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

        TextView gallery = popupWindowView.findViewById(R.id.local);
        TextView camera = popupWindowView.findViewById(R.id.tokenphoto);
        TextView close = popupWindowView.findViewById(R.id.close);
        // 相册选择头像
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
                popupforCircle.dismiss();
            }
        });
        // 拍照选择头像
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("权限：" + ContextCompat.checkSelfPermission(CreateCircleActivityFinishInfo.this, Manifest.permission.CAMERA));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(CreateCircleActivityFinishInfo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 只需要相机权限,不需要SD卡读写权限
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAKE_PHOTO_PERMISSION);
                } else {
                    takePhotoForAvatar();
                }
                popupforCircle.dismiss();
            }
        });
        // 关闭
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupforCircle.dismiss();
            }
        });
    }
    public void takePhotoForAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
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
                    ToastUtils.getToast(this, "相机权限被禁止,无法打开照相机");
                }
                break;
            // 请求SD卡写入权限,一般不可能会弹出来,以防万一
            case AppConstants.REQUEST_SD_WRITE_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("SD权限申请成功");
                } else {
                    ToastUtils.getToast(this, "没有读写SD卡的权限");
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(this, "取消上传");
            return;
        }
        Intent resultIntent = null;
        switch (requestCode) {
            //如果是来自相册,直接裁剪图片
            case AppConstants.REQUEST_CODE_GALLERY:
                resultIntent = ImageCropUtils.cropImage(data.getData());
                break;
            case AppConstants.REQUEST_CODE_CAMERA:
                resultIntent = ImageCropUtils.cropImage(PTApplication.imageLocalCache);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                //设置图片框并上传
                switch (type) {
                    case 1:
                        avatarTime = String.valueOf(System.currentTimeMillis());
                        new OssUtils().setCircleImageToView(AppConstants.YY_PT_OSS_CIRCLE_AVATAR, String.valueOf(circleId),avatarTime,civ_perfectcircleInfo_avatar);
                        break;
                    case 2:
                        bgTime = String.valueOf(System.currentTimeMillis());
                        new OssUtils().setCircleImageToView(AppConstants.YY_PT_OSS_CIRCLE_BG, String.valueOf(circleId),bgTime,iv_perfectcircleInfo_bg);
                        break;
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
}
