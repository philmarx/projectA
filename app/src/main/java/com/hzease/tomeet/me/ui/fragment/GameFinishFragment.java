package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.widget.adapters.AddscoreAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/5/8.
 */

public class GameFinishFragment extends BaseFragment implements IMeContract.View {

    //定义一个图片数组
    int[] gameType = {R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_3,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
            R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1,R.drawable.two_one4_2,R.drawable.two_one4_3,R.drawable.two_one4_4,R.drawable.two_one4_5};
    @BindView(R.id.tv_finish_roomname_fmt)
    TextView tv_finish_roomname_fmt;
    @BindView(R.id.tv_finish_roomtime_fmt)
    TextView tv_finish_roomtime_fmt;
    @BindView(R.id.tv_finish_roomplace_fmt)
    TextView tv_finish_roomplace_fmt;
    @BindView(R.id.tv_finish_roommoney_fmt)
    TextView tv_finish_roommoney_fmt;
    @BindView(R.id.tv_finish_roomnotice_fmt)
    TextView tv_finish_roomnotice_fmt;
    @BindView(R.id.lv_me_finishgame_fmt)
    ListView lv_me_finishgame_fmt;
    @BindView(R.id.iv_typeIcon_fmt)
    ImageView iv_typeIcon_fmt;
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;

    public static GameFinishFragment newInstance() {
        return new GameFinishFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示 或 刷新 我的信息
     */
    @Override
    public void showMyInfo() {

    }

    /**
     * 显示我的房间
     *
     * @param myJoinRoomBean
     */
    @Override
    public void showMyRooms(HomeRoomsBean myJoinRoomBean,boolean isLoadMore) {

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

    /**
     * 认证成功
     */
    @Override
    public void authorizedSuccess() {

    }


    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {
        iv_typeIcon_fmt.setImageResource(gameType[data.getRoom().getGame().getId()-6]);
        tv_finish_roomname_fmt.setText(data.getRoom().getName());
        String time = "活动时间：";
        String beginTime = data.getRoom().getBeginTime();
        beginTime = beginTime.replace("-", ".");
        String endTime = data.getRoom().getEndTime();
        endTime = endTime.replace("-", ".");
        time = time + beginTime + " - " + endTime;
        tv_finish_roomtime_fmt.setText(time);
        tv_finish_roomplace_fmt.setText("活动地点：" + data.getRoom().getPlace());
        tv_finish_roommoney_fmt.setText("保证金：" + data.getRoom().getMoney() + "元");
        tv_finish_roomnotice_fmt.setText("活动介绍：" + data.getRoom().getDescription());
        AddscoreAdapter adapter = new AddscoreAdapter(data.getMembers());
        lv_me_finishgame_fmt.setAdapter(adapter);
    }

    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_gamefinish;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        long roomId = getArguments().getLong("roomId");
        mPresenter.gameFinish(roomId);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }

}
