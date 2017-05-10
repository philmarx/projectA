package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/5/10.
 */

public class ActivityFragment extends Fragment {

    long circleId;
    private RecyclerView recyclerView;

    public ActivityFragment(long circleId) {
        this.circleId = circleId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameincircle,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_circlegame_item);
        initDatas();
        return view;
    }

    private void initDatas() {
        PTApplication.getRequestService().findRoomsByCircle(circleId,0,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeRoomsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeRoomsBean homeRoomsBean) {
                        if (homeRoomsBean.isSuccess()){
                            initAdapter(homeRoomsBean.getData());
                        }
                    }
                });
    }

    private void initAdapter(List<HomeRoomsBean.DataBean> data) {
        MyJoinRoomsAdapter adapter = new MyJoinRoomsAdapter(data,getContext());
        recyclerView.setAdapter(adapter);
    }
}
