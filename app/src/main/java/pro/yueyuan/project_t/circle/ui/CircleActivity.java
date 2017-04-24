package pro.yueyuan.project_t.circle.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.CirclePresenter;
import pro.yueyuan.project_t.circle.CirclePresenterModule;
import pro.yueyuan.project_t.circle.DaggerICircleComponent;
import pro.yueyuan.project_t.circle.ICircleContract;
import pro.yueyuan.project_t.circle.fragment.CircleFragment;
import pro.yueyuan.project_t.circle.fragment.CircleInfoFragment;
import pro.yueyuan.project_t.circle.fragment.MotifityCircleFragment;
import pro.yueyuan.project_t.circle.fragment.MyCircleFragment;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class CircleActivity extends NavigationActivity {

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    @Inject
    CirclePresenter mCriclePresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;
    @BindView(R.id.rb_friend_fmt)
    RadioButton rb_friend_fmt;
    @BindView(R.id.rb_circle_fmt)
    RadioButton rb_circle_fmt;
    @BindView(R.id.rg_circle_selector)
    RadioGroup rg_circle_selector;

    //设置第一个按钮是否选中
    private boolean isChecked=true;

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
        return R.layout.activity_circle;
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
            //圈子Fragment
            MyCircleFragment myCircleFragment = MyCircleFragment.newInstance();
            //圈子详情Fragment
            CircleInfoFragment circleInfoFragment = CircleInfoFragment.newInstance();
            //修改圈子公告
            MotifityCircleFragment motifityCircleFragment = MotifityCircleFragment.newInstance();
            mFragmentList.add(circleFragment);
            mFragmentList.add(myCircleFragment);
            mFragmentList.add(circleInfoFragment);
            mFragmentList.add(motifityCircleFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_bidding_activity);
        }
        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerICircleComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .circlePresenterModule(new CirclePresenterModule(((ICircleContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }
        rb_friend_fmt.setChecked(isChecked);
        rg_circle_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_friend_fmt:
                        replaceFragment(new CircleFragment());
                        break;
                    case R.id.rb_circle_fmt:
                        replaceFragment(new MyCircleFragment());
                        isChecked=false;
                        break;
                }
            }
        });
        navigation_bottom.getMenu().findItem(R.id.navigation_bidding).setChecked(true).setEnabled(false);
    }

    //Fragment启动方法：
    private void replaceFragment(Fragment fragment) {
        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager =getSupportFragmentManager();
        // 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fl_content_bidding_activity, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        //transaction.addToBackStack(null);
        // 5.提交事物,调用commit()方法来完成
        transaction.commit();
    }
}
