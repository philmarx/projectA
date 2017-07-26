package com.hzease.tomeet.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Key on 2017/7/26 16:37
 * email: MrKey.K@gmail.com
 * description:
 */

public class TakePhotoUtils {

    public static void takePhotoOnActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        Logger.e("requestCode: " + requestCode + "  resultCode: " + resultCode + "  length1: " + new File(PTApplication.imageLocalCache.getPath()).length());
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(PTApplication.getInstance(), "取消上传头像");
            return;
        }
        Intent resultIntent = null;
        switch (requestCode) {
            //如果是来自相册,直接裁剪图片
            case AppConstants.REQUEST_CODE_GALLERY:
                //activity.grantUriPermission(activity.getPackageName(), intent.getData(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //activity.grantUriPermission("com.android.camera", intent.getData(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                resultIntent = ImageCropUtils.cropImage(intent.getData());
                break;
            case AppConstants.REQUEST_CODE_CAMERA:
                resultIntent = ImageCropUtils.cropImage(PTApplication.imageLocalCache);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                //设置图片框并上传
                int tempPic = intent.getIntExtra("tempPic", 0);
                switch (tempPic) {
                    case R.id.iv_pic_head_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_AVATAR, (ImageView) activity.findViewById(tempPic));
                        break;
                    case R.id.iv_pic_two_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE1, (ImageView) activity.findViewById(tempPic));
                        break;
                    case R.id.iv_pic_three_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE2, (ImageView) activity.findViewById(tempPic));
                        break;
                    case R.id.iv_pic_four_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE3, (ImageView) activity.findViewById(tempPic));
                        break;
                    case R.id.iv_pic_five_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE4, (ImageView) activity.findViewById(tempPic));
                        break;
                    case R.id.iv_pic_six_aty:
                        new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_IMAGE5, (ImageView) activity.findViewById(tempPic));
                        break;
                }
                break;
        }
        if (requestCode == AppConstants.REQUEST_CODE_GALLERY || requestCode == AppConstants.REQUEST_CODE_CAMERA) {
            if (resultIntent != null) {
                // 只有Intent正确回来的时候才会进来,所有的判断都在创建Intent的时候做
                Logger.d(resultIntent);
                activity.startActivityForResult(resultIntent, AppConstants.REQUEST_CODE_CROP);
            } else {
                // 创建不了,大部分可能是因为没权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 只需要相机权限,不需要SD卡读写权限
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.REQUEST_SD_WRITE_PERMISSION);
                }
            }
        }
    }

    /**
     * 打开相册选照片
     * @param activity
     */
    public static void takeGallery(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
    }

    /**
     * 打开相机拍照
     * @param activity
     */
    public static void takeCamera(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(PTApplication.getInstance(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 只需要相机权限,不需要SD卡读写权限
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAKE_PHOTO_PERMISSION);
        } else {
            takeCameraForAvatar(activity);
        }
    }


    private static void takeCameraForAvatar(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
        if (ImageCropUtils.checkFileExists()) {
            activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA);
        }
    }
}
