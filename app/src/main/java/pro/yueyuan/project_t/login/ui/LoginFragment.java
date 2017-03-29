package pro.yueyuan.project_t.login.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.utils.CountDownButtonHelper;
import pro.yueyuan.project_t.utils.PhoneNumberUtils;
import pro.yueyuan.project_t.utils.RongCloudInitUtils;
import pro.yueyuan.project_t.utils.ToastUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/3/20 15:28
 * email: MrKey.K@gmail.com
 * description:
 */

public class LoginFragment extends BaseFragment implements ILoginContract.View {

    public static final int SIGN_IN_FOR_PASSWORD = 1;
    public static final int SIGN_IN_FOR_SMS_CODE = 2;
    public static final int LOGIN_FOR_SIGN_UP = 3;

    public int loginType = 1;

    @BindView(R.id.rb_sign_in_login_fmt)
    RadioButton rb_sign_in_login_fmt;
    @BindView(R.id.rb_sign_up_login_fmt)
    RadioButton rb_sign_up_login_fmt;
    /**
     * 忘记密码按钮
     */
    @BindView(R.id.tv_forget_login_fmt)
    TextView tv_forget_login_fmt;
    @BindView(R.id.tv_forgetbak_login_fmt)
    TextView tv_forgetbak_login_fmt;
    /**
     * 通过短信登录按钮
     */
    @BindView(R.id.tv_login4sms_login_fmt)
    TextView tv_login4sms_login_fmt;
    /**
     * 密码输入框
     */
    @BindView(R.id.et_password_login_fmt)
    EditText et_password_login_fmt;
    /**
     * 登录等,下一步按钮
     */
    @BindView(R.id.b_next_login_fmt)
    Button b_next_login_fmt;
    /**
     * 手机号码
     */
    @BindView(R.id.et_phone_number_login_fmt)
    EditText et_phone_number_login_fmt;

