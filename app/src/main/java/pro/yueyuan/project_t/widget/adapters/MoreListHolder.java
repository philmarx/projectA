package pro.yueyuan.project_t.widget.adapters;

import android.view.View;
import android.widget.TextView;
import pro.yueyuan.project_t.BaseHolder;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.RankingBean;

/**
 * Created by xuq on 2017/4/5.
 */

public class MoreListHolder extends BaseHolder<RankingBean.DataBean>{
    public TextView item_order;
    public TextView item_name;
    public TextView item_point;

    @Override
    public View initHolderView() {
        View view = View.inflate(PTApplication.getInstance(), R.layout.item_ranking,null);
        item_order = (TextView) view.findViewById(R.id.item_ranking_order);
        item_name = (TextView) view.findViewById(R.id.item_ranking_name);
        item_point = (TextView) view.findViewById(R.id.item_ranking_point);
        return view;
    }

    @Override
    public void bindData(RankingBean.DataBean dataBean) {
        item_order.setText(dataBean.getRanking()+"");
        item_name.setText(dataBean.getNickname()+"");
        item_point.setText(dataBean.getPoint()+"");
    }


}
