package com.hzease.tomeet.chat.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.InvitationsBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.InvitationsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class AddFriendFragment extends BaseFragment {

    @BindView(R.id.lv_phone_invitations_fmt)
    ListView lv_phone_invitations_fmt;

    public AddFriendFragment() {
    }

    public static AddFriendFragment newInstance() {
        return new AddFriendFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_add_friend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        PTApplication.getRequestService().getInviations(PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InvitationsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InvitationsBean invitationsBean) {
                        initInvitations(invitationsBean.isSuccess(),invitationsBean.getData());
                    }
                });
    }

    private void initInvitations(boolean success, List<InvitationsBean.DataBean> data) {
        if (success){
            lv_phone_invitations_fmt.setAdapter(new InvitationsAdapter(data,mContext));
        }else{
            ToastUtils.getToast(mContext,"网络链接错误");
        }
    }

    @OnClick(R.id.all_add_friend_fmt)
    public void onViewClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.REQUEST_CODE_CONTACT);
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_content_add_friend_activity, AddPhoneContactsFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.REQUEST_CODE_CONTACT) {
            if (grantResults[0] == 0) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_content_add_friend_activity, AddPhoneContactsFragment.newInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                ToastUtils.getToast(mContext, "通讯录权限获取失败，无法读取联系人");
            }
        }
    }
}
