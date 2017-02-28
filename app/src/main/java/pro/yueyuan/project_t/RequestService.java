package pro.yueyuan.project_t;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Key on 2016/11/23 00:16
 * email: MrKey.K@gmail.com
 */

public interface RequestService {

    @GET("{path}")
    Call<String> getString(@Path("path") String path);

    @POST("login")
    Call<String> getString(@Query("logincode") String logincode,
                           @Query("password") String password,
                           @Query("submit") String submit,
                           @Query("table") String table);

    // 极光短信
//    @POST("phone")
//    Call<SMSCodeBean> getSMSCode(@Body SMSPhoneNumberBean phone);
    @POST("phone")
    Call<String> getSMSCode(@Query("mobile") String mobile, @Query("temp_id") int temp_id);
}
