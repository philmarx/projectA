package pro.yueyuan.project_t.login.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.UserInfoBean;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends NetActivity {

    //微信登录
    private static IWXAPI WXapi;
    @BindView(R.id.wxLogin)
    Button wxLogin;
    @BindView(R.id.password)
    EditText mPwd;
    @BindView(R.id.userName)
    EditText mUserName;
    private String WX_APP_ID = "wx7c783e373c89fc39";
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.login_getSMScode)
    EditText getSmsCode;
    @BindView(R.id.get_sms)
    Button getsmsCode;


    private String userName;
    private String Pwd;




    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });*/
        wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXlogin();
            }


        });
        //用户名
        userName=mUserName.getText().toString().trim();
        //密码
        Pwd = mPwd.getText().toString().trim();


        login.setOnClickListener(new View.OnClickListener() {
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
                            }
                        });
            }
        });


    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
        getsmsCode.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    /**
     * 微信登录
     */
    private void WXlogin() {
        WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        WXapi.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        WXapi.sendReq(req);
    }

}
