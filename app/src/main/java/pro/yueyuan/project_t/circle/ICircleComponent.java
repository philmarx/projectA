package pro.yueyuan.project_t.circle;


import dagger.Component;
import pro.yueyuan.project_t.circle.ui.CircleActivity;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
import pro.yueyuan.project_t.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = CirclePresenterModule.class)
public interface ICircleComponent {

    void inject(CircleActivity circleActivity);

}
