package pro.yueyuan.project_t.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pro.yueyuan.project_t.AppConstants;

/**
 * Created by Key on 2017/2/28 19:36
 * email: MrKey.K@gmail.com
 * description:
 */

public class SMSCodeUtils {
    public static OkHttpClient sendSMSCode() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", AppConstants.YY_PT_BASE64_AUTH)
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient;
    }
}
