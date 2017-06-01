package com.hzease.tomeet.ranking.ui;

import android.os.Bundle;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.ActivityTypeBean;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.ranking.IRankContract;

import java.util.List;

/**
 * Created by xuq on 2017/6/1.
 */

public class RankingBakFragment extends BaseFragment implements IRankContract.View {
    @Override
    public void setPresenter(IRankContract.Presenter presenter) {

    }

    /**
     * 刷新adapter
     *
     * @param rankingBean
     */
    @Override
    public void reflush(RankingBean rankingBean) {

    }

    /**
     * 显示一级目录
     *
     * @param data
     */
    @Override
    public void showMainList(List<ActivityTypeBean.DataBean> data) {

    }

    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_rankingbak;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
