package pro.yueyuan.project_t;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.widget.plugins.LocationPlugin;

/**
 * Created by xuq on 2017/3/16.
 */

public class MyExtensionModule extends DefaultExtensionModule {
    private LocationPlugin mPlugin = new LocationPlugin();

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        pluginModules.add(mPlugin);
        return pluginModules;
    }
}
