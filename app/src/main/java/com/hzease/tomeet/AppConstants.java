package com.hzease.tomeet;

/**
 * Created by Key on 2017/2/17 16:40
 * email: MrKey.K@gmail.com
 * description: 为了防止命名空间污染，请在所有常量前面加TOMEET_
 * 项目内所有的PT都是Project_t的缩写
 */

public interface AppConstants {

    // 正式服务器地址
    String YY_PT_SERVER_PATH = "http://tomeet-app.hzease.com/";

    // 局域网服务器
    //String YY_PT_SERVER_PATH = "http://192.168.0.195:8080/";

    // 阿里云OSS终端地址
    String YY_PT_OSS_ENDPOINT_URL = "http://oss-cn-hangzhou.aliyuncs.com";

    // 阿里云OSS bucket地址
    String YY_PT_OSS_PATH = "http://tomeet-app-files.hzease.com/";

    // 阿里云OSS 头像
    String YY_PT_OSS_AVATAR = "/avatar";
    String YY_PT_OSS_IMAGE1 = "/image1";
    String YY_PT_OSS_IMAGE2 = "/image2";
    String YY_PT_OSS_IMAGE3 = "/image3";
    String YY_PT_OSS_IMAGE4 = "/image4";
    String YY_PT_OSS_IMAGE5 = "/image5";
    String YY_PT_OSS_CIRCLE_BG = "/bg";
    String YY_PT_OSS_CIRCLE_AVATAR = "/avatar";

    //意见反馈图片
    String YY_PT_OSS_FEEDBACK = "/not_feedback";
    //投诉
    String YY_PT_OSS_COMPLAINT = "/not_complaint";
    //迟到
    String YY_PT_OSS_LATE = "/not_late";

    // 阿里云OSS 用户
    String YY_PT_OSS_USER = "user/";

    // 阿里云OSS 圈子图片
    String YY_PT_OSS_CIRCLE = "circle/";

    // 阿里云OSS 用户储存目录
    String YY_PT_OSS_USER_PATH = YY_PT_OSS_PATH + YY_PT_OSS_USER;

    // 阿里云OSS 用户储存目录
    String YY_PT_OSS_CIRCLE_PATH = YY_PT_OSS_PATH + YY_PT_OSS_CIRCLE;

    // 阿里云OSS 头像预览大小
    String YY_PT_OSS_THUMBNAIL = "?x-oss-process=image/resize,m_lfit,w_100,h_100";

    // 阿里云OSS 头像预览  /avatar?x-oss-process=image/resize,m_lfit,w_100,h_100
    String YY_PT_OSS_AVATAR_THUMBNAIL = YY_PT_OSS_AVATAR + YY_PT_OSS_THUMBNAIL;

    // 阿里云OSS bucket名字
    String YY_PT_OSS_NAME = "tomeet-app-files";

    //navigation_me 返回值请求码
    int YY_PT_NAVIGATION_ME_REQUEST_CODE = 10081;

    //navigation_chat 返回值请求码
    int YY_PT_NAVIGATION_CHAT_REQUEST_CODE = 10082;

    //splash过来的intent.setFlag 码
    int YY_PT_NAVIGATION_SPLASH_REQUEST_CODE = 10083;

    // 登录成功（已登录）
    int YY_PT_LOGIN_SUCCEED = 20081;

    // 登录失败(未登录)
    int YY_PT_LOGIN_FAILED = 20082;

    // 登录中
    int YY_PT_LOGIN_LOADING = 20080;

    // 好感度
    // 金色
    int[] GOLD_POINT = new int[]{9,10};
    // 蓝色
    int[] BLUE_POINT = new int[]{7,8};
    // 绿色
    int[] GREEN_POINT = new int[]{5,6};
    // 灰色
    int[] GRAY_POINT = new int[]{3,4};
    // 红色
    int[] RED_POINT = new int[]{1,2};
    // 不显示的单相思
    int[] BLANK_POINT = new int[]{0,0};


    // 相册 160
    int REQUEST_CODE_GALLERY = 0xa0;
    // 拍照 161
    int REQUEST_CODE_CAMERA = 0xa1;
    // 裁剪 162
    int REQUEST_CODE_CROP = 0xa2;
    // 联系人 163
    int REQUEST_CODE_CONTACT = 0xa3;

    // 正方形 X = Y
    int CROP_OUTPUT_XY = 1080;
    // 裁剪后图片的宽(X)和高(Y)
    int CROP_OUTPUT_X = 1080;
    int CROP_OUTPUT_Y = 1080;


    // 权限常量
    int REQUEST_TAKE_PHOTO_PERMISSION = 0xaa;
    int REQUEST_SD_WRITE_PERMISSION = 0xab;
    int REQUEST_LOCATION_PERMISSION = 0xac;
    // 手机号登录渠道
    String LOGIN_PHONE = "PHONE";
    // 第三方登录
    // 微信
    String AUTHORIZED_LOGIN_WX = "WECHAT";
    // QQ
    String AUTHORIZED_LOGIN_QQ = "QQ";
    // 微博
    String AUTHORIZED_LOGIN_WB = "WEIBO";


    // 常用字
    String TOMEET_ROOM_ID = "roomId";

    // 融云系统管理员ID
    String TOMEET_ADMIN_ID = "888888";

    // 微信APP ID
    String TOMEET_WX_APP_ID = "wxc9ba3b479546a874";
    String TOMEET_WX_APP_SECRET = "82d0d662a41af1b4e3ef432b300ae7b0";
    String TOMEET_WX_APP_PARTNER_ID = "1480291952";
    // 腾讯QQ
    String TOMEET_QQ_APP_ID = "1106142011";
    String TOMEET_QQ_APP_KEY = "NDfWoUTz4yPxxbeA";

    // 万能密码
    String TOMEET_EVERY_ROOM_PASSWORD = "AMIIBCgseMsjbg2ds3BZ";

    // sp中游戏类型的key
    String TOMEET_SP_GAME_TYPE = "game_type_list";
    // sp中筛选记忆的key
    String TOMEET_SP_FILTRATE_GAME_TYPE_MEMORY = "filtrate_game_type_memory";

    // logo的地址
    String TOMMET_SHARE_LOGO = "http://www.hzease.com/image/logo200x200.png";

    // game的地址
    String TOMMET_SHARE_GAME = "http://www.hzease.com/game/";

    /**
     * 后会有期app分享  tomeet://www.hzease.com?action=invited&key1=   &key2=
     */
    String TOMMET_SHARE_APP = "https://hzease.com/share/";

    // 邀请进入房间
    String TOMMET_SHARE_APP_ROOM = TOMMET_SHARE_APP + "room.html?userId=";
    // 分享进入应用
    String TOMMET_SHARE_APP_SHARE = TOMMET_SHARE_APP + "share.html?userId=";
    // 添加好友
    String TOMMET_SHARE_ADD_FRIEND = TOMMET_SHARE_APP+"addFriend.html?userId=";

    //sp 名字
    String TOMMET_SHARED_PREFERENCE = "ToMeet_SharedPreference";
    // 更新
    String TOMMET_SP_UPDATE_TIME = "update_time";

}