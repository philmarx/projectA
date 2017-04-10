package pro.yueyuan.project_t;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @OnClick({
            R.id.tv_personspace_sendoredit_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_personspace_sendoredit_fmt:
                if (type == SEND_NOTE){
                    initPopupWindow(v);
                }else{
                    Logger.e("编辑");
                }
                break;
        }
    }

    private void initPopupWindow(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_smallpaper, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
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
