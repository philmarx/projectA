package com.hzease.tomeet.login.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.login.ILoginContract;
import com.hzease.tomeet.utils.MatchUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/8/28.
 */

public class LoginFragmentV3 extends BaseFragment implements ILoginContract.View {
    //手机号
    @BindView(R.id.et_phone_number_login_fmt)
    EditText et_phone_number_login_fmt;
    //密码
    @BindView(R.id.et_password_login_fmt)
    EditText et_password_login_fmt;
    //切换密码验证码登录
    @BindView(R.id.tv_login4sms_login_fmt)
    TextView tv_login4sms_login_fmt;
    //下一步或者登陆
    @BindView(R.id.bt_login_next_fmt)
    Button bt_login_next_fmt;
    //注册
    @BindView(R.id.tv_regesist_account_fmt)
    TextView tv_regesist_account_fmt;
    @BindView(R.id.tv_line)
    TextView tv_line;
    //三方登录的昵称
    private String mNickName;
    //三方登录的性别
    private boolean mGender;
    // 三方登录头像地址
    private String mAvatarUrl;
    private ILoginContract.Presenter mPresenter;
    private FragmentTransaction transaction;
    private LoginActivity loginActivity;
    private String phoneNumber;
    private String type;


    public static LoginFragmentV3 newInstance() {
        return new LoginFragmentV3();
    }

    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @OnClick({
            R.id.ll_login4wechat_fmt,
            R.id.ll_login4qq_fmt,
            R.id.bt_login_next_fmt,
            R.id.tv_login4sms_login_fmt,
            R.id.tv_regesist_account_fmt,
            R.id.tv_forget_pwd_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_login4wechat_fmt:
                // 转圈
                showLoadingDialog();
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart：" + share_media.name());
                        //changeLoadView(true);
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Logger.i("onComplete:WX   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                        //changeLoadView(false);
                        mPresenter.authLogin(AppConstants.AUTHORIZED_LOGIN_WX, map.get("uid"));
                        mAvatarUrl = map.get("iconurl");
                        mNickName = map.get("name");
                        mGender = "男".equals(map.get("gender"));
                        type = "WECHAT";
                        Logger.e("nickName" + mNickName + "\navatar" + mAvatarUrl + "\nuserId" + PTApplication.userId + "\ntoken" + PTApplication.userToken + "\ntype" + type);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Logger.e("onError: " + throwable.getMessage());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel: " + share_media.toString());
                        hideLoadingDialog();
                    }
                });
                break;
            case R.id.ll_login4qq_fmt:
                // 转圈
                showLoadingDialog();
                //qq登录
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart: " + share_media.toString());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Logger.i("onComplete:QQ   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                        mPresenter.authLogin(AppConstants.AUTHORIZED_LOGIN_QQ, map.get("uid"));
                        mAvatarUrl = map.get("iconurl");
                        mNickName = map.get("name");
                        mGender = "男".equals(map.get("gender"));
                        type = "QQ";
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Logger.e("onError");
                        hideLoadingDialog();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel");
                        hideLoadingDialog();
                    }
                });
                break;
            case R.id.bt_login_next_fmt:
                phoneNumber = et_phone_number_login_fmt.getText().toString().trim();
                if ("验证码登录".equals(tv_login4sms_login_fmt.getText().toString())) {
                    //手机号密码登录
                    String password = et_password_login_fmt.getText().toString().trim();
                    if (MatchUtils.isPhoneNumber(phoneNumber)) {
                        if (password.length() > 5) {
                            mPresenter.phonePasswordSignIn(phoneNumber, password);
                        } else {
                            ToastUtils.getToast("密码不会小于6位哦");
                        }
                    }

                } else {
                    //手机验证码登录
                    if (MatchUtils.isPhoneNumber(phoneNumber)) {
                        mPresenter.getSmsCode(phoneNumber);
                    }
                }
                break;
            case R.id.tv_login4sms_login_fmt:
                if ("验证码登录".equals(tv_login4sms_login_fmt.getText().toString())) {
                    tv_login4sms_login_fmt.setText("使用密码登录");
                    et_password_login_fmt.setVisibility(View.GONE);
                    tv_line.setVisibility(View.GONE);
                    bt_login_next_fmt.setText("下一步");
                    if (et_phone_number_login_fmt.getText().toString().trim().length() == 11) {
                        bt_login_next_fmt.setEnabled(true);
                    }
                } else {
                    tv_login4sms_login_fmt.setText("验证码登录");
                    et_password_login_fmt.setVisibility(View.VISIBLE);
                    tv_line.setVisibility(View.VISIBLE);
                    bt_login_next_fmt.setText("登录");
                    bt_login_next_fmt.setEnabled(false);
                }
                if ("验证码登录".equals(tv_login4sms_login_fmt.getText().toString())) {
                    et_phone_number_login_fmt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (et_phone_number_login_fmt.getText().toString().trim().length() == 11 && et_password_login_fmt.getText().toString().trim().length() >= 1) {
                                bt_login_next_fmt.setEnabled(true);
                            } else {
                                bt_login_next_fmt.setEnabled(false);
                            }
                        }
                    });
                    et_password_login_fmt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (et_phone_number_login_fmt.getText().toString().trim().length() == 11 && et_password_login_fmt.getText().toString().trim().length() >= 1) {
                                bt_login_next_fmt.setEnabled(true);
                            } else {
                                bt_login_next_fmt.setEnabled(false);
                            }
                        }
                    });
                } else {
                    et_phone_number_login_fmt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (et_phone_number_login_fmt.getText().toString().trim().length() == 11) {
                                bt_login_next_fmt.setEnabled(true);
                            } else {
                                bt_login_next_fmt.setEnabled(false);
                            }
                        }
                    });
                }
                break;
            //跳转到注册页面
            case R.id.tv_regesist_account_fmt:
                transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(3));
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            //找回密码
            case R.id.tv_forget_pwd_fmt:
                transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(1));
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void loginSuccess(String loginType) {
        Logger.d("登录成功");
        // 跳转到转进来的页面
        EventBus.getDefault().post(new UserInfoBean());
        getActivity().setResult(AppConstants.YY_PT_LOGIN_SUCCEED);
        //getActivity().finish();
        if (!loginType.equals(AppConstants.LOGIN_PHONE)) {
            PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, loginType, PTApplication.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NoDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError" + e.getMessage());
                        }

                        @Override
                        public void onNext(NoDataBean noDataBean) {
                            if (noDataBean.isSuccess()) {
                                Logger.e("保存三方信息成功");
                            } else {
                                ToastUtils.getToast(noDataBean.getMsg());
                            }
                        }
                    });
        }
    }

    @Override
    public void loginFailed(String info) {
        ToastUtils.getToast(info);
        getActivity().setResult(AppConstants.YY_PT_LOGIN_FAILED);
    }

    @Override
    public void finishInfo(String loginType) {
        //changeLoadView(false);
        // 将 fragment_container View 中的内容替换为此 Fragment ，
        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
        Bundle bundle = new Bundle();
        bundle.putString("avatarUrl", mAvatarUrl);
        bundle.putString("nickName", mNickName);
        bundle.putBoolean("gender", mGender);
        bundle.putString("type", type);
        loginActivity.mFragmentList.get(2).setArguments(bundle);
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(2));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
        if (!loginType.equals(AppConstants.LOGIN_PHONE)) {
            PTApplication.getRequestService().saveThreePartInfo(mNickName, mAvatarUrl, PTApplication.userToken, loginType, PTApplication.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NoDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError" + e.getMessage());
                        }

                        @Override
                        public void onNext(NoDataBean noDataBean) {
                            if (noDataBean.isSuccess()) {
                                Logger.e("保存三方信息成功");
                            } else {
                                ToastUtils.getToast(noDataBean.getMsg());
                            }
                        }
                    });
        }
    }

    @Override
    public void checkInitResult(boolean isSuccess, String msg) {
        Logger.i("LoginFragment.view:  " + isSuccess + "  --  (" + msg + ")" + "\nflags:  " + getActivity().getIntent().getFlags());
        if (isSuccess) {
            EventBus.getDefault().post(new UserInfoBean());
            getActivity().finish();
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

    //获取验证码结果
    @Override
    public void SmsCodeResult(StringDataBean data) {
        Bundle bundle = new Bundle();
        if (data.isSuccess()) {
            bundle.putString("phone", phoneNumber);
            loginActivity.mFragmentList.get(4).setArguments(bundle);
            transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(4));
            transaction.addToBackStack(null);
            // 执行事务
            transaction.commit();
        } else {
            if (data.getMsg().contains("秒后获取验证码")) {
                String millis = "";
                for (int i = 0; i < data.getMsg().length(); i++) {
                    if (data.getMsg().charAt(i) >= 48 && data.getMsg().charAt(i) <= 57) {
                        millis += data.getMsg().charAt(i);
                    }
                }
                bundle.putString("phone", phoneNumber);
                bundle.putString("millis", millis);
                loginActivity.mFragmentList.get(4).setArguments(bundle);
                transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(4));
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
            }
            //ToastUtils.getToast(data.getMsg());
        }
    }

    @Override
    public void toBindAccout() {
        Bundle bundle = new Bundle();
        bundle.putString("avatarUrl", mAvatarUrl);
        bundle.putString("nickName", mNickName);
        bundle.putBoolean("gender", mGender);
        bundle.putString("type", type);
        loginActivity.mFragmentList.get(5).setArguments(bundle);
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(5));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_loginv3;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_regesist_account_fmt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        loginActivity = (LoginActivity) getActivity();
        transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        if ("验证码登录".equals(tv_login4sms_login_fmt.getText().toString())) {
            et_phone_number_login_fmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (et_phone_number_login_fmt.getText().toString().trim().length() == 11 && et_password_login_fmt.getText().toString().trim().length() >= 1) {
                        bt_login_next_fmt.setEnabled(true);
                    } else {
                        bt_login_next_fmt.setEnabled(false);
                    }
                }
            });
            et_password_login_fmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (et_phone_number_login_fmt.getText().toString().trim().length() == 11 && et_password_login_fmt.getText().toString().trim().length() >= 1) {
                        bt_login_next_fmt.setEnabled(true);
                    } else {
                        bt_login_next_fmt.setEnabled(false);
                    }
                }
            });
        } else {
            et_phone_number_login_fmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (et_phone_number_login_fmt.getText().toString().trim().length() == 11) {
                        bt_login_next_fmt.setEnabled(true);
                    } else {
                        bt_login_next_fmt.setEnabled(false);
                    }
                }
            });
        }
    }

}
