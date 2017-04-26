package com.hzease.tomeet.me;


import dagger.Component;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.me.ui.MeActivity;
import com.hzease.tomeet.utils.FragmentScoped;

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
