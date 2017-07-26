package com.hzease.tomeet.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by Key on 2017/4/19 15:44
 * email: MrKey.K@gmail.com
 * description:
 */

public class ImageCropUtils {
    /**
     * 创建一个剪切图片拿返回值的Intent,照片不设扩展名,不会出现在图库,自动检查文件夹,如果不存在自动创建
     * 拍照上传,剪切后覆盖拍照的照片
     * @param localFileUri 本地文件的Uri路径,安卓包下的Uri
     * @return 剪切并储存的Intent
     */
    public static Intent cropImage(Uri localFileUri) {
        Logger.e(localFileUri.toString());
        // 先检查SD卡和文件权限
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        if (!checkFileExists()) {
            return null;
        }
        intent.setDataAndType(localFileUri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", AppConstants.CROP_OUTPUT_XY);
        intent.putExtra("outputY", AppConstants.CROP_OUTPUT_XY);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(PTApplication.imageLocalCacheRealPath));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // 人脸识别
        Logger.e(intent.toString());
        return intent;
    }

    /**
     * 检查能否保存
     * @return false:不能存 true:可以保存
     */
    public static boolean checkFileExists() {
        // 检查SD卡是否可用
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Logger.e("SD卡不存在:  " + Environment.getExternalStorageState());
            ToastUtils.getToast(PTApplication.getInstance(), "SD卡不存在");
            return false;
        }
        // 检查 '/SD卡/ease/' 文件夹是否存在
        if (!PTApplication.imageLocalCachePath.exists()) {
            if (!PTApplication.imageLocalCachePath.mkdirs()) {
                Logger.e("文件夹创建失败");
                ToastUtils.getToast(PTApplication.getInstance(), "请检查储存权限");
                return false;
            }
        }
        // 检查 '/SD卡/ease/imageTemp' 如果不是'文件',则删除, 其实可以直接判断是否存在,存在直接删除
        File file = new File(PTApplication.imageLocalCache.getPath());
        if (file.isDirectory()) {
            if (!file.delete()) {
                Logger.e("非文件删除失败");
                ToastUtils.getToast(PTApplication.getInstance(), "请检查储存权限");
                return false;
            }
        }
        return true;
    }
}
