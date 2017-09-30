package com.hzease.tomeet.me.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 提现界面
 * Created by xuq on 2017/6/21.
 */

public class WithdrawalsFragment extends BaseFragment {
    @BindView(R.id.et_withdrawals_alipayAmount_fmt)
    EditText et_withdrawals_alipayAmount_fmt;
    @BindView(R.id.tv_withdrawals_isRealName_fmt)
    TextView tv_withdrawals_isRealName_fmt;
    @BindView(R.id.et_withdrawals_money_fmt)
    EditText et_withdrawals_money_fmt;
    @BindView(R.id.tv_deposit_isOut_fmt)
    TextView tv_deposit_isOut_fmt;
    @BindView(R.id.tv_withdrawals_desc_fmt)
    TextView tv_withdrawals_desc_fmt;
    @BindView(R.id.load_View)
    AVLoadingIndicatorView load_View;
    private FragmentTransaction transaction;
    private MeActivity meActivity;

    @OnClick({
            R.id.bt_withdrawals_apply_fmt,
            R.id.tv_withdrawals_all_fmt,
            R.id.tv_withdrawals_isRealName_fmt
    })
    public void onClick(final View v){
        switch (v.getId()){
            case R.id.bt_withdrawals_apply_fmt:
                if (et_withdrawals_alipayAmount_fmt.getText().toString().trim().isEmpty()){
                    ToastUtils.getToast("请输入支付宝账号");
                }else if (et_withdrawals_money_fmt.getText().toString().trim().isEmpty()){
                    ToastUtils.getToast("请输入提现金额");
                }else{
                    String alipayAmount = et_withdrawals_alipayAmount_fmt.getText().toString().trim();
                    final String money = String.valueOf((Double.valueOf(et_withdrawals_money_fmt.getText().toString().trim())*100)).split("\\.")[0];
                    if (money.equals("0")){
                        ToastUtils.getToast("最少提现0.1元");
                        return;
                    }
                    PTApplication.getRequestService().withdrawals(alipayAmount,money,PTApplication.PT_USER_IMEI,PTApplication.userToken,PTApplication.userId)
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
                                    Logger.e("isSuccess" + noDataBean.toString());
                                    if (noDataBean.isSuccess()){
                                        ToastUtils.getToast("提现成功！");
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }else{
                                        ToastUtils.getToast(noDataBean.getMsg());
                                        if ("请先实名认证".equals(noDataBean.getMsg())){
                                            initWithdrawalsPop(v);
                                        }
                                    }
                                }
                            });
                }
                break;
            case R.id.tv_withdrawals_all_fmt:
                et_withdrawals_money_fmt.setText(String.valueOf(PTApplication.myInfomation.getData().getAmount()/100.0));
                break;
            case R.id.tv_withdrawals_isRealName_fmt:
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(3));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    //提示跳转到实名认证
    private void initWithdrawalsPop(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_outreason, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getActivity().getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getActivity().getWindow().setAttributes(wlBackground);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlBackground);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        Button istrue =  contentView.findViewById(R.id.bt_outreason_true_fmt);
        Button cancel =  contentView.findViewById(R.id.bt_outreason_cancel_fmt);
        TextView tv_outreason_reason_fmt =  contentView.findViewById(R.id.tv_outreason_reason_fmt);
        tv_outreason_reason_fmt.setText("是否跳转到实名认证");
        istrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                transaction.replace(R.id.fl_content_me_activity, meActivity.mFragmentList.get(3));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public static WithdrawalsFragment newInstance() {
        return new WithdrawalsFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_withdrawals;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Logger.e(String.valueOf(Float.valueOf(PTApplication.myInfomation.getData().getAmount())/100.0));
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        if (PTApplication.myInfomation.getData().isAuthorized()){
            tv_withdrawals_isRealName_fmt.setText(PTApplication.myInfomation.getData().getRealName());
            tv_withdrawals_isRealName_fmt.setTextColor(Color.rgb(184,184,184));
            tv_withdrawals_isRealName_fmt.setEnabled(false);
        }
        tv_withdrawals_desc_fmt.setText("可提现金额"+String.valueOf(PTApplication.myInfomation.getData().getAmount()/100.0)+"元");
        et_withdrawals_money_fmt.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_withdrawals_money_fmt != null){
                    if (!et_withdrawals_money_fmt.getText().toString().isEmpty()) {
                        if (Double.valueOf(et_withdrawals_money_fmt.getText().toString()) > Double.valueOf(PTApplication.myInfomation.getData().getAmount())/100.0) {
                            tv_deposit_isOut_fmt.setVisibility(View.VISIBLE);
                        } else {
                            tv_deposit_isOut_fmt.setVisibility(View.GONE);
                        }
                    }
                    if (isChanged) {// ----->如果字符未改变则返回
                        return;
                    }
                    String str = s.toString();

                    isChanged = true;
                    String cuttedStr = str;
                /* 删除字符串中的dot */
                    for (int i = str.length() - 1; i >= 0; i--) {
                        char c = str.charAt(i);
                        if ('.' == c) {
                            cuttedStr = str.substring(0, i) + str.substring(i + 1);
                            break;
                        }
                    }
                /* 删除前面多余的0 */
                    int NUM = cuttedStr.length();
                    int zeroIndex = -1;
                    for (int i = 0; i < NUM - 2; i++) {
                        char c = cuttedStr.charAt(i);
                        if (c != '0') {
                            zeroIndex = i;
                            break;
                        }else if(i == NUM - 3){
                            zeroIndex = i;
                            break;
                        }
                    }
                    if(zeroIndex != -1){
                        cuttedStr = cuttedStr.substring(zeroIndex);
                    }
                /* 不足3位补0 */
                    if (cuttedStr.length() < 3) {
                        cuttedStr = "0" + cuttedStr;
                    }
                /* 加上dot，以显示小数点后两位 */
                    cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)
                            + "." + cuttedStr.substring(cuttedStr.length() - 2);

                    et_withdrawals_money_fmt.setText(cuttedStr);

                    et_withdrawals_money_fmt.setSelection(et_withdrawals_money_fmt.length());
                    isChanged = false;
                }
                }
        });
    }
}
