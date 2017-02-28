package pro.yueyuan.project_t;

/**
 * Created by Key on 2017/2/17 16:40
 * email: MrKey.K@gmail.com
 * description: 为了防止命名空间污染，请在所有常量前面加YY_PT_
 * 项目内所有的PT都是Project_t的缩写
 */

public interface AppConstants {
    // 服务器地址
    String YY_PT_SERVER_PATH = "http://www.weather.com.cn/";

    //
    String YY_PT_BEIJING = "data/sk/101010100.html";

    //极光短信 base64(appKey:masterSecret)
    String YY_PT_BASE64_AUTH = "Basic 1MDk1YmRlMTA2ZDUyMzE1NzYxOGM0ZjU4OjM4MDFhMmYxY2NhZmY0MGI2MzRiOWI4Mw==";

    // 极光地址
    String YY_PT_JSMS_URL = "https://api.sms.jpush.cn/v1/codes";
}
