package pro.yueyuan.project_t.me;

import javax.inject.Inject;

import pro.yueyuan.project_t.data.source.PTRepository;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class MePresenter implements IMeContract.Presenter {

    private final PTRepository mPTRepository;

    private final IMeContract.View mMeView;

    @Inject
    public MePresenter(PTRepository mPTRepository, IMeContract.View mMeView) {
        this.mPTRepository = mPTRepository;
        this.mMeView = mMeView;
    }

    @Inject
    void setupListeners() {
        mMeView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }


    /**
     * 加载我的头像
     */
    @Override
    public void loadMyAvatar() {
        // mPTRepository;
    }
}