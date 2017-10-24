package com.hzease.tomeet.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuq on 2017/9/5.
 */

public class TimeUtils {

    public static String getDatas(String datas) {
        try {
            Date dateCreate = new SimpleDateFormat("yyyy-MM-dd").parse(datas);
            if (dateCreate.getYear() != new Date().getYear()) {
                return datas.substring(2);
            }
            long diff = dateCreate.getTime() / 86400000 - ((System.currentTimeMillis()) / 86400000);
            Logger.e("date1" + dateCreate.getTime() / 86400000);
            Logger.e("date2" + (System.currentTimeMillis()) / 86400000);
            //Logger.e("diff" + diff + datas.substring(10));
            Logger.e(diff + "");
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

    public static String calculateTime(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long now = (System.currentTimeMillis() + offSet) / 60000;
        long create = (time + offSet) / 60000;
        long diff = now - create;
        if (diff == 0) {
            return "刚刚";
        } else if (diff < 60) {
            return diff + "分钟前";
        } else if (diff < 1440) {
            return diff / 60 + "小时前";
        } else {
            return diff / 60 / 24 + "天前";
        }
    }
}
