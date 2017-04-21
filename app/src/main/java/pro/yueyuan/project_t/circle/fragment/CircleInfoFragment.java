package pro.yueyuan.project_t.circle.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import butterknife.BindView;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.ICircleContract;
import pro.yueyuan.project_t.utils.Untils4px2dp;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/20.
 */

public class CircleInfoFragment extends BaseFragment implements ICircleContract.View {

    @BindView(R.id.viewPager_tab)
    ViewPager viewPagerTab;
    private String[] tabTitles = {"活动", "产出"};

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private ICircleContract.Presenter mPresenter;

    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static CircleInfoFragment newInstance() {
        return new CircleInfoFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circleinfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTabTitle();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, Untils4px2dp.px2dp(150),Untils4px2dp.px2dp(150));
            }
        });
    }

    private void initTabTitle() {
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }
    }


    //修改tablayout下划线的长度
    public void setIndicator (TabLayout tabs,int leftDip,int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
