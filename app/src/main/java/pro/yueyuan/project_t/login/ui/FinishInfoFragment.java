package pro.yueyuan.project_t.login.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.utils.RongCloudInitUtils;
import pro.yueyuan.project_t.utils.ToastUtils;
import pro.yueyuan.project_t.widget.CircleImageView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/22.
 */

public class FinishInfoFragment extends BaseFragment implements ILoginContract.View {
    @BindView(R.id.civ_finishinfo_icon_fmt)
    CircleImageView civFinishinfoIconFmt;
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
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 600;
    private static int output_Y = 600;
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
    public void Onclick(View v){
        //获取昵称
        mNickName = etFinishinfoNameFmt.getText().toString().trim();
        //获取密码
        mPwd = et_finishinfo_pwd_fmt.getText().toString().trim();
        //获取性别
        if (rbFinishinfoMaleFmt.isChecked()){
            //性别为男
            isMale = true;
        }else if (rbFinishinfoFemaleFmt.isChecked()){
            //性别为女
            isMale = false;
        }
        switch (v.getId()){
            case R.id.civ_finishinfo_icon_fmt:
                initPopupWindow();
                break;
            /**
             * 注册成功
             */
            case R.id.bt_finishinfo_success_fmt:
                mPresenter.finishInfo(null,isMale,mNickName,mPwd,null,PTApplication.userToken,PTApplication.userId);
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
        Logger.d("注册成功");
        // 融云初始化
        new RongCloudInitUtils().RongCloudInit();
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
    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }
    protected void initPopupWindow(){
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.pop, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
        //popupWindow.setHeight(LayoutParams.FILL_PARENT);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.activity_login, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明
        backgroundAlpha(0.5f);
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

        Button local = (Button)popupWindowView.findViewById(R.id.local);
        Button takephoto = (Button)popupWindowView.findViewById(R.id.tokenphoto);
        Button close = (Button)popupWindowView.findViewById(R.id.close);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getToast(PTApplication.getInstance(),"本地");
                choseHeadImageFromGallery();
                popupWindow.dismiss();
            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getToast(PTApplication.getInstance(),"打开相机");
                choseHeadImageFromCameraCapture();
                popupWindow.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToastUtils.getToast(PTApplication.getInstance(),"关闭");
                popupWindow.dismiss();
            }
        });
    }
        //通过摄像头选择头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    //从相册选择头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");//选择图片
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intentFromGallery,CODE_GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == getActivity().RESULT_CANCELED) {//取消
//            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            ToastUtils.getToast(getContext(), "取消");
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST://如果是来自本地的
                cropRawPhoto(intent.getData());//直接裁剪图片
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    ToastUtils.getToast(getContext(), "没有SDCard!");
                }
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);//设置图片框
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //把裁剪的数据填入里面
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    /**
     * 菜单弹出方向
     *
     */
    public enum Location {
        BOTTOM;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
// 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            civFinishinfoIconFmt.setImageBitmap(photo);
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory()+"/Ask");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory()+"/Ask", "okkk.jpg");
            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
