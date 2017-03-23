package pro.yueyuan.project_t.me.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.me.IMeContract;

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

    private List<String> mDatas;
    /*//登录按钮,若用户登录则隐藏
    @BindView(R.id.mine_login)
    Button mineLogin;
    //用户名字
    @BindView(R.id.mine_userName)
    TextView mineUserName;
    //花草数量
    @BindView(R.id.mine_flowerNum)
    TextView mineFlowerNum;
    //设置按钮
    @BindView(R.id.mine_setting)
    ImageView mineSetting;
    //分享按钮
    @BindView(R.id.mine_share)
    ImageView mineShare;
    //组队信息
    @BindView(R.id.mine_teamInfo)
    ListView mineTeamInfo;
    //当没有组队信息时显示该信息
    @BindView(R.id.mine_noitem)
    TextView mineNoitem;
    //我的余额
    @BindView(R.id.mine_balance)
    TextView mineBalance;
    //我的保证金
    @BindView(R.id.mine_BZmoney)
    TextView mineBZmoney;
    //我的表白信
    @BindView(R.id.mine_letterNum)
    TextView mineLetterNum;*/

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
            R.id.mybalance
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mybalance:
                MeActivity meActivity = (MeActivity) getActivity();
                FragmentTransaction transaction = meActivity.getSupportFragmentManager().beginTransaction();
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(1));
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                BottomNavigationView bottomNavigationView = (BottomNavigationView) meActivity.findViewById(R.id.navigation_bottom);
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
        myrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        initDatas();
        myrecycle.setAdapter(new CommonAdapter<String>(getContext(), R.layout.item_activitytype, mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.textView16, s);
            }
        });

    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }


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

    @Override
    public void showMyAvatar() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
