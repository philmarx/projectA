package pro.yueyuan.project_t;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import pro.yueyuan.project_t.utils.PermissionTools;


public class PermissionActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PermissionTools.removePermission(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionTools.REQUEST_STATUS_CODE:  // 授权回掉
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    PermissionTools.notify(this,true);
                } else {
                    // Permission Denied
                    PermissionTools.notify(this,false);
                }
                break;
            case PermissionTools.REQUEST_PERMISSION_SETTING:  //  设置系统权限回掉
//                Toast.makeText(PermissionActivity.this, "设置系统权限回掉", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
