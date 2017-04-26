package pro.yueyuan.project_t.me;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import io.realm.Realm;
import io.rong.imkit.RongIM;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.FeedBackBean;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UpdatePwdBean;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.data.UserOrderBean;
import pro.yueyuan.project_t.data.source.PTRepository;
import pro.yueyuan.project_t.utils.ToastUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
     * 加载我的账户信息
     */
    @Override
    public void loadMyInfo() {
        PTApplication.getRequestService().getMyInfomation(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        ToastUtils.getToast(PTApplication.getInstance(), "加载用户信息失败，请检查网络");
                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        Logger.e("userInfoBean:  " + userInfoBean.toString());
                        if (userInfoBean.isSuccess()) {
                            PTApplication.myInfomation = userInfoBean;
                            mMeView.showMyInfo();
                        } else {
                            ToastUtils.getToast(PTApplication.getInstance(), userInfoBean.getMsg());
                        }
                    }
                });
    }


    /**
     * 注销用户
     */
    @Override
    public void logoutUser() {
        PTApplication.userId = "";
        PTApplication.userToken = "";
        // 注销个人信息
        PTApplication.myInfomation = null;
        // 清空本地保存
        mPTRepository.saveUserIdAndToken();
        // 注销融云
        RongIM.getInstance().logout();
        PTApplication.isRongCloudInit = false;
        Realm.removeDefaultConfiguration();
        // 注销阿里云OSS
        PTApplication.aliyunOss = null;
        PTApplication.aliyunOssExpiration = 0;
       // 停止发送友盟用户信息
        MobclickAgent.onProfileSignOff();
    }

    @Override
    public void getMyJoinRooms(Integer page, Integer size, String token, String userId) {
        PTApplication.getRequestService().getMyRooms("0","10",token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyJoinRoomBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(MyJoinRoomBean myJionRoomBean) {
                        Logger.e("getMyJoinRooms....onNext:::  " + myJionRoomBean.isSuccess());
                        if (myJionRoomBean.isSuccess()){
                            mMeView.showMyRooms(myJionRoomBean);
                        }
                    }
                });
    }

    @Override
    public void getOrderById(long userId) {
        PTApplication.getRequestService().getOrderById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserOrderBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(UserOrderBean userOrderBean) {
                        Logger.e("onNext");
                        if (userOrderBean.isSuccess()){
                            mMeView.showRequestUserOrder(userOrderBean);
                        }
                    }
                });
    }

    @Override
    public void updatePwd(String password, String password2, String token, String userId) {
        PTApplication.getRequestService().updatePwd(password,password2,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdatePwdBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mMeView.updatePwdSuccess(false,"网络连接失败");
                    }

                    @Override
                    public void onNext(UpdatePwdBean updatePwdBean) {
                        Logger.e("onNext");
                        if (updatePwdBean.isSuccess()){
                            Logger.e(""+updatePwdBean.isSuccess());
                            mMeView.updatePwdSuccess(true,updatePwdBean.getMsg());
                            Logger.e(updatePwdBean.getMsg());
                        }else{
                            mMeView.updatePwdSuccess(false,updatePwdBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 提交反馈
     *
     * @param content
     * @param token
     * @param userId
     */
    @Override
    public void feedBack(String content, String token, String userId) {
        PTApplication.getRequestService().feedBack(content,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeedBackBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeView.feedBackSuccess(false,"网络连接失败");
                    }

                    @Override
                    public void onNext(FeedBackBean feedBackBean) {
                        if (feedBackBean.isSuccess()){
                            mMeView.feedBackSuccess(true,feedBackBean.getMsg());
                        }else {
                            mMeView.feedBackSuccess(false,feedBackBean.getMsg());
                        }
                    }
                });
    }

}
