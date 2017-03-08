package pro.yueyuan.project_t.me;


import dagger.Component;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.me.ui.MineActivity;
import pro.yueyuan.project_t.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = MinePresenterModule.class)
public interface IMineComponent {

    void inject(MineActivity mineActivity);

}
