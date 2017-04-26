package com.hzease.tomeet.home;


import dagger.Component;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = HomePresenterModule.class)
public interface IHomeComponent {

    void inject(HomeActivity homeActivity);

}
