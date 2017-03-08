package pro.yueyuan.project_t;

import android.os.Bundle;

/**
 * Created by Key on 2016/10/11 15:17
 * email: MrKey.K@gmail.com
 * description: 网络层基类
 */
public abstract class NetActivity extends BaseActivity {

    public RequestService mRequestService;

    /**
     * @param savedInstanceState
     * description: 子线程去跑网络请求
     */
    @Override
    protected void beforeInit(final Bundle savedInstanceState) {

        // 单例获取
        mRequestService = PTApplication.getRequestService();

        // retrofit2 网络请求，写在netInit中，子类直接mRequestService调方法
        netInit(savedInstanceState);
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     * @param savedInstanceState
     */
    protected abstract void netInit(Bundle savedInstanceState);
}
