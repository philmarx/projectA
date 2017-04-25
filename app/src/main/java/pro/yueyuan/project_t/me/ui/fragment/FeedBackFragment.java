package pro.yueyuan.project_t.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UserOrderBean;
import pro.yueyuan.project_t.me.IMeContract;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.utils.ToastUtils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class FeedBackFragment extends BaseFragment implements IMeContract.View {
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    MeActivity meActivity;
    @BindView(R.id.bt_me_feedback_fmt)
    Button bt_me_feedback_fmt;
    @BindView(R.id.et_me_feedback_fmt)
    EditText et_me_feedback_fmt;
    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }
    @OnClick({
            R.id.bt_me_feedback_fmt
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_me_feedback_fmt:
                String content = et_me_feedback_fmt.getText().toString().trim();
                mPresenter.feedBack(content,PTApplication.userToken,PTApplication.userId);
                break;
        }
    }

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

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
        if (isSuccess){
            ToastUtils.getToast(PTApplication.getInstance(),"提交成功");
            meActivity.getSupportFragmentManager().popBackStack();
        }else{
            ToastUtils.getToast(PTApplication.getInstance(),"提交失败");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
    }
}
