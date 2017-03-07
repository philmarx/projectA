package pro.yueyuan.project_t.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.orhanobut.logger.Logger;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.home.IHomeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class HomeFragment extends BaseFragment implements IHomeContract.View {

    @BindView(R.id.amap)
    MapView amap;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.login_qq)
    Button loginQq;

    @BindView(R.id.iv_home_fragment)
    ImageView iv_home_fragment;

    //高德地图
    private AMap mAMap;

    //QQ登录
    private Tencent mTencent;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        amap.onResume();
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        amap.onCreate(savedInstanceState);
        amap.getMap();

        loginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQlogin();
            }
        });

    }

    /**
     * QQ登录
     */
    private void QQlogin() {
        mTencent = Tencent.createInstance("1105938559", mContext);
        mTencent.login(getActivity(), "all", new IUiListener() {
            //登录成功,在此可以获取用户信息
            @Override
            public void onComplete(Object o) {
                Logger.e("AAA", "登录成功");
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onPause() {
        super.onPause();
        amap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        amap.onDestroy();
    }

    /**
     * 显示我的头像
     */
    @Override
    public void showMyAvatar() {

    }

    @OnClick(R.id.b_home_fragment)
    public void onClick() {
        mPresenter.loadMyAvatar();
    }
}
