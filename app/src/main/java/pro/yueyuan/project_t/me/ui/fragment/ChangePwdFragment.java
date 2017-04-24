package pro.yueyuan.project_t.me.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.data.UserOrderBean;
import pro.yueyuan.project_t.me.IMeContract;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.utils.ToastUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class ChangePwdFragment extends BaseFragment implements IMeContract.View{

    @BindView(R.id.bt_me_updatepwd_fmt)
    Button bt_me_updatepwd_fmt;
    @BindView(R.id.et_me_olderpwd_fmt)
    EditText et_me_olderpwd_fmt;
    @BindView(R.id.et_me_newpwd_fmt)
    EditText et_me_newpwd_fmt;
    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;


    @OnClick({
            R.id.bt_me_updatepwd_fmt
    })
    public void onClick(View v){
     switch (v.getId()){
         case R.id.bt_me_updatepwd_fmt:
             String olderPwd = et_me_olderpwd_fmt.getText().toString().trim();
             String newPwd = et_me_newpwd_fmt.getText().toString().trim();
             mPresenter.updatePwd(olderPwd,newPwd, PTApplication.userToken,PTApplication.userId);
             break;
     }
    }
    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static ChangePwdFragment newInstance() {
        return new ChangePwdFragment();
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

    }


    @Override
    public void showMyRooms(MyJoinRoomBean myJoinRoomBean) {

    }

    @Override
    public void showRequestUserOrder(UserOrderBean userOrderBean) {

    }

    /**
     * 更新密码成功
     */
    @Override
    public void updatePwdSuccess(boolean isSuccess,String msg) {
        Logger.e("updatePwdSuccess");
        if (isSuccess){
            ToastUtils.getToast(PTApplication.getInstance(),msg);
            meActivity.getSupportFragmentManager().popBackStack();
        }else{
            ToastUtils.getToast(PTApplication.getInstance(),msg);
        }
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
        return R.layout.fragment_changepwd;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
    }
}
