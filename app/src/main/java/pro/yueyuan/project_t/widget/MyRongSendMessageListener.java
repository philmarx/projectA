package pro.yueyuan.project_t.widget;

import com.orhanobut.logger.Logger;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;

/**
 * Created by Key on 2017/3/30 22:24
 * email: MrKey.K@gmail.com
 * description:
 */

public class MyRongSendMessageListener implements RongIM.OnSendMessageListener {
    @Override
    public Message onSend(Message message) {
        Logger.w(message.getSentStatus().name() + "  " + message.getSentStatus().getValue());
        return null;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        Logger.d(sentMessageErrorCode.getMessage());
        return false;
    }
}
