package pro.yueyuan.project_t.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;


import java.util.ArrayList;

public class PermissionTools {

    private static SimpleArrayMap<Activity, PermissionsCallBack> permissionsCallBackMap = new SimpleArrayMap<>();

    // 状态码、标志位
    public static final int REQUEST_STATUS_CODE = 10;
    public static final int REQUEST_PERMISSION_SETTING = 11;
    private static Dialog dialog;

    //常量字符串数组，将需要申请的权限写进去，同时必须要在Androidmanifest.xml中声明。
    private static String[] PERMISSIONS_GROUP = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.VIBRATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.GET_TASKS,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.BROADCAST_STICKY,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK
    };

    /**
     * 初始化先系那个所有的权限都初始化一遍，一般在MainActivity 调用
     *  @param activity
     * @param des
     * @param callBack
     */
    public static void initPermissions(Activity activity, String des, String[] PERMISSSONS, PermissionsCallBack callBack) {
        if (callBack != null) permissionsCallBackMap.put(activity, callBack);
        // 一个list，用来存放没有被授权的权限
        ArrayList<String> denidArray = new ArrayList<>();
        // 遍历PERMISSIONS_GROUP，将没有被授权的权限存放进denidArray
        String[] permissons = PERMISSIONS_GROUP;
        if (PERMISSSONS != null && PERMISSSONS.length > 0) {
            permissons = PERMISSSONS;
        }
        for (String permission : permissons) {
            int grantCode = ContextCompat.checkSelfPermission(activity, permission);
            if (grantCode == PackageManager.PERMISSION_DENIED) {  // 被拒绝
                denidArray.add(permission);
            }else if (grantCode == PackageManager.PERMISSION_GRANTED){
                //  有权限
                callBack.callback(true);
            }
        }
        // 将denidArray转化为字符串数组，方便下面调用requestPermissions来请求授权
        String[] denidPermissions = denidArray.toArray(new String[denidArray.size()]);

        // 如果该字符串数组长度大于0，说明有未被授权的权限
        if (denidPermissions.length > 0) {
            // 遍历denidArray，用showRationaleUI来判断，每一个没有得到授权的权限是否是用户手动拒绝的
            for (String permission : denidArray) {
                if (!showRationaleUI(activity, permission)) {
                    // 判断App是否是首次启动
                    if (!isAppFirstRun(activity)) {  // 不是第一次
                        showDialog(activity,des);
                    }
                }
                break;
            }
            requestPermissions(activity, denidPermissions);
        }
    }

    private static void showDialog(final Activity activity, String des) {
            dialog = DialogManager.getInstance().ShowPermissionsDialog(activity, des, "无情拒绝", "现在设置", new DialogManager.OnDialogClickListener() {
                @Override
                public void onOtherClickListener(View v) {
                }

                @Override
                public void onLeftClickListener(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (permissionsCallBackMap.containsKey(activity)){
                        permissionsCallBackMap.get(activity).callback(false);
                    }
                }

                @Override
                public void onRightClickListener(View v) {
                    // 进入App设置页面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                }
            });
        dialog.show();
    }

    /**
     * 关于shouldShowRequestPermissionRationale函数的一点儿注意事项：
     * ***1).应用安装后第一次访问，则直接返回false；
     * ***2).第一次请求权限时，用户Deny了，再次调用shouldShowRequestPermissionRationale()，则返回true；
     * ***3).第二次请求权限时，用户Deny了，并选择了“never ask again”的选项时，再次调用shouldShowRequestPermissionRationale()时，返回false；
     * ***4).设备的系统设置中，禁止了应用获取这个权限的授权，则调用shouldShowRequestPermissionRationale()，返回false。
     */
    public static boolean showRationaleUI(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 对权限字符串数组中的所有权限进行申请授权，如果用户选择了“never ask again”，则不会弹出系统的Permission申请授权对话框
     */
    public static void requestPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_STATUS_CODE);
    }


    /**
     * 用来判断，App是否是首次启动： true 表示第一次 ，false  表示不是第一次
     * ***由于每次调用shouldShowRequestPermissionRationale得到的结果因情况而变，因此必须判断一下App是否首次启动，才能控制好出现Dialog和SnackBar的时机
     */
    public static boolean isAppFirstRun(Activity activity) {
        //  默认返回false  表示第一次
        boolean isFrist = SpUtils.getBooleanValue(activity, "first_run");
        if (!isFrist) {  // 第一次启动app
            SpUtils.saveBoolean(activity, "first_run", true);
            return true;
        } else {  // 不是第一次启动app
            SpUtils.saveBoolean(activity, "first_run", true);
            return false;
        }
    }

    /**
     * 移除全校接口对象
     *
     * @param activity
     */
    public static void removePermission(Activity activity) {
        permissionsCallBackMap.remove(activity);
    }

    public static void notify(Activity activity, boolean isAllow) {
        if (permissionsCallBackMap.containsKey(activity)) {
            permissionsCallBackMap.get(activity).callback(isAllow);
        }
    }

    /**
     * 权限操作回调接口
     */
    public interface PermissionsCallBack {
        /**
         * 是否被允许
         *
         * @param Allow
         */
         void callback(boolean Allow);
    }
}
