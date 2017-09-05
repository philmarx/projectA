package com.hzease.tomeet.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuq on 2017/9/5.
 */

public class TimeUtils {

    public static String getDatas(String datas) {
        try {
            Date dateCreate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datas);
            if (dateCreate.getYear() != new Date().getYear()) {
                return datas.substring(2);
            }
            long diff = dateCreate.getTime() / 86400000 - (System.currentTimeMillis()+ 3600 * 8) / 86400000;
            Logger.e("diff" + diff + datas.substring(10));
            switch ((int) diff) {
                case 0:
                    return "今天" + datas.substring(10);
                case 1:
                    return "明天" + datas.substring(10);
                case 2:
                    return "后天" + datas.substring(10);
                default:
                    return datas.substring(5);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return datas.substring(2);
        }
    }
}
