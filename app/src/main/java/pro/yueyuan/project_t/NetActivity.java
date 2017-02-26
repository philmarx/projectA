package pro.yueyuan.project_t;

import android.os.Bundle;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Key on 2016/10/11 15:17
 * email: MrKey.K@gmail.com
 * description: 网络层基类
 */
public abstract class NetActivity extends BaseActivity {

    protected Retrofit mRetrofit;
    protected RequestService mRequestService;

    /**
     * @param savedInstanceState
     * description: 子线程去跑网络请求
     */
    @Override
    protected void beforeInit(final Bundle savedInstanceState) {
        // 这里封装只是举个例子,实际直接在Presenter中使用Retrofit就行了
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build();

        mRequestService = mRetrofit.create(RequestService.class); //这里采用的是Java的动态代理模式，把请求方式写这里
        // retrofit2 网络请求，写在netInit中，子类直接mRequestService调方法
        netInit(savedInstanceState);
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     * @param savedInstanceState
     */
    protected abstract void netInit(Bundle savedInstanceState);
}
