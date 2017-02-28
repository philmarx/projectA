package pro.yueyuan.project_t.splash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.RequestService;
import pro.yueyuan.project_t.data.SMSCodeBean;
import pro.yueyuan.project_t.data.SMSPhoneNumberBean;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.utils.SMSCodeUtils;
import pro.yueyuan.project_t.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SplashActivity extends NetActivity {

    @BindView(R.id.tv_version_splash)
    TextView tv_version_splash;
    @BindView(R.id.b_test_splash)
    Button b_test_splash;

    int count = 0;

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        b_test_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.getToast(SplashActivity.this, "测试线程是否卡住 -- " + count++);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {
        final Long start = System.currentTimeMillis();
        mRequestService.getString(AppConstants.YY_PT_BEIJING).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                tv_version_splash.setText(response.body());
                final long dValue = System.currentTimeMillis() - start;
                Logger.d("response: " + response + "--   call: " + call + "--   花时：" + dValue + "毫秒");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (dValue < 3000) {
                            SystemClock.sleep(3000 - dValue);
                        }
                        Logger.d("Splash 总共显示" + (System.currentTimeMillis() - start) + "毫秒");
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tv_version_splash.setText("获取失败");
                Logger.e(t, "  --onFailure-- ", call);
            }
        });


        // sms
        new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_JSMS_URL)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .client(SMSCodeUtils.sendSMSCode())
                .build().create(RequestService.class)
                .getSMSCode(new SMSPhoneNumberBean("18698569593"))
                .enqueue(new Callback<SMSCodeBean>() {
                    @Override
                    public void onResponse(Call<SMSCodeBean> call, Response<SMSCodeBean> response) {
                        Logger.d(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<SMSCodeBean> call, Throwable t) {
                        Logger.d(t);
                    }
                });
    }
}
