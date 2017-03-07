package pro.yueyuan.project_t.splash.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.utils.OssUtils;

public class SplashActivity extends NetActivity {

    @BindView(R.id.tv_version_splash)
    TextView tv_version_splash;

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
        // 停止1.5秒
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
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
        // 初始化用户.查看本地是否已保存
        SharedPreferences sp = getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE);
        // 如果用户没登录,用一个临时用户初始化三个key去获取OSS TOKEN
        PTApplication.userId = sp.getString("userId", "");
        PTApplication.userToken = sp.getString("userToken", "");
        Logger.d("userId: " + PTApplication.userId + " -- userToken: " + PTApplication.userToken);

        // 初始化OSS,
        OssUtils.aliyunOssInit();

        // version

    }


}
