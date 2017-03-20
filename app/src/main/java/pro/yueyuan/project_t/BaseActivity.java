package pro.yueyuan.project_t;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongBaseActivity;

/**
 * Created by Key on 2016/10/10 14:57
 * email: MrKey.K@gmail.com
 * description: 第一层基类
 */

public abstract class BaseActivity extends AutoLayoutActivity{
    private ViewFlipper mContentView;

    private Unbinder unbinder;

    private boolean backFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        //隐藏掉整个ActionBar
        getSupportActionBar().hide();
        //启动activity时 不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 竖屏显示，不能转动
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        unbinder = ButterKnife.bind(this);
        // 因为有延迟，先在子线程请求网络数据，拿到后初始化，不影响主线程的本地数据初始化
        beforeInit(savedInstanceState);
        // 先初始化本地数据和布局
        initLayout(savedInstanceState);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * @return 返回布局文件ID
     */
    protected abstract int getContentViewId();

    /**
     *TODO 初始化布局文件
     */
    protected abstract void initLayout(Bundle savedInstanceState);

    /**
     *TODO 加载网络数据
     */
    protected abstract void beforeInit(Bundle savedInstanceState);

    /**
     * 重写返回键,两次返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(backFlag){
            //退出
            super.onBackPressed();
        }else{
            //单击一次提示信息
            Toast.makeText(this, "双击退出", Toast.LENGTH_SHORT).show();
            backFlag=true;
            new Thread(){
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //3秒之后，修改flag的状态
                    backFlag=false;
                }
            }.start();
        }
    }
}
