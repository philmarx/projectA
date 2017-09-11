package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/23.
 */

public class BindOthersFragment extends BaseFragment {

    @BindView(R.id.tv_setting_bindingwechat_fmt)
    TextView tv_setting_bindingwechat_fmt;
    @BindView(R.id.rl_bind_wechat_fmt)
    AutoRelativeLayout rl_bind_wechat_fmt;
    @BindView(R.id.tv_setting_bindingqq_fmt)
    TextView tv_setting_bindingqq_fmt;
    @BindView(R.id.rl_bind_qq_fmt)
    AutoRelativeLayout rl_bind_qq_fmt;
    @BindView(R.id.tv_binding_weibo_fmt)
    TextView tv_binding_weibo_fmt;
    @BindView(R.id.rl_bind_weibo_fmt)
    AutoRelativeLayout rl_bind_weibo_fmt;

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    @OnClick({
            R.id.rl_bind_wechat_fmt,
            R.id.rl_bind_qq_fmt,
            R.id.rl_bind_weibo_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_bind_wechat_fmt:
                if (!tv_setting_bindingwechat_fmt.getText().toString().equals("已绑定")){
                    //去绑定
                    // 转圈
                    showLoadingDialog();
                    //绑定微信
                    UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            Logger.e("onStart：" + share_media.name());
                            hideLoadingDialog();
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            Logger.i("onComplete:WX   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                            PTApplication.getRequestService().bind3Part(PTApplication.userToken,"WECHAT",map.get("unionid"),PTApplication.userId)
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
                                            Bind3Part(noDataBean.isSuccess(),noDataBean.getMsg());
                                        }
                                    });
                        }
                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            Logger.e("onError: " + throwable.getMessage());
                            hideLoadingDialog();
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Logger.e("onCancel: " + share_media.toString());
                            hideLoadingDialog();
                        }
                    });
                }else{
                    //去解绑微信
                    Logger.e("123123123");
                    UnboundFragment unboundFragment = UnboundFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("boundtype","微信号");
                    bundle.putString("type","WECHAT");
                    unboundFragment.setArguments(bundle);
                    transaction.replace(R.id.fl_content_me_activity,unboundFragment);
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.rl_bind_qq_fmt:
                if (!tv_setting_bindingqq_fmt.getText().toString().equals("已绑定")){
                    showLoadingDialog();
                    //绑定QQ
                    UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            Logger.e("onStart: " + share_media.toString());
                            hideLoadingDialog();
                        }
                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            Logger.i("onComplete:QQ   " + share_media.toString() + "\n\nmap: " + map.toString() + "\n\ni: " + i);
                            PTApplication.getRequestService().bind3Part(PTApplication.userToken,"QQ",map.get("openid"),PTApplication.userId)
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
                                            Bind3Part(noDataBean.isSuccess(),noDataBean.getMsg());
                                        }
                                    });
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            Logger.e("onError");
                            hideLoadingDialog();
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            Logger.e("onCancel");
                            hideLoadingDialog();
                        }
                    });
                }else{
                    Logger.e("123123123");
                    UnboundFragment unboundFragment = UnboundFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("boundtype","QQ号");
                    bundle.putString("type","QQ");
                    unboundFragment.setArguments(bundle);
                    transaction.replace(R.id.fl_content_me_activity,unboundFragment);
                    // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
            case R.id.rl_bind_weibo_fmt:
                ToastUtils.getToast("暂未开通");
                break;
        }
    }

    //绑定成功后
    private void Bind3Part(boolean success, String msg) {
        if (success){
            ToastUtils.getToast("绑定成功");
            getActivity().getSupportFragmentManager().popBackStack();
        }else{
            ToastUtils.getToast(msg);
        }
    }

    public static BindOthersFragment newInstance() {
        return new BindOthersFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_bindothers;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        PTApplication.getRequestService().isBind3Part(PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MapDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MapDataBean mapDataBean) {
                        if (mapDataBean.isSuccess()){
                            Map<String, Boolean> data = mapDataBean.getData();
                            for (String key:data.keySet()) {
                                switch (key){
                                    case "QQ":
                                        if (data.get(key)){
                                            tv_setting_bindingqq_fmt.setText("已绑定");
                                        }
                                        break;
                                    case "WEIBO":
                                        if (data.get(key)){
                                            tv_binding_weibo_fmt.setText("已绑定");
                                        }
                                        break;
                                    case "WECHAT":
                                        if (data.get(key)){
                                            tv_setting_bindingwechat_fmt.setText("已绑定");
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    public void showLoadingDialog() {
        ((MeActivity) getActivity()).showLoadingDialog();
    }
    public void hideLoadingDialog() {
        ((MeActivity) getActivity()).hideLoadingDialog();
    }
}
