package com.hzease.tomeet.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;

public class ToastUtils {
    private static Toast toast = null;
    private static TextView toastText = null;

    private ToastUtils(){}

    /**
     * Created by Key on 2016/12/21 13:53
     * email: MrKey.K@gmail.com
     * description:超级连弹土司
     * @param context 上下文
     * @param s 弹出的字符串
     */
    public static void getToast(Context context, String s){
        if(toast == null){
            toast = new Toast(PTApplication.getInstance());
            View toastView = View.inflate(PTApplication.getInstance(), R.layout.toast_normal, null);
            toastView.setAlpha(0.85f);
            toastText = toastView.findViewById(R.id.tv_toast);
            toast.setView(toastView);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 180);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toastText.setText(s);
        toast.show();
    }
}
