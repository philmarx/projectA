package pro.yueyuan.project_t.widget.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.SearchCircleBean;

/**
 * Created by xuq on 2017/4/24.
 */

public class SearchCircleAdapter extends BaseAdapter {

    List<SearchCircleBean.DataBean> list;
    public SearchCircleAdapter(List<SearchCircleBean.DataBean> list) {
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
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_searchcircle_listview,null);
            viewHolder.mCircleName = (TextView) convertView.findViewById(R.id.tv_searchcircle_name_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCircleName.setText(list.get(position).getName());
        return convertView;
    }

    class ViewHolder{
        TextView mCircleName;
    }
}
