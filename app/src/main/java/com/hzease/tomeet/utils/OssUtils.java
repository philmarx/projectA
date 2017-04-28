package com.hzease.tomeet.utils;

import android.widget.ImageView;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.OssInfoBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/3/6 18:31
 * email: MrKey.K@gmail.com
 * description:
 */

public class OssUtils {

    private PutObjectRequest putObjectRequest;
    private String mImageName;

    private void checkInit() {
        // 判断对象是否已经初始化
        if (PTApplication.aliyunOssExpiration < System.currentTimeMillis() || PTApplication.aliyunOss == null) {
            // 过期 , 或者 没初始化
            Logger.e("没初始化过的:  " + PTApplication.aliyunOssExpiration);
            aliyunOssInit(true);
        } else {
            Logger.e("OSS对象可用的:  " + PTApplication.aliyunOssExpiration);
            // OSS对象可用的
            uploadEverything();
        }
    }

    /**
     * oss 对象初始化,如果有挂起任务,初始化结束后执行
     */
    private void aliyunOssInit(final boolean isHaveTask) {

        if (PTApplication.userId.isEmpty() || PTApplication.userToken.isEmpty()) {
            ToastUtils.getToast(PTApplication.getInstance(), "上传服务初始化失败,请确认是否登录");
            PTApplication.aliyunOss = null;
            PTApplication.aliyunOssExpiration = 0;
            return;
        }

        PTApplication.getRequestService().getOssInfo(PTApplication.userId, PTApplication.userToken)
                .enqueue(new Callback<OssInfoBean>() {
                    @Override
                    public void onResponse(Call<OssInfoBean> call, Response<OssInfoBean> response) {
                        OssInfoBean.DataBean ossInfo = response.body().getData();
                        Logger.d("过期时间:  " + ossInfo.getExpiration() + "\nId: " + ossInfo.getAccessKeyId() + "\nSecret: " + ossInfo.getAccessKeySecret() + "\nSecurityToken: " + ossInfo.getSecurityToken());
                        PTApplication.aliyunOss = getOSS(ossInfo.getAccessKeyId(), ossInfo.getAccessKeySecret(), ossInfo.getSecurityToken());
                        // 设置过期时间为40分钟后
                        PTApplication.aliyunOssExpiration = System.currentTimeMillis() + (40 * 60 * 1000);
                        // 如果有等待任务,去执行等待任务
                        // Logger.e("是否执行挂起任务: " + isHaveTask);
                        if (isHaveTask) {
                            uploadEverything();
                        }
                    }

                    @Override
                    public void onFailure(Call<OssInfoBean> call, Throwable t) {
                        Logger.e(t.getMessage());
                        ToastUtils.getToast(PTApplication.getInstance(), "上传服务初始化失败,请稍候重试");
                        PTApplication.aliyunOss = null;
                        PTApplication.aliyunOssExpiration = 0;
                    }
                });
    }

    /**
     * 初始化OSS对象
     * @param ossAccessKeyId     !
     * @param ossAccessKeySecret !
     * @param ossSecurityToken   !
     * @return OSS
     */
    private OSS getOSS(String ossAccessKeyId, String ossAccessKeySecret, String ossSecurityToken) {
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
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     * @param imagePath 图片上传路径，用常量
     * @param imageView 需要设置显示图片的控件
     */
    public void setImageToHeadView(String imagePath, ImageView imageView) {
        File file = new File(PTApplication.imageLocalCache.getPath());
        if (file.exists() && file.length() > 0) {
            // 上传头像
            this.uploadAvatar(imagePath, PTApplication.imageLocalCache.getPath());
            // 上传头像签名
            mImageName = imagePath.replaceFirst("/", "");

            // 加载头像
            Glide.with(imageView.getContext())
                    .load(PTApplication.imageLocalCache)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        } else {
            ToastUtils.getToast(PTApplication.getInstance(), "上传失败");
            Logger.e("上传失败");
        }
    }

    /**
     * 上传用户图片
     * 使用Application中的OSS对象和userId
     * @param imageName 存储在OSS的文件名
     * @param imagePath 文件的本地路径
     */
    public void uploadAvatar(final String imageName, final String imagePath) {
        // 构造上传请求,第一个参数是bucketName,第二个参数ObjectName,第三个参数本地图片路径
        // 头像是"/avatar"
        putObjectRequest = new PutObjectRequest(AppConstants.YY_PT_OSS_NAME, AppConstants.YY_PT_OSS_USER_MYSELF + imageName, imagePath);
        Logger.d("imagePath: " + putObjectRequest.getBucketName() + " : "  + putObjectRequest.getObjectKey() + "  UploadFilePath: " + putObjectRequest.getUploadFilePath());
        checkInit();
    }

    /**
     * 上传用户头像
     * 使用Application中的OSS对象和userId,只需要传路径
     * @param imageName 上传的ObjectName
     * @param imageBytes 图片byte数组
     */
    public void uploadAvatar(final String imageName, final byte[] imageBytes) {
        // 构造上传请求,第一个参数是bucketName,第二个参数ObjectName,第三个参数本地图片路径
        putObjectRequest = new PutObjectRequest(AppConstants.YY_PT_OSS_NAME, AppConstants.YY_PT_OSS_USER_MYSELF + imageName, imageBytes);
        Logger.d("imageBytes: " + putObjectRequest.getBucketName() + " : " + putObjectRequest.getObjectKey());
        checkInit();
    }

    /**
     * 上传功能 --- 抽取重载的重复方法
     */
    private void uploadEverything() {
        // 异步上传时可以设置进度回调
        this.putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // TODO: 2017/4/18  到时候加个小细节,在头像上加个上传的百分比
                // Logger.d("currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = PTApplication.aliyunOss.asyncPutObject(this.putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                PTApplication.getRequestService().updateImageSignature(PTApplication.userId, PTApplication.userToken, mImageName, String.valueOf(System.currentTimeMillis()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                                ToastUtils.getToast(PTApplication.getInstance(), "头像修改失败");
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.v(noDataBean.toString());
                                String s = "头像上传失败";
                                if (noDataBean.isSuccess()) {
                                    s = "头像上传成功";
                                }
                                ToastUtils.getToast(PTApplication.getInstance(), s);
                            }
                        });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    Logger.e("clientExcepion: " + clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode: " + serviceException.getErrorCode());
                    Logger.e("RequestId: " + serviceException.getRequestId());
                    Logger.e("HostId: " + serviceException.getHostId());
                    Logger.e("RawMessage: " + serviceException.getRawMessage());
                }
            }
        });

        // task.cancel(); // 可以取消任务

        // task.waitUntilFinished(); // 可以等待直到任务完成
    }

    /**
     * 下载用户头像
     *
     * @param userId    用户ID
     * @deprecated 暂时用不到
     */
    public void downloadAvatar(String userId) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(AppConstants.YY_PT_OSS_NAME, userId + "/avatar");

        // 开始异步下载
        OSSAsyncTask task = PTApplication.aliyunOss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
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
}
