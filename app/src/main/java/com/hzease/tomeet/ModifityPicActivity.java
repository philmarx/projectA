package com.hzease.tomeet;

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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by xuq on 2017/4/26.
 */

public class ModifityPicActivity extends NetActivity {
    @BindView(R.id.iv_pic_head_aty)
    ImageView iv_pic_head_aty;
    @BindView(R.id.iv_pic_two_aty)
    ImageView iv_pic_two_aty;
    @BindView(R.id.iv_pic_three_aty)
    ImageView iv_pic_three_aty;
    @BindView(R.id.iv_pic_four_aty)
    ImageView iv_pic_four_aty;
    @BindView(R.id.iv_pic_five_aty)
    ImageView iv_pic_five_aty;
    @BindView(R.id.iv_pic_six_aty)
    ImageView iv_pic_six_aty;
    @BindView(R.id.tv_modifitypic_name_aty)
    TextView tv_modifitypic_name_aty;
    private PopupWindow popupWindow;

    String mImage1;
    String mImage2;
    String mImage3;
    String mImage4;
    String mImage5;
    String userId;
    private int tempPic;
    private String nickName;
    private Uri uriForFileApiN;

    @OnClick({
            R.id.iv_pic_head_aty,
            R.id.iv_pic_two_aty,
            R.id.iv_pic_three_aty,
            R.id.iv_pic_four_aty,
            R.id.iv_pic_five_aty,
            R.id.iv_pic_six_aty
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_pic_head_aty:
                tempPic=001;
                initPopupWindow();
                break;
            case R.id.iv_pic_two_aty:
                tempPic=002;
                initPopupWindow();
                break;
            case R.id.iv_pic_three_aty:
                tempPic=003;
                initPopupWindow();
                break;
            case R.id.iv_pic_four_aty:
                tempPic=004;
                initPopupWindow();
                break;
            case R.id.iv_pic_five_aty:
                tempPic=005;
                initPopupWindow();
                break;
            case R.id.iv_pic_six_aty:
                tempPic=006;
                initPopupWindow();
                break;
        }
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
    private void initPopupWindow() {
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

        Button gallery = (Button) popupWindowView.findViewById(R.id.local);
        Button camera = (Button) popupWindowView.findViewById(R.id.tokenphoto);
        Button close = (Button) popupWindowView.findViewById(R.id.close);
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
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
                popupWindow.dismiss();
            }
        });
        // 拍照选择头像
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("权限："+ ContextCompat.checkSelfPermission(PTApplication.getInstance(), android.Manifest.permission.CAMERA));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(PTApplication.getInstance(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 只需要相机权限,不需要SD卡读写权限
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, AppConstants.REQUEST_TAKE_PHOTO_PERMISSION);
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
            uriForFileApiN = FileProvider.getUriForFile(this, "com.hzease.tomeet.FileProvider", new File(PTApplication.imageLocalCachePath, "/imageTemp"));
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
                    ToastUtils.getToast(PTApplication.getInstance(), "相机权限被禁止,无法打开照相机");
                }
                break;
            // 请求SD卡写入权限,一般不可能会弹出来,以防万一
            case AppConstants.REQUEST_SD_WRITE_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("SD权限申请成功");
                } else {
                    ToastUtils.getToast(PTApplication.getInstance(), "没有读写SD卡的权限");
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(PTApplication.getInstance(), "取消上传头像");
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
                //设置图片框并上传
                switch(tempPic){
                    case 001:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_AVATAR, iv_pic_head_aty);
                        break;
                    case 002:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE1, iv_pic_two_aty);
                        break;
                    case 003:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE2, iv_pic_three_aty);
                        break;
                    case 004:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE3, iv_pic_four_aty);
                        break;
                    case 005:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE4, iv_pic_five_aty);
                        break;
                    case 006:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE5, iv_pic_six_aty);
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

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_moditifypic;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        mImage1 = bundle.getString("image1Signature","0");
        mImage2 = bundle.getString("image2Signature","0");
        mImage3 = bundle.getString("image3Signature","0");
        mImage4 = bundle.getString("image4Signature","0");
        mImage5 = bundle.getString("image5Signature","0");
        nickName = bundle.getString("nickname");
        Logger.e("bundle: " + bundle.toString());
        tv_modifitypic_name_aty.setText(nickName);
        Glide.with(PTApplication.getInstance())
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR)
                .thumbnail(0.1f)
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(iv_pic_head_aty);
        if (!mImage1.equals("0")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE1)
                    .signature(new StringSignature(mImage1))
                    .into(iv_pic_two_aty);
        }
        if (!mImage2.equals("0")){
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE2)
                    .signature(new StringSignature(mImage2))
                    .into(iv_pic_three_aty);
        }
        if (!mImage3.equals("0")){
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE3)
                    .signature(new StringSignature(mImage3))
                    .into(iv_pic_four_aty);
        }
        if(!mImage4.equals("0")){
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE4)
                    .signature(new StringSignature(mImage4))
                    .into(iv_pic_five_aty);
        }
        if (!mImage5.equals("0")) {
            Glide.with(PTApplication.getInstance())
                    .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_IMAGE5)
                    .signature(new StringSignature(mImage5))
                    .into(iv_pic_six_aty);
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
