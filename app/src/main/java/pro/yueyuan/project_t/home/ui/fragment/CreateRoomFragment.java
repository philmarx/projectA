package pro.yueyuan.project_t.home.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.ShareLocationActivity;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.home.IHomeContract;
import pro.yueyuan.project_t.utils.AMapLocUtils;
import pro.yueyuan.project_t.utils.ToastUtils;
import pro.yueyuan.project_t.widget.CityPicker;
import pro.yueyuan.project_t.widget.plugins.SelectData;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/31.
 */

public class CreateRoomFragment extends BaseFragment implements IHomeContract.View {

    private static final int RESULT_PLACE = 10086;
    private PopupWindow mCityPop;
    private View cityPopView,mDatePopView;
    private CityPicker cityPicker;
    private static String mSheng,mShi,mquxian;
    public static String city_all="";
    private TextView Select_Ok,Select_Cancel;
    @BindView(R.id.rl_createroom_starttime_fmt)
    RelativeLayout rl_createroom_starttime_fmt;
    @BindView(R.id.rl_createroom_endtime_fmt)
    RelativeLayout rl_createroom_endtime_fmt;
    @BindView(R.id.tv_createroom_starttime_fmt)
    TextView tv_createroom_starttime_fmt;
    @BindView(R.id.tv_createroom_endttime_fmt)
    TextView tv_createroom_endttime_fmt;
    @BindView(R.id.rl_createroom_gametype_fmt)
    RelativeLayout rl_createroom_gametype_fmt;
    @BindView(R.id.rl_createroom_choseplace_fmt)
    RelativeLayout rl_createroom_choseplace_fmt;
    @BindView(R.id.tv_createroom_gametype_fmt)
    TextView tv_createroom_gametype_fmt;

    private double mLongitude;
    private double mLatitude;
    private String cityCode;
    private String cityName;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;

    private SelectData selectData;

    public BottomNavigationView bottomNavigationView;

    public CreateRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    public static CreateRoomFragment newInstance() {
        return new CreateRoomFragment();
    }

    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @OnClick({
            R.id.rl_createroom_starttime_fmt,
            R.id.rl_createroom_endtime_fmt,
            R.id.rl_createroom_gametype_fmt,
            R.id.rl_createroom_choseplace_fmt
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_createroom_starttime_fmt:
                selectData = new SelectData(getActivity(), true);
                selectData.showAtLocation(tv_createroom_starttime_fmt, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        tv_createroom_starttime_fmt.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                    }
                });
                break;
            case R.id.rl_createroom_endtime_fmt:
                selectData = new SelectData(getActivity(), true);
                selectData.showAtLocation(tv_createroom_endttime_fmt, Gravity.BOTTOM, 0, 0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        tv_createroom_endttime_fmt.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                    }
                });
                break;
            case R.id.rl_createroom_gametype_fmt:
                backgroundAlpha(0.3f);
                showSelectionCityPOP(rl_createroom_gametype_fmt);
                break;
            case R.id.rl_createroom_choseplace_fmt:
                Intent openSend = new Intent(getActivity(),ShareLocationActivity.class);
                Logger.e(mLongitude+"");
                Logger.e(mLatitude+"");
                Logger.e(cityCode+"");
                Logger.e(cityName+"");
                openSend.putExtra("lon",mLongitude);
                openSend.putExtra("lat",mLatitude);
                openSend.putExtra("cityCode",cityCode);
                openSend.putExtra("cityName",cityName);
                startActivityForResult(openSend,RESULT_PLACE);
                break;
        }
    }

    private void initLogLat() {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                mLongitude = aMapLocation.getLongitude();
                mLatitude = aMapLocation.getLatitude();
                cityCode = aMapLocation.getCityCode();
                cityName = aMapLocation.getCity();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_createroom;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
        selectionCityPOP(R.layout.select_city_pop_main_layout);
        initLogLat();
    }

    @Override
    public void initGameList(List<ShowGameListBean.DataBean> data) {

    }

    @Override
    public void initRoomsList(List<HomeRoomsBean.DataBean> date) {

    }


    private void showSelectionCityPOP(View view) {
        if (!mCityPop.isShowing()) {
            mCityPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 选择城市 自定义控件
     *
     * @param resId
     */
    private void selectionCityPOP(int resId) {
        LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cityPopView = mLayoutInflater.inflate(resId, null);
        mCityPop = new PopupWindow(cityPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 必须设置background才能消失
        mCityPop.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.fullscreen_share_bg));
        mCityPop.setOutsideTouchable(true);
        // 自定义动画
        mCityPop.setAnimationStyle(R.style.AnimationBottomFade);
        // 使用系统动画
        //mCityPop.setAnimationStyle(R.style.mypopwindow_anim_style);

        cityPicker=(CityPicker) cityPopView.findViewById(R.id.citypicker);
        Select_Ok=(TextView) cityPopView.findViewById(R.id.Select_City_Ok);
        Select_Cancel=(TextView) cityPopView.findViewById(R.id.Select_City_Cancel);
        cityPicker.setCity(new CityPicker.testCity() {
            @Override
            public void cityAll(String sheng, String shi) {
                // TODO Auto-generated method stub
                mSheng=sheng;
                mShi=shi;
                handler.sendEmptyMessage(1);
            }
        });

        Select_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_all=mSheng+"  "+mShi;
                tv_createroom_gametype_fmt.setText(""+city_all);
                mCityPop.dismiss();
            }
        });

        Select_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_all="";
                mCityPop.dismiss();
            }
        });
        // popWindow消失监听方法
        mCityPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        mCityPop.update();
        mCityPop.setTouchable(true);
        mCityPop.setFocusable(true);
    }

    public static Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
					/*sheng_Text.setText(mSheng+"");
					shi_Text.setText(mShi+"");*/
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PLACE && resultCode ==getActivity().RESULT_OK){
            if (data != null){
                String place = data.getStringExtra(ShareLocationActivity.PLACE_NAME);
                ToastUtils.getToast(PTApplication.getInstance(),place);
            }
        }
    }
}
