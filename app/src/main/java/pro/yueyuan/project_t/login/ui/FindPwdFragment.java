package pro.yueyuan.project_t.login.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.utils.CountDownButtonHelper;
import pro.yueyuan.project_t.utils.MatchUtils;
import pro.yueyuan.project_t.utils.ToastUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/21.
 */

public class FindPwdFragment extends BaseFragment implements ILoginContract.View {

    @BindView(R.id.et_forgetpwd_phone_fmt)
    EditText etForgetpwdPhoneFmt;
    @BindView(R.id.et_forgetpwd_pwd_fmt)
    EditText etForgetpwdPwdFmt;
    @BindView(R.id.tv_forgetpwd_getsmscode_fmt)
    TextView tvForgetpwdGetsmscodeFmt;
    @BindView(R.id.bt_forgetpwd_next_fmt)
    Button btForgetpwdNextFmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;

    public FindPwdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static FindPwdFragment newInstance() {
        return new FindPwdFragment();
    }



    @Override
    public void setPresenter(@NonNull ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void smsCodeCountdown(StringDataBean stringDataBean) {
        if (stringDataBean.isSuccess()) {
            //成功后开始倒计时
            ToastUtils.getToast(getContext(),"发送验证码成功");
            tvForgetpwdGetsmscodeFmt.setTextColor(Color.rgb(184,184,184));
            CountDownButtonHelper helper = new CountDownButtonHelper(tvForgetpwdGetsmscodeFmt,"发送验证码",60,1);
            helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                @Override
                public void finish() {
                    tvForgetpwdGetsmscodeFmt.setTextColor(Color.rgb(255,103,102));
                }
            });
            helper.start();
        } else {
            // 失败提示
            ToastUtils.getToast(getContext(), "获取失败,请稍候重试");
        }
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
    }

    @Override
    public void getAuthLoginInfo(String id, String token) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_forgetpwd;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
    }
    @OnClick({
            R.id.et_forgetpwd_phone_fmt,
            R.id.et_forgetpwd_pwd_fmt,
            R.id.tv_forgetpwd_getsmscode_fmt,
            R.id.bt_forgetpwd_next_fmt
    })
    public void Onclick(View v){
        switch (v.getId()){
            case R.id.tv_forgetpwd_getsmscode_fmt:
                // 获取验证码
                String phoneNumber = etForgetpwdPhoneFmt.getText().toString().trim();
                if (MatchUtils.isPhoneNumber(phoneNumber)) {
                    mPresenter.getSmsCode(phoneNumber);
                }
                break;
            case R.id.bt_forgetpwd_next_fmt:
                //设置新密码

                break;
        }
    }
}
