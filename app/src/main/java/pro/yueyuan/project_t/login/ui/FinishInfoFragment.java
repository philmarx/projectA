package pro.yueyuan.project_t.login.ui;

import android.os.Bundle;

import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.login.ILoginContract;

/**
 * Created by xuq on 2017/3/22.
 */

public class FinishInfoFragment extends BaseFragment implements ILoginContract.View{
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
    public int getContentViewId() {
        return R.layout.fragment_finishnfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
