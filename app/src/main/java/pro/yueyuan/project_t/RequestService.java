package pro.yueyuan.project_t;

import pro.yueyuan.project_t.data.ActivityTypeBean;
import pro.yueyuan.project_t.data.FinishInfoBean;
import pro.yueyuan.project_t.data.FriendListBean;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.LoginBean;
import pro.yueyuan.project_t.data.MyAmountInfoBean;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.OssInfoBean;
import pro.yueyuan.project_t.data.RankingBean;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.data.StringDataBean;
import pro.yueyuan.project_t.data.UserOrderBean;
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

    /**
     * 获取好友列表
     *
     * @param userId 用户ID
     * @param token  用户token
     * @return 好友列表
     */
    @POST("friend/findFriends")
    Observable<FriendListBean> getFriendList(@Query("userId") String userId, @Query("token") String token);

    @GET("{url_path}")
    Call<String> getString(@Path("url_path") String path);

    /**
     * 登录 获取token
     *
     * @return
     */
    @POST("user/login")
    Observable<LoginBean> login(@Query("phone") String phone, @Query("password") String password);

    /**
     * 获取手机验证码
     *
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
    Observable<LoginBean> createRoom(@Query("beginTime") String beginTime, @Query("description") String description, @Query("endTime") String endTime,
                                        @Query("manCount") String manCount, @Query("memberCount") String memberCount, @Query("money") String money,
                                        @Query("name") String name, @Query("password") String password, @Query("place") String place,
                                        @Query("token") String token, @Query("userId") String userId, @Query("womanCount") String womanCount,
                                        @Query("gameId") String gameId);

    @POST("user/loginBySmsCode")
    Observable<LoginBean> login4sms(@Query("phone") String phone, @Query("smsCode") String smsCode);

    /**
     * 获取账号信息
     */
    @POST("user/findByToken")
    Observable<MyAmountInfoBean> getNickName(@Query("userId") String token, @Query("token") String userid);

    /**
     * 完善用户信息
     */
    @POST("user/initInfo")
    Observable<FinishInfoBean> finishInfo(@Query("age") Integer age, @Query("gender") boolean gender, @Query("nickname") String nickname,
                                          @Query("password") String password, @Query("place") String place, @Query("token") String token,
                                          @Query("userId") String userId);

    /**
     * 获取我加入的房间信息
     */
    @POST("room/findMyJoinRooms")
    Observable<MyJoinRoomBean> getMyRooms(@Query("page") String page, @Query("size") String size, @Query("token") String token, @Query("userId") String userId);
    /**
     * 显示活动分类
     */
    @POST("game/list")
    Observable<ShowGameListBean> getGameList(@Query("key") String key,@Query("value") String value);

    /**
     * 显示活动排行
     */
    @POST("order/getOrders")
    Observable<RankingBean> getRanking(@Query("gameId") Integer gameId);


    /**
     * 根据userId查看用户的排名信息
     */
    @POST("order/getOrderByUserId")
    Observable<UserOrderBean> getOrderById(@Query("userId") long userId);

    /**
     * 获取所有大厅所有房间
     */
    @POST("room/findRoomsByGameOrder")
    Observable<HomeRoomsBean> getRoomsByGameOrder(@Query("gameId") Integer gameId, @Query("games") String games, @Query("latitude") double latitude,
                                                  @Query("longitude") double longitude, @Query("page") Integer page, @Query("size") Integer size,
                                                  @Query("sort") String sort, @Query("state") Integer state);
}
