package pro.yueyuan.project_t.data.source;

import javax.inject.Inject;

/**
 * Created by Key on 2016/11/28 22:36
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTRepository implements IPTDataSource {

    private final IPTDataSource mPTLocalDataSource;

    private final IPTDataSource mPTRemoteDataSource;

    @Inject
    public PTRepository(@Local IPTDataSource mPTLocalDataSource, @Remote IPTDataSource mPTRemoteDataSource) {
        this.mPTLocalDataSource = mPTLocalDataSource;
        this.mPTRemoteDataSource = mPTRemoteDataSource;
    }

}
