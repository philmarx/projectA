package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameTypeBean;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuq on 2017/5/2.
 */

public class TypeTwoAdapter extends RecyclerView.Adapter<TypeTwoAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    int[] typeOne = {R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_3,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6};
    int[] typeTwo = {R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6};
    int[] typeThree = {R.drawable.two_one3_1,R.drawable.two_one3_2,R.drawable.two_one3_3,R.drawable.two_one3_4,R.drawable.two_one3_5,R.drawable.two_one3_6,R.drawable.two_one3_7};
    int[] typeFour = {R.drawable.two_one4_1,R.drawable.two_one4_2,R.drawable.two_one4_3,R.drawable.two_one4_4,R.drawable.two_one4_5};

    private List<int[]> mIcons = new ArrayList<>();
    private List<GameTypeBean.ChildrenBean> mDatas;
    int type;

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    private TypeTwoAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(TypeTwoAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public TypeTwoAdapter(List<GameTypeBean.ChildrenBean> list, int type, Context context) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = list;
        this.type = type;
        mIcons.add(typeOne);
        mIcons.add(typeTwo);
        mIcons.add(typeThree);
        mIcons.add(typeFour);
        Logger.e("TypeTwoAdapter");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gamelist,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(mDatas.get(type).getChildren().get(position).getName());
        holder.imageView.setImageResource(mIcons.get(type)[position]);
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.get(type).getChildren().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.game_icon);
            textView = (TextView) itemView.findViewById(R.id.game_name);
        }
    }
}
