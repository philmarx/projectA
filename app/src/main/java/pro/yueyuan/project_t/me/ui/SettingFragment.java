package pro.yueyuan.project_t.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class SettingFragment extends BaseFragment implements IMeContract.View {

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    /**
     * 实名认证
     */
    @BindView(R.id.rl_setting_authentication_fmt)
    RelativeLayout rlSettingAuthenticationFmt;
    /**
     * 修改登录密码
     */
    @BindView(R.id.rl_setting_changepwd_fmt)
    RelativeLayout rlSettingChangedPwdFmt;
    /**
     * 修改支付宝
     */
    @BindView(R.id.rl_setting_changealipay_fmt)
    RelativeLayout rlSettingChangeAlipayFmt;
    /**
     * 使用设置
     */
    @BindView(R.id.rl_setting_use_fmt)
    RelativeLayout rlSettingUseFmt;
    /**
     * 意见反馈
     */
    @BindView(R.id.rl_setting_feedback_fmt)
    RelativeLayout rlSettingFeedBackFmt;
    /**
     * 退出当前账号
     */
    @BindView(R.id.bt_setting_logout_fmt)
    Button btSettingLogoutFmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    /**
     * 点击事件
     *
     * @return
     */
    @OnClick({
            R.id.rl_setting_authentication_fmt,
            R.id.rl_setting_changepwd_fmt,
            R.id.rl_setting_changealipay_fmt,
            R.id.rl_setting_use_fmt,
            R.id.rl_setting_feedback_fmt,
            R.id.bt_setting_logout_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_authentication_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(3));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_changepwd_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(4));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_changealipay_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(5));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_use_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(6));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.rl_setting_feedback_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(7));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bt_setting_logout_fmt:
                mPresenter.logoutUser();
                startActivity(new Intent(getContext(), HomeActivity.class));
                break;
        }
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyAvatar() {

    }

    @Override
    public void showMyInfo(String nickName, String amount) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
