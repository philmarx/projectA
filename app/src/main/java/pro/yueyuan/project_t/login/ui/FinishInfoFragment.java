package pro.yueyuan.project_t.login.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.utils.ImageCropUtils;
import pro.yueyuan.project_t.utils.OssUtils;
import pro.yueyuan.project_t.utils.ToastUtils;
import pro.yueyuan.project_t.widget.CircleImageView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/22.
 */

public class FinishInfoFragment extends BaseFragment implements ILoginContract.View {
    @BindView(R.id.civ_finishinfo_icon_fmt)
    CircleImageView civ_finishinfo_icon_fmt;
    @BindView(R.id.et_finishinfo_name_fmt)
    EditText etFinishinfoNameFmt;
    @BindView(R.id.et_finishinfo_pwd_fmt)
    EditText et_finishinfo_pwd_fmt;
    @BindView(R.id.rb_finishinfo_male_fmt)
    RadioButton rbFinishinfoMaleFmt;
    @BindView(R.id.rb_finishinfo_female_fmt)
    RadioButton rbFinishinfoFemaleFmt;
    @BindView(R.id.rg_finishinfo_sex_fmt)
    RadioGroup rgFinishinfoSexFmt;
    @BindView(R.id.bt_finishinfo_success_fmt)
    Button btFinishinfoSuccessFmt;

    private PopupWindow popupWindow;

    //设置用户昵称
    private String mNickName;
    //设置用户密码
    private String mPwd;
    //设置用户性别
    private boolean isMale = true;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;

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
            //昵称
            R.id.et_finishinfo_name_fmt,
            //密码
            R.id.et_finishinfo_pwd_fmt,
            //完成
            R.id.bt_finishinfo_success_fmt
    })
    public void Onclick(View v) {
        //获取昵称
        mNickName = etFinishinfoNameFmt.getText().toString().trim();
        //获取密码
        mPwd = et_finishinfo_pwd_fmt.getText().toString().trim();
        //获取性别
        if (rbFinishinfoMaleFmt.isChecked()) {
            //性别为男
            isMale = true;
        } else if (rbFinishinfoFemaleFmt.isChecked()) {
            //性别为女
            isMale = false;
        }
        switch (v.getId()) {
            case R.id.civ_finishinfo_icon_fmt:
                initPopupWindow();
                break;
            /**
             * 注册成功
             */
            case R.id.bt_finishinfo_success_fmt:
                mPresenter.finishInfo(null, isMale, mNickName, mPwd, null, PTApplication.userToken, PTApplication.userId);
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


    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {
        // 跳转到转进来的页面
        getActivity().setResult(AppConstants.YY_PT_LOGIN_SUCCEED);
        getActivity().finish();
        Logger.d("初始化完成");
    }

    @Override
    public void getAuthLoginInfo(String id, String token) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_finishnfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }


    /**
     * 底部弹出popwind
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {

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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, AppConstants.REQUEST_CODE_GALLERY);
                popupWindow.dismiss();
            }
        });
        // 拍照选择头像
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
        if (ImageCropUtils.checkFileExists()) {
            startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.e("onRequestPermissionsResult:\n" + requestCode + "\n" + Arrays.toString(permissions) + "\n" + Arrays.toString(grantResults));

        switch(requestCode) {
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
        Logger.i("requestCode: " + requestCode + "   resultCode: " + resultCode + "  intent: " + intent);

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
                resultIntent = ImageCropUtils.cropImage(PTApplication.imageLocalCache);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                //设置图片框并上传
                setImageToHeadView();
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

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView() {
        File file = new File(PTApplication.imageLocalCache.getPath());
        Logger.i("file:  " + file + "\ngetPath:  " + PTApplication.imageLocalCache.getPath() + "\ntoString: " + PTApplication.imageLocalCache.toString());
        if (file.exists() && file.length() > 0) {
            new OssUtils().uploadAvatar(AppConstants.YY_PT_OSS_AVATAR, PTApplication.imageLocalCache.getPath());
            Logger.e(PTApplication.imageLocalCache.toString());
            Glide.with(this)
                    .load(PTApplication.imageLocalCache)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(civ_finishinfo_icon_fmt);
        } else {
            ToastUtils.getToast(mContext, "上传失败");
            Logger.e("上传失败");
        }
    }
}
