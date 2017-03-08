package pro.yueyuan.project_t.data.source;

import android.support.annotation.NonNull;

/**
 * Created by Key on 2016/11/28 22:34
 * email: MrKey.K@gmail.com
 * description:
 */

public interface IPTDataSource {

    /**
     * 获取头像
     * @param userId 用户ID
     */
    void getAvatar(@NonNull String userId);
}
