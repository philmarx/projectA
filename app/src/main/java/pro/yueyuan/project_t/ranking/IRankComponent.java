package pro.yueyuan.project_t.ranking;


import dagger.Component;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.ranking.ui.RankingActivity;
import pro.yueyuan.project_t.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = RankPresenterModule.class)
public interface IRankComponent {

    void inject(RankingActivity rankActivity);

}
