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

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

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
            holder.gamePlace.setText(list.get(position).getPlace() + "1");
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
            String createTime = list.get(position).getBeginTime();
            try {
                String timestate = getDatas(createTime + ":00");
                switch (timestate) {
                    case "-2":
                        String afteryestoday = "昨天" + createTime.substring(11);
                        holder.gameTime.setText(afteryestoday);
                        break;
                    case "-1":
                        String yestoday = "昨天" + createTime.substring(11);
                        holder.gameTime.setText(yestoday);
                        break;
                    case "0":
                        String today = "今天" + createTime.substring(11);
                        holder.gameTime.setText(today);
                        break;
                    case "1":
                        String tomorrow = "明天" + createTime.substring(11);
                        holder.gameTime.setText(tomorrow);
                        break;
                    case "2":
                        String afterTomorrow = "后天" + createTime.substring(11);
                        holder.gameTime.setText(afterTomorrow);
                        break;
                    case "out":
                        String datas = createTime.substring(5);
                        datas.replace("-", ".");
                        holder.gameTime.setText(datas);
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (holder1 instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder1;
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    //footerViewHolder.mTvLoadText.setText("上拉加载更多...");
                    footerViewHolder.mLoadLayout.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footerViewHolder.mLoadLayout.setVisibility(View.VISIBLE);
                    footerViewHolder.mTvLoadText.setText("正在加载...");
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
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
    }

    private String getDatas(String datas) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datas);
        Calendar today = Calendar.getInstance();
        Calendar createTime = Calendar.getInstance();
        createTime.setTime(date1);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        createTime.set(Calendar.HOUR, 0);
        createTime.set(Calendar.MINUTE, 0);
        createTime.set(Calendar.SECOND, 0);
        long intervalMilli = createTime.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        // -2:前天 -1：昨天 0：今天 1：明天 2：后天， out：显示日期
        if (xcts >= -2 && xcts <= 2) {
            return String.valueOf(xcts);
        } else {
            return "out";
        }
    }
}
