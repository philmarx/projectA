package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/5/10.
 */

public class LevelFragment extends BaseFragment {
    private final long circleId;
    //经验条
    @BindView(R.id.msb_circleinfo_level_experience_fmt)
    SeekBar msb_circleinfo_level_experience_fmt;
    @BindView(R.id.iv_circleinfo_level_lv1_fmt)
    ImageView iv_circleinfo_level_lv1_fmt;
    @BindView(R.id.iv_circleinfo_level_lv2_fmt)
    ImageView iv_circleinfo_level_lv2_fmt;
    @BindView(R.id.iv_circleinfo_level_lv3_fmt)
    ImageView iv_circleinfo_level_lv3_fmt;
    @BindView(R.id.iv_circleinfo_level_lv4_fmt)
    ImageView iv_circleinfo_level_lv4_fmt;
    @BindView(R.id.iv_circleinfo_level_lv5_fmt)
    ImageView iv_circleinfo_level_lv5_fmt;
    //经验百分比
    @BindView(R.id.tv_circleinfo_level_experience__fmt)
    TextView tv_circleinfo_level_experience__fmt;
    //签到
    @BindView(R.id.iv_circleinfo_level_sign_fmt)
    ImageView iv_circleinfo_level_sign_fmt;
    //使用徽章
    @BindView(R.id.tv_circleinfo_level_use_fmt)
    TextView tv_circleinfo_level_use_fmt;

    public LevelFragment(long circleId) {
        this.circleId = circleId;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_level;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //关闭seekbar的触摸事件
        msb_circleinfo_level_experience_fmt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initDatas();
        iv_circleinfo_level_sign_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().signup(String.valueOf(circleId), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                signSuccess(noDataBean.isSuccess(), noDataBean.getMsg());

                            }
                        });
            }
        });
        tv_circleinfo_level_use_fmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTApplication.getRequestService().useBadge("1", String.valueOf(circleId), PTApplication.userToken, PTApplication.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("onError" + e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    ToastUtils.getToast(mContext, "使用成功");
                                } else {
                                    ToastUtils.getToast(mContext, noDataBean.getMsg());
                                    Logger.e(noDataBean.getMsg());
                                }
                            }
                        });
            }
        });
    }

    private void signSuccess(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.getToast(mContext, "签到成功");
            iv_circleinfo_level_sign_fmt.setImageResource(R.drawable.sgin_in_success);
            iv_circleinfo_level_sign_fmt.setVisibility(View.GONE);
        } else {
            ToastUtils.getToast(mContext, msg);
        }

    }

    private void initDatas() {
        PTApplication.getRequestService().getCircleInfo(circleId, PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EnterCircleInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(EnterCircleInfoBean enterCircleInfoBean) {
                        Logger.e("isSuccess" + enterCircleInfoBean.isSuccess());
                        if (enterCircleInfoBean.isSuccess()) {
                            initExperience(enterCircleInfoBean.getData());
                        }
                    }
                });
    }

    private void initExperience(EnterCircleInfoBean.DataBean data) {
        if (data.getCircle().isSign()) {
            iv_circleinfo_level_sign_fmt.setVisibility(View.GONE);
        }
        if (data.getExperience() >= 0) {
            Logger.e("true" + data.getExperience() * 125 / 100);
            msb_circleinfo_level_experience_fmt.setProgress(data.getExperience() * 125 / 100);
            tv_circleinfo_level_experience__fmt.setText(data.getExperience() + "/500");
        } else {
            msb_circleinfo_level_experience_fmt.setProgress(0);
            tv_circleinfo_level_experience__fmt.setText(0 + "/500");
        }
        int which = data.getExperience() * 125 / 100 / 125;
        switch (which) {
            case 0:
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_encircle);
                break;
            case 1:
                iv_circleinfo_level_lv2_fmt.setImageResource(R.drawable.progress_encircle);
                break;
            case 2:
                iv_circleinfo_level_lv3_fmt.setImageResource(R.drawable.progress_encircle);
                break;
            case 3:
                iv_circleinfo_level_lv4_fmt.setImageResource(R.drawable.progress_encircle);
                break;
            case 4:
                iv_circleinfo_level_lv5_fmt.setImageResource(R.drawable.progress_encircle);
                break;
        }

    }
}
