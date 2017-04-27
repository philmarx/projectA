package com.hzease.tomeet.data;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

public class GlobalInfo {
    private static GlobalInfo globalInfo;

    public float deviceHeight;
    public float deviceWidth;
    public static float px2dp;
    public float px2sp;
    public boolean homeLockState=true;

    public static GlobalInfo getInstance(Activity activity) {
        if (globalInfo == null) {
            globalInfo = new GlobalInfo(activity);
        }
        return globalInfo;
    }

    @SuppressWarnings("deprecation")
    public GlobalInfo(Activity activity) {
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            deviceWidth = display.getWidth();
            deviceHeight = display.getHeight();
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
            deviceHeight = size.y;
        }
        px2dp = activity.getResources().getDisplayMetrics().density;
        px2sp = activity.getResources().getDisplayMetrics().scaledDensity;
        homeLockState = true;
    }
}
