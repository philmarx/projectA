package pro.yueyuan.project_t.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static Toast toast = null;
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
            toast = Toast.makeText(context,"", Toast.LENGTH_SHORT);
        }
        toast.setText(s);
        toast.show();
    }
}
