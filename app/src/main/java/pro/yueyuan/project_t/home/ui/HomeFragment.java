package pro.yueyuan.project_t.home.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.RequestService;
import pro.yueyuan.project_t.home.IHomeContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class HomeFragment extends BaseFragment implements IHomeContract.View {

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

        // sms
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
                });

    }

    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
