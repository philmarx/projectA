package pro.yueyuan.project_t.data.source;

import javax.inject.Singleton;

import dagger.Component;
import pro.yueyuan.project_t.PTApplicationModule;

/**
 * Created by Key on 2016/11/28 22:47
 * email: MrKey.K@gmail.com
 * description:
 */
@Singleton
@Component(modules = {PTRepositoryModule.class, PTApplicationModule.class})
public interface IPTRepositoryComponent {

    PTRepository getPTRepository();

}
