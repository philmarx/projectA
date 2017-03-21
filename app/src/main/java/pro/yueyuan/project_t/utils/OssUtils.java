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
import pro.yueyuan.project_t.data.OssInfoBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public static void aliyunOssInit() {

        String ossUserId;
        String ossUserToken;
        if (PTApplication.userId.isEmpty() || PTApplication.userToken.isEmpty()) {
            ToastUtils.getToast(PTApplication.getInstance(), "图片数据初始化失败");
            return;
        } else {
            ossUserId = PTApplication.userId;
            ossUserToken = PTApplication.userToken;
        }

        PTApplication.getRequestService().getOssInfo(ossUserId, ossUserToken).enqueue(new Callback<OssInfoBean>() {
            @Override
            public void onResponse(Call<OssInfoBean> call, Response<OssInfoBean> response) {
                OssInfoBean.DataBean ossInfo = response.body().getData();
                PTApplication.aliyunOss = getOSS(ossInfo.getAccessKeyId(), ossInfo.getAccessKeySecret(), ossInfo.getSecurityToken());
                Logger.d("过期时间:  " + ossInfo.getExpiration());
            }

            @Override
            public void onFailure(Call<OssInfoBean> call, Throwable t) {
                Logger.e(t.getMessage());
                ToastUtils.getToast(PTApplication.getInstance(), "获取图片数据失败,正在重新获取");
                aliyunOssInit();
            }
        });
    }

    /**
     * 初始化OSS对象
     * @param ossAccessKeyId !
     * @param ossAccessKeySecret !
     * @param ossSecurityToken !
     * @return OSS
     */
    private static OSS getOSS(String ossAccessKeyId, String ossAccessKeySecret, String ossSecurityToken) {
        OSSClient ossClient;

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossAccessKeyId, ossAccessKeySecret, ossSecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        ossClient = new OSSClient(PTApplication.getInstance(), AppConstants.YY_PT_OSS_ENDPOINT_URL, credentialProvider, conf);

        return ossClient;
    }

    /**
     * 下载用户头像
     * @param aliyunOss OSS对象
     * @param userId 用户ID
     * @deprecated 暂时用不到
     */
    public static void downloadAvatar(OSS aliyunOss, String userId) {
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

    /**
     * 上传用户头像
     * @param aliyunOss OSS对象
     * @param userId 用户ID
     * @param imagePath 文件的本地路径
     */
    public static void uploadAvatar(OSS aliyunOss, String userId, String imagePath) {
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
