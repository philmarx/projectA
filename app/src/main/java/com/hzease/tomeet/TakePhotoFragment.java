package com.hzease.tomeet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Key on 2017/7/27 18:33
 * email: MrKey.K@gmail.com
 * description:
 */


public abstract class TakePhotoFragment extends BaseFragment {

    protected int imageViewCheckedId;

    protected SparseArray<String> imageName;

    {
        // TODO 添加 Fragment 中对应ViewID的图片名字
        imageName = new SparseArray<>();
        imageName.put(R.id.civ_finishinfo_icon_fmt, AppConstants.YY_PT_OSS_AVATAR);

        long millis = SystemClock.currentThreadTimeMillis();
        imageName.put(R.id.iv_feedback_photo_one, AppConstants.YY_PT_OSS_FEEDBACK + (millis));
        imageName.put(R.id.iv_feedback_photo_two, AppConstants.YY_PT_OSS_FEEDBACK + (millis + 1));
        imageName.put(R.id.iv_feedback_photo_three, AppConstants.YY_PT_OSS_FEEDBACK + (millis + 2));
    }

    protected void takePhotoPopupWindow(int imageViewCheckedId) {
        this.imageViewCheckedId = imageViewCheckedId;
        View popupWindowView = View.inflate(mContext, R.layout.pop, null);
        //内容，高度，宽度
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        /*ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);*/
        //显示位置
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                takeGallery();
                popupWindow.dismiss();
            }
        });
        // 拍照选择头像
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(PTApplication.getInstance(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 只需要相机权限,不需要SD卡读写权限
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAKE_PHOTO_PERMISSION);
                } else {
                    takeCameraForAvatar();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.e("onRequestPermissionsResult:\n" + requestCode + "\n" + Arrays.toString(permissions) + "\n" + Arrays.toString(grantResults));

        if (requestCode == AppConstants.REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults[0] == 0) {
                Logger.i("相机权限申请成功");
                takeCameraForAvatar();
            } else {
                new AlertDialog.Builder(mContext)
                        .setMessage("我们需要相机权限才能拍照哦~\n\n设置路径：\n设置->应用->后会有期->权限")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Logger.e("intent: " + intent);
        Logger.e("requestCode: " + requestCode + "  resultCode: " + resultCode + "  length1: " + new File(PTApplication.imageLocalCache.getPath()).length());

        if (resultCode == Activity.RESULT_OK) {
            // 加工照片
            switch (requestCode) {
                //如果是来自相册,直接裁剪图片
                case AppConstants.REQUEST_CODE_GALLERY:
                    startActivityForResult(ImageCropUtils.cropImage(intent.getData()), AppConstants.REQUEST_CODE_CROP);
                    break;
                // 拍照后存在本地，返回URI
                case AppConstants.REQUEST_CODE_CAMERA:
                    startActivityForResult(ImageCropUtils.cropImage(PTApplication.imageLocalCache), AppConstants.REQUEST_CODE_CROP);
                    break;
                case AppConstants.REQUEST_CODE_CROP:
                    //设置图片框并上传
                    new OssUtils().setImageToHeadView(imageName.get(imageViewCheckedId), (ImageView) getActivity().findViewById(imageViewCheckedId));
                    break;
            }
        } else {
            ToastUtils.getToast(PTApplication.getInstance(), "取消上传头像");
        }
    }

    /**
     * 打开相册选照片
     */
    public void takeGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
    }

    /**
     * 打开相机拍照
     */
    private void takeCameraForAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);

        if (ImageCropUtils.checkFileExists()) {
            startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA);
        }
    }
}
