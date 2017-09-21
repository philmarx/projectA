package com.hzease.tomeet.login.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bumptech.glide.Glide;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.TakePhotoActivity;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.KeyboardUtils;
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.waterWaveProgress.WaterWaveProgress;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/22.
 * 2017年8月18日 19:37:39
 */

public class FinishInfoFragment extends BaseFragment implements ILoginContract.View {
    // 昵称
    @BindView(R.id.et_finishinfo_name_fmt)
    EditText et_finishinfo_name_fmt;
    // 密码
    @BindView(R.id.et_finishinfo_pwd_fmt)
    EditText et_finishinfo_pwd_fmt;
    // 男女选择Group
    @BindView(R.id.rg_finishinfo_sex_fmt)
    RadioGroup rg_finishinfo_sex_fmt;
    //显示年龄
    @BindView(R.id.tv_finishinfo_age_fmt)
    TextView tv_finishinfo_age_fmt;
    //头像
    @BindView(R.id.civ_finishinfo_icon_fmt)
    ImageView civ_finishinfo_icon_fmt;
    //选填人ID
    @BindView(R.id.et_finishinfo_recommender_id_fmt)
    EditText et_finishinfo_recommender_id_fmt;
    @BindView(R.id.rb_finishinfo_male_fmt)
    RadioButton rb_finishinfo_male_fmt;
    @BindView(R.id.rb_finishinfo_female_fmt)
    RadioButton rb_finishinfo_female_fmt;

    // 水波
    @BindView(R.id.wwp_finishinfo_icon_fmt)
    WaterWaveProgress wwp_finishinfo_icon_fmt;


    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;
    private String birthday;
    private OSSProgressCallback<PutObjectRequest> progressCallback;
    private int percent;

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
            R.id.ll_tosetage_fmt
    })
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.civ_finishinfo_icon_fmt:
                KeyboardUtils.hideKeyboard(getActivity());
                ((TakePhotoActivity) getActivity()).takePhotoPopupWindow(v.getId());
                break;
            /**
             * 点击完成之前需要做一些检测
             */
            case R.id.bt_finishinfo_success_fmt:
                String recommenderAccount = et_finishinfo_recommender_id_fmt.getText().toString().trim();
                //获取昵称
                String nickName = et_finishinfo_name_fmt.getText().toString().trim();
                //检查昵称格式
                if (nickName.length() < 2 && nickName.length() > 10 || nickName.isEmpty()) {
                    ToastUtils.getToast("昵称长度为2~10个字！");
                    break;
                }
                //获取密码
                String password = et_finishinfo_pwd_fmt.getText().toString().trim();
                //检查密码格式是否包含空格
                if (password.length() < 5 && password.length() > 16 || password.isEmpty()) {
                    ToastUtils.getToast("密码长度为6~16位！");
                    break;
                }
                //Logger.e(String.valueOf(birthday) + "" + (String.valueOf(birthday) == null));
                if (TextUtils.isEmpty(birthday)) {
                    ToastUtils.getToast("请选择年龄");
                    break;
                }
                // 检查头像
                if (percent == 0) {
                    ToastUtils.getToast("请上传头像");
                    break;
                } else if (percent != 100) {
                    ToastUtils.getToast("请等待头像上传");
                    break;
                }
                // 性别 男true 女false
                boolean sex = rg_finishinfo_sex_fmt.getCheckedRadioButtonId() == R.id.rb_finishinfo_male_fmt;
                mPresenter.finishInfo(birthday, sex, nickName, password, recommenderAccount);
                break;
            case R.id.ll_tosetage_fmt:
                // 收起键盘
                KeyboardUtils.hideKeyboard(getActivity());
                //InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);

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
    public void loginSuccess(String loginType) {
    }

    @Override
    public void loginFailed(String info) {
    }

    @Override
    public void finishInfo(String loginType) {
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
            mPresenter.getMyInfo(PTApplication.userId, PTApplication.userToken);
            if ("splash".equals(getActivity().getIntent().getStringExtra("from"))) {
                startActivity(new Intent(mContext, HomeActivity.class).putExtra("from","finish"));
            } else {
                EventBus.getDefault().post(new UserInfoBean());
            }
            //getActivity().finish();
        } else {
            ToastUtils.getToast(msg);
        }
    }

    @Override
    public void showLoadingDialog() {
        ((LoginActivity) getActivity()).showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        ((LoginActivity) getActivity()).hideLoadingDialog();
    }

    @Override
    public void SmsCodeResult(StringDataBean data) {

    }

    @Override
    public void toBindAccout() {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_finishinfo;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Logger.e("transit: " + transit + " enter: " + enter + " nextAnim: " + nextAnim);
        if (enter) {
            // 清空数据
            percent = 0;
            et_finishinfo_name_fmt.setText("");
            et_finishinfo_pwd_fmt.setText("");
            tv_finishinfo_age_fmt.setText("");
            et_finishinfo_recommender_id_fmt.setText("");
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        wwp_finishinfo_icon_fmt.setWaterAlpha(0.6f);
        wwp_finishinfo_icon_fmt.animateWave();
        progressCallback = new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                    return;
                }
                percent = (int) (currentSize * 1.0 / totalSize * 100);
                Logger.e("Request: " + putObjectRequest);
                Logger.i(percent + "%  currentSize: " + currentSize + "  totalSize: " + totalSize);
                if (percent == 0 || percent == 100) {
                    if (wwp_finishinfo_icon_fmt.getVisibility() == View.VISIBLE) {
                        wwp_finishinfo_icon_fmt.post(new Runnable() {
                            @Override
                            public void run() {
                                wwp_finishinfo_icon_fmt.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    wwp_finishinfo_icon_fmt.post(new Runnable() {
                        @Override
                        public void run() {
                            if (wwp_finishinfo_icon_fmt.getVisibility() == View.GONE) {
                                wwp_finishinfo_icon_fmt.setVisibility(View.VISIBLE);
                            }
                            wwp_finishinfo_icon_fmt.setProgress(percent);
                        }
                    });
                }
            }
        };
        ((TakePhotoActivity) getActivity()).setProgressCallback(progressCallback);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String avatarUrl = bundle.getString("avatarUrl", "");
            String nickName = bundle.getString("nickName", "");
            boolean gender = bundle.getBoolean("gender");
            if (!avatarUrl.isEmpty()) {
                Glide.with(mContext)
                        .load(avatarUrl)
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(civ_finishinfo_icon_fmt);
                et_finishinfo_name_fmt.setText(nickName);
                if (gender) {
                    rb_finishinfo_male_fmt.setChecked(true);
                } else {
                    rb_finishinfo_female_fmt.setChecked(true);
                }

                // 上传头像
                PTApplication.getRequestService().downloadPicFromNet(avatarUrl).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Logger.e("response:  " + response.isSuccessful() + "   Multimap: " + response.headers().toMultimap().toString() + "   message: " + response.message() + "   body().toString: " + response.body().toString());
                        if (response.isSuccessful() && "image/jpeg".equals(response.headers().get("Content-Type"))) {
                            try {
                                OssUtils ossUtils = new OssUtils();
                                ossUtils.setProgressCallback(progressCallback);
                                ossUtils.byteArrayUploadImage(AppConstants.YY_PT_OSS_AVATAR, response.body().bytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.getToast("上传第三方头像失败，请手动选择头像");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ToastUtils.getToast("上传第三方头像失败，请手动选择头像");
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((TakePhotoActivity) getActivity()).onActivityResult(requestCode, resultCode, data);
    }
}
