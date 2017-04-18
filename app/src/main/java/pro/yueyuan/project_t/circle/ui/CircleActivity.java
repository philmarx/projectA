package pro.yueyuan.project_t.circle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.CirclePresenter;
import pro.yueyuan.project_t.circle.fragment.CircleFragment;
import pro.yueyuan.project_t.home.ui.HomeFragment;
import pro.yueyuan.project_t.home.ui.fragment.CreateRoomFragment;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class CircleActivity extends NavigationActivity{

    @Inject
    CirclePresenter mCriclePresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_bidding;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            CircleFragment circleFragment = CircleFragment.newInstance();
            mFragmentList.add(circleFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_bidding_activity);
        }
        navigation_bottom.getMenu().findItem(R.id.navigation_bidding).setChecked(true).setEnabled(false);
    }
}
