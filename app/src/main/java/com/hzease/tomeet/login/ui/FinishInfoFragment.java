package com.hzease.tomeet.login.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/22.
 */

public class FinishInfoFragment extends BaseFragment implements ILoginContract.View {
    // 头像
    @BindView(R.id.civ_finishinfo_icon_fmt)
    CircleImageView civ_finishinfo_icon_fmt;
    // 昵称
    @BindView(R.id.et_finishinfo_name_fmt)
    EditText et_finishinfo_name_fmt;
    // 密码
    @BindView(R.id.et_finishinfo_pwd_fmt)
    EditText et_finishinfo_pwd_fmt;
    // 男女选择Group
    @BindView(R.id.rg_finishinfo_sex_fmt)
    RadioGroup rg_finishinfo_sex_fmt;
    // 完成按钮
    @BindView(R.id.bt_finishinfo_success_fmt)
    Button bt_finishinfo_success_fmt;
    //显示年龄
    @BindView(R.id.tv_finishinfo_age_fmt)
    TextView tv_finishinfo_age_fmt;
    //设置年龄
    @BindView(R.id.rl_finishinfo_setage_fmt)
    AutoRelativeLayout rl_finishinfo_setage_fmt;
    private PopupWindow popupWindow;
    private Uri uriForFileApiN;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;
    private String birthday;

    public FinishInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    public static FinishInfoFragment newInstance() {
        return new FinishInfoFragment();
    }

    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick({
            //头像
            R.id.civ_finishinfo_icon_fmt,
            //完成
            R.id.bt_finishinfo_success_fmt,
            //设置年龄
            R.id.rl_finishinfo_setage_fmt
    })
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.civ_finishinfo_icon_fmt:
                initPopupWindow();
                break;
            /**
             * 点击完成之前需要做一些检测
             */
            case R.id.bt_finishinfo_success_fmt:
                //获取昵称
                String nickName = et_finishinfo_name_fmt.getText().toString().trim();
                // TODO: 2017/4/21 检查昵称格式
                if (nickName.length() < 2 && nickName.length() > 10) {
                    ToastUtils.getToast(mContext, "昵称长度为2~10个字！");
                    return;
                }
                //获取密码
                String password = et_finishinfo_pwd_fmt.getText().toString().trim();
                // TODO: 2017/4/24 检查密码格式是否包含空格
                if (password.length() < 5 && password.length() > 16) {
                    ToastUtils.getToast(mContext, "密码长度为6~16位！");
                    return;
                }
                // 性别 男true 女false
                boolean sex = rg_finishinfo_sex_fmt.getCheckedRadioButtonId() == R.id.rb_finishinfo_male_fmt;
                mPresenter.finishInfo(String.valueOf(birthday), sex, nickName, password);
                break;
            case R.id.rl_finishinfo_setage_fmt:
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(mContext, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        birthday = dateDesc;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long age = 0;
                        try {
                            long birthdayLong = sdf.parse(dateDesc).getTime();
                            long now = System.currentTimeMillis();
                            age = (now - birthdayLong) / 365 / 24 / 60 / 60 / 1000;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tv_finishinfo_age_fmt.setText(String.valueOf(age));
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(22) // pick view text size
                        .colorCancel(Color.parseColor("#b8b8b8")) //color of cancel button
                        .colorConfirm(Color.parseColor("#03b5e3"))//color of confirm button
                        .minYear(1950) //min year in loop
                        .maxYear(2010) // max year in loop
                        //.showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                        .dateChose("2000-6-15") // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(getActivity());
                break;
        }
    }

    @Override
    public void smsCodeCountdown(StringDataBean stringDataBean) {
    }

    @Override
    public void loginSuccess() {
    }

    @Override
    public void loginFailed(String info) {
    }

    @Override
    public void finishInfo() {
    }

    @Override
    public void registerSuccess() {
    }

    @Override
    public void getAuthLoginInfo() {
    }

    /**
     * 检查初始化返回值，比如昵称重复之类的
     * 完善信息界面专用
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void checkInitResult(boolean isSuccess, String msg) {
        if (isSuccess) {
            if (AppConstants.YY_PT_NAVIGATION_SPLASH_REQUEST_CODE == getActivity().getIntent().getFlags()) {
                startActivity(new Intent(mContext, HomeActivity.class));
            } else {
                EventBus.getDefault().post(new UserInfoBean());
            }
            getActivity().finish();
        } else {
            ToastUtils.getToast(mContext, msg);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_finishnfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Logger.v("getFlags: " + getActivity().getIntent().getFlags());
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
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.pop, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.activity_login, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
                Logger.i("权限：" + ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
            uriForFileApiN = FileProvider.getUriForFile(mContext, "com.hzease.tomeet.FileProvider", new File(PTApplication.imageLocalCachePath, "/imageTemp"));
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
                    ToastUtils.getToast(mContext, "相机权限被禁止,无法打开照相机");
                }
                break;
            // 请求SD卡写入权限,一般不可能会弹出来,以防万一
            case AppConstants.REQUEST_SD_WRITE_PERMISSION:
                if (grantResults[0] == 0) {
                    Logger.i("SD权限申请成功");
                } else {
                    ToastUtils.getToast(mContext, "没有读写SD卡的权限");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(getContext(), "取消上传头像");
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
                new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_AVATAR, civ_finishinfo_icon_fmt);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
