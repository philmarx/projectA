package pro.yueyuan.project_t.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.orhanobut.logger.Logger;

import java.io.File;

import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.PTApplication;

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
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Logger.e("SD卡不存在:  " + Environment.getExternalStorageState());
            ToastUtils.getToast(PTApplication.getInstance(), "SD卡不存在");
            return null;
        }
        File file = new File(PTApplication.imageLocalCache.getPath());
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Logger.e("文件夹创建失败");
                ToastUtils.getToast(PTApplication.getInstance(), "请检查储存权限");
                return null;
            }
        }
        if (!file.isFile()) {
            if (!file.delete()) {
                Logger.e("非文件删除失败");
                ToastUtils.getToast(PTApplication.getInstance(), "请检查储存权限");
                return null;
            }
        }
        Logger.d(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // 人脸识别
        return intent;
    }
}
