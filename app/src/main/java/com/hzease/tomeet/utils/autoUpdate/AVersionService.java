package com.hzease.tomeet.utils.autoUpdate;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.hzease.tomeet.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Response;

import static java.util.logging.Level.SEVERE;


public abstract class AVersionService extends Service {
    private VersionParams versionParams;
    public static final String VERSION_PARAMS_KEY = "VERSION_PARAMS_KEY";
    public static final String VERSION_PARAMS_TYPE = "VERSION_PARAMS_TYPE";
    public static final String AUTOMATIC = "AUTOMATIC";
    public static final String MANUAL = "MANUAL";
    private String type;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("onStartCommand   intent: " + intent);
        if (intent != null) {
            type = intent.getStringExtra(VERSION_PARAMS_TYPE);
            Logger.e("更新方式：" + type);
            versionParams = intent.getParcelableExtra(VERSION_PARAMS_KEY);
            requestVersionUrlSync();
        } else {
            ToastUtils.getToast("更新检查失败");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestVersionUrlSync() {
        requestVersionUrl();
    }

    public abstract void onResponses(AVersionService service, String response);

    StringCallback stringCallback = new StringCallback() {
        @Override
        public void onSuccess(String s, Call call, Response response) {
            if (response.isSuccessful()) {
                onResponses(AVersionService.this, s);
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            long pauseTime = versionParams.getPauseRequestTime();
            //不为-1 间隔请求
            if (pauseTime > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestVersionUrlSync();
                    }
                }, pauseTime);
            }

        }
    };

    public VersionParams getVersionParams() {
        return versionParams;
    }

    public void setVersionParams(VersionParams versionParams) {
        this.versionParams = versionParams;
    }

    private void requestVersionUrl() {
        OkGo.init(getApplication());
        OkGo.getInstance().debug("AVersionService", SEVERE, true);
        String url = versionParams.getRequestUrl();
        HttpRequestMethod requestMethod = versionParams.getRequestMethod();
        HttpParams params = versionParams.getRequestParams();
        HttpHeaders headers = versionParams.getHttpHeaders();
        switch (requestMethod) {
            case GET:
                OkGo.get(url).params(params).headers(headers).execute(stringCallback);
                break;
            case POST:
                OkGo.post(url).params(params).headers(headers).execute(stringCallback);
                break;
            case POSTJSON:
                String json = getRequestParams(params);
                if (json != null)
                    OkGo.post(url).upJson(json).headers(headers).execute(stringCallback);
                else
                    OkGo.post(url).headers(headers).execute(stringCallback);
                break;
        }


    }

    private String getRequestParams(HttpParams params) {
        String json;
        JSONObject jsonObject = new JSONObject();
        for (ConcurrentHashMap.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue().get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        json = jsonObject.toString();
        return json;
    }


    public void showVersionDialog(String downloadUrl, String title, String updateMsg) {
        Intent intent = new Intent(getApplicationContext(), versionParams.getCustomDownloadActivityClass());
        if (updateMsg != null)
            intent.putExtra("text", updateMsg);
        if (downloadUrl != null)
            intent.putExtra("downloadUrl", downloadUrl);
        if (title != null)
            intent.putExtra("title", title);
        Logger.e("put versionParams: " + versionParams);
        intent.putExtra(VERSION_PARAMS_KEY, versionParams);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
    }

    public String getType() {
        return type;
    }
}
