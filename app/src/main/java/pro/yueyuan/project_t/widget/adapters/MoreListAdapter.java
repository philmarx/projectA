package pro.yueyuan.project_t.widget.adapters;

import java.util.ArrayList;

import pro.yueyuan.project_t.BaseHolder;
import pro.yueyuan.project_t.BasicAdapter;
import pro.yueyuan.project_t.data.RankingBean;

/**
 * Created by xuq on 2017/4/5.
 */

public class MoreListAdapter extends BasicAdapter<RankingBean.DataBean> {


    public MoreListAdapter(ArrayList<RankingBean.DataBean> list) {
        super(list);
    }

    @Override
    protected BaseHolder getHolder(int position) {
        return new MoreListHolder();
    }
}
