package pro.yueyuan.project_t.utils;

import io.rong.imlib.model.MessageContent;

/**
 * Created by Key on 2017/3/29 09:40
 * email: MrKey.K@gmail.com
 * description:
 */

public class MyTextUtils {
    public static String getMessageToString(MessageContent messageContent) {
        return new String(messageContent.encode()).substring(12).split("\",\"")[0];
    }
}
