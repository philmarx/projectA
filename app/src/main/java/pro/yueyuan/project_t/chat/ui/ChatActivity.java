package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;

import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.R;

public class ChatActivity extends NavigationActivity{

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
        return R.layout.activity_chat;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        navigation_bottom.getMenu().findItem(R.id.navigation_chat).setChecked(true).setEnabled(false);
    }
}
