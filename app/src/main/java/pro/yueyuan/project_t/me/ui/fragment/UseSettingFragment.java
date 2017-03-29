package pro.yueyuan.project_t.me.ui.fragment;

import android.os.Bundle;

import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class UseSettingFragment extends BaseFragment implements IMeContract.View {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static UseSettingFragment newInstance() {
        return new UseSettingFragment();
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

    @Override
    public int getContentViewId() {
        return R.layout.fragment_usesetting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
