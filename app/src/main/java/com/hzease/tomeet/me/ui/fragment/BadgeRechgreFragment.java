package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.BadgeBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * 叶子充值界面
 * Created by xuq on 2017/5/18.
 */

public class BadgeRechgreFragment extends BaseFragment implements IMeContract.View {

    @BindView(R.id.tfl_badge_regchre_fmt)
    TagFlowLayout tfl_badge_regchre_fmt;
    @BindView(R.id.cb_badge_alipay_fmt)
    CheckBox cb_badge_alipay_fmt;
    @BindView(R.id.cb_badge_wxpay_fmt)
    CheckBox cb_badge_wxpay_fmt;
    @BindView(R.id.bt_me_changpwd_fmt)
    Button bt_me_changpwd_fmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    private String[] badgeCount = {"5", "10", "20", "30", "50", "100"};
    private String[] badgeMoneys = {"5元", "10元", "20元", "30元", "50元", "100元"};
    private List<BadgeBean> mDatas;
    private String pay;
    private String count;
    private String money;

    @OnClick({
            R.id.bt_me_changpwd_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_me_changpwd_fmt:
                //TODO 执行充值操作
                if (cb_badge_alipay_fmt.isChecked()){
                    ToastUtils.getToast(getContext(),pay + money + "元,获得徽章" + count + "枚");
                }else{
                    ToastUtils.getToast(getContext(),pay + money + "元,获得徽章" + count + "枚");
                }
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static BadgeRechgreFragment newInstance() {
        return new BadgeRechgreFragment();
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
     * @param isLoadMore
     */
    @Override
    public void showMyRooms(MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore) {

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

    /**
     * 显示结束房间信息
     *
     * @param data
     */
    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    /**
     * 显示待评价成员
     *
     * @param data
     */
    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

    }

    /**
     * 显示道具数量
     *
     * @param data
     */
    @Override
    public void showPropsMum(PropsMumBean.DataBean data) {

    }

    /**
     * 修改昵称成功
     */
    @Override
    public void showChangeNameSuccess() {

    }

    /**
     * 显示购买道具结果
     *
     * @param success
     * @param msg
     */
    @Override
    public void showBuyPropsResult(int index,boolean success, String msg) {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_badgerechgre;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        initDatas();
        tfl_badge_regchre_fmt.setAdapter(new TagAdapter<BadgeBean>(mDatas) {
            @Override
            public View getView(FlowLayout parent, int position, BadgeBean badgeBean) {
                AutoLinearLayout view = (AutoLinearLayout) View.inflate(getContext(), R.layout.item_taglayout, null);
                if (position == 0){
                    view.setPadding(80,24,80,20);
                }
                if (position == 5){
                    view.setPadding(56,24,56,20);
                }
                TextView badgeCount = (TextView) view.findViewById(R.id.tv_badge_count_fmt);
                TextView badgeMoney = (TextView) view.findViewById(R.id.tv_badge_money_fmt);
                badgeCount.setText(mDatas.get(position).getBadgeCount());
                badgeMoney.setText(mDatas.get(position).getBadgeMoney());
                return view;
            }
        });
        tfl_badge_regchre_fmt.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                count = mDatas.get(position).getBadgeCount();
                money = mDatas.get(position).getBadgeMoney();
                Logger.e(position+"\ncount " + count + "\nmoney" + money);
                return true;
            }
        });
        cb_badge_alipay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_badge_wxpay_fmt.setChecked(false);
                    cb_badge_alipay_fmt.setChecked(isChecked);
                    pay = "支付宝充值";
                }
            }
        });
        cb_badge_wxpay_fmt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cb_badge_alipay_fmt.setChecked(false);
                cb_badge_wxpay_fmt.setChecked(isChecked);
                pay = "微信充值";
            }
        });
    }

    //加载对象
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            BadgeBean bean = new BadgeBean();
            bean.setBadgeCount(badgeCount[i]);
            bean.setBadgeMoney(badgeMoneys[i]);
            mDatas.add(bean);
        }
    }

}
