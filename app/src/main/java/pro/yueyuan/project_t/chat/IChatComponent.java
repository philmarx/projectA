package pro.yueyuan.project_t.chat;

import dagger.Component;
import pro.yueyuan.project_t.chat.ui.ChatVersion2Activity;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
import pro.yueyuan.project_t.utils.FragmentScoped;

/**
 * Created by Key on 2017/3/24 17:19
 * email: MrKey.K@gmail.com
 * description:
 */

@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = ChatPresenterModule.class)
public interface IChatComponent {
    void inject(ChatVersion2Activity mChatVersion2Activity);
}
