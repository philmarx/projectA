package pro.yueyuan.project_t.login.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.login.DaggerILoginComponent;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.login.LoginPresenter;
import pro.yueyuan.project_t.login.LoginPresenterModule;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class LoginActivity extends NetActivity {

    //微信登录
    private static IWXAPI WXapi;

    private String WX_APP_ID = "wx7c783e373c89fc39";

    @Inject
    LoginPresenter mLoginPresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;



    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            LoginFragment loginFragment = LoginFragment.newInstance();
            FindPwdFragment findPwdFragment = FindPwdFragment.newInstance();
            mFragmentList.add(loginFragment);
            mFragmentList.add(findPwdFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_login_activity);
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0;i<size;i++){
            DaggerILoginComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    // .loginPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                    .loginPresenterModule(new LoginPresenterModule(((ILoginContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }




        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });*/
       /*wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXlogin();
            }


        });*/

    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
    }

    /**
     * 微信登录
     */
/*    private void WXlogin() {
        WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        WXapi.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        WXapi.sendReq(req);
    }*/

}
