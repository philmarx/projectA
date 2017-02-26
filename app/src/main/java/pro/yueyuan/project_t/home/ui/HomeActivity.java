package pro.yueyuan.project_t.home.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.home.DaggerIHomeComponent;
import pro.yueyuan.project_t.home.HomePresenter;
import pro.yueyuan.project_t.home.HomePresenterModule;
import pro.yueyuan.project_t.home.IHomeContract;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class HomeActivity extends NetActivity {

    @Inject
    HomePresenter mHomePresenter;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;

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
        return R.layout.activity_home;
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
            HomeFragment homeFragment = HomeFragment.newInstance();
            mFragmentList.add(homeFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_home_activity);
        }

        // dagger2
        DaggerIHomeComponent.builder()
                .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                // .homePresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                .homePresenterModule(new HomePresenterModule(((IHomeContract.View) (mFragmentList.get(0)))))
                .build().inject(this);
    }
}
