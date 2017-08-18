package com.hzease.tomeet.utils.autoUpdate;

import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;

public class FileHelper {

    public static String getDownloadApkCachePath() {

        String appCachePath = null;


        if (checkSDCard()) {
            appCachePath = Environment.getExternalStorageDirectory() + "/AllenVersionPath/";
        } else {
            appCachePath = Environment.getDataDirectory().getPath() + "/AllenVersionPath/";
        }
        File file = new File(appCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        Logger.e("appCachePath: " + appCachePath + "\nfile: " + file);
        return appCachePath;
    }

    public static boolean checkSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        return sdCardExist;

    }


}