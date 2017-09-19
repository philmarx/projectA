package com.hzease.tomeet;

import com.google.gson.Gson;
import com.hzease.tomeet.data.LoginBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserOrderBean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    public  void test2() throws Exception {
        String serverVersion = "1.2.18";
        String appVersion = "1.2.17";

        String[] splitServerVersion = serverVersion.split("\\.");
        String[] splitAppVersion = appVersion.split("\\.");
        System.out.println(Arrays.toString(splitServerVersion) + "   " + Arrays.toString(splitAppVersion));

        int length = serverVersion.length();
        for (int i = 0; i < length; i++) {
            if (Integer.valueOf(splitServerVersion[i]) > Integer.valueOf(splitAppVersion[i])) {
                break;
            }
            System.out.println(splitServerVersion[i] + "  " + splitAppVersion[i]);
        }
    }

    @Test
    public  void test1() throws Exception {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("asdf");
        objects.addAll(null);
        System.out.println(objects);
    }

    @Test
    public void jsonExpose() throws Exception {
        String jsonStr = "{\n" +
                "    \"success\": true,\n" +
                "    \"msg\": \"\",\n" +
                "    \"data\": {\n" +
                "        \"room\": {\n" +
                "            \"isScoring\": true\n" +
                "        },\n" +
                "        \"users\": [\n" +
                "            {\n" +
                "                \"friendId\": 10000000001,\n" +
                "                \"nickname\": \"卧床不起\",\n" +
                "                \"avatarSignature\": \"1503049492208\",\n" +
                "                \"labels\": [\n" +
                "                    \"智慧担当\",\n" +
                "                    \"一表人才\",\n" +
                "                    \"颜值担当\",\n" +
                "                    \"幽默风趣\",\n" +
                "                    \"傲娇\"\n" +
                "                ],\n" +
                "                \"friendPoint\": 7,\n" +
                "                \"signed\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"friendId\": 10000000002,\n" +
                "                \"nickname\": \"xml\",\n" +
                "                \"avatarSignature\": \"1502434206820\",\n" +
                "                \"labels\": [\n" +
                "                    \"萌妹纸\",\n" +
                "                    \"耿直boy\",\n" +
                "                    \"头脑灵活\",\n" +
                "                    \"傲娇\",\n" +
                "                    \"厉害了我的哥\"\n" +
                "                ],\n" +
                "                \"friendPoint\": 9,\n" +
                "                \"signed\": true\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        /*WaitEvaluateV2Bean bean = new Gson().fromJson(jsonStr, WaitEvaluateV2Bean.class);
        System.out.println(bean.toString());

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String s = gson.toJson(bean.getData().getEvaluations());

        System.out.println(s);*/

        //toBeEvaluationFriendsV2  查看评价的接口

        //evaluateFriendsV2        评价的接口，最后一个参数用toJson

        Map map = new Gson().fromJson(jsonStr, Map.class);
        System.out.println(map);
    }


    @Test
    public void joinRoomAndReady() throws Exception {
        long phone = 88800000070L;
        String pwd = "123456";
        final String roomId = "1000000000700";

        final RequestService requestService = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class);

        for (int i = 0; i <= 15; i++) {
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