package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.chat.ChatPresenter;
import pro.yueyuan.project_t.chat.ChatPresenterModule;
import pro.yueyuan.project_t.chat.DaggerIChatComponent;
import pro.yueyuan.project_t.chat.IChatContract;
import pro.yueyuan.project_t.utils.ActivityUtils;

/**
 * Created by Key on 2017/3/24 16:56
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatVersion2Activity extends NavigationActivity {

    @Inject
    ChatPresenter mChatPresenter;

    /**
     * fragment的集合
     */
    private ArrayList<Fragment> mFragmentList;

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_version2;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        navigation_bottom.getMenu().findItem(R.id.navigation_chat).setChecked(true).setEnabled(false);
        //初始化fragment集合
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //创建fragment
            ChatFragment chatFragment = ChatFragment.newInstance();
            mFragmentList.add(chatFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_chat_version2_activity);
        }

        // dagger2
        DaggerIChatComponent.builder()
                .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                // .chatPresenterModule过时的原因是：PTRepositoryModule中的注解出错 @Local和@Remote
                .chatPresenterModule(new ChatPresenterModule(((IChatContract.View) (mFragmentList.get(0)))))
                .build().inject(this);

    }
}
