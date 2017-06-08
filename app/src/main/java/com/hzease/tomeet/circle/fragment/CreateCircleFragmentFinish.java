package com.hzease.tomeet.circle.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.circle.ICircleContract;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.login.ui.FinishInfoFragment;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ImageCropUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/25.
 */

public class CreateCircleFragmentFinish extends BaseFragment implements ICircleContract.View {

    private static final int RESULT_PLACE = 10086;
    //创建fragment事务管理器对象
    FragmentTransaction transaction;
    CircleActivity mCircleActivity;
    @BindView(R.id.et_createcircle_circlename_fmt)
    EditText et_createcircle_circlename_fmt;
    @BindView(R.id.ll_createcircle_location_fmt)
    AutoLinearLayout ll_createcircle_location_fmt;
    @BindView(R.id.bt_createcircle_finish_fmt)
    Button bt_createcircle_finish_fmt;
    @BindView(R.id.tv_createcircle_location_fmt)
    TextView tv_createcircle_location_fmt;
    @BindView(R.id.civ_createcircle_circleavatar_fmt)
    CircleImageView civ_createcircle_circleavatar_fmt;
    private ICircleContract.Presenter mPresenter;
    private PopupWindow popupWindow;
    private double mLongitude;
    private double mLatitude;
    private String cityCode;
    private String cityName;

    private String mPlaceName;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    AutoRelativeLayout rl_circle_head;

    @OnClick({
            R.id.bt_createcircle_finish_fmt,
            R.id.ll_createcircle_location_fmt,
            R.id.all_createcircle_circleIcon_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_createcircle_location_fmt:
                Intent openSend = new Intent(getActivity(), ShareLocationActivity.class);
                Logger.e(mLongitude + "");
                Logger.e(mLatitude + "");
                Logger.e(cityCode + "");
                Logger.e(cityName + "");
                Logger.e(PTApplication.userToken + "token");
                openSend.putExtra("lon", mLongitude);
                openSend.putExtra("lat", mLatitude);
                openSend.putExtra("cityCode", cityCode);
                openSend.putExtra("cityName", cityName);
                startActivityForResult(openSend, RESULT_PLACE);
                break;
            case R.id.bt_createcircle_finish_fmt:
                String circleName = et_createcircle_circlename_fmt.getText().toString().trim();
                //创建圈子
                mPresenter.createCircle("","",cityName,mLatitude,mLongitude,circleName,"",mPlaceName,PTApplication.userToken,PTApplication.userId);
                break;
            case R.id.all_createcircle_circleIcon_fmt:
                initPopupWindow();
                break;
        }
    }

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static CreateCircleFragmentFinish newInstance() {
        return new CreateCircleFragmentFinish();
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
                Logger.i("权限："+ ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA));
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

    public void takePhotoForAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PTApplication.imageLocalCache);
        if (ImageCropUtils.checkFileExists()) {
            startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA);
        }
    }
    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_createcircle_next;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mCircleActivity = (CircleActivity) getActivity();
        transaction = mCircleActivity.getSupportFragmentManager().beginTransaction();
        rl_circle_head = (AutoRelativeLayout) mCircleActivity.findViewById(R.id.circle_head);
        rl_circle_head.setVisibility(View.GONE);
        bottomNavigationView = (BottomNavigationView) mCircleActivity.findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        initLogLat();

    }

    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                cityCode = aMapLocation.getCityCode();
                cityName = aMapLocation.getCity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == Activity.RESULT_CANCELED) {//取消
            ToastUtils.getToast(getContext(), "取消上传头像");
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
                new OssUtils().setImageToHeadView(AppConstants.YY_PT_OSS_PATH, civ_createcircle_circleavatar_fmt);
                break;
        }
        if (requestCode == RESULT_PLACE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
//                String cityName = data.getStringExtra(ShareLocationActivity.CITY_NAME);
                mPlaceName = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                tv_createcircle_location_fmt.setText(mPlaceName);
            }
        }else if (requestCode == AppConstants.REQUEST_CODE_GALLERY || requestCode == AppConstants.REQUEST_CODE_CAMERA) {
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
     * 创建圈子成功
     */
    @Override
    public void createSuccess() {
        transaction.replace(R.id.fl_content_bidding_activity, mCircleActivity.mFragmentList.get(2));
        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
        transaction.addToBackStack(null);
        transaction.commit();
        //ActivityUtils.addFragmentToActivity(mCircleActivity.getSupportFragmentManager(), mCircleActivity.mFragmentList.get(2), R.id.fl_content_bidding_activity);
    }

    /**
     * 展示喊话内容
     *  @param isSuccess
     * @param commentList
     */
    @Override
    public void showDeclaration(boolean isSuccess, List<CommentItemBean.DataBean> commentList, boolean isLoadMore) {

    }



    /**
     * 完成喊话后的展示
     *
     * @param isSuccess
     */
    @Override
    public void showDeclareSucccess(boolean isSuccess,String msg) {

    }

    @Override
    public void refreshOneDeclaration(CommentItemBean.DataBean dataBean) {

    }

    /**
     * 显示推荐圈子
     *
     * @param data
     */
    @Override
    public void showRecommandCircle(List<CircleInfoBean.DataBean> data) {

    }

    /**
     * 显示附近圈子
     *
     * @param data
     */
    @Override
    public void showNeayByCircle(List<CircleInfoBean.DataBean> data) {

    }

    @Override
    public void showCircleInfo(EnterCircleInfoBean.DataBean data) {

    }

    @Override
    public void joinCircleSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 退出圈子成功
     *
     * @param msg
     */
    @Override
    public void signOutCircleSuccess(String msg) {

    }

    /**
     * 修改圈子公告成功
     *
     * @param msg
     */
    @Override
    public void modifitySuccess(String msg) {

    }

    /**
     * 显示圈内房间
     *
     * @param data
     */
    @Override
    public void showRoomsByCircle(List<HomeRoomsBean.DataBean> data) {

    }

    @Override
    public void showMyCircle(List<CircleInfoBean.DataBean> data) {

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
