package com.hzease.tomeet;

import com.hzease.tomeet.data.AccountBean;
import com.hzease.tomeet.data.ActivityBean;
import com.hzease.tomeet.data.AlipayOrderInfoBean;
import com.hzease.tomeet.data.AppVersionBean;
import com.hzease.tomeet.data.Bind3Part;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CircleMemberBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.CreateRoomBean;
import com.hzease.tomeet.data.DepositBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.EvaluteBean;
import com.hzease.tomeet.data.FeedBackBean;
import com.hzease.tomeet.data.FriendListBean;
import com.hzease.tomeet.data.FriendLocationBean;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.data.HavaBZmoneyRoomBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.InvitationsBean;
import com.hzease.tomeet.data.JoinCircleBean;
import com.hzease.tomeet.data.LeafDetailedBean;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.MoneyDetailsBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.OssInfoBean;
import com.hzease.tomeet.data.PhoneContactBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.data.RefundMoneyData;
import com.hzease.tomeet.data.SearchCircleBean;
import com.hzease.tomeet.data.SimpleGroupInfoBean;
import com.hzease.tomeet.data.SimpleUserInfoBean;
import com.hzease.tomeet.data.SmallPaperBean;
import com.hzease.tomeet.data.StringDataBean;
import com.hzease.tomeet.data.ThreePartBean;
import com.hzease.tomeet.data.UpdatePwdBean;
import com.hzease.tomeet.data.UserGameRankingBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.UserOrderBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.data.WxpayOrderInfoBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
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
     * 忘记密码
     */
    @POST("user/updatePasswordByCode")
    Observable<NoDataBean> forgetPwd(@Query("password") String password, @Query("phone") String phone, @Query("smsCode") String smsCode);

    /**
     * get请求
     *
     * @param path 路径
     */
    @GET("{url_path}")
    Call<String> getString(@Path("url_path") String path);

    /**
     * 登录 获取token
     */
    @POST("user/login")
    Observable<LoginBean> login(@Query("phone") String phone, @Query("password") String password);

    /**
     * 获取手机验证码
     *
     * @param phone 手机号码
     */
    @FormUrlEncoded
    @POST("user/getSmsCode")
    Observable<StringDataBean> getSMSCode(@Field("phone") String phone);


    /**
     * 绑定手机号
     */
    @POST("user/bindPhone")
    Observable<NoDataBean> bindPhone(@Query("phone") String phone, @Query("smsCode") String smsCode, @Query("token") String token, @Query("userId") String userId);

    /**
     * 获取 OSS鉴权TOKEN
     */
    @FormUrlEncoded
    @POST("photo/getToken")
    Call<OssInfoBean> getOssInfo(@Field("userId") String userId, @Field("token") String token);

    /**
     * 获取所有活动种类
     */
    @FormUrlEncoded
    @POST("game/list")
    Observable<GameTypeBean> getActivityType(@Field("secret") String secret);

    /**
     * 创建房间
     */
    @POST("room/createRoom")
    Observable<CreateRoomBean> createRoom(@Query("beginTime") String beginTime, @Query("description") String description, @Query("belongCircle") long belongCircle,
                                          @Query("endTime") String endTime, @Query("city") String city, @Query("manCount") Integer manCount, @Query("latitude") double latitude,
                                          @Query("longitude") double longitude, @Query("memberCount") Integer memberCount, @Query("money") Integer money,
                                          @Query("name") String name, @Query("password") String password, @Query("place") String place,
                                          @Query("token") String token, @Query("userId") String userId, @Query("womanCount") Integer womanCount,
                                          @Query("gameId") Integer gameId, @Query("open") boolean open, @Query("gameMode") Integer gameMode);

    /**
     * 短信登录
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
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
    Observable<UserInfoBean> finishInfo(@Query("birthday") String birthday, @Query("gender") boolean gender, @Query("nickname") String nickname,
                                        @Query("password") String password, @Query("token") String token,
                                        @Query("userId") String userId,@Query("recommenderAccount") String recommenderAccount);

    /**
     * 获取我加入的房间信息
     */
    @POST("room/findMyJoinRooms")
    Observable<MyJoinRoomsBean> getMyRooms(@Query("page") Integer page, @Query("size") Integer size, @Query("token") String token, @Query("userId") String userId);

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
    Observable<HomeRoomsBean> getRoomsByGameOrder(@Query("city") String city, @Query("gameId") String gameId, @Query("games") String games, @Query("latitude") double latitude,
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
    Observable<FeedBackBean> feedBack(@Query("content") String content, @Query("token") String token, @Query("userId") String userId, @Query("photoUrl") String photoUrl);

    /**
     * 查找圈子
     */
    @POST("circle/findByName")
    Observable<SearchCircleBean> searchCircle(@Query("name") String name, @Query("page") Integer page, @Query("size") Integer size);

    /**
     * 保存头像图片等
     *
     * @param userId    id
     * @param userToken token
     * @param which     哪一个图片
     * @param signature 图片签名（用当前毫秒值）
     * @return success
     */
    @POST("photo/save")
    Observable<NoDataBean> updateImageSignature(@Query("userId") String userId, @Query("token") String userToken, @Query("which") String which, @Query("signature") String signature);

    /**
     * 创建圈子
     */
    @POST("circle/create")
    Observable<JoinCircleBean> createCircle(@Query("city") String city,
                                            @Query("latitude") double latitude, @Query("longitude") double longitude, @Query("name") String name, @Query("notice") String notice,
                                            @Query("place") String place, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看喊话
     */
    @POST("declaration/findDeclaration")
    Observable<CommentItemBean> getDeclaration(@Query("city") String city, @Query("page") Integer page, @Query("size") Integer size);

    /**
     * 创建喊话
     */
    @POST("declaration/declare")
    Observable<NoDataBean> declare(@Query("city") String city, @Query("content") String content, @Query("token") String token, @Query("userId") String userId);

    /**
     * 实名认证
     */
    @POST("user/authorized")
    Observable<NoDataBean> authorized(@Query("idCard") String idCard, @Query("realName") String realName, @Query("token") String token, @Query("userId") String userId);

    /**
     * 修改密码
     */
    @POST("user/updatePassword")
    Observable<NoDataBean> changePwd(@Query("password") String password, @Query("password2") String password2, @Query("token") String token, @Query("userId") String userId);

    /**
     * 推荐圈子
     */
    @POST("circle/findRecommand")
    Observable<CircleInfoBean> findRecommand();

    /**
     * 附近圈子
     */
    @POST("circle/findCircleNearby")
    Observable<CircleInfoBean> findNearBy(@Query("latitude") double latitude, @Query("longitude") double longitude);

    /**
     * 我的圈子
     */
    @POST("circle/findMyCircle")
    Observable<CircleInfoBean> findMyCircle(@Query("page") Integer page, @Query("size") Integer size, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看房间结束结果
     */
    @POST("room/findRoomResult")
    Observable<GameFinishBean> gameFinishInfo(@Query("roomId") long roomId);

    /**
     * 加入房间
     */
    @POST("room/joinRoom")
    Observable<NoDataBean> joinRoom(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId, @Query("password") String password);

    /**
     * 离开房间，不是退出
     */
    @POST("room/leave")
    Observable<NoDataBean> leaveRoom(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    @POST("room/findRoom")
    Observable<GameChatRoomBean> getGameChatRoomInfo(@Query("roomId") String roomId);

    /**
     * 查看圈子详情
     */
    @POST("circle/findCircleInfo")
    Observable<EnterCircleInfoBean> getCircleInfo(@Query("circleId") long circleId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 加入圈子
     */
    @POST("circle/join")
    Observable<NoDataBean> joinCircle(@Query("circleId") long circleId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 退出圈子
     */
    @POST("circle/quit")
    Observable<NoDataBean> signOutCircle(@Query("circleId") long circleId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 房间里点准备
     */
    @POST("room/ready")
    Observable<NoDataBean> gameReady(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 房间里点取消
     */
    @POST("room/cancelReady")
    Observable<NoDataBean> gameCancelReady(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 房间里点退出
     */
    @POST("room/quitRoom")
    Observable<NoDataBean> exitRoom(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 修改圈子公告
     */
    @POST("circle/update")
    Observable<NoDataBean> moditityNotice(@Query("circleId") long circleId, @Query("notice") String notice, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看成员
     */
    @POST("circle/findMembers")
    Observable<CircleMemberBean> findMembers(@Query("circleId") long circleId);

    /**
     * 查看圈内房间
     */
    @POST("circle/findRoomsByCircle")
    Observable<HomeRoomsBean> findRoomsByCircle(@Query("circleId") long circleId, @Query("page") Integer page, @Query("size") Integer size, @Query("state") Integer state);

    /**
     * 开始活动
     */
    @POST("room/roomStart")
    Observable<NoDataBean> startRoom(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 查看待评价好友
     */
    @POST("friend/findRoomFriends")
    Observable<WaitEvaluateBean> findRoomFriends(@Query("roomId") long roomId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 评价
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("friend/evalute")
    Observable<NoDataBean> evaluteGame(@Body EvaluteBean evaluteBean);


    /**
     * 查看待评价好友V2 2017年8月22日 12:22:19
     */
    @POST("friend/findRoomFriendsV2")
    Observable<WaitEvaluateV2Bean> toBeEvaluationFriendsV2(@Query("roomId") long roomId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 评价 V2 2017年8月22日 12:22:28
     * @param evaluations 用过滤Expose的toJson
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("friend/evaluteV2")
    Observable<NoDataBean> evaluateFriendsV2(@Body String evaluations);

    /**
     * 发送小纸条
     */
    @POST("message/sendNote")
    Observable<NoDataBean> sendNote(@Query("content") String content, @Query("receiverId") String receiverId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 获取别人的 昵称 ID 头像签名
     */
    @POST("friend/findOtherInfo")
    Observable<SimpleUserInfoBean> getOtherAvatar(@Query("otherId") String otherId);

    /**
     * 踢人
     */
    @POST("room/tiRen")
    Observable<NoDataBean> outMan(@Query("memberId") long memberId, @Query("roomId") long roomId, @Query("token") String token, @Query("userId") long userId, @Query("reason") String reason);

    /**
     * 房间内查看到人的信息
     */
    @POST("order/findGameRankingByUserId")
    Observable<UserGameRankingBean> findGameRankingByUserId(@Query("userId") long userId, @Query("gameId") int gameId);

    /**
     * 下载图片
     */
    @GET
    Call<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    /**
     * 评论喊话
     */
    @POST("declaration/evaluate")
    Observable<NoDataBean> commentCircleOfFriend(@Query("content") String content, @Query("declaration") long declaration, @Query("toUserId") long toUserId, @Query("token") String token, @Query("userId") long userId);

    /**
     * 发送位置
     */
    @POST("room/sendLocationV2")
    Observable<NoDataBean> sendLocationV2(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("udid") String udid, @Query("roomId") long roomId, @Query("token") String token, @Query("userId") long userId, @Query("place") String place);

    /**
     * 查看一条喊话
     */
    @POST("declaration/findOne")
    Observable<CommentItemBean> getOneDeclaration(@Query("id") long declarationId);

    /**
     * 房主取消开始
     */
    @POST("room/managerCancelRoom")
    Observable<NoDataBean> managerCancelBegin(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 点出发
     */
    @POST("room/attend")
    Observable<NoDataBean> memberGo(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 签到
     */
    @POST("room/sign")
    Observable<NoDataBean> roomCheck(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("roomId") long roomId, @Query("token") String token, @Query("userId") long userId);

    /**
     * 房间内 点 我没迟到
     */
    @POST("room/notLate")
    Observable<NoDataBean> iAmNotLate(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId);

    /**
     * 投诉 ，不在房间内的投诉，房间号填0
     *
     * @param token    自己token
     * @param userId   自己ID
     * @param roomId   不在房间内的投诉，房间号填0
     * @param personId 对方ID
     * @param content  内容
     * @return NoDataBean
     */
    @POST("room/complaint")
    Observable<NoDataBean> complaintOther(@Query("token") String token, @Query("userId") String userId, @Query("roomId") String roomId, @Query("personId") long personId, @Query("content") String content, @Query("photoUrl") String photoUrl);

    /**
     * 创建支付宝订单
     *
     * @param totalAmount 金额
     * @return data为String订单信息
     */
    @POST("alipay/createOrder")
    Observable<AlipayOrderInfoBean> createAlipayOrder(@Query("token") String token, @Query("userId") String userId, @Query("totalAmount") String totalAmount);

    /***
     * 查看道具数量
     */
    @POST("prop/findProp")
    Observable<PropsMumBean> findPropsMum(@Query("token") String token, @Query("userId") String userId);

    /**
     * 修改昵称
     */
    @POST("user/updateNickname")
    Observable<NoDataBean> changeName(@Query("nickname") String nickname, @Query("token") String token, @Query("userId") String userId);

    /**
     * 消费明细
     */
    @POST("user/findTransactionDetails")
    Observable<MoneyDetailsBean> getDetails(@Query("page") int page, @Query("size") int size, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看保证金充值明细
     */
    @POST("alipay/findOrders")
    Observable<DepositBean> getDeposit(@Query("page") int page, @Query("size") int size, @Query("token") String token, @Query("userId") String userId);

    /**
     * 申请退款
     *
     * @param id           订单ID
     * @param refundAmount 退款金额（单位：分）
     */
    @POST("alipay/refund")
    Observable<NoDataBean> applyDeposit(@Query("id") long id, @Query("refundAmount") String refundAmount, @Query("token") String token, @Query("userId") long userId);

    /**
     * 创建微信订单
     *
     * @param totalAmount 金额
     * @return 订单信息
     */
    @POST("weixin/createOrder")
    Observable<WxpayOrderInfoBean> createWXOrder(@Query("token") String token, @Query("userId") String userId, @Query("totalFee") String totalAmount);


    /**
     * 上传圈子头像或者背景
     */
    @POST("circle/updateImage")
    Observable<NoDataBean> uploadCircleImage(@Query("circleId") String circleId, @Query("signature") String signature, @Query("token") String token, @Query("userId") String userId, @Query("type") Integer type);

    /**
     * 签到
     */
    @POST("circle/sign")
    Observable<NoDataBean> signup(@Query("circleId") String circleId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看我收到的小纸条
     */
    @POST("message/findNotesByReceiverId")
    Observable<SmallPaperBean> getMyReceivePaper(@Query("page") Integer page, @Query("size") Integer size, @Query("userId") String userId, @Query("token") String token);

    /**
     * 删除小纸条
     */
    @POST("message/abandonNote")
    Observable<NoDataBean> deleteNote(@Query("noteId") long noteId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 收起放好小纸条
     */
    @POST("message/storeNote")
    Observable<NoDataBean> saveNote(@Query("noteId") long noteId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 购买道具
     */
    @POST("prop/buyProp")
    Observable<NoDataBean> buyProp(@Query("count") Integer count, @Query("token") String token, @Query("type") Integer type, @Query("userId") String userId);

    /**
     * 修改房间信息
     *
     * @param beginTime   开始时间
     * @param description 描述
     * @param endTime     结束时间
     * @param manCount    男生人数
     * @param memberCount 所有人数
     * @param name        房间名称
     * @param roomId      房间ID
     * @param token       用户Token
     * @param userId      用户UserId
     * @param womanCount  女生人数
     */
    @POST("room/updateRoom")
    Observable<NoDataBean> updateRoomInfo(@Query("beginTime") String beginTime, @Query("description") String description, @Query("endTime") String endTime, @Query("manCount") Integer manCount,
                                          @Query("memberCount") Integer memberCount, @Query("name") String name, @Query("roomId") String roomId, @Query("token") String token, @Query("userId") String userId,
                                          @Query("womanCount") Integer womanCount);

    /**
     * 上传通讯录
     */
    @POST("friend/loadFriendsByPhones")
    Observable<PhoneContactBean> getPhoneContactFriends(@Query("token") String token, @Query("userId") String userId, @Query("phoneStr") String phoneStr);

    /**
     * 发送好友请求
     */
    @POST("friend/invitateFriend")
    Observable<NoDataBean> sendInvitate(@Query("friendId") String friendId, @Query("origin") String origin, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看邀请列表
     */
    @POST("friend/findInvitations")
    Observable<InvitationsBean> getInviations(@Query("token") String token, @Query("userId") String userId);

    /**
     * 处理好友请求
     *
     * @param invitationId 请求id
     * @param state        状态
     * @param token        token
     * @param userId       id
     */
    @POST("friend/receiveInvitation")
    Observable<NoDataBean> handleInviate(@Query("invitationId") String invitationId, @Query("state") int state, @Query("token") String token, @Query("userId") String userId);

    /**
     * 退款
     *
     * @param alipayAccount zfb账号
     * @param money         钱
     * @param imei          串号
     * @param token         token
     * @param userId        id
     */
    @POST("withdrawals/byAlipayV2")
    Observable<NoDataBean> withdrawals(@Query("alipayAccount") String alipayAccount, @Query("money") String money, @Query("imei") String imei, @Query("token") String token, @Query("userId") String userId);

    /**
     * 使用叶子增加圈子经验值
     */
    @POST("circle/useBadge")
    Observable<NoDataBean> useBadge(@Query("badge") String badge, @Query("circleId") String circleId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看第三方绑定情况
     */
    @POST("user/isBind3Part")
    Observable<MapDataBean> getThirdPartyBindingState(@Query("token") String token, @Query("userId") String userId);

    /**
     * 查看有保证金的房间
     */
    @POST("user/findLockMoneyDetails")
    Observable<HavaBZmoneyRoomBean> findHaveBZmoneyRoom(@Query("token") String token, @Query("userId") String userId);

    /**
     * 查看是否绑定第三方
     */
    @POST("user/isBind3Part")
    Observable<MapDataBean> isBind3Part(@Query("token") String token, @Query("userId") String userId);

    /**
     * banding第三方
     */
    @POST("user/bind3Part")
    Observable<Bind3Part> bind3Part(@Query("token") String token, @Query("type") String type, @Query("uid") String uid, @Query("userId") String userId);

    /**
     * 设置房间是否公开
     */
    @POST("circle/setOpen")
    Observable<NoDataBean> setOpen(@Query("isOpen") boolean isOpen, @Query("roomId") String roomId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 消除标签
     */
    @POST("user/removeLabel")
    Observable<NoDataBean> clearLabels(@Query("removedLabel") String removedLabel, @Query("token") String token, @Query("userId") String userId);

    /**
     * 通过邀请直接成为好友
     *
     * @param token      自己的
     * @param receiverId 自己的
     * @param senderId   传过来的
     */
    @POST("friend/becameFriend")
    Observable<NoDataBean> becameFriend(@Query("token") String token, @Query("receiverId") String receiverId, @Query("senderId") String senderId, @Query("origin") String origin);

    @POST("circle/findSimpleCircleInfo")
    Observable<SimpleGroupInfoBean> getCircleSampleInfo(@Query("circleId") String circleId);

    /**
     * 回复小纸条
     */
    @POST("message/replyNote")
    Observable<NoDataBean> replyNote(@Query("content") String content, @Query("noteId") String noteId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 设置是否在线
     */
    @POST("room/setOnline")
    Observable<NoDataBean> setOnline(@Query("online") boolean online, @Query("roomId") String roomId, @Query("userId") String userId, @Query("token") String token);

    /**
     * 补签
     */
    @POST("room/signAgain")
    Observable<NoDataBean> buqian(@Query("latitude") String latitude, @Query("longitude") String longitude, @Query("roomId") String roomId, @Query("userId") String userId, @Query("token") String token);

    /**
     * 已读回复的纸条
     */
    @POST("message/readReplyNote")
    Observable<NoDataBean> readReplyNote(@Query("noteId") String noteId, @Query("userId") String userId, @Query("token") String token);

    /**
     * 查看发送的小纸条
     */
    @POST("message/findNotesBySenderId")
    Observable<SmallPaperBean> mySendPaper(@Query("page") Integer page, @Query("size") Integer size, @Query("userId") String userId, @Query("token") String token);

    /**
     * 修改出生日期
     */
    @POST("user/updateUserInfo")
    Observable<NoDataBean> updateBirthday(@Query("birthday") String birthday, @Query("userId") String userId, @Query("token") String token);

    /**
     * 支付宝购买VIP
     */
    @POST("prop/alipay/createOrder")
    Observable<AlipayOrderInfoBean> buyVIPbyAlipay(@Query("count") int count, @Query("propType") String propType, @Query("token") String token, @Query("userId") String userId);

    /**
     * 微信购买VIP
     */
    @POST("prop/weixin/createOrder")
    Observable<WxpayOrderInfoBean> buyVIPbyWechat(@Query("count") int count, @Query("propType") String propType, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看我加入的房间是否已开始
     */
    @POST("room/findMyRunningRooms")
    Observable<HomeRoomsBean> findMyRunningRooms(@Query("token") String token, @Query("userId") String userId, @Query("gameId") String gameId);

    /**
     * 我没迟到，添加图片和理由还有证明人id
     */
    @POST("room/notLateWithReason")
    Observable<NoDataBean> noLateWithReason(@Query("certifierId") String certifierId, @Query("photoUrl") String photoUrl, @Query("reason") String reason, @Query("roomId") String roomId, @Query("token") String token, @Query("userId") String userId);

    /**
     * 完善圈子信息
     */
    @POST("circle/updateInfo")
    Observable<NoDataBean> perfectCircleInfo(@Query("avatarSignature") String avatarSignature, @Query("bgSignature") String bgSignature, @Query("circleId") long circleId,
                                             @Query("notice") String notice, @Query("token") String token, @Query("userId") String userId);

    /**
     * 查看叶子使用记录
     */
    @POST("badge/findBadgeDetails")
    Observable<LeafDetailedBean> findLeafDetails(@Query("page") int page, @Query("size") int size, @Query("token") String token, @Query("userId") String userId);

    /**
     * 活动信息
     */
    @POST("activity/findAll")
    Observable<ActivityBean> findAllActivity();

    /**
     * 版本信息
     */
    @FormUrlEncoded
    @POST("application/findOne")
    Observable<AppVersionBean> findAppVersion(@Field("platform") String platform);

    /**
     * 创建支付宝购买叶子订单
     */
    @POST("badge/alipay/createOrder")
    Observable<AlipayOrderInfoBean> createAlipayBadgeOrder(@Query("token") String token,@Query("totalAmount") int totalAmount,@Query("userId") String userId);
    /**
     * 创建微信购买叶子订单
     */
    @POST("badge/weixin/createOrder")
    Observable<WxpayOrderInfoBean> createWxBadgeOrder(@Query("token") String token,@Query("totalFee") int totalFee,@Query("userId") String userId);
    /**
     * 合并账户
     */
    @POST("user/mergeV2")
    Observable<LoginBean> mergeAccout(@Query("password") String password, @Query("phone") String phone, @Query("token") String token, @Query("userId") String userId);

    /**
     * 验证验证码
     */
    @POST("user/validateSmscode")
    Observable<NoDataBean> validateSmsCode(@Query("phone") String phone,@Query("smscode") String smscode);
    /**
     * 查看可退保证金
     */
    @POST("user/calcRefundMoney")
    Observable<RefundMoneyData> refundMoney(@Query("token") String token,@Query("userId") String userId);
    /**
     * 设置账号名
     */
    @POST("user/setAccount")
    Observable<NoDataBean> setAccount(@Query("account") String account,@Query("token") String token,@Query("userId") String userId);
    /**
     * 查看好友位置
     */
    @POST("room/findMemberLocation")
    Observable<FriendLocationBean> findLocation(@Query("roomId") String roomId,@Query("token") String token,@Query("userId") String userId);
    /**
     * 根据Account查找用户
     */
    @POST("user/findIdByAccount")
    Observable<AccountBean> findByAccount(@Query("account") String account);
    /**
     * 保存或修改三方信息
     */
    @POST("user/saveThreePartInfo")
    Observable<NoDataBean> saveThreePartInfo(@Query("nickname") String nickname, @Query("photoUrl") String photoUrl, @Query("token") String token,@Query("type") String type,@Query("userId") String userId);

    /**
     * 获取自己的三方信息
     */
    @POST("user/findMyThreePartInfo")
    Observable<ThreePartBean> getMy3PartInfo(@Query("token") String token,@Query("userId") String userId,@Query("type") String type);

    /**
     * 解绑第三方信息
     */
    @POST("user/unBind3Part")
    Observable<NoDataBean> unBind3Part(@Query("password") String password,@Query("token") String token,@Query("type") String type,@Query("userId") String userId);

    /**
     * 删除评论或者喊话
     */
    @POST("declaration/remove")
    Observable<NoDataBean> removeDeclaration(@Query("token") String token,@Query("id") String id,@Query("type") int type,@Query("userId") String userId);

    /**
     * 强制解绑
     */
    @POST("user/forceUnbind")
    Observable<NoDataBean> forceUnbind(@Query("mergeFromId") String mergeFromId,@Query("mergeToId") String mergeToId,@Query("password") String password,@Query("token") String token,
                                       @Query("type") String type,@Query("userId") String userId);

    /**
     * 合并
     */
    @POST("user/newMarge")
    Observable<NoDataBean> newMarge(@Query("mergeFromId") String mergeFromId,@Query("mergeToId") String mergeToId,@Query("password") String password,@Query("token") String token,@Query("userId") String userId);

    /**
     * 通过token更改密码
     */
    @POST("user/updatePasswordByToken")
    Observable<NoDataBean> updatePwdByToken(@Query("password") String password,@Query("token") String token,@Query("userId") String userId);
}


