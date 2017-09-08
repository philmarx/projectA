package com.hzease.tomeet.widget.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;
import com.hzease.tomeet.widget.CircleImageView;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateAdapterV3 extends RecyclerView.Adapter<WaitEvaluateAdapterV3.ViewHolder> {

    private Context context;
    public List<WaitEvaluateV2Bean.DataBean.EvaluationsBean> mDatas;

    public WaitEvaluateAdapterV3(Context context, List<WaitEvaluateV2Bean.DataBean.EvaluationsBean> mDatas) {
       this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_evaluatev3, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //每次填充数据之前先移除 TextWatcher 监听器，然后为 EditText 填充数据 ，最后在为 EditText 添加 TextWatcher 监听器，这是edittext在recycleview的item中重绘的问题
        if (holder.et_evaluate_item.getTag() instanceof TextWatcher){
            holder.et_evaluate_item.removeTextChangedListener((TextWatcher) holder.et_evaluate_item.getTag());
        }
        //这里是设置标签
        holder.et_evaluate_item.setText(mDatas.get(position).getLabel());
        //头像
        Glide.with(holder.itemView.getContext())
                .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position).getFriendId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(holder.itemView.getContext()))
                .signature(new StringSignature(mDatas.get(position).getAvatarSignature()))
                .into(holder.memberIcon);
        //昵称
        holder.memberName.setText(mDatas.get(position).getNickname());
        //是否签到
        if (mDatas.get(position).isSigned()){
            holder.tv_isSign.setVisibility(View.GONE);
            holder.ll_show.setVisibility(View.VISIBLE);
        }else{
            holder.tv_isSign.setVisibility(View.VISIBLE);
            holder.ll_show.setVisibility(View.GONE);
        }
        //是否是好友
        if (mDatas.get(position).isfriend()){
            holder.ll_isfriend.setVisibility(View.GONE);
        }else{
            holder.ll_isfriend.setVisibility(View.VISIBLE);
        }

        String friendPoint = mDatas.get(position).getFriendPoint();
        //设置好感度
        holder.sb_likevalue.setProgress(Integer.valueOf(friendPoint));
        holder.tv_likevalue.setText(friendPoint + "分");
        holder.sb_likevalue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                holder.tv_likevalue.setText(i + 1 + "分");
                switch (i+1){
                    case 1:
                    case 2:
                        holder.sb_likevalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_red));
                        break;
                    case 3:
                    case 4:
                        holder.sb_likevalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_gray));
                        break;
                    case 5:
                    case 6:
                        holder.sb_likevalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_green));
                        break;
                    case 7:
                    case 8:
                        holder.sb_likevalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_blue));
                        break;
                    case 9:
                    case 10:
                        holder.sb_likevalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_gold));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDatas.get(position).setFriendId(String.valueOf(seekBar.getProgress()));
            }
        });
        //设置表现分
        holder.sb_showvalue.setProgress(Integer.valueOf(mDatas.get(position).getRoomEvaluationPoint()));
        holder.tv_showvalue.setText(mDatas.get(position).getRoomEvaluationPoint() + "分");
        holder.sb_showvalue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                holder.tv_showvalue.setText(i+1 + "分");
                /*switch (i){
                    case 0:
                    case 1:
                    case 2:
                        holder.sb_showvalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_red));
                        break;
                    case 3:
                    case 4:
                        holder.sb_showvalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_gray));
                        break;
                    case 5:
                    case 6:
                        holder.sb_showvalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_green));
                        break;
                    case 7:
                    case 8:
                        holder.sb_showvalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_blue));
                        break;
                    case 9:
                    case 10:
                        holder.sb_showvalue.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_gold));
                        break;
                }*/
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDatas.get(position).setRoomEvaluationPoint(String.valueOf(seekBar.getProgress()));
            }
        });

        holder.ftl_label.setAdapter(new TagAdapter<String>(mDatas.get(position).getLabels()) {
            @Override
            public View getView(FlowLayout parent, int p, String s) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_labels, null);
                TextView textView = view.findViewById(R.id.tv_labels);
                textView.setText(mDatas.get(position).getLabels().get(p));
                return view;
            }
        });
        holder.ftl_label.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //这里是点击设置标签
                mDatas.get(position).setLabel(mDatas.get(position).getLabels().get(position));
                holder.et_evaluate_item.setText(mDatas.get(position).getLabels().get(position));
                return true;
            }
        });
        //你懂得
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mDatas.get(position).setLabel(editable.toString());
            }
        };
        holder.et_evaluate_item.addTextChangedListener(watcher);
        holder.et_evaluate_item.setTag(watcher);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.memberIcon)
        CircleImageView memberIcon;
        @BindView(R.id.memberName)
        TextView memberName;
        @BindView(R.id.tv_isSign)
        TextView tv_isSign;
        @BindView(R.id.sb_showvalue)
        SeekBar sb_showvalue;
        @BindView(R.id.sb_likevalue)
        SeekBar sb_likevalue;
        @BindView(R.id.tv_likevalue)
        TextView tv_likevalue;
        @BindView(R.id.tv_showvalue)
        TextView tv_showvalue;
        @BindView(R.id.ftl_label)
        TagFlowLayout ftl_label;
        @BindView(R.id.et_evaluate_item)
        EditText et_evaluate_item;
        @BindView(R.id.ll_show)
        LinearLayout ll_show;
        @BindView(R.id.ll_isfriend)
        LinearLayout ll_isfriend;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
