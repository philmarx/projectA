package pro.yueyuan.project_t.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/23.
 */

public class MyWalletFragment extends BaseFragment implements IMeContract.View  {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    BottomNavigationView bottomNavigationView;
    public MyWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        // mPresenter.start();
    }

    public static MyWalletFragment newInstance() {
        return new MyWalletFragment();
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
        return R.layout.fragment_mywallet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
