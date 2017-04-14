package pro.yueyuan.project_t.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.GlobalInfo;


public class DialogManager {
    private static DialogManager dialog;

    public static DialogManager getInstance() {
        if (dialog == null) {
            dialog = new DialogManager();
        }
        return dialog;
    }

    private OnDialogClickListener linstener;


    public interface OnDialogClickListener {
        /**
         * 其他点击
         */
        void onOtherClickListener(View v);

        /**
         * 点击左边按钮
         */
        void onLeftClickListener(View v);

        /**
         * 点击右边按钮
         */
        void onRightClickListener(View v);

    }

    /**
     * 权限设置 dialog
     *
     * @param activity
     * @param message
     * @param left
     * @param right
     * @param linstener
     */
    public Dialog ShowPermissionsDialog(Activity activity, String message, String left, String right, final OnDialogClickListener linstener) {
        final Dialog dialog = new Dialog(activity, R.style.no_title);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_permission, null);

        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
        TextView tvLeft = (TextView) dialogView.findViewById(R.id.tvLeft);
        TextView tvRight = (TextView) dialogView.findViewById(R.id.tvRight);

        tvMessage.setText(message);
        tvLeft.setText(left);
        tvRight.setText(right);

        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (GlobalInfo.getInstance(activity).deviceWidth* 0.8); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(lp);

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linstener == null) return;
                linstener.onLeftClickListener(v);
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linstener == null) return;
                linstener.onRightClickListener(v);
            }
        });
        return dialog;
    }

}
