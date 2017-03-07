package pro.yueyuan.project_t.splash.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.utils.OssUtils;
import pro.yueyuan.project_t.utils.ToastUtils;

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
        // 初始化用户.查看本地是否已保存
        SharedPreferences sp = getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE);
        // 如果用户没登录,用一个临时用户初始化三个key去获取OSS TOKEN
        sp.getString("userId", "2");
        sp.getString("userToken", "kExSh84dD6grsk0F67Had8xD5FGrofxyCE5yNilJQw89VqDq3XvQubOksjC41Wn07B3jXOKOY1ML1bxN1gItCNOlHD2lJLBAdBWFlRD8kO8=");
        // 登陆后重新获取一下token,然后token有效期为1小时.每半小时重新获取一次,所以把这个方法抽出来

        // 初始化OSS,
        PTApplication.aliyunOss = OssUtils.aliyunOssInit();
    }
}
