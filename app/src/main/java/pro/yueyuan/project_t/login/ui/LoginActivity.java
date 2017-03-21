package pro.yueyuan.project_t.login.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.login.DaggerILoginComponent;
import pro.yueyuan.project_t.login.ILoginContract;
import pro.yueyuan.project_t.login.LoginPresenter;
import pro.yueyuan.project_t.login.LoginPresenterModule;
import pro.yueyuan.project_t.utils.ActivityUtils;

public class LoginActivity extends LoginBaseActivity {

    //微信登录
    private static IWXAPI WXapi;

    private String WX_APP_ID = "wx7c783e373c89fc39";

    @Inject
    LoginPresenter mLoginPresenter;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;



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
            mFragmentList.add(loginFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_login_activity);
        }

        // dagger2
        DaggerILoginComponent.builder()
                .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                // .loginPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                .loginPresenterModule(new LoginPresenterModule(((ILoginContract.View) (mFragmentList.get(0)))))
                .build().inject(this);
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
        //用户名
       //userName=mUserName.getText().toString().trim();
        //密码
      //Pwd = mPwd.getText().toString().trim();


/*        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestService.login("17702525841","000000")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<UserInfoBean>() {
                            @Override
                            public void onCompleted() {
                                //Log.e("BBB","completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("BBB","error");
                            }

                            @Override
                            public void onNext(UserInfoBean userInfoBean) {
                                String token = userInfoBean.getMsg();
                                Log.e("BBB",token);
                                if(userInfoBean.isSuccess()){
                                    PTApplication.userId = String.valueOf(userInfoBean.getData().getId());
                                    PTApplication.userToken = String.valueOf(userInfoBean.getData().getToken());
                                    startActivity(new Intent(LoginActivity.this,FinishActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });*/


    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
/*        getsmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        mRequestService.getSMSCode("18758183175")
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Observer<UserInfoBean>() {
                                    @Override
                                    public void onCompleted() {
                                    }
                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                    @Override
                                    public void onNext(UserInfoBean userInfoBean) {
                                        Log.e("BBB",userInfoBean.isSuccess()+"");
                                    }
                                });
            }
        });*/
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
