package pro.yueyuan.project_t.login.ui;

import android.os.Bundle;

import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/28.
 */

public class SetNewPwdFragment extends BaseFragment implements ILoginContract.View  {

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private ILoginContract.Presenter mPresenter;

    public SetNewPwdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    public static SetNewPwdFragment newInstance() {
        return new SetNewPwdFragment();
    }

    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
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
    public int getContentViewId() {
        return 0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
