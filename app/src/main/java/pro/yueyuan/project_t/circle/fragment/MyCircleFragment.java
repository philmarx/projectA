package pro.yueyuan.project_t.circle.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.circle.ICircleContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/18.
 */

public class MyCircleFragment extends BaseFragment implements ICircleContract.View {

    @BindView(R.id.rv_mycircle_fmt)
    RecyclerView rvMycircleFmt;
    @BindView(R.id.lv_recommendedcircle_fmt)
    RecyclerView lvRecommendedcircleFmt;
    //定义一个集合用来接受View
    private List<View> list = new ArrayList<>();

    //recycleview 测试集合
    private LayoutInflater mInflater;
    private List<String> mDatas = new ArrayList<>();
    String[] strings = {"A", "B", "C", "A", "B", "C", "A", "B", "C",};
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
        mInflater = LayoutInflater.from(getContext());
        initViewPagerItem();
        initmDatas();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvMycircleFmt.setLayoutManager(linearLayoutManager);
        lvRecommendedcircleFmt.setLayoutManager(new LinearLayoutManager(getContext()));
        lvRecommendedcircleFmt.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));
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
                container.addView(list.get(position), 0);
                return list.get(position);
            }
        });
        rvMycircleFmt.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = mInflater.inflate(R.layout.item_circle_recycle, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.mContent = (TextView) view.findViewById(R.id.content);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.mContent.setText(mDatas.get(position));
            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }
        });


        lvRecommendedcircleFmt.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = mInflater.inflate(R.layout.item_circle_listview, parent, false);
                ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.mContent = (TextView) view.findViewById(R.id.list_item_content);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.mContent.setText(mDatas.get(position));
            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }
        });
    }

    private void initmDatas() {
        for (int i = 0; i < strings.length; i++) {
            mDatas.add(strings[i]);
        }
    }

    private void initViewPagerItem() {
        LayoutInflater lf = getActivity().getLayoutInflater().from(getContext());
        View view1 = lf.inflate(R.layout.item_viewpager_home, null);
        View view2 = lf.inflate(R.layout.item_viewpager_next, null);
        list.add(view1);
        list.add(view2);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mContent;
    }

}
