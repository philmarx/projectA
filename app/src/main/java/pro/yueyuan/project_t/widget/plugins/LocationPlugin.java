package pro.yueyuan.project_t.widget.plugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

import io.rong.imkit.plugin.location.AMapLocationActivity;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.utilities.PermissionCheckUtil;
import pro.yueyuan.project_t.chat.ui.SendLocationActivity;

/**
 * Created by xuq on 2017/3/16.
 */

public class LocationPlugin implements IPluginModule{


    public LocationPlugin() {
    }
    /**
     * 设置展示的图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, io.rong.imkit.R.drawable.rc_ext_plugin_location_selector);
    }

    /**
     * 设置图标下的title
     * @param context
     * @return
     */
    @Override
    public String obtainTitle(Context context) {
        return context.getString(io.rong.imkit.R.string.rc_plugin_location);
    }

    /**
     * 在这里做跳转
     */
    @Override
    public void onClick(final Fragment currentFragment, final RongExtension extension) {
        String[] permissions = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_NETWORK_STATE"};
        if(PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {
            String[] items = new String[]{currentFragment.getString(io.rong.imkit.R.string.rc_plugin_location_message)};
            OptionsPopupDialog.newInstance(currentFragment.getActivity(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if(which == 0) {
                        Intent result = new Intent(currentFragment.getActivity(), SendLocationActivity.class);
                        extension.startActivityForPluginResult(result, 1, LocationPlugin.this);
                    }
                }
            }).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
