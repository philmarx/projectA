package pro.yueyuan.project_t.login.ui;

import android.content.SharedPreferences;

import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;

/**
 * Created by Key on 2017/3/6 20:58
 * email: MrKey.K@gmail.com
 * description: 在生命中期的销毁中,把id和token写进SP
 */

public abstract class LoginBaseActivity extends NetActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE).edit();
        editor.putString("userId", PTApplication.userId);
        editor.putString("userToken", PTApplication.userToken);
        editor.apply();
    }
}
