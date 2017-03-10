package pro.yueyuan.project_t;

import pro.yueyuan.project_t.data.ActivityTypeBean;
import pro.yueyuan.project_t.data.OssInfoBean;
import pro.yueyuan.project_t.data.UserInfoBean;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Key on 2016/11/23 00:16
 * email: MrKey.K@gmail.com
 */

public interface RequestService {

    @GET("{url_path}")
    Call<String> getString(@Path("url_path") String path);

    /**
     * 登录 获取token
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<UserInfoBean> login(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/getSmsCode")
    Observable<UserInfoBean> getSMSCode(@Field("phone") String phone);

    /**
     * 获取 OSS鉴权TOKEN
     */
    @FormUrlEncoded
    @POST("photo/getToken")
    Call<OssInfoBean> getOssInfo(@Field("userId") String userId, @Field("token") String token);
    /**
     * 获取两个参数的返回字符串
     */
    @FormUrlEncoded
    @POST("{url_path}")
    Observable<String> getString(@Path("url_path") String path, @Field("args1") String args1, @Field("args2") String args2);
    /**
     * 获取所有活动种类
     */
    @FormUrlEncoded
    @POST("game/list")
    Observable<ActivityTypeBean> getActivityType(@Field("key") String key, @Field("value") String value);
    /**
     * 创建房间
     */
    @FormUrlEncoded
    @POST("/room/createRoom")
    Observable<UserInfoBean> createRoom(@Field("beginTime") String beginTime,@Field("description") String description,@Field("endTime") String endTime,
                                        @Field("manCount") String manCount,@Field("memberCount") String memberCount,@Field("money") String money,
                                        @Field("name") String name,@Field("password") String password,@Field("place") String place,
                                        @Field("token") String token,@Field("userId") String userId,@Field("womanCount") String womanCount,
                                        @Field("gameId") String gameId);

}
