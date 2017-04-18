package pro.yueyuan.project_t;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import pro.yueyuan.project_t.widget.plugins.LocationPlugin;

/**
 * Created by xuq on 2017/3/16.
 */

public class MyExtensionModule extends DefaultExtensionModule {
    private LocationPlugin mPlugin = new LocationPlugin();
    private IPluginModule image = new ImagePlugin();
    private IPluginModule file = new FilePlugin();
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList<>();
        pluginModules.add(image);
        pluginModules.add(file);
        pluginModules.add(mPlugin);
        return pluginModules;
    }
}
