package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
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
import com.hzease.tomeet.utils.EventUtil;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import io.rong.eventbus.EventBus;
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
    int mExperience;
    public LevelFragment(long circleId) {
        this.circleId = circleId;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_level;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Logger.i("注册EventBus");
        }
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
                                    ToastUtils.getToast("使用成功");
                                } else {
                                    ToastUtils.getToast(noDataBean.getMsg());
                                    Logger.e(noDataBean.getMsg());
                                }
                            }
                        });
            }
        });
    }

    private void signSuccess(boolean isSuccess, String msg) {
        if (isSuccess) {
            ToastUtils.getToast("签到成功");
            iv_circleinfo_level_sign_fmt.setImageResource(R.drawable.sgin_in_success);
            iv_circleinfo_level_sign_fmt.setVisibility(View.GONE);
            tv_circleinfo_level_experience__fmt.setText((mExperience+2) + "/2000");
        } else {
            ToastUtils.getToast(msg);
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
        int experience = data.getExperience();
        if (experience < 0 ){
            iv_circleinfo_level_sign_fmt.setVisibility(View.GONE);
            mExperience = 100;
        }else{
            mExperience = experience;
        }
        int level = 0;
        if (experience >=100 && experience <200){
            Logger.e("Level1~2");
            level = 1;
            msb_circleinfo_level_experience_fmt.setProgress((experience-100) * 5 );
            tv_circleinfo_level_experience__fmt.setText(experience + "/2000");
        }else if (experience >= 200 && experience <500){
            Logger.e("Level2~3");
            level = 2;
            msb_circleinfo_level_experience_fmt.setProgress(((experience-200) * 5/3)+500);
            tv_circleinfo_level_experience__fmt.setText(experience + "/2000");
        }else if (experience >= 500 && experience <1000){
            Logger.e("Level3~4");
            level = 3;
            msb_circleinfo_level_experience_fmt.setProgress((experience-500)+1000);
            tv_circleinfo_level_experience__fmt.setText(experience + "/2000");
        }else if (experience >= 1000 && experience <2000){
            Logger.e("Level4~5");
            level = 4;
            msb_circleinfo_level_experience_fmt.setProgress(((experience-1000 )/ 2 )+1500);
            tv_circleinfo_level_experience__fmt.setText(experience + "/2000");
        }else if (experience >= 2000){
            msb_circleinfo_level_experience_fmt.setProgress(experience);
            tv_circleinfo_level_experience__fmt.setText(experience + "/2000");
            level = 5;
        }else{
            msb_circleinfo_level_experience_fmt.setProgress(0);
            tv_circleinfo_level_experience__fmt.setText("0/2000");
        }
        switch (level){
            case 1:
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_encircle);
                break;
            case 2:
                iv_circleinfo_level_lv2_fmt.setImageResource(R.drawable.progress_encircle);
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_circle);
                break;
            case 3:
                iv_circleinfo_level_lv3_fmt.setImageResource(R.drawable.progress_encircle);
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_circle);
                break;
            case 4:
                iv_circleinfo_level_lv4_fmt.setImageResource(R.drawable.progress_encircle);
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_circle);
                break;
            case 5:
                iv_circleinfo_level_lv5_fmt.setImageResource(R.drawable.progress_encircle);
                iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_circle);
                break;
        }
    }
    public  void onEventMainThread(EventUtil event) {
        String msglog = "----onEventMainThread收到了消息："+event.getMsg();
        msb_circleinfo_level_experience_fmt.setProgress(0);
        tv_circleinfo_level_experience__fmt.setText("100/2000");
        iv_circleinfo_level_lv1_fmt.setImageResource(R.drawable.progress_encircle);
        iv_circleinfo_level_sign_fmt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }
}
