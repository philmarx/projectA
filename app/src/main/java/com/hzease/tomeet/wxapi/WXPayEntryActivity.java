package com.hzease.tomeet.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseActivity;
import com.hzease.tomeet.R;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

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

    private IWXAPI api;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_result;
    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        String success = getIntent().getStringExtra("alipayIsSuccess");
        if (!TextUtils.isEmpty(success)) {
            // 默认显示叉叉
            switch (success) {
                case "9000":
                    // 充值成功
                    iv_rechargeresult_resulticon_fmt.setImageResource(R.drawable.recharg_success);
                    tv_rechargeresult_resulticon_fmt.setText("充值成功");
                    break;
                case "8000":
                    // 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    tv_rechargeresult_resulticon_fmt.setText("订单正在处理中");
                    break;
                case "4000":
                    // 订单支付失败
                    tv_rechargeresult_resulticon_fmt.setText("订单支付失败");
                    break;
                case "5000":
                    // 重复请求
                    tv_rechargeresult_resulticon_fmt.setText("订单支付失败");
                    break;
                case "6001":
                    // 用户中途取消
                    tv_rechargeresult_resulticon_fmt.setText("取消充值");
                    break;
                case "6002":
                    // 网络连接出错
                    tv_rechargeresult_resulticon_fmt.setText("网络连接出错");
                    break;
                case "6004":
                    // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    tv_rechargeresult_resulticon_fmt.setText("支付结果未知，请查询余额或明细");
                    break;
                default:
                    // 其它支付错误
                    tv_rechargeresult_resulticon_fmt.setText("其它支付错误");
                    break;
            }
        }

        api = WXAPIFactory.createWXAPI(this, AppConstants.TOMEET_WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void beforeInit(Bundle savedInstanceState) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.e( "onPayFinish, errCode = " + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Logger.e( "onPayFinish, errCode = " + baseResp.errCode + "\ntype: " + baseResp.getType());
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // 默认显示叉叉
            switch(baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    // 充值成功
                    iv_rechargeresult_resulticon_fmt.setImageResource(R.drawable.recharg_success);
                    tv_rechargeresult_resulticon_fmt.setText("充值成功");
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    // 充值失败，或者签名错误
                    tv_rechargeresult_resulticon_fmt.setText("充值失败");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    // 用户取消
                    tv_rechargeresult_resulticon_fmt.setText("取消充值");
                    break;
            }
        }
    }

    @OnClick({
            R.id.bt_rechargeresult_mywallet_fmt,
            R.id.bt_rechargeresult_rechargeagain_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            // 返回我的钱包
            case R.id.bt_rechargeresult_mywallet_fmt:
                // TODO: 2017/6/13 返回钱包
                break;
            // 继续充值
            case R.id.bt_rechargeresult_rechargeagain_fmt:
                break;
        }
        finish();
    }
}
