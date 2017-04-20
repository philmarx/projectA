package pro.yueyuan.project_t.splash.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.login.ui.LoginActivity;
import pro.yueyuan.project_t.utils.RongCloudInitUtils;
import pro.yueyuan.project_t.utils.ToastUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends NetActivity {

    @BindView(R.id.tv_version_splash)
    TextView tv_version_splash;
    private long startTime;
    private long waitTime = 3000;

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
        startTime = System.currentTimeMillis();
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {
        // 初始化用户.查看本地是否已保存
        final SharedPreferences sp = getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE);
        // 先用临时变量确认本地存的用户名和token还是否有效
        final String userId_temp = sp.getString("userId", "");
        final String userToken_temp = sp.getString("userToken", "");

        Logger.i("SplashActivity读取本地:  \nuserId: " + userId_temp + "\nuserToken: " + userToken_temp);
        if (!TextUtils.isEmpty(userId_temp) && !TextUtils.isEmpty(userToken_temp)) {
            PTApplication.getRequestService().getMyInfomation(userToken_temp, userId_temp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserInfoBean>() {
                        @Override
                        public void onCompleted() {
                            startHome();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean.isSuccess()) {
                                PTApplication.myInfomation = userInfoBean;
                                Logger.e(PTApplication.myInfomation + "");
                                PTApplication.userId = userId_temp;
                                PTApplication.userToken = userToken_temp;
                                // 融云
                                new RongCloudInitUtils().RongCloudInit();
                            } else {
                                Logger.e("登录失败: " + userInfoBean.getMsg());
                                ToastUtils.getToast(SplashActivity.this, "登录失效,请重新登录");
                                // 清楚本地记录
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear().apply();
                            }
                        }
                    });
        } else {
            startHome();
        }
    }

    public void startHome() {
        // 初始化完个人信息后计算下时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 最后睡,睡完结束
                long endTime = System.currentTimeMillis() - startTime;
                if (endTime < waitTime) {
                    SystemClock.sleep(waitTime - endTime);
                }
                Logger.w("初始化用了: " + endTime + "\n总共用时: " + (System.currentTimeMillis() - startTime));
                // 决定去向
                if (PTApplication.myInfomation == null || PTApplication.myInfomation.getData().isIsInit()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    // 先去初始化
                    startActivityForResult(new Intent(SplashActivity.this, LoginActivity.class), AppConstants.YY_PT_NAVIGATION_ME_REQUEST_CODE);
                }
                finish();
            }
        }).start();
    }
}
