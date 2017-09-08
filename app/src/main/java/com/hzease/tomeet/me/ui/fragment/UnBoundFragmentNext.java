package com.hzease.tomeet.me.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/9/8.
 */

public class UnBoundFragmentNext extends BaseFragment {
    @BindView(R.id.tv_unbound_head)
    TextView tv_unbound_head;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.bt_sure_unbound)
    Button bt_sure_unbound;
    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;

    public static UnBoundFragmentNext newInstance(){
        return new UnBoundFragmentNext();
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_sureunbound;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        Bundle bundle = getArguments();
        final String type = bundle.getString("type");
        String boundtype = bundle.getString("boundtype");
        tv_unbound_head.setText("解绑" + boundtype);
        bt_sure_unbound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PTApplication.getRequestService().unBind3Part(et_pwd.getText().toString().trim(),PTApplication.userToken,type,PTApplication.userId)
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
                                if (noDataBean.isSuccess()){
                                    ToastUtils.getToast("解绑成功");
                                    meActivity.getSupportFragmentManager().popBackStack();
                                }else{
                                    ToastUtils.getToast(noDataBean.getMsg());
                                }
                            }
                        });
            }
        });
    }
}
