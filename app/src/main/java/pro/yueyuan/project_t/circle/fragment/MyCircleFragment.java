package pro.yueyuan.project_t.circle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.ICircleContract;
import pro.yueyuan.project_t.widget.CustomViewPager;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/18.
 */

public class MyCircleFragment extends BaseFragment implements ICircleContract.View {

    //定义一个集合用来接受View
    private List<View> list = new ArrayList<>();
    @BindView(R.id.cvp_mycircle_fmt)
    ViewPager cvpMycircleFmt;
    private ICircleContract.Presenter mPresenter;


    @Override
    public void setPresenter(ICircleContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public static MyCircleFragment newInstance() {
        return new MyCircleFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initViewPagerItem();
        cvpMycircleFmt.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position),0);
                return list.get(position);
            }
        });
    }

    private void initViewPagerItem() {
        LayoutInflater lf = getActivity().getLayoutInflater().from(getContext());
        View view1 = lf.inflate(R.layout.item_viewpager_home,null);
        View view2 = lf.inflate(R.layout.item_viewpager_next,null);
        list.add(view1);
        list.add(view2);
    }
}
