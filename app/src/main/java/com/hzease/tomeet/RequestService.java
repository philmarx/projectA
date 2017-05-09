package com.hzease.tomeet;

import com.hzease.tomeet.data.ActivityTypeBean;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.FeedBackBean;
import com.hzease.tomeet.data.FriendListBean;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.OssInfoBean;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.data.SearchCircleBean;
import com.hzease.tomeet.data.ShowGameListBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.UpdatePwdBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.UserOrderBean;
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
    @POST("friend/findAllFriends")
    Observable<FriendListBean> getFriendList(@Query("userId") String userId, @Query("token") String token);

    /**
     * get请求
     * @param path 路径
     * @return
     */
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
    @POST("room/createRoom")
    Observable<NoDataBean> createRoom(@Query("beginTime") String beginTime, @Query("description") String description, @Query("belongCircle") Integer belongCircle,
                                     @Query("endTime") String endTime,@Query("city") String city, @Query("manCount") Integer manCount,@Query("latitude") double latitude,
                                      @Query("longitude") double longitude, @Query("memberCount") Integer memberCount, @Query("money") Integer money,
                                     @Query("name") String name, @Query("password") String password, @Query("place") String place,
                                     @Query("token") String token, @Query("userId") String userId, @Query("womanCount") Integer womanCount,
                                     @Query("gameId") Integer gameId,@Query("open") boolean open, @Query("gameMode") int gameMode);

    /**
     * 短信登录
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
     * @return
     */
    @POST("user/loginBySmsCode")
    Observable<LoginBean> login4sms(@Query("phone") String phone, @Query("smsCode") String smsCode);

    /**
     * 获取账号信息
     */
    @POST("user/findByToken")
    Observable<UserInfoBean> getMyInfomation(@Query("token") String token, @Query("userId") String userId);

    /**
     * @param gender   性别
     * @param nickname 昵称
     * @param password 密码
     * @param token    自己token
     * @param userId   自己的id
     * @return 成功后用户对象
     */
    @POST("user/initInfo")
    Observable<UserInfoBean> finishInfo(@Query("gender") boolean gender, @Query("nickname") String nickname,
                                        @Query("password") String password, @Query("token") String token,
                                        @Query("userId") String userId);

    /**
     * 获取我加入的房间信息
     */
    @POST("room/findMyJoinRooms")
    Observable<HomeRoomsBean> getMyRooms(@Query("page") String page, @Query("size") String size, @Query("token") String token, @Query("userId") String userId);

    /**
     * 显示活动分类
     */
    @POST("game/list")
    Observable<ShowGameListBean> getGameList(@Query("key") String key, @Query("value") String value);

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
    Observable<HomeRoomsBean> getRoomsByGameOrder(@Query("city")String city,@Query("gameId") Integer gameId, @Query("games") String games, @Query("latitude") double latitude,
                                                  @Query("longitude") double longitude, @Query("page") Integer page, @Query("size") Integer size,
                                                  @Query("sort") String sort, @Query("state") Integer state);

    /**
     * 第三方登录
     */
    @POST("user/authLogin")
    Observable<LoginBean> authLogin(@Query("type") String type, @Query("uid") String uid);

    /**
     * 修改登录密码
     */
    @POST("user/updatePassword")
    Observable<UpdatePwdBean> updatePwd(@Query("password") String password, @Query("password2") String password2, @Query("token") String token, @Query("userId") String userId);

    /**
     * 提交反馈
     */
    @POST("user/suggest")
    Observable<FeedBackBean> feedBack(@Query("content") String content, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查找圈子
     * @param name
     * @param page
     * @param size
     * @return
     */
    @POST("circle/findByName")
    Observable<SearchCircleBean> searchCircle(@Query("name") String name,@Query("page") Integer page,@Query("size") Integer size);

    /**
     * 保存头像图片等
     * @param userId id
     * @param userToken token
     * @param which 哪一个图片
     * @param signature 图片签名（用当前毫秒值）
     * @return success
     */
    @POST("photo/save")
    Observable<NoDataBean> updateImageSignature(@Query("userId")String userId, @Query("token")String userToken, @Query("which")String which, @Query("signature")String signature);

    /**
     * 创建圈子
     * @param avatarSignature
     * @param bgSignature
     * @param city
     * @param latitude
     * @param longitude
     * @param name
     * @param notice
     * @param place
     * @param token
     * @param userId
     * @return
     */
    @POST("circle/create")
    Observable<UpdatePwdBean> createCircle(@Query("avatarSignature") String avatarSignature,@Query("bgSignature") String bgSignature,@Query("city") String city,
                                           @Query("latitude") double latitude,@Query("longitude") double longitude,@Query("name") String name,@Query("notice") String notice,
                                           @Query("place") String place,@Query("token") String token,@Query("userId") String userId);

    /**
     * 查看喊话
     */
    @POST("declaration/findDeclaration")
    Observable<CommentItemBean> getDeclaration(@Query("city") String city,@Query("page") String page,@Query("size") String size);

    /**
     * 创建喊话
     */
    @POST("declaration/declare")
    Observable<NoDataBean> declare(@Query("city") String city,@Query("content") String content,@Query("token") String token,@Query("userId") String userId);

    /**
     * 实名认证
     */
    @POST("user/authorized")
    Observable<NoDataBean> authorized(@Query("idCard") String idCard,@Query("realName") String realName,@Query("token") String token,@Query("userId") String userId);

    /**
     * 修改密码
     */
    @POST("user/updatePassword")
    Observable<NoDataBean> changePwd(@Query("password") String password,@Query("password2") String password2,@Query("token") String token,@Query("userId") String userId);

    /**
     * 推荐圈子
     */
    @POST("circle/findRecommand")
    Observable<CircleInfoBean> findRecommand();

    /**
     * 附近圈子
     */
    @POST("circle/findCircleNearby")
    Observable<CircleInfoBean> findNearBy(@Query("latitude") double latitude,@Query("longitude") double longitude);

    /**
     * 查看房间结束结果
     */
    @POST("room/findRoomResult")
    Observable<GameFinishBean> gameFinishInfo(@Query("roomId") long roomId);

    /**
     * 加入房间
     * @param token
     * @param userId
     * @param roomId
     * @param password
     * @return
     */
    @POST("room/joinRoom")
    Observable<NoDataBean> joinRoom(@Query("token") String token, @Query("userId")String userId, @Query("roomId")String roomId, @Query("password")String password);

    /**
     * 离开房间，不是退出
     * @param token
     * @param userId
     * @param roomId
     * @return
     */
    @POST("room/leave")
    Observable<NoDataBean> leaveRoom(@Query("token") String token, @Query("userId")String userId, @Query("roomId")String roomId);

    @POST("room/findRoom")
    Observable<GameChatRoomBean> getGameChatRoomInfo(@Query("roomId")String roomId);
}
