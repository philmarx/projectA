package com.hzease.tomeet.login.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.hzease.tomeet.utils.OssUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.eventbus.EventBus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/22.
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
    CircleImageView civ_finishinfo_icon_fmt;
    @BindView(R.id.rb_finishinfo_male_fmt)
    RadioButton rb_finishinfo_male_fmt;
    @BindView(R.id.rb_finishinfo_female_fmt)
    RadioButton rb_finishinfo_female_fmt;
    Unbinder unbinder;


    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;
    private String birthday;
    private String avatarUrl = "";

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
                ((TakePhotoActivity) getActivity()).takePhotoPopupWindow(v.getId());
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
                if (!avatarUrl.isEmpty()) {
                    PTApplication.getRequestService().downloadPicFromNet(avatarUrl).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Logger.e("response:  " + response.isSuccessful() + "   Multimap: " + response.headers().toMultimap().toString() + "   message: " + response.message() + "   body().toString: " + response.body().toString());
                            if (response.isSuccessful() && "image/jpeg".equals(response.headers().get("Content-Type"))) {
                                try {
                                    new OssUtils().byteArrayUploadImage(AppConstants.YY_PT_OSS_AVATAR, response.body().bytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtils.getToast(mContext, "加载第三方头像失败，请手动上传头像");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

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
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_finishnfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        avatarUrl = bundle.getString("avatarUrl","");
        String nickName = bundle.getString("nickName","");
        boolean gender = bundle.getBoolean("gender");
        if (!avatarUrl.isEmpty()) {
            try {
                URL url = new URL(avatarUrl);
                Glide.with(mContext)
                        .load(url)
                        .into(civ_finishinfo_icon_fmt);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            et_finishinfo_name_fmt.setText(nickName);
            if (gender) {
                rb_finishinfo_male_fmt.setChecked(true);
            } else {
                rb_finishinfo_female_fmt.setChecked(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
