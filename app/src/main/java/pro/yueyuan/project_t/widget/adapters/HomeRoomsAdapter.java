package pro.yueyuan.project_t.widget.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.HomeRoomsBean;

/**
 * Created by xuq on 2017/4/10.
 */

public class HomeRoomsAdapter extends BaseAdapter {

    private List<HomeRoomsBean.DataBean> list;

    public HomeRoomsAdapter(List<HomeRoomsBean.DataBean> list) {
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
            convertView = View.inflate(PTApplication.getInstance(), R.layout.item_home_rooms,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_homeroomsitem_name = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_name);
            viewHolder.tv_homeroomsitem_time = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_time);
            viewHolder.tv_homeroomsitem_place = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_place);
            viewHolder.tv_homeroomsitem_distance = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_distance);
            viewHolder.tv_homeroomsitem_distanceandmoeny = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_distanceandmoeny);
            viewHolder.tv_homeroomsitem_moeny = (TextView) convertView.findViewById(R.id.tv_homeroomsitem_moeny);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_homeroomsitem_name.setText(list.get(position).getName());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time;
        //计算持续时间
        String continuedTimes = "活动时间:";
        try {
            Date beginTime = df.parse(list.get(position).getBeginTime());
            Date endTime = df.parse(list.get(position).getEndTime());
            //获得日期时间
            String data = list.get(position).getBeginTime().substring(5,list.get(position).getBeginTime().length());
            continuedTimes = continuedTimes + data;
            long dif = endTime.getTime()-beginTime.getTime();
            double times = dif/1000/60/60.0;
            if (times==Math.floor(times)){
                continuedTimes = continuedTimes + " 持续"+ (int)times + "小时";
            }else if(Math.floor(times)==0){
                continuedTimes = continuedTimes + " 持续" + (int)(times*60) + "分钟";
            }else{
                double integer = Math.floor(times);
                double middle = times - integer;
                continuedTimes = continuedTimes + " 持续" + (int)integer + "小时" + (int)(middle*60) + "分钟";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.tv_homeroomsitem_time.setText(continuedTimes);
        viewHolder.tv_homeroomsitem_place.setText(list.get(position).getPlace());
        LatLng latLng1 = new LatLng(30.4,30.4);
        LatLng latLng2 = new LatLng(list.get(position).getLatitude(),list.get(position).getLongitude());
        float distance = AMapUtils.calculateLineDistance(latLng1,latLng2);
        if (String.valueOf(list.get(position).getMoney()).equals("0")){
            viewHolder.tv_homeroomsitem_distance.setVisibility(View.VISIBLE);
            viewHolder.tv_homeroomsitem_distance.setText(distance+"");
            viewHolder.tv_homeroomsitem_distanceandmoeny.setVisibility(View.GONE);
            viewHolder.tv_homeroomsitem_moeny.setVisibility(View.GONE);
        }else{
            viewHolder.tv_homeroomsitem_distanceandmoeny.setVisibility(View.VISIBLE);
            viewHolder.tv_homeroomsitem_moeny.setVisibility(View.VISIBLE);
            viewHolder.tv_homeroomsitem_distance.setText(distance+" | ");
            viewHolder.tv_homeroomsitem_distance.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder{
        TextView tv_homeroomsitem_name;
        TextView tv_homeroomsitem_time;
        TextView tv_homeroomsitem_place;
        TextView tv_homeroomsitem_distance;
        TextView tv_homeroomsitem_distanceandmoeny;
        TextView tv_homeroomsitem_moeny;
    }


}
