package pro.yueyuan.project_t.me;


import dagger.Component;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
import pro.yueyuan.project_t.home.ui.HomeActivity;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = MePresenterModule.class)
public interface IMeComponent {

    void inject(MeActivity meActivity);

}
