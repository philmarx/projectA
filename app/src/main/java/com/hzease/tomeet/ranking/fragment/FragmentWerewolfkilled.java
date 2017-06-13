package com.hzease.tomeet.ranking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.data.UserGameRankingBean;
import com.hzease.tomeet.widget.CircleImageView;
import com.hzease.tomeet.widget.adapters.MoreListAdapter;
import com.mingle.widget.LoadingView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/12.
 */

public class FragmentWerewolfkilled extends BaseFragment {
    int gameId;
    //暂无数据
    @BindView(R.id.no_data)
    TextView no_data;
    //所有数据
    @BindView(R.id.arl_datas)
    AutoRelativeLayout arl_datas;
    //loading
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;
    //第二名的头像
    @BindView(R.id.civ_ranking_no2_icon)
    CircleImageView civ_ranking_no2_icon;
    //第一名的头像
    @BindView(R.id.civ_ranking_no1_icon)
    CircleImageView civ_ranking_no1_icon;
    //第三名的头像
    @BindView(R.id.civ_ranking_no3_icon)
    CircleImageView civ_ranking_no3_icon;
    //第二名的名字
    @BindView(R.id.tv_ranking_no2_name)
    TextView tv_ranking_no2_name;
    //第一名的名字
    @BindView(R.id.tv_ranking_no1_name)
    TextView tv_ranking_no1_name;
    //第三名的名字
    @BindView(R.id.tv_ranking_no3_name)
    TextView tv_ranking_no3_name;
    //第二名的分数
    @BindView(R.id.tv_ranking_no2_point)
    TextView tv_ranking_no2_point;
    //第一名的分数
    @BindView(R.id.tv_ranking_no1_point)
    TextView tv_ranking_no1_point;
    //第三名的分数
    @BindView(R.id.tv_ranking_no3_point)
    TextView tv_ranking_no3_point;
    //剩余排名的展示
    @BindView(R.id.lv_ranking_others_fmt)
    ListView lv_ranking_others_fmt;
    //我的排名
    @BindView(R.id.tv_ranking_myranking_fmt)
    TextView tv_ranking_myranking_fmt;
    //我的头像
    @BindView(R.id.civ_ranking_myicon_fmt)
    CircleImageView civ_ranking_myicon_fmt;
    //我的昵称
    @BindView(R.id.tv_ranking_myname_fmt)
    TextView tv_ranking_myname_fmt;
    //我的分数
    @BindView(R.id.tv_ranking_mypoint_fmt)
    TextView tv_ranking_mypoint_fmt;

    public FragmentWerewolfkilled(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ranking_all;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        load_View.setVisibility(View.VISIBLE);
        arl_datas.setVisibility(View.GONE);
        //查看活动的排名
        PTApplication.getRequestService().getRanking(gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RankingBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(RankingBean rankingBean) {
                        if (rankingBean.isSuccess()) {
                            Logger.e("进来了嘛");
                            if (rankingBean.getData().size() == 0) {
                                no_data.setVisibility(View.VISIBLE);
                                load_View.setVisibility(View.GONE);
                            } else {
                                Logger.e("进来了嘛");
                                initRanking(rankingBean.getData());
                            }

                        }
                    }
                });
        PTApplication.getRequestService().findGameRankingByUserId(Long.valueOf(PTApplication.userId),gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserGameRankingBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserGameRankingBean userGameRankingBean) {
                        if (userGameRankingBean.isSuccess()){
                            initMyRanking(userGameRankingBean.getData());
                        }
                    }
                });

    }

    /**
     * 加载我自己的排名
     * @param data
     */
    private void initMyRanking(UserGameRankingBean.DataBean data) {
        if (data.getRanking() <= 50){
            tv_ranking_myranking_fmt.setText(data.getRanking()+"");
        }else{
            tv_ranking_myranking_fmt.setText("未上榜");
        }
        Glide.with(mContext)
                .load(AppConstants.YY_PT_OSS_USER_PATH + PTApplication.userId + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                .into(civ_ranking_myicon_fmt);
        tv_ranking_myname_fmt.setText(PTApplication.myInfomation.getData().getNickname());
        tv_ranking_mypoint_fmt.setText(data.getPoint());
    }

    //填充排名
    private void initRanking(final List<RankingBean.DataBean> data) {
        if (data.size() == 1){
            tv_ranking_no1_name.setText(data.get(0).getNickname());
            tv_ranking_no1_point.setText(data.get(0).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(0).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(0).getAvatarSignature()))
                    .into(civ_ranking_no1_icon);
            civ_ranking_no1_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(0).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        if (data.size() == 2){
            tv_ranking_no1_name.setText(data.get(0).getNickname());
            tv_ranking_no1_point.setText(data.get(0).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(0).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(0).getAvatarSignature()))
                    .into(civ_ranking_no1_icon);
            tv_ranking_no2_name.setText(data.get(1).getNickname());
            tv_ranking_no2_point.setText(data.get(1).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(1).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(1).getAvatarSignature()))
                    .into(civ_ranking_no2_icon);
            civ_ranking_no1_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(0).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            civ_ranking_no2_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(1).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        if (data.size() >= 3){
            tv_ranking_no1_name.setText(data.get(0).getNickname());
            tv_ranking_no1_point.setText(data.get(0).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(0).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(0).getAvatarSignature()))
                    .into(civ_ranking_no1_icon);
            tv_ranking_no2_name.setText(data.get(1).getNickname());
            tv_ranking_no2_point.setText(data.get(1).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(1).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(1).getAvatarSignature()))
                    .into(civ_ranking_no2_icon);
            tv_ranking_no3_name.setText(data.get(2).getNickname());
            tv_ranking_no3_point.setText(data.get(2).getPoint() + "分");
            Glide.with(mContext)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(2).getUserId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .signature(new StringSignature(data.get(2).getAvatarSignature()))
                    .into(civ_ranking_no3_icon);
            civ_ranking_no1_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(0).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            civ_ranking_no2_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(1).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            civ_ranking_no3_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = data.get(2).getUserId();
                    Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId",userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            initOtherRanking(data);
        }
        load_View.setVisibility(View.GONE);
        arl_datas.setVisibility(View.VISIBLE);

    }

    private void initOtherRanking(final List<RankingBean.DataBean> data) {
        MoreListAdapter adapter = new MoreListAdapter(data, mContext);
        lv_ranking_others_fmt.setAdapter(adapter);
        lv_ranking_others_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long userId = data.get(position+3).getUserId();
                Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}