    /**
     * 倒计时开关
     */
    private CountDownButtonHelper helper;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fregment_login;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @OnClick({
            // 登录按钮
            R.id.rb_sign_in_login_fmt,
            // 注册按钮
            R.id.rb_sign_up_login_fmt,
            // 忘记密码 or 获取验证码 按钮
            R.id.tv_forget_login_fmt,
            // 通过验证码 or 通过手机号密码 登录 按钮
            R.id.tv_login4sms_login_fmt,
            // 下一步(登录或注册)按钮
            R.id.b_next_login_fmt,
            R.id.tv_forgetbak_login_fmt
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rb_sign_in_login_fmt:
                Logger.d("登录");
                // 密码框
                et_password_login_fmt.setHint(R.string.password);
                // 忘记密码框
                //tv_forget_login_fmt.setText(R.string.forget_pwd);
                tv_forgetbak_login_fmt.setVisibility(View.VISIBLE);
                tv_forgetbak_login_fmt.setText(R.string.forget_pwd);
                tv_forget_login_fmt.setVisibility(View.INVISIBLE);
                // 下一步按钮
                b_next_login_fmt.setText(R.string.sign_in);
                // 通过短信登录按钮
                tv_login4sms_login_fmt.setText(R.string.login4sms);
                tv_login4sms_login_fmt.setVisibility(View.VISIBLE);
                et_phone_number_login_fmt.setText("");
                et_password_login_fmt.setText("");
                loginType = SIGN_IN_FOR_PASSWORD;
                break;

            case R.id.rb_sign_up_login_fmt:
                Logger.d("注册");
                // 密码框
                et_password_login_fmt.setHint(R.string.sms_code);
                // 忘记密码框
                tv_forgetbak_login_fmt.setVisibility(View.INVISIBLE);
                tv_forget_login_fmt.setVisibility(View.VISIBLE);
                tv_forget_login_fmt.setText(R.string.get_sms_code);
                // 下一步按钮
                b_next_login_fmt.setText(R.string.sign_up);
                // 通过短信登录按钮
                tv_login4sms_login_fmt.setText(R.string.user_agreement);
                tv_login4sms_login_fmt.setVisibility(View.VISIBLE);
                et_phone_number_login_fmt.setText("");
                et_password_login_fmt.setText("");
                loginType = LOGIN_FOR_SIGN_UP;
                break;

            case R.id.tv_forget_login_fmt:
                Logger.d("忘记密码 or 获取验证码");
                switch (loginType) {
                    case SIGN_IN_FOR_PASSWORD:

                        break;
                    case SIGN_IN_FOR_SMS_CODE:
                    case LOGIN_FOR_SIGN_UP:
                        // 获取验证码
                        String phoneNumber = et_phone_number_login_fmt.getText().toString().trim();
                        if (PhoneNumberUtils.isPhoneNumber(phoneNumber)) {
                            mPresenter.getSmsCode(phoneNumber);
                        }
                        break;
                }
                break;
            case R.id.tv_forgetbak_login_fmt:
                /**
                 * 跳转到忘记密码
                 */
                Logger.d("忘记密码");
                LoginActivity loginActivity = (LoginActivity) getActivity();
                FragmentTransaction transaction = loginActivity.getSupportFragmentManager().beginTransaction();
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(1));
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.tv_login4sms_login_fmt:
                Logger.d("短信登录和手机登录切换");
                switch (loginType) {

                    case SIGN_IN_FOR_PASSWORD:
                        // 密码框
                        et_password_login_fmt.setHint(R.string.sms_code);
                        // 忘记密码框
                        tv_forget_login_fmt.setVisibility(View.VISIBLE);
                        tv_forgetbak_login_fmt.setVisibility(View.INVISIBLE);
                        tv_forget_login_fmt.setText(R.string.get_sms_code);
                        // 下一步按钮
                        b_next_login_fmt.setText(R.string.sign_in);
                        // 通过短信登录按钮
                        tv_login4sms_login_fmt.setText(R.string.login4password);
                        tv_login4sms_login_fmt.setVisibility(View.VISIBLE);
                        et_phone_number_login_fmt.setText("");
                        et_password_login_fmt.setText("");
                        loginType = SIGN_IN_FOR_SMS_CODE;
                        break;

                    case SIGN_IN_FOR_SMS_CODE:
                        // 密码框
                        et_password_login_fmt.setHint(R.string.password);
                        // 忘记密码框
                        tv_forget_login_fmt.setText(R.string.forget_pwd);
                        // 下一步按钮
                        b_next_login_fmt.setText(R.string.sign_in);
                        // 通过短信登录按钮
                        tv_login4sms_login_fmt.setText(R.string.login4sms);
                        tv_login4sms_login_fmt.setVisibility(View.VISIBLE);

                        loginType = SIGN_IN_FOR_PASSWORD;
                        break;

                    case LOGIN_FOR_SIGN_UP:
                        Logger.d("展示用户协议");
                        // TODO 展示用户协议
                }
                break;

            case R.id.b_next_login_fmt:
                // 手机号检测
                String phoneNumber = et_phone_number_login_fmt.getText().toString().trim();
                String password = et_password_login_fmt.getText().toString().trim();
                Logger.d("phoneNumber: " + phoneNumber + "   password: " + password);
                if (PhoneNumberUtils.isPhoneNumber(phoneNumber)) {
                    switch (loginType) {
                        case SIGN_IN_FOR_PASSWORD:
                            // 密码登录
                            Logger.d("密码登录");
                            if (password.length() > 5) {
                                mPresenter.phonePasswordSignIn(phoneNumber, password);
                            } else {
                                ToastUtils.getToast(getContext(), "密码不会小于6位哦");
                            }
                            break;
                        case SIGN_IN_FOR_SMS_CODE:
                            // 验证码登录
                            Logger.d("验证码登录");
                            if (password.length() == 6) {
                                mPresenter.smsCodeSignIn(phoneNumber, password);
                            } else {
                                ToastUtils.getToast(getContext(), "验证码是6位数字哦");
                            }
                            break;
                        case LOGIN_FOR_SIGN_UP:
                            // 验证码注册
                            Logger.d("验证码注册");
                            if (password.length() == 6) {
                                mPresenter.smsCodeSignIn(phoneNumber, password);
                            } else {
                                ToastUtils.getToast(getContext(), "验证码是6位数字哦");
                            }
                            break;
                    }
                }
                break;
        }
    }

    /**
     * 获取验证码后开始倒计时60秒
     *
     * @param stringDataBean
     */
    @Override
    public void smsCodeCountdown(StringDataBean stringDataBean) {
        if (stringDataBean.isSuccess()) {
            //成功后开始倒计时
            ToastUtils.getToast(getContext(), "发送验证码成功");
            tv_forget_login_fmt.setTextColor(Color.rgb(184, 184, 184));
            helper = new CountDownButtonHelper(tv_forget_login_fmt, "发送验证码", 60, 1);
            helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                @Override
                public void finish() {
                    tv_forget_login_fmt.setTextColor(Color.rgb(255, 103, 102));
                }
            });
            helper.start();
        } else {
            // 失败提示
            ToastUtils.getToast(getContext(), "获取失败,请稍候重试");
        }
    }

    /**
     * 登录成功
     * from:smsCodeSignIn
     * from:phonePasswordSignIn
     */
    @Override
    public void loginSuccess() {
        // 跳转到转进来的页面
        getActivity().setResult(AppConstants.YY_PT_LOGIN_SUCCEED);
        getActivity().finish();
        Logger.d("登录成功");
        // 登录成功,保存用户id token
        mPresenter.saveUserIdAndToken();

        // 融云初始化
        new RongCloudInitUtils().RongCloudInit();

        // OSS, 不用再这里初始化.先放这里,在需要上传的时候再初始化
        // OssUtils.aliyunOssInit();
    }

    /**
     * 登录失败.返回原因
     * from: smsCodeSignIn
     * from: phonePasswordSignIn
     *
     * @param info 失败原因
     */
    @Override
    public void loginFailed(String info) {
        ToastUtils.getToast(getContext(), info);
        getActivity().setResult(AppConstants.YY_PT_LOGIN_FAILED);
    }

    @Override
    public void finishInfo() {
        // 登录成功,保存用户id token
        mPresenter.saveUserIdAndToken();

        LoginActivity loginActivity = (LoginActivity) getActivity();
        FragmentTransaction transaction = loginActivity.getSupportFragmentManager().beginTransaction();
        // 将 fragment_container View 中的内容替换为此 Fragment ，
        // 然后将该事务添加到返回堆栈，以便用户可以向后导航
        transaction.replace(R.id.fl_content_login_activity, loginActivity.mFragmentList.get(2));
        transaction.addToBackStack(null);
        // 执行事务
        transaction.commit();
    }

    /**
     * 注册成功
     */
    @Override
    public void registerSuccess() {

    }

    /**
     * 当fragment被销毁的时候，取消计时器
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(helper!= null){
            helper.stop();
        }
    }

}