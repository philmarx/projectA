package com.hzease.tomeet.me.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.PropsShopBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.SpacesItemProps;
import com.hzease.tomeet.widget.adapters.PropsShopAdapter;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/27.
 */

public class PropsCenterFragment extends BaseFragment implements IMeContract.View {

    //道具商城
    @BindView(R.id.rv_propsShop_fmt)
    RecyclerView rv_propsShop_fmt;
    //小纸条
    @BindView(R.id.all_props_small_paper_fmt)
    AutoRelativeLayout all_props_small_paper_fmt;
    //标签消除卡
    @BindView(R.id.all_props_lables_dismiss_fmt)
    AutoRelativeLayout all_props_lables_dismiss_fmt;
    //改名卡
    @BindView(R.id.all_props_name_change_fmt)
    AutoRelativeLayout all_props_name_change_fmt;
    //vip
    @BindView(R.id.all_props_vip_fmt)
    AutoRelativeLayout all_props_vip_fmt;
    //道具名称
    private String[] propsName = {"小纸条", "标签消除卡", "改名卡", "VIP1个月", "VIP3个月", "VIP1年"};
    //道具图片
    private int[] propsIcons = {R.drawable.propsshop_small_paper, R.drawable.propsshop_labels_dismiss, R.drawable.propsshop_name_change,
            R.drawable.propsshop_vip, R.drawable.propsshop_vip, R.drawable.propsshop_vip};
    //道具背景
    private int[] propsbg = {R.drawable.props_small_paper_bg, R.drawable.props_labels_dismiss_bg, R.drawable.props_name_change_bg,
            R.drawable.props_vip_1_bg, R.drawable.props_vip_3_bg, R.drawable.props_vip_12_bg};
    //道具描述
    private String[] propsDic = {"喜欢就要说出来", "代表月亮消灭你", "改名都不是个事儿", "来一场华丽的变身", "来一场华丽的变身", "来一场华丽的变身"};
    //道具价格
    private String[] propMoney = {"1", "1", "10", "5", "15", "20"};
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private List<PropsShopBean> mDatas;


    @OnClick({
            R.id.all_props_small_paper_fmt,
            R.id.all_props_lables_dismiss_fmt,
            R.id.all_props_name_change_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.all_props_small_paper_fmt:
                initPopupWindow(v,0,false);
                break;
            case R.id.all_props_lables_dismiss_fmt:
                initPopupWindow(v,1,false);
                break;
            case R.id.all_props_name_change_fmt:
                initPopupWindow(v,2,false);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public static PropsCenterFragment newInstance() {
        return new PropsCenterFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }

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

    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_propscenter;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        initDatas();
        rv_propsShop_fmt.addItemDecoration(new SpacesItemProps(15));
        rv_propsShop_fmt.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
        PropsShopAdapter adapter = new PropsShopAdapter(mDatas, getContext());
        adapter.setOnItemClickLitener(new PropsShopAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                initPopupWindow(view,position,true);
            }
        });
        rv_propsShop_fmt.setAdapter(adapter);
    }

    //添加道具对象
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            PropsShopBean shopBean = new PropsShopBean();
            shopBean.setPropsName(propsName[i]);
            shopBean.setPropsIcon(propsIcons[i]);
            shopBean.setPropsDic(propsDic[i]);
            shopBean.setPropsMoney(propMoney[i]);
            mDatas.add(shopBean);
        }
    }
    /**
     * 弹出popupwindow
     * @param view
     * @param bgIndex
     * @param isBuy
     */
    private void initPopupWindow(View view, int bgIndex, final boolean isBuy) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_myprops, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            }
        });
        //pop背景
        AutoLinearLayout bg = (AutoLinearLayout) contentView.findViewById(R.id.all_props_bg_pop);
        //取消
        Button  cancel = (Button) contentView.findViewById(R.id.bt_props_cancel_pop);
        //使用或购买
        Button buyoruse = (Button) contentView.findViewById(R.id.bt_props_buyoruse_pop);
        bg.setBackgroundResource(propsbg[bgIndex]);
        //判断显示购买还是显示使用
        if (isBuy){
            buyoruse.setText("购买");
        }else{
            buyoruse.setText("使用");
        }
        //关闭pop
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //执行购买或者使用
        buyoruse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBuy){
                    //TODO 执行购买方法
                    ToastUtils.getToast(getContext(),"购买成功");
                    popupWindow.dismiss();
                }else{
                    //TODO 执行使用方法
                    ToastUtils.getToast(getContext(),"使用成功");
                    popupWindow.dismiss();
                }
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
