package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xuq on 2017/5/3.
 */

public class MyJoinRoomsAdapter extends RecyclerView.Adapter {
    List<MyJoinRoomsBean.DataBean> list;
    int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.one_5, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    public int getmLoadMoreStatus() {
        return mLoadMoreStatus;
    }


    public List<MyJoinRoomsBean.DataBean> getList() {
        return list;
    }

    public void setList(List<MyJoinRoomsBean.DataBean> list) {
        this.list = list;
        if (list.isEmpty()) {
            this.mLoadMoreStatus = NO_LOAD_MORE;
        } else {
            this.mLoadMoreStatus = PULLUP_LOAD_MORE;
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, MyJoinRoomsBean.DataBean position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MyJoinRoomsAdapter(Context context) {
        this.list = new ArrayList<>();
        this.mLoadMoreStatus = LOADING_MORE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activitytype, parent, false);
            if (mOnItemClickLitener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(view, (MyJoinRoomsBean.DataBean) view.getTag());
                    }
                });
            }
            return new ViewHolder(view);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_footview_layout, parent, false);
            return new FooterViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) holder1;
            holder.itemView.setTag(list.get(position));
            int imageResource = gameType[list.get(position).getGame().getId()];
            //Logger.w("imageResource: " + imageResource);
            holder.gameType.setImageResource(imageResource);
            holder.roomName.setText(list.get(position).getName());
            LatLng latLng1 = new LatLng(PTApplication.myLatitude, PTApplication.myLongitude);
            LatLng latLng2 = new LatLng(list.get(position).getLatitude(),list.get(position).getLongitude());
            holder.gamePlace.setText(list.get(position).getPlace() + " · " + String.format("%.2f", AMapUtils.calculateLineDistance(latLng1,latLng2)/1000)+"KM");
            int state = list.get(position).getState();
            switch (state) {
                case 0:
                case 1:
                    holder.isReady.setTextColor(Color.rgb(3, 181, 19));
                    holder.isReady.setText("未开始");
                    break;
                case 2:
                    holder.isReady.setTextColor(Color.rgb(3, 181, 19));
                    holder.isReady.setText("进行中");
                    break;
                case 3:
                    holder.isReady.setTextColor(Color.rgb(255, 131, 115));
                    if (list.get(position).isEvaluated()) {
                        holder.isReady.setText("已评价");
                    } else {
                        holder.isReady.setText("待评价");
                    }
                    break;
                case 4:
                    holder.isReady.setTextColor(Color.rgb(184, 184, 184));
                    holder.isReady.setText("已结束");
            }
            //活动开始时间
            holder.gameTime.setText(getDatas(list.get(position).getBeginTime()));

        } else if (holder1 instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder1;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    Logger.e("隐藏..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    Logger.e("正在加载..." + position);
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    Logger.e("已经到底了..." + position);
                    footerViewHolder.mTvLoadText.setText("已经到底了，不要再拉了！Σ( ° △ °|||)︴　");
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameType;
        TextView roomName;
        TextView gamePlace;
        TextView isReady;
        TextView gameTime;

        public ViewHolder(View itemView) {
            super(itemView);
            gameType = (ImageView) itemView.findViewById(R.id.iv_me_gametype_item);
            roomName = (TextView) itemView.findViewById(R.id.tv_me_roomname_item);
            gamePlace = (TextView) itemView.findViewById(R.id.tv_me_roomplace_item);
            isReady = (TextView) itemView.findViewById(R.id.tv_me_isready_item);
            gameTime = (TextView) itemView.findViewById(R.id.tv_me_time_item);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLoadText)
        TextView mTvLoadText;
        @BindView(R.id.loadLayout)
        LinearLayout mLoadLayout;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }

    private String getDatas(String datas) {
        try {
            Date dateCreate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datas);
            if (dateCreate.getYear() != new Date().getYear()) {
                return datas.substring(2);
            }
            long diff = dateCreate.getTime() / 86400000 - System.currentTimeMillis() / 86400000;
            switch((int) diff) {
                case 0:
                    return "今天" + datas.substring(10);
                case 1:
                    return "明天" + datas.substring(10);
                case 2:
                    return "后天" + datas.substring(10);
                default:
                    return datas.substring(5);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return datas.substring(2);
        }
    }
}
