package com.hzease.tomeet;

import com.google.gson.Gson;
import com.hzease.tomeet.data.UserOrderBean;

import org.junit.Test;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void jsonToMap() throws Exception {
        String s = "{\n" +
                "      \"image1Signature\" : \"1500947824656\",\n" +
                "      \"image2Signature\" : \"1498817137942\",\n" +
                "      \"image3Signature\" : \"\",\n" +
                "      \"image4Signature\" : \"1497945941757\",\n" +
                "      \"image5Signature\" : \"1497945794061\"\n" +
                "    }";

        System.out.println(s);

        Map map = new Gson().fromJson(s, Map.class);

        System.out.println(map);

        new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class).getOrderById(10000000000L)
                .subscribe(new Subscriber<UserOrderBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserOrderBean userOrderBean) {
                        System.out.println(userOrderBean.toString());
                    }
                });

    }
}