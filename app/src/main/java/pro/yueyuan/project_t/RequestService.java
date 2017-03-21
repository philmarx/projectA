package pro.yueyuan.project_t;

import pro.yueyuan.project_t.data.ActivityTypeBean;
import pro.yueyuan.project_t.data.OssInfoBean;
import pro.yueyuan.project_t.data.RoomListBean;
import pro.yueyuan.project_t.data.StringDataBean;
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
    @POST("user/login")
    Observable<UserInfoBean> login(@Query("phone") String phone, @Query("password") String password);

    /**
     * 获取手机验证码
     * @param phone 手机号码
     * @return
     */
    @FormUrlEncoded
    @POST("user/getSmsCode")
    Observable<StringDataBean> getSMSCode(@Field("phone") String phone);

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
    @POST("/room/createRoom")
    Observable<UserInfoBean> createRoom(@Query("beginTime") String beginTime,@Query("description") String description,@Query("endTime") String endTime,
                                        @Query("manCount") String manCount,@Query("memberCount") String memberCount,@Query("money") String money,
                                        @Query("name") String name,@Query("password") String password,@Query("place") String place,
                                        @Query("token") String token,@Query("userId") String userId,@Query("womanCount") String womanCount,
                                        @Query("gameId") String gameId);

    /**
     *
     */
    @POST("/room/findRoomsByGameOrder")
    Observable<RoomListBean> queryRoom(@Query("gameId") String gameId, @Query("latitude") String latitude, @Query("longitude") String longitude,
                                       @Query("page") String page, @Query("size") String size, @Query("sort") String sort);



}
