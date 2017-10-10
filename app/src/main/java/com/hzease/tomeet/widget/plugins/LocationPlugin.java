package com.hzease.tomeet.widget.plugins;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.amap.api.location.AMapLocation;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.utilities.PermissionCheckUtil;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ShareLocationActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by xuq on 2017/3/16.*/



public class LocationPlugin implements IPluginModule{
    private double mLongitude;
    private double mLatitude;
    private String cityCode;
    private String cityName;
    public LocationPlugin() {
    }
/**
     * 设置展示的图标
     * @param context
     * @return*/
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, io.rong.imkit.R.drawable.rc_ext_plugin_location_selector);
    }

/**
     * 设置图标下的title
     * @param context
     * @return*/


    @Override
    public String obtainTitle(Context context) {
        return context.getString(io.rong.imkit.R.string.rc_plugin_location);
    }

/**
     * 在这里做跳转*/


    @Override
    public void onClick(final Fragment currentFragment, final RongExtension extension) {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                cityCode = aMapLocation.getCityCode();
                cityName = aMapLocation.getCity();
            }
        });
        String[] permissions = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_NETWORK_STATE"};
        if(PermissionCheckUtil.requestPermissions(currentFragment, permissions)) {

            /*Intent result = new Intent(currentFragment.getActivity(), ShareLocationActivity.class);
            result.putExtra("lon",mLongitude);
            result.putExtra("lat",mLatitude);
            result.putExtra("cityCode",cityCode);
            result.putExtra("cityName",cityName);
            extension.startActivityForPluginResult(result, 1, LocationPlugin.this);*/


            String[] items = new String[]{currentFragment.getString(io.rong.imkit.R.string.rc_plugin_location_message)};
            OptionsPopupDialog.newInstance(currentFragment.getActivity(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    Logger.e("lon"+mLongitude);
                    Logger.e("lat"+mLatitude);
                    Logger.e("cityCode"+cityCode);
                    Logger.e("cityName"+cityName);
                    if(which == 0) {
                        Intent result = new Intent(currentFragment.getActivity(), ShareLocationActivity.class);
                        result.putExtra("lon",mLongitude);
                        result.putExtra("lat",mLatitude);
                        result.putExtra("cityCode",cityCode);
                        result.putExtra("cityName",cityName);
                        extension.startActivityForPluginResult(result, 2, LocationPlugin.this);
                    }
                }
            }).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
