package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import pro.yueyuan.project_t.R;

/**
 * Created by xuq on 2017/3/15.
 */

public class ConversationActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
    }
}
