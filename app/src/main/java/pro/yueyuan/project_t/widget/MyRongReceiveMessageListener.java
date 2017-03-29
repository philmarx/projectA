package pro.yueyuan.project_t.widget;

import com.orhanobut.logger.Logger;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Key on 2017/3/27 16:56
 * email: MrKey.K@gmail.com
 * description:
 */

public class MyRongReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
    @Override
    public boolean onReceived(Message message, int left) {
        Logger.e("融云监听设置  " + left + "  message : " + new String(message.getContent().encode()));
        return false;
    }
}
