package pro.yueyuan.project_t.home.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.orhanobut.logger.Logger;

import butterknife.BindView;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.ActivityTypeBean;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivityBak extends NetActivity {


    @BindView(R.id.creatRoom)
    Button creatRoom;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;

    @Override
    protected void netInit(Bundle savedInstanceState) {
        mRequestService.getActivityType("secret","app.yueyuan.pro")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivityTypeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ActivityTypeBean activityTypeBean) {
                        Logger.e(activityTypeBean.isSuccess()+"");
                    }
                });
       creatRoom.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {

           }
       });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_homebak;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }
}
