package pro.yueyuan.project_t.widget.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.UserOrderBean;

/**
 * Created by xuq on 2017/4/7.
 */

public class OrderListAdapter extends BaseAdapter {

    public ArrayList<UserOrderBean.DataBean.OrdersBean> list;
    public OrderListAdapter(ArrayList<UserOrderBean.DataBean.OrdersBean> list){
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_personinfo,null);
            viewHolder = new ViewHolder();
            viewHolder.member = (TextView) convertView.findViewById(R.id.tv_item_member_aty);
            viewHolder.score = (TextView) convertView.findViewById(R.id.tv_item_score_aty);
            viewHolder.order = (TextView) convertView.findViewById(R.id.tv_item_score_aty);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.member.setText(list.get(position).getCount()+"");
        viewHolder.score.setText(list.get(position).getPoint()+"");
        viewHolder.order.setText(list.get(position).getRanking()+"");
        return convertView;
    }
    public class ViewHolder{
        public TextView member;
        public TextView score;
        public TextView order;
    }
}
