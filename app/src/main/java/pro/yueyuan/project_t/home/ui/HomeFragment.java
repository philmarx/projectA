package pro.yueyuan.project_t.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.tauth.Tencent;
import com.zaaach.citypicker.CityPickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJionRoomBean;
import pro.yueyuan.project_t.home.IHomeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class HomeFragment extends BaseFragment implements IHomeContract.View {
    private static final int REQUEST_CODE_PICK_CITY = 233;

    @BindView(R.id.tv_home_cityname_fmt)
    TextView tv_home_cityname_fmt;
    @BindView(R.id.ll_home_chosecity_fmt)
    LinearLayout ll_home_chosecity_fmt;

    /*@BindView(R.id.login)
    Button login;
    @BindView(R.id.login_qq)
    Button loginQq;

    @BindView(R.id.iv_home_fragment)
    ImageView iv_home_fragment;*/


    //QQ登录
    private Tencent mTencent;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }



    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_homebak;
    }
    /**
     * 点击时间的处理
     */
    @OnClick({
            R.id.ll_home_chosecity_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_home_chosecity_fmt:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
                Gson gson = new Gson();
                String json = "{\n" +
                        "    \"success\": true,\n" +
                        "    \"msg\": \"查找成功\",\n" +
                        "    \"data\": [\n" +
                        "        {\n" +
                        "            \"id\": 41,\n" +
                        "            \"name\": \"乒乓球44\",\n" +
                        "            \"place\": \"杭州西湖\",\n" +
                        "            \"creator\": {\n" +
                        "                \"id\": 10000000000,\n" +
                        "                \"nickname\": \"test4\",\n" +
                        "                \"amount\": 0,\n" +
                        "                \"phone\": \"17702525841\",\n" +
                        "                \"email\": \"test@outlook.com\",\n" +
                        "                \"gender\": true,\n" +
                        "                \"place\": \"测试地址\",\n" +
                        "                \"age\": 22,\n" +
                        "                \"isInit\": true,\n" +
                        "                \"labels\": [\n" +
                        "                    {\n" +
                        "                        \"id\": 1,\n" +
                        "                        \"name\": \"90后\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"beginTime\": \"2017-03-29 15:28\",\n" +
                        "            \"endTime\": \"2017-03-22 18:35\",\n" +
                        "            \"createTime\": \"2017-03-27 15:51\",\n" +
                        "            \"state\": 1,\n" +
                        "            \"game\": {\n" +
                        "                \"id\": 28,\n" +
                        "                \"name\": \"短途旅行\",\n" +
                        "                \"children\": null\n" +
                        "            },\n" +
                        "            \"money\": 2323,\n" +
                        "            \"joinMember\": 1,\n" +
                        "            \"joinManMember\": 1,\n" +
                        "            \"joinWomanMember\": 0,\n" +
                        "            \"phone\": \"17702525841\",\n" +
                        "            \"memberCount\": 5,\n" +
                        "            \"manCount\": 3,\n" +
                        "            \"womanCount\": 2,\n" +
                        "            \"description\": \"打乒乓球\",\n" +
                        "            \"longitude\": 22.3,\n" +
                        "            \"latitude\": 33.33,\n" +
                        "            \"joinMemberIds\": [\n" +
                        "                10000000000,\n" +
                        "                10000000020\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 42,\n" +
                        "            \"name\": \"乒乓球55\",\n" +
                        "            \"place\": \"杭州西湖\",\n" +
                        "            \"creator\": {\n" +
                        "                \"id\": 10000000000,\n" +
                        "                \"nickname\": \"test4\",\n" +
                        "                \"amount\": 0,\n" +
                        "                \"phone\": \"17702525841\",\n" +
                        "                \"email\": \"test@outlook.com\",\n" +
                        "                \"gender\": true,\n" +
                        "                \"place\": \"测试地址\",\n" +
                        "                \"age\": 22,\n" +
                        "                \"isInit\": true,\n" +
                        "                \"labels\": [\n" +
                        "                    {\n" +
                        "                        \"id\": 1,\n" +
                        "                        \"name\": \"90后\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"beginTime\": \"2017-03-29 15:28\",\n" +
                        "            \"endTime\": \"2017-03-22 18:35\",\n" +
                        "            \"createTime\": \"2017-03-27 15:51\",\n" +
                        "            \"state\": 0,\n" +
                        "            \"game\": {\n" +
                        "                \"id\": 28,\n" +
                        "                \"name\": \"短途旅行\",\n" +
                        "                \"children\": null\n" +
                        "            },\n" +
                        "            \"money\": 1001,\n" +
                        "            \"joinMember\": 1,\n" +
                        "            \"joinManMember\": 1,\n" +
                        "            \"joinWomanMember\": 0,\n" +
                        "            \"phone\": \"17702525841\",\n" +
                        "            \"memberCount\": 5,\n" +
                        "            \"manCount\": 0,\n" +
                        "            \"womanCount\": 0,\n" +
                        "            \"description\": \"打乒乓球\",\n" +
                        "            \"longitude\": 22.3,\n" +
                        "            \"latitude\": 33.33,\n" +
                        "            \"joinMemberIds\": [\n" +
                        "                10000000000,\n" +
                        "                10000000020\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 40,\n" +
                        "            \"name\": \"乒乓球33\",\n" +
                        "            \"place\": \"杭州西湖\",\n" +
                        "            \"creator\": {\n" +
                        "                \"id\": 10000000000,\n" +
                        "                \"nickname\": \"test4\",\n" +
                        "                \"amount\": 0,\n" +
                        "                \"phone\": \"17702525841\",\n" +
                        "                \"email\": \"test@outlook.com\",\n" +
                        "                \"gender\": true,\n" +
                        "                \"place\": \"测试地址\",\n" +
                        "                \"age\": 22,\n" +
                        "                \"isInit\": true,\n" +
                        "                \"labels\": [\n" +
                        "                    {\n" +
                        "                        \"id\": 1,\n" +
                        "                        \"name\": \"90后\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"beginTime\": \"2017-03-29 15:28\",\n" +
                        "            \"endTime\": \"2017-03-22 18:35\",\n" +
                        "            \"createTime\": \"2017-03-27 15:50\",\n" +
                        "            \"state\": 2,\n" +
                        "            \"game\": {\n" +
                        "                \"id\": 28,\n" +
                        "                \"name\": \"短途旅行\",\n" +
                        "                \"children\": null\n" +
                        "            },\n" +
                        "            \"money\": 588,\n" +
                        "            \"joinMember\": 1,\n" +
                        "            \"joinManMember\": 1,\n" +
                        "            \"joinWomanMember\": 0,\n" +
                        "            \"phone\": \"17702525841\",\n" +
                        "            \"memberCount\": 5,\n" +
                        "            \"manCount\": 0,\n" +
                        "            \"womanCount\": 0,\n" +
                        "            \"description\": \"打乒乓球\",\n" +
                        "            \"longitude\": 22.3,\n" +
                        "            \"latitude\": 33.33,\n" +
                        "            \"joinMemberIds\": [\n" +
                        "                10000000000,\n" +
                        "                10000000020\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 38,\n" +
                        "            \"name\": \"乒乓球11\",\n" +
                        "            \"place\": \"杭州西湖\",\n" +
                        "            \"creator\": {\n" +
                        "                \"id\": 10000000000,\n" +
                        "                \"nickname\": \"test4\",\n" +
                        "                \"amount\": 0,\n" +
                        "                \"phone\": \"17702525841\",\n" +
                        "                \"email\": \"test@outlook.com\",\n" +
                        "                \"gender\": true,\n" +
                        "                \"place\": \"测试地址\",\n" +
                        "                \"age\": 22,\n" +
                        "                \"isInit\": true,\n" +
                        "                \"labels\": [\n" +
                        "                    {\n" +
                        "                        \"id\": 1,\n" +
                        "                        \"name\": \"90后\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            },\n" +
                        "            \"beginTime\": \"2017-03-29 15:28\",\n" +
                        "            \"endTime\": \"2017-03-23 17:33\",\n" +
                        "            \"createTime\": \"2017-03-27 15:46\",\n" +
                        "            \"state\": 3,\n" +
                        "            \"game\": {\n" +
                        "                \"id\": 28,\n" +
                        "                \"name\": \"短途旅行\",\n" +
                        "                \"children\": null\n" +
                        "            },\n" +
                        "            \"money\": 0,\n" +
                        "            \"joinMember\": 1,\n" +
                        "            \"joinManMember\": 1,\n" +
                        "            \"joinWomanMember\": 0,\n" +
                        "            \"phone\": \"17702525841\",\n" +
                        "            \"memberCount\": 5,\n" +
                        "            \"manCount\": 0,\n" +
                        "            \"womanCount\": 0,\n" +
                        "            \"description\": \"打乒乓球\",\n" +
                        "            \"longitude\": 22.3,\n" +
                        "            \"latitude\": 33.33,\n" +
                        "            \"joinMemberIds\": [\n" +
                        "                10000000000,\n" +
                        "                10000000020\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
                MyJionRoomBean rooms = gson.fromJson(json,MyJionRoomBean.class);
                Logger.e(rooms.toString());
                break;
        }
    }

    /**
     * 重写onActivityResult
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == getActivity().RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tv_home_cityname_fmt.setText(city);
            }
        }
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

       /* loginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQlogin();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });*/

    }

    /**
     * QQ登录
     */
    private void QQlogin() {
        /*mTencent = Tencent.createInstance("1105938559", mContext);
        mTencent.login(getActivity(), "all", new IUiListener() {
            //登录成功,在此可以获取用户信息
            @Override
            public void onComplete(Object o) {
                Logger.e("AAA", "登录成功");
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });*/
    }


    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示我的头像
     */
    @Override
    public void showMyAvatar() {

       /* Glide.with(this)
                .load("http://oss.yueyuan.pro/user/888888/1111.jpg?x-oss-process=image/resize,m_lfit,w_100,h_100")
                // 圆形裁剪
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(iv_home_fragment);*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    /*@OnClick(R.id.b_home_fragment)
    public void onClick() {
        mPresenter.loadMyAvatar();
    }*/

}
