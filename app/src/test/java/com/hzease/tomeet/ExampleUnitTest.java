package com.hzease.tomeet;

import com.google.gson.Gson;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserOrderBean;

import org.junit.Test;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void joinRoomAndReady() throws Exception {
        long phone = 88800000001L;
        String pwd = "123456";
        final String roomId = "1000000000516";

        final RequestService requestService = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class);

        for (int i = 0; i < 19; i++) {
            final long newPhone = phone + i;
            requestService.login(String.valueOf(newPhone), pwd).subscribe(new Subscriber<LoginBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("login: " + e.getMessage());
                }

                @Override
                public void onNext(final LoginBean loginBean) {
                    if (loginBean.isSuccess()) {
                        requestService.joinRoom(loginBean.getData().getToken(), loginBean.getData().getId(), roomId, "")
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        System.out.println("joinRoom: " + e.getMessage());
                                    }

                                    @Override
                                    public void onNext(final NoDataBean noDataBean) {
                                        if (noDataBean.isSuccess()) {
                                            requestService.gameReady(loginBean.getData().getToken(), loginBean.getData().getId(), roomId)
                                                    .subscribe(new Subscriber<NoDataBean>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            System.out.println("gameReady: " + e.getMessage());
                                                        }

                                                        @Override
                                                        public void onNext(NoDataBean noDataBean) {
                                                            System.out.println(newPhone + " - " + loginBean.getData().getId() + " gameReady准备完成  " + noDataBean.toString());
                                                        }
                                                    });
                                        } else {
                                            System.out.println(newPhone + " - " + loginBean.getData().getId() + "  -gameReady-  " + noDataBean.toString());
                                        }
                                    }
                                });
                    } else {
                        System.out.println(newPhone + " - " + loginBean.toString());
                    }
                }
            });
        }
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