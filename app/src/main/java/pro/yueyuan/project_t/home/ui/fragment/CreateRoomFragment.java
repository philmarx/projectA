package pro.yueyuan.project_t.home.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.home.IHomeContract;
import pro.yueyuan.project_t.widget.plugins.SelectData;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/3/31.
 */

public class CreateRoomFragment extends BaseFragment implements IHomeContract.View {
    @BindView(R.id.rl_createroom_starttime_fmt)
    RelativeLayout rl_createroom_starttime_fmt;
    @BindView(R.id.rl_createroom_endtime_fmt)
    RelativeLayout rl_createroom_endtime_fmt;
    @BindView(R.id.et_createroom_starttime_fmt)
    EditText et_createroom_starttime_fmt;
    @BindView(R.id.et_createroom_endttime_fmt)
    TextView et_createroom_endttime_fmt;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;

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
            R.id.rl_createroom_endtime_fmt
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_createroom_starttime_fmt:
                SelectData selectData  = new SelectData(getActivity(),true);
                selectData.showAtLocation(et_createroom_starttime_fmt, Gravity.BOTTOM,0,0);
                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
                    @Override
                    public void onClick(String year, String month, String day, String hour, String minute) {
                        Toast.makeText(getActivity(),
                                year + "-" + month + "-" + day+" "+hour+":"+minute,
                                Toast.LENGTH_LONG).show();
                        et_createroom_starttime_fmt.setText(year + "-" + month + "-" + day+" "+hour+":"+minute);
                    }
                });
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_createroom;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void showMyAvatar() {

    }

    @Override
    public void initGameList(List<ShowGameListBean.DataBean> data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
