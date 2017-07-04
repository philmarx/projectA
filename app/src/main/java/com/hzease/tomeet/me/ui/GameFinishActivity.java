package com.hzease.tomeet.me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.widget.adapters.AddscoreAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/4.
 */

public class GameFinishActivity extends NetActivity {
    //定义一个图片数组
    int[] gameType = {R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};
    @BindView(R.id.tv_finish_roomname_fmt)
    TextView tv_finish_roomname_fmt;
    @BindView(R.id.tv_finish_roomtime_fmt)
    TextView tv_finish_roomtime_fmt;
    @BindView(R.id.tv_finish_roomplace_fmt)
    TextView tv_finish_roomplace_fmt;
    @BindView(R.id.tv_finish_roommoney_fmt)
    TextView tv_finish_roommoney_fmt;
    @BindView(R.id.tv_finish_roomnotice_fmt)
    TextView tv_finish_roomnotice_fmt;
    @BindView(R.id.lv_me_finishgame_fmt)
    ListView lv_me_finishgame_fmt;
    @BindView(R.id.iv_typeIcon_fmt)
    ImageView iv_typeIcon_fmt;
    long roomId;
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_gamefinish;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = intent.getLongExtra("roomId",0);
        PTApplication.getRequestService().gameFinishInfo(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameFinishBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GameFinishBean gameFinishBean) {
                        if (gameFinishBean.isSuccess()) {
                            showFinishInfo(gameFinishBean.getData());
                        } else {
                            Logger.e(gameFinishBean.getMsg());
                        }

                    }
                });
    }

    private void showFinishInfo(final GameFinishBean.DataBean data) {
        iv_typeIcon_fmt.setImageResource(gameType[data.getRoom().getGame().getId()-6]);
        tv_finish_roomname_fmt.setText(data.getRoom().getName());
        String time = "活动时间：";
        String beginTime = data.getRoom().getBeginTime();
        beginTime = beginTime.replace("-", ".");
        String endTime = data.getRoom().getEndTime();
        endTime = endTime.replace("-", ".");
        time = time + beginTime + " - " + endTime;
        tv_finish_roomtime_fmt.setText(time);
        tv_finish_roomplace_fmt.setText("活动地点：" + data.getRoom().getPlace());
        tv_finish_roommoney_fmt.setText("保证金：" + data.getRoom().getMoney()/100.0f + "元");
        tv_finish_roomnotice_fmt.setText("活动介绍：" + data.getRoom().getDescription());
        AddscoreAdapter adapter = new AddscoreAdapter(data.getMembers(),this);
        lv_me_finishgame_fmt.setAdapter(adapter);
        lv_me_finishgame_fmt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameFinishActivity.this, PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",data.getMembers().get(position).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
