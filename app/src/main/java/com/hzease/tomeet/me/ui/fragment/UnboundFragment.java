package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.ThreePartBean;
import com.hzease.tomeet.me.ui.MeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/9/8.
 */

public class UnboundFragment extends BaseFragment {
    @BindView(R.id.tv_unbound_head)
    TextView tv_unbound_head;
    @BindView(R.id.iv_3part_avatar)
    ImageView iv_3part_avatar;
    @BindView(R.id.iv_3part_name)
    TextView iv_3part_name;
    @BindView(R.id.bt_3part_unbound)
    Button bt_3part_unbound;

    /**
     * 获得当前activiy
     */
    MeActivity meActivity;

    /**
     * 创建fragment事务管理器对象
     */
    FragmentTransaction transaction;
    public static UnboundFragment newInstance(){
        return new UnboundFragment();
    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_unbound;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        meActivity = (MeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        Bundle bundle = getArguments();
        final String boundtype = bundle.getString("boundtype");
        final String type = bundle.getString("type");
        tv_unbound_head.setText("解绑" + boundtype);
        PTApplication.getRequestService().getMy3PartInfo(PTApplication.userToken,PTApplication.userId,type)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoadingDialog();
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        hideLoadingDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThreePartBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ThreePartBean threePartBean) {
                        if (threePartBean.isSuccess()){
                            // 头像
                            Glide.with(mContext)
                                    .load(threePartBean.getData().getPhotoUrl())
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                                    .signature(new StringSignature(PTApplication.myInfomation.getData().getAvatarSignature()))
                                    .into(iv_3part_avatar);
                            if (type.equals("WECHAT")){
                                iv_3part_name.setText("已绑定微信: " + threePartBean.getData().getNickname());
                            }else{
                                iv_3part_name.setText("已绑定QQ: " + threePartBean.getData().getNickname());
                            }
                        }else{
                            if (type.equals("WECHAT")){
                                iv_3part_avatar.setImageResource(R.drawable.unbound_wechat);
                                iv_3part_name.setText("确定解绑微信号吗？");
                            }else{
                                iv_3part_avatar.setImageResource(R.drawable.unbound_qq);
                                iv_3part_name.setText("确定解绑QQ号吗？");
                            }
                        }
                    }
                });

        bt_3part_unbound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnBoundFragmentNext unboundFragment = UnBoundFragmentNext.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("type",type);
                bundle.putString("boundtype",boundtype);
                unboundFragment.setArguments(bundle);
                transaction.replace(R.id.fl_content_me_activity,unboundFragment);
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                //transaction.addToBackStack(null);
                transaction.commit();
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
