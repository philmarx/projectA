package pro.yueyuan.project_t;

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
    @GET("{path}")
    Call<String> getString(@Path("path") String path);
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

}
