package com.hzease.tomeet.widget.adapters;

import android.content.Context;
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
import com.hzease.tomeet.widget.flowtag.FlowTagLayout;
import com.hzease.tomeet.widget.flowtag.OnTagClickListener;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.*;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by xuq on 2017/5/11.
 */

public class WaitEvaluateAdapterV2 extends RecyclerView.Adapter<WaitEvaluateAdapterV2.ViewHolder> {

    private Context context;
    public List<WaitEvaluateV2Bean.DataBean.EvaluationsBean> mDatas;

    public WaitEvaluateAdapterV2(Context context,List<WaitEvaluateV2Bean.DataBean.EvaluationsBean> mDatas) {
       this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_evaluatev2, null);
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
        Logger.e("name" + mDatas.get(position).getNickname() + "\nfriendPoint" + friendPoint);
        switch (friendPoint){
            case "0":
                holder.rb_green_friend.setChecked(true);
                break;
            case "9":
                holder.rb_gold_friend.setChecked(true);
                break;
            case "7":
                holder.rb_blue_friend.setChecked(true);
                break;
            case "5":
                holder.rb_green_friend.setChecked(true);
                break;
            case "3":
                holder.rb_gray_friend.setChecked(true);
                break;
            case "1":
                holder.rb_red_friend.setChecked(true);
                break;
        }
        holder.sb_showvalue.setProgress(Integer.valueOf(mDatas.get(position).getRoomEvaluationPoint()));
        holder.tv_showvalue.setText(mDatas.get(position).getRoomEvaluationPoint() + "分");
        holder.sb_showvalue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.tv_showvalue.setText(seekBar.getProgress() + "分");
                mDatas.get(position).setRoomEvaluationPoint(String.valueOf(seekBar.getProgress()));
            }
        });
        //设置好感度
        holder.rg_friends.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_gold_friend:
                        mDatas.get(position).setFriendId("9");
                        Logger.e("gold");
                        Logger.e("name" + mDatas.get(position).getNickname() + "\npoint" + mDatas.get(position).getFriendId());
                        break;
                    case R.id.rb_blue_friend:
                        mDatas.get(position).setFriendId("7");
                        Logger.e("blue");
                        Logger.e("name" + mDatas.get(position).getNickname() + "\npoint" + mDatas.get(position).getFriendId());
                        break;
                    case R.id.rb_green_friend:
                        mDatas.get(position).setFriendId("5");
                        Logger.e("green");
                        Logger.e("name" + mDatas.get(position).getNickname() + "\npoint" + mDatas.get(position).getFriendId());
                        break;
                    case R.id.rb_gray_friend:
                        mDatas.get(position).setFriendId("3");
                        Logger.e("gray");
                        Logger.e("name" + mDatas.get(position).getNickname() + "\npoint" + mDatas.get(position).getFriendId());
                        break;
                    case R.id.rb_red_friend:
                        mDatas.get(position).setFriendId("1");
                        Logger.e("red");
                        Logger.e("name" + mDatas.get(position).getNickname() + "\npoint" + mDatas.get(position).getFriendId());
                        break;
                }
            }
        });
       /* MyGridLayoutManager layout = new MyGridLayoutManager(context, 3);
        layout.setScrollEnabled(false);
        holder.rcv_labels.setLayoutManager(layout);
        holder.rcv_labels.addItemDecoration(new SpacesItemProps(8));
        EvaluateLabelsAdapter adapter = new EvaluateLabelsAdapter(mDatas.get(position).getLabels(), context);
        holder.rcv_labels.setAdapter(adapter);
        adapter.setOnItemClickLitener(new EvaluateLabelsAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.getToast("标签" + mDatas.get(position).getLabels().get(position));
            }
        });*/
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
        @BindView(R.id.rg_friends)
        RadioGroup rg_friends;
        @BindView(R.id.rb_gold_friend)
        RadioButton rb_gold_friend;
        @BindView(R.id.rb_blue_friend)
        RadioButton rb_blue_friend;
        @BindView(R.id.rb_green_friend)
        RadioButton rb_green_friend;
        @BindView(R.id.rb_gray_friend)
        RadioButton rb_gray_friend;
        @BindView(R.id.rb_red_friend)
        RadioButton rb_red_friend;
        @BindView(R.id.sb_showvalue)
        SeekBar sb_showvalue;
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
