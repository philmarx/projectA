package com.hzease.tomeet.circle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.MyJoinRoomsAdapter;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuq on 2017/5/10.
 *
 */

public class ActivityFragment extends Fragment {


    long circleId;
    private RecyclerView recyclerView;
    private MyJoinRoomsAdapter adapter;

    public ActivityFragment() {
    }

    public ActivityFragment(long circleId) {
        this.circleId = circleId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameincircle,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_circlegame_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
        adapter = new MyJoinRoomsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        initDatas();
        return view;
    }

    private void initDatas() {
        PTApplication.getRequestService().findRoomsByCircle(circleId, 0, 10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyJoinRoomsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError",e.getMessage());
                    }

                    @Override
                    public void onNext(MyJoinRoomsBean homeRoomsBean) {
                        Logger.e(""+homeRoomsBean.isSuccess() + "    msg : " + homeRoomsBean.getMsg());
                        // TODO: 2017/5/19  msg : Optional int parameter 'state' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.
                        if (homeRoomsBean.isSuccess()){
                            adapter.setList(homeRoomsBean.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
