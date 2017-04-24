package pro.yueyuan.project_t.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.data.UserOrderBean;
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
    @BindView(R.id.tv_mewallet_amount_fmt)
    TextView tv_mewallet_amount_fmt;
    @BindView(R.id.tv_mewallet_lockamount_fmt)
    TextView tv_mewallet_lockamount_fmt;
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
    public void showMyInfo(UserInfoBean userInfoBean) {
        tv_mewallet_amount_fmt.setText(userInfoBean.getData().getAmount()+".0");
        tv_mewallet_lockamount_fmt.setText(userInfoBean.getData().getLockAmount()+".0");
    }
    @Override
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {

    }

    @Override
    public void showRequestUserOrder(UserOrderBean userOrderBean) {

    }

    /**
     * 更新密码成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    /**
     * 提交反馈成功
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mywallet;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        mPresenter.loadMyInfo(PTApplication.userId,PTApplication.userToken);
    }
}
