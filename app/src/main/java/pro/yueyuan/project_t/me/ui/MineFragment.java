package pro.yueyuan.project_t.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.login.ui.LoginActivity;
import pro.yueyuan.project_t.me.IMeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class MineFragment extends BaseFragment implements IMeContract.View {

    //登录按钮,若用户登录则隐藏
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
    TextView mineLetterNum;

    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IMeContract.Presenter mPresenter;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mineLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        mineTeamInfo.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        });
        mineTeamInfo.setEmptyView(mineNoitem);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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
}
