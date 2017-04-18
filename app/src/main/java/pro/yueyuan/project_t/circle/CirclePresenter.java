package pro.yueyuan.project_t.circle;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.data.source.PTRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class CirclePresenter implements ICircleContract.Presenter {

    private final PTRepository mPTRepository;

    private final ICircleContract.View mCircleView;

    @Inject
    public CirclePresenter(PTRepository mPTRepository, ICircleContract.View mCircleView) {
        this.mPTRepository = mPTRepository;
        this.mCircleView = mCircleView;
    }

    @Inject
    void setupListeners() {
        mCircleView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }
}
