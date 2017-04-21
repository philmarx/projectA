package pro.yueyuan.project_t.circle.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.ICircleContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/21.
 */

public class MotifityCircleFragment extends BaseFragment implements ICircleContract.View {

    private ICircleContract.Presenter mPresenter;

    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;
    public static MotifityCircleFragment newInstance(){
        return new MotifityCircleFragment();
    }
    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_motifitycircle_first;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
