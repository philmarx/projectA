package pro.yueyuan.project_t.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import javax.inject.Singleton;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.source.IPTDataSource;

import static android.content.Context.MODE_PRIVATE;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2016/11/28 22:37
 * email: MrKey.K@gmail.com
 * description: 本地数据
 */
@Singleton
public class PTLocalDataSource implements IPTDataSource {

    private Context mContext;

    public PTLocalDataSource(@NonNull Context context) {
        mContext = checkNotNull(context);
    }

    /**
     * 获取头像
     *
     * @param userId 用户ID
     */
    @Override
    public void getAvatar(@NonNull String userId) {

    }

    /**
     * 保存用户名密码,直接从PTApplication里面读,不需要传参数
     */
    @Override
    public void saveUserIdAndToken() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE).edit();
        editor.putString("userId", PTApplication.userId);
        editor.putString("userToken", PTApplication.userToken);
        editor.apply();
        Logger.d("保存成功");
    }

    /**
     * 读取用户信息,先本地,后网络
     */
    @Override
    public void loadUserIdAndToken() {
        SharedPreferences sp = mContext.getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE);
        PTApplication.userId = sp.getString("userId", "");
        PTApplication.userToken = sp.getString("userToken", "");
        Logger.d("读取到本地:  userId: " + PTApplication.userId + " -- userToken: " + PTApplication.userToken);
    }
}
