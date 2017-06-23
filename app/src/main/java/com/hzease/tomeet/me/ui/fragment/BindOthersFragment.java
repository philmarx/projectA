package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MapDataBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

    @OnClick({
            R.id.rl_bind_wechat_fmt,
            R.id.rl_bind_qq_fmt,
            R.id.rl_bind_weibo_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_bind_wechat_fmt:
                //绑定微信
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart：" + share_media.name());
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
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel: " + share_media.toString());
                    }
                });
                break;
            case R.id.rl_bind_qq_fmt:
                //绑定QQ
                UMShareAPI.get(PTApplication.getInstance()).getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Logger.e("onStart: " + share_media.toString());
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
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Logger.e("onCancel");
                    }
                });
                break;
            case R.id.rl_bind_weibo_fmt:
                ToastUtils.getToast(mContext,"暂未开通");
                break;
        }
    }

    //绑定成功后
    private void Bind3Part(boolean success, String msg) {
        if (success){
            ToastUtils.getToast(mContext,"绑定成功");
            getActivity().getSupportFragmentManager().popBackStack();
        }else{
            ToastUtils.getToast(mContext,msg);
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
                                            rl_bind_qq_fmt.setEnabled(false);
                                        }
                                        break;
                                    case "WEIBO":
                                        if (data.get(key)){
                                            tv_binding_weibo_fmt.setText("已绑定");
                                            rl_bind_weibo_fmt.setEnabled(false);
                                        }
                                        break;
                                    case "WECHAT":
                                        if (data.get(key)){
                                            tv_setting_bindingwechat_fmt.setText("已绑定");
                                            rl_bind_wechat_fmt.setEnabled(false);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }
}
