package pro.yueyuan.project_t.me.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.me.DaggerIMeComponent;
import pro.yueyuan.project_t.me.IMeContract;
import pro.yueyuan.project_t.me.MePresenter;
import pro.yueyuan.project_t.me.MePresenterModule;
import pro.yueyuan.project_t.me.ui.fragment.AuthenticationFragment;
import pro.yueyuan.project_t.me.ui.fragment.ChangeAlipayFragment;
import pro.yueyuan.project_t.me.ui.fragment.ChangePwdFragment;
import pro.yueyuan.project_t.me.ui.fragment.FeedBackFragment;
import pro.yueyuan.project_t.me.ui.fragment.UseSettingFragment;
import pro.yueyuan.project_t.utils.ActivityUtils;

/**
 * Created by Key on 2017/3/8 14:27
 * email: MrKey.K@gmail.com
 * description:
 */

public class MeActivity extends NavigationActivity {

    @Inject
    MePresenter mMePresenter;


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
        return R.layout.activity_me;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        navigation_bottom.getMenu().findItem(R.id.navigation_me).setChecked(true).setEnabled(false);
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            /**
             * 个人中心fragment 0
             */
            MeFragment meFragment = MeFragment.newInstance();
            /**
             * 我的余额fragment 1
             */
            MyWalletFragment myWalletFragment = MyWalletFragment.newInstance();
            /**
             * 设置界面fragment 2
             */
            SettingFragment settingFragment = SettingFragment.newInstance();
            /**
             * 实名认证fragment 3
             */
            AuthenticationFragment authenticationFragment = AuthenticationFragment.newInstance();
            /**
             * 修改登录密码fragment 4
             */
            ChangePwdFragment changePwdFragment = ChangePwdFragment.newInstance();
            /**
             * 修改支付宝fragment 5
             */
            ChangeAlipayFragment changeAlipayFragment = ChangeAlipayFragment.newInstance();
            /**
             * 使用设置fragment 6
             */
            UseSettingFragment useSettingFragment = UseSettingFragment.newInstance();
            /**
             * 意见反馈fragment 7
             */
            FeedBackFragment feedBackFragment = FeedBackFragment.newInstance();
            mFragmentList.add(meFragment);
            mFragmentList.add(myWalletFragment);
            mFragmentList.add(settingFragment);
            mFragmentList.add(authenticationFragment);
            mFragmentList.add(changePwdFragment);
            mFragmentList.add(changeAlipayFragment);
            mFragmentList.add(useSettingFragment);
            mFragmentList.add(feedBackFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_me_activity);
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerIMeComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .mePresenterModule(new MePresenterModule(((IMeContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

    }
}
