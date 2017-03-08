package pro.yueyuan.project_t.me;

import javax.inject.Inject;

import pro.yueyuan.project_t.data.source.PTRepository;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class MinePresenter implements IMineContract.Presenter {

    private final PTRepository mPTRepository;

    private final IMineContract.View mMineView;

    @Inject
    public MinePresenter(PTRepository mPTRepository, IMineContract.View mHomeView) {
        this.mPTRepository = mPTRepository;
        this.mMineView = mHomeView;
    }

    @Inject
    void setupListeners() {
        mMineView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }


}
