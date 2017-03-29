package pro.yueyuan.project_t;

/**
 * Created by Key on 2017/2/17 16:40
 * email: MrKey.K@gmail.com
 * description: 为了防止命名空间污染，请在所有常量前面加YY_PT_
 * 项目内所有的PT都是Project_t的缩写
 */

public interface AppConstants {
    // 服务器地址
    String YY_PT_SERVER_PATH = "http://app.yueyuan.pro/";

    // 阿里云OSS终端地址
    String YY_PT_OSS_ENDPOINT_URL = "http://oss-cn-hangzhou.aliyuncs.com";

    // 阿里云OSS bucket地址
    String YY_PT_OSS_PATH = "http://oss.yueyuan.pro/";

    // 阿里云OSS 头像
    String YY_PT_OSS_AVATAR = "/avatar";

    // 阿里云OSS 用户
    String YY_PT_OSS_USER = "user/";

    // 阿里云OSS 用户储存目录
    String YY_PT_OSS_USER_PATH = YY_PT_OSS_PATH + YY_PT_OSS_USER;

    // 阿里云OSS 头像预览大小
    String YY_PT_OSS_THUMBNAIL = "?x-oss-process=image/resize,m_lfit,w_100,h_100";

    // 阿里云OSS 头像预览
    String YY_PT_OSS_AVATAR_THUMBNAIL = YY_PT_OSS_AVATAR + YY_PT_OSS_THUMBNAIL;

    // 阿里云OSS bucket名字
    String YY_PT_OSS_NAME = "projectt";

    //navigation_me 返回值请求码
    int YY_PT_NAVIGATION_ME_REQUEST_CODE = 10081;

    //navigation_chat 返回值请求码
    int YY_PT_NAVIGATION_CHAT_REQUEST_CODE = 10082;

    // 登录成功
    int YY_PT_LOGIN_SUCCEED = 20081;

    // 登录失败
    int YY_PT_LOGIN_FAILED = 20082;

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
}
