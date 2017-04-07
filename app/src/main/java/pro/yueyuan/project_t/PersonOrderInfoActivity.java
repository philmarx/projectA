package pro.yueyuan.project_t;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.yueyuan.project_t.data.RankingBean;
import pro.yueyuan.project_t.data.UserOrderBean;
import pro.yueyuan.project_t.widget.adapters.OrderListAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/4/6.
 */

public class PersonOrderInfoActivity extends NetActivity {


    int SEND_NOTE = 001;
    int EDIT_PIC = 002;
    int type = EDIT_PIC;


    @BindView(R.id.tv_personspace_username_fmt)
    TextView tv_personspace_username_fmt;
    @BindView(R.id.tv_personspace_sendoredit_fmt)
    TextView tv_personspace_sendoredit_fmt;
    @BindView(R.id.tv_personspace_usernamebak_fmt)
    TextView tv_personspace_usernamebak_fmt;
    @BindView(R.id.lv_personspace_order_fmt)
    ListView lv_personspace_order_fmt;

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personspace;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        long userId = bundle.getLong("userId");
        Logger.e(userId + "");
        if (String.valueOf(userId).equals(PTApplication.userId)) {
            type = EDIT_PIC;
            tv_personspace_sendoredit_fmt.setText("编辑");
        }else{
            type=SEND_NOTE;
            tv_personspace_sendoredit_fmt.setText("传纸条");
        }
        PTApplication.getRequestService().getOrderById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserOrderBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(UserOrderBean userOrderBean) {
                        Logger.e("onNext");
                        tv_personspace_username_fmt.setText(userOrderBean.getData().getNickname());
                        tv_personspace_usernamebak_fmt.setText(userOrderBean.getData().getNickname());
                        lv_personspace_order_fmt.setAdapter(new OrderListAdapter((ArrayList<UserOrderBean.DataBean.OrdersBean>) userOrderBean.getData().getOrders()));
                    }
                });
    }
}
