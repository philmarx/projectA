package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuq on 2017/6/5.
 */

public class RechargeResultFragment extends BaseFragment {
    boolean isSuccess;
    FragmentTransaction transaction;
    //充值结果的图标
    @BindView(R.id.iv_rechargeresult_resulticon_fmt)
    ImageView iv_rechargeresult_resulticon_fmt;
    //充值结果的文字
    @BindView(R.id.tv_rechargeresult_resulticon_fmt)
    TextView tv_rechargeresult_resulticon_fmt;
    //我的钱包
    @BindView(R.id.bt_rechargeresult_mywallet_fmt)
    Button bt_rechargeresult_mywallet_fmt;
    //继续充值
    @BindView(R.id.bt_rechargeresult_rechargeagain_fmt)
    Button bt_rechargeresult_rechargeagain_fmt;
    @OnClick({
            R.id.bt_rechargeresult_mywallet_fmt,
            R.id.bt_rechargeresult_rechargeagain_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_rechargeresult_mywallet_fmt:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.bt_rechargeresult_rechargeagain_fmt:
                // 将 fragment_container View 中的内容替换为此 Fragment ，
                transaction.replace(R.id.fl_content_me_activity, RechargeFragment.newInstance());
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
        }
    }
    public RechargeResultFragment(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_result;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (isSuccess){
            //充值成功
            iv_rechargeresult_resulticon_fmt.setImageResource(R.drawable.recharg_success);
            tv_rechargeresult_resulticon_fmt.setText("充值成功");
            bt_rechargeresult_rechargeagain_fmt.setText("继续充值");
        }else{
            //充值失败
            iv_rechargeresult_resulticon_fmt.setImageResource(R.drawable.recharg_falied);
            tv_rechargeresult_resulticon_fmt.setText("充值失败");
            bt_rechargeresult_rechargeagain_fmt.setText("重新充值");
        }
    }
}
