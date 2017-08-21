package com.hzease.tomeet.circle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/7/26.
 */

public class MotifityCircleActivity extends NetActivity {
    @BindView(R.id.et_circlenotices_content_fmt)
    EditText et_circlenotices_content_fmt;
    private long circleId;

    @OnClick({
            R.id.bt_circlenotices_moditity_fmt
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_circlenotices_moditity_fmt:
                final String notices = et_circlenotices_content_fmt.getText().toString().trim();
                PTApplication.getRequestService().moditityNotice(circleId, notices, PTApplication.userToken, PTApplication.userId)
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
                                if (noDataBean.isSuccess()) {
                                    modifitySuccess(noDataBean.getMsg());
                                }else{
                                    ToastUtils.getToast(noDataBean.getMsg());
                                }
                            }
                        });
                break;
        }
    }

    private void modifitySuccess(String msg) {
        ToastUtils.getToast("修改圈子成功!!!");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_motifitycircle_first;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Intent intent = getIntent();
        circleId = intent.getLongExtra("circleId",0);
    }
}
