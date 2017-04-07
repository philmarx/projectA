package pro.yueyuan.project_t.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UserOrderBean;
import pro.yueyuan.project_t.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/6.
 */

public class PersonOrderInfoFragment extends BaseFragment implements IMeContract.View,View.OnClickListener {

    int SEND_NOTE = 001;
    int EDIT_PIC = 002;
    int type = EDIT_PIC;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    @BindView(R.id.lv_personspace_order_fmt)
    ListView lv_personspace_order_fmt;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;

    private long userId;

    public PersonOrderInfoFragment() {
        super();
    }

    public PersonOrderInfoFragment(long userId) {
        this.userId = userId;
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static PersonOrderInfoFragment newInstance() {
        return new PersonOrderInfoFragment();
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
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {
    }

    /**
     * 显示请求用户的排行信息
     * @param userOrderBean
     */
    @Override
    public void showRequestUserOrder(UserOrderBean userOrderBean) {
        tv_personspace_username_fmt.setText(userOrderBean.getData().getNickname());
        tv_personspace_usernamebak_fmt.setText(userOrderBean.getData().getNickname());
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_personspace;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        Logger.e(String.valueOf(userId));
        if(String.valueOf(userId).equals("0")){
            type = EDIT_PIC;
            tv_personspace_sendoredit_fmt.setText("编辑");
            mPresenter.getOrderById(Long.valueOf(PTApplication.userId));
        }else {
            type = SEND_NOTE;
            tv_personspace_sendoredit_fmt.setText("传纸条");
            mPresenter.getOrderById(Long.valueOf(this.userId));
        }
    }
    @Override
    public void onClick(View v) {

    }
}
