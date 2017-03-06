package pro.yueyuan.project_t.utils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;

import pro.yueyuan.project_t.AppConstants;
import pro.yueyuan.project_t.PTApplication;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Key on 2017/3/6 18:31
 * email: MrKey.K@gmail.com
 * description:
 */

public class OssUtils {

    /**
     * getToken也在这里初始化好了
     * @return 可以操作OSS的对象
     */
    public static OSS aliyunOssInit() {
        // TODO getToken
        new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build();

        // TODO 等返回json写好后再添加

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(PTApplication.OssAccessKey, PTApplication.OssAccessSecret, PTApplication.OssToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        return new OSSClient(PTApplication.getInstance(), AppConstants.YY_PT_OSS_ENDPOINT_URL, credentialProvider, conf);
    }

    public static void downImage(OSS aliyunOss, String userId) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(AppConstants.YY_PT_OSS_NAME, userId + "/avatar");

        // 开始异步下载
        OSSAsyncTask task = aliyunOss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                Logger.d("文件长度: ", result.getContentLength());

                InputStream inputStream = result.getObjectContent();

                byte[] buffer = new byte[2048];

                int len;

                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        // TODO 处理下载的数据
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常
                if (clientException != null) {
                    // 本地异常如网络异常等
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // task.cancel(); // 可以取消任务

        // task.waitUntilFinished(); // 如果需要等待任务完成

        // TODO 完成后刷新界面

        // GetObjectResult result = task.getResult(); // 阻塞等待结果返回...没事别用这个
    }

    public static void uploadImage(OSS aliyunOss, String userId, String imagePath) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest("projectt", userId + "/avatar", imagePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logger.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = aliyunOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Logger.d("PutObject", "UploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // task.cancel(); // 可以取消任务

        // task.waitUntilFinished(); // 可以等待直到任务完成
    }

}
