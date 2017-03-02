package pro.yueyuan.project_t.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.orhanobut.logger.Logger;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.BindView;
import butterknife.ButterKnife;
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
       /* // sms
        new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_JSMS_URL)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                //.client(new SMSCodeUtils().sendSMSCode())
                .build().create(RequestService.class)
                .getSMSCode("18698569593", 1)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Logger.d(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Logger.d(t);
                    }
                });*/

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
        mTencent = Tencent.createInstance("1105938559",mContext);
        mTencent.login(getActivity(), "all", new IUiListener() {
            //登录成功,在此可以获取用户信息
            @Override
            public void onComplete(Object o) {
                Logger.e("AAA","登录成功");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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
}
