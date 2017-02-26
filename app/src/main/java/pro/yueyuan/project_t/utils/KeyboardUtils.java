package pro.yueyuan.project_t.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Key on 2016/12/21 13:53
 * email: MrKey.K@gmail.com
 * description:
 */

public class KeyboardUtils {

    /**
     * 打开键盘
     * @param context
     * @param editText
     */
    public static void openKeyboard(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
