package pro.yueyuan.project_t.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.MyJionRoomBean;
import pro.yueyuan.project_t.me.IMeContract;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class MeFragment extends BaseFragment implements IMeContract.View {
    @BindView(R.id.myrecycle)
    RecyclerView myrecycle;
    @BindView(R.id.mybalance)
    LinearLayout mybalance;
    @BindView(R.id.iv_me_setting_fmt)
    ImageView iv_me_setting_fmt;
    @BindView(R.id.tv_me_nickname_fmt)
    TextView tvMeNickNameFmt;
    @BindView(R.id.tv_me_amount_fmt)
    TextView tvMeAmountFmt;
    public BottomNavigationView bottomNavigationView;

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    private List<MyJionRoomBean.DataBean> mDatas;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    public MeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();
        // mPresenter.start();
    }
    public static MeFragment newInstance() {
        return new MeFragment();
    }


    @OnClick({
            R.id.mybalance,
            R.id.iv_me_setting_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mybalance:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(1));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
            case R.id.iv_me_setting_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(2));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
        }
    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_mebak;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        /**
         * 显示我的信息
         */
        mPresenter.loadMyInfo(PTApplication.userId,PTApplication.userToken);
        /**
         * 显示我加入的活动
         */
        mPresenter.getMyJoinRooms(0,10,PTApplication.userToken,PTApplication.userId);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        Logger.e(PTApplication.userId);
        Logger.e(PTApplication.userToken);

        /**
         * 获取当前activity
         */
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();


        myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        //对recycleView进行数据展示
        myrecycle.setAdapter(new CommonAdapter<MyJionRoomBean.DataBean>(getContext(),R.layout.item_activitytype,mDatas) {
            @Override
            protected void convert(ViewHolder holder, MyJionRoomBean.DataBean dataBean, int position) {
                int state = dataBean.getState();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time;
                //计算持续时间
                String continuedTimes = "活动时间:";
                try {
                    Date beginTime = df.parse(dataBean.getBeginTime());
                    Date endTime = df.parse(dataBean.getEndTime());
                     //获得日期时间
                    String data = dataBean.getBeginTime().substring(5,dataBean.getBeginTime().length());
                    continuedTimes = continuedTimes + data;
                    long dif = endTime.getTime()-beginTime.getTime();
                    long times = dif/1000/60/60;
                    int exchange = (int) (times);
                    if(times == exchange){
                        time = String.valueOf(exchange);
                        continuedTimes = continuedTimes+"持续"+times+"小时";
                    }else if(times < 1){
                        time = String.valueOf((times/60)*60);
                        continuedTimes = continuedTimes+"持续"+times+"分钟";
                    }else{
                        String hours = String.valueOf(exchange);
                        long mins = times-exchange;
                        continuedTimes = continuedTimes+"持续"+hours+"小时"+String.valueOf(mins)+"分钟";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (state){
                    case 0:
                        holder.setText(R.id.item_continued,continuedTimes);
                        holder.setText(R.id.item_place,"活动地点："+dataBean.getPlace());
                        holder.setText(R.id.item_state,"未开始");
                        break;
                    case 1:
                        holder.setText(R.id.item_continued,continuedTimes);
                        holder.setText(R.id.item_place,"活动地点："+dataBean.getPlace());
                        holder.setText(R.id.item_state,"进行中");
                        break;
                    case 2:
                        holder.setText(R.id.item_continued,continuedTimes);
                        holder.setText(R.id.item_place,"活动地点："+dataBean.getPlace());
                        holder.setText(R.id.item_state,"待评价");
                        holder.setVisible(R.id.item_evaluate,true);
                        break;
                    case 3:
                        holder.setText(R.id.item_continued,continuedTimes);
                        holder.setText(R.id.item_place,"活动地点："+dataBean.getPlace());
                        holder.setText(R.id.item_state,"已结束");
                        break;
                }
            }
        });
    }


    /*private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }*/


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示我的头像
     */
    @Override
    public void showMyAvatar() {
    }

    /**
     * 显示我的信息
     */
    @Override
    public void showMyInfo(String nickName, String amount) {
        tvMeNickNameFmt.setText(nickName);
        tvMeAmountFmt.setText(amount);
    }

    /**
     * 显示我的消息
     * @param myJionRoomBean
     */
    @Override
    public void showMyRooms(MyJionRoomBean myJionRoomBean) {
        mDatas = myJionRoomBean.getData();
        Logger.e(String.valueOf(mDatas.size()));
    }
}